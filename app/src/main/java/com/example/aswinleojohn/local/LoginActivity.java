package com.example.aswinleojohn.local;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by aswinleojohn on 20/06/17.
 */

public class LoginActivity extends Activity {
    SharedPreferences pref,tokenpref;
    SharedPreferences.Editor editor;
    EditText emailet;
    EditText passwordet;
    Button loginbtn;
    String email;
    String password;
    String responseString;
    String fcmtoken;
    LoginActivityThread loginActivityThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        pref = getSharedPreferences("loginstatus", MODE_PRIVATE);
        tokenpref = getSharedPreferences("tokenstat", MODE_PRIVATE);
        editor = pref.edit();
        fcmtoken=tokenpref.getString("fcmtoken",null);
        emailet=(EditText)findViewById(R.id.emailet);
        passwordet=(EditText)findViewById(R.id.passwordet);
        loginbtn=(Button)findViewById(R.id.login);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 email=emailet.getText().toString();
                password=passwordet.getText().toString();
                loginActivityThread=new LoginActivityThread();
                try{
                responseString=(String)loginActivityThread.execute(email,password,fcmtoken).get();
                    if(Integer.parseInt(responseString)>0){
                        editor.putBoolean("loggedinstat",true);
                        editor.commit();
                        editor.putString("userid",responseString);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USER_ID", responseString);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
