package com.example.aswinleojohn.local;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by aswinleojohn on 18/06/17.
 */

public class SignupActivityThread extends AsyncTask {
    String responseString;
    Response response;
    OkHttpClient client;
    Request request;
    RequestBody formBody;
    String name;
    String email;
    String password;
    String fcmtoken;
    String link="http://192.168.1.4/fr/newuser.php";

    @Override
    protected String doInBackground(Object[] params) {
        name=(String) params[0];
        email=(String) params[1];
        password=(String) params[2];
        fcmtoken=(String) params[3];
        Log.e(TAG, "Async task: name:"+name+" email:"+email+" Password:"+password );
        client = new OkHttpClient();
        formBody = new FormBody.Builder()
                .add("name", name).add("email",email).add("password",password).add("fcmtoken",fcmtoken)
                .build();
        request = new Request.Builder()
                .url(link)
                .post(formBody)
                .build();
        try {
            response = client.newCall(request).execute();
            responseString= response.body().string();
            Log.e(TAG, "Sign up thread Response "+responseString );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
