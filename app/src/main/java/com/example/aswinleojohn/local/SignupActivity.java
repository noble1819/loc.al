package com.example.aswinleojohn.local;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


/**
 * Created by aswinleojohn on 18/06/17.
 */

public class SignupActivity extends Activity {
    private static final String TAG = "Signup Activity";
    SharedPreferences pref,tokenpref;
    SharedPreferences.Editor editor;
    Boolean loggedin;
    String fcmtoken;
    String responseString="nothing";
    String name;
    String email;
    String password;
    TextView nametv;
    TextView emailtv;
    TextView passwordtv;
    Button btn;
    Button loginbtn;
    SignupActivityThread signupActivityThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);
        pref = getSharedPreferences("loginstatus", MODE_PRIVATE);
        tokenpref = getSharedPreferences("tokenstat", MODE_PRIVATE);
        editor = pref.edit();
        nametv=(TextView)findViewById(R.id.name);
        emailtv=(TextView)findViewById(R.id.email);
        passwordtv=(TextView)findViewById(R.id.password);
        btn=(Button)findViewById(R.id.signupbtn);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name = nametv.getText().toString();
                email = emailtv.getText().toString();
                password = passwordtv.getText().toString();
                fcmtoken=tokenpref.getString("fcmtoken",null);
                Log.e(TAG, "Sign up Activity onCreate:name :" + name + " email: " + email + " Password: " + password+" fcm token:"+fcmtoken);
                signupActivityThread=new SignupActivityThread();
                try {
                    responseString=(String)signupActivityThread.execute(name,email,password,fcmtoken).get();
                    Log.e(TAG, "Sign up Activity Response text from thread :" + responseString);
                    editor.putBoolean("loggedinstat",true);
                    editor.commit();
                    editor.putString("userid",responseString);
                    editor.commit();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", responseString);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
