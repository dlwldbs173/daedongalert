package com.a000webhostapp.daedongalert.daedongalert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoticeInflateActivity extends AppCompatActivity {

    private LinearLayout noticeLayout = null;
    private Bitmap contentIMG = null;
    private InputStream attachFileDown = null;
    private String[] file_name = null;
    private String[] file_link = null;
    final static private String TAG = NoticeInflateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_inflate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        String subject = intent.getStringExtra("subject");

        final LinearLayout noticeLayout = (LinearLayout) findViewById(R.id.noticeLayout);
        this.noticeLayout = noticeLayout;
        Log.d(TAG, link);

        TextView subjectText = (TextView) findViewById(R.id.subjectView);
        subjectText.setText(subject);
        subjectText.setPaintFlags(subjectText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        Response.Listener<String> responseListener;
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response+"");
                String content = null;
                content = response.substring(response.indexOf("CONTENT_BEGIN") + "CONTENT_BEGIN".length(), response.indexOf("CONTENT_END"));
                            if(response.contains("IMG_BEGIN")) {
                                final String img_link = response.substring(response.indexOf("IMG_BEGIN")+"IMG_BEGIN".length(), response.indexOf("IMG_END"));
                                Log.d(TAG, "img found!"+img_link);

                                new Thread() {
                                    @Override
                                    public void run() {
                                        Bitmap bmImg = BitmapFactory.decodeStream(ImgIS(img_link));
                                        contentIMG = bmImg;

                                        Bundle bundle = new Bundle();
                                        Message msg = handler_img.obtainMessage();
                                        msg.setData(bundle);
                                        handler_img.sendMessage(msg);
                        }
                    }.start();
                }
                if(response.contains("FILE_BEGIN")) {
                    String file_link_name = response.substring(response.indexOf("FILE_BEGIN")+"FILE_BEGIN".length(), response.indexOf("FILE_END"));
                    Log.d(TAG, "File Found!"+file_link_name);
                    String[] file_list = file_link_name.split("NEXT");
                    int i=0;

                    LinearLayout attatchLayout = (LinearLayout) findViewById(R.id.attatchLayout);
                    for (String a : file_list) {
                        final String[] file = a.split("SEPARATE");
                        TextView fileName = new TextView(getApplicationContext());
                        fileName.setText(file[1]);
                        fileName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        fileName.setTextColor(Color.parseColor("#000000"));
                        fileName.setBackgroundResource(R.drawable.cell_shape);
                        fileName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file[0]));
                                startActivity(intent);
                            }
                        });
                        attatchLayout.addView(fileName, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    }
                    TextView Trash = (TextView) findViewById(R.id.Trash);
                    attatchLayout.removeView(Trash);
                } else {
                    LinearLayout attachNo = (LinearLayout) findViewById(R.id.attachNo);
                    noticeLayout.removeView(attachNo);
                }
                if(content.length() != 0) {
                    TextView contentText = textMake(content, Gravity.LEFT);
                    noticeLayout.addView(contentText);
                }
            }
        };
        Log.d(TAG, "NoticeContent queued!");
        NoticeContentRequest noticeContentRequest = new NoticeContentRequest(link, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(noticeContentRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    TextView textMake (String set_text, int gravity) {
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView meals = new TextView(getApplicationContext());
        meals.setText(set_text);
        meals.setLayoutParams(textParams);
        meals.setGravity(gravity);
        meals.setTextColor(Color.parseColor("#000000"));
        meals.setBackgroundResource(R.drawable.cell_shape);
        meals.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        return meals;
    }

    Handler handler_img = new Handler() {
        public void handleMessage(Message msg) {
            ImageView contentImage = new ImageView(getApplicationContext());   //ImageView 생성
            contentImage.setImageBitmap(contentIMG);

            noticeLayout.addView(contentImage, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    };

    private InputStream ImgIS (String URL) {
        URL url = null;
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //HTTP 연결
            connection.setDoInput(true);
            connection.connect();

            int length = connection.getContentLength();
            inputStream = connection.getInputStream();    //INPUT STREAM
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
