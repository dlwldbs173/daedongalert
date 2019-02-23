package com.a000webhostapp.daedongalert.daedongalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    final static private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        Button LoginButton = (Button) findViewById(R.id.LoginButton);
        TextView RegisterSwitch = (TextView) findViewById(R.id.RegisterSwitch);

        RegisterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseListener_login;
                responseListener_login = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d(TAG, jsonObject.toString());
                            int statement = jsonObject.getInt("statement");
                            Log.d(TAG, statement + "");
                            if(statement == 1) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else if (statement == 2) {
                                LoginPWFailure();
                            } else {
                                LoginIDFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                String encrypt_userPassword = encryptSHA512(userPassword);
                Log.d(TAG, "LoginRequest queued!");
                LoginRequest loginRequest = new LoginRequest(userID, encrypt_userPassword, responseListener_login);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(loginRequest);
            }
        });
    }

    void LoginIDFailure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("실패");
        builder.setMessage("ID를 찾을 수 없습니다.");
        builder.setNegativeButton("다시시도", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    void LoginPWFailure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("실패");
        builder.setMessage("비밀번호가 올바르지 않습니다.");
        builder.setNegativeButton("다시시도", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    public static String encryptSHA512(String target) {
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(target.getBytes());

            StringBuffer stringBuffer = new StringBuffer();

            for(byte b : sh.digest()) {
                stringBuffer.append(Integer.toHexString(0xff & b));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
}
