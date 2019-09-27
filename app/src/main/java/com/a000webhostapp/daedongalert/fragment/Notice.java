package com.a000webhostapp.daedongalert.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.a000webhostapp.daedongalert.daedongalert.NoticeInflateActivity;
import com.a000webhostapp.daedongalert.daedongalert.R;
import com.a000webhostapp.daedongalert.daedongalert.noticeRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;



public class Notice extends Fragment {

    final static private String TAG = Notice.class.getSimpleName();

    public static Notice newInstance() {
        return new Notice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.notice_fragment, container, false);
        TableLayout layout = (TableLayout) view.findViewById(R.id.noticeTable);

        Response.Listener<String> responseListener_notice;
        responseListener_notice = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Notice : "+response);

                String[] listLink = response.split("LINK_BEGIN");
                final String[] noticeList = listLink[0].split("SEPARATE");
                final String[] linkList = listLink[1].split("SEPARATE");

                TableLayout noticeTable = (TableLayout) view.findViewById(R.id.noticeTable);
                TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                TableRow existRow = (TableRow) view.findViewById(R.id.noticeRow);

                for(int i=0 ; i<noticeList.length ; i++) {
                    final int final_i = i;
                    TableRow noticeRow = new TableRow(getActivity());
                    TextView noticeText = textMake(noticeList[i], Gravity.LEFT);
                    noticeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), NoticeInflateActivity.class);
                            intent.putExtra("link", linkList[final_i]);
                            intent.putExtra("subject", noticeList[final_i]);
                            startActivity(intent);
                        }
                    });
                    noticeRow.addView(noticeText);
                    noticeTable.addView(noticeRow);
                }
                noticeTable.removeView(existRow);
            }
        };
        Log.d(TAG, "Notice queued!");
        noticeRequest noticeRequest = new noticeRequest(responseListener_notice);
        RequestQueue queue_notice = Volley.newRequestQueue(getActivity());
        queue_notice.add(noticeRequest);

        return view;
    }

    TextView textMake (String set_text, int gravity) {
        TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TextView meals = new TextView(getActivity());
        meals.setText(set_text);
        meals.setLayoutParams(textParams);
        meals.setGravity(gravity);
        meals.setTextColor(Color.parseColor("#000000"));
        meals.setBackgroundResource(R.drawable.cell_shape);
        meals.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        return meals;
    }
}
