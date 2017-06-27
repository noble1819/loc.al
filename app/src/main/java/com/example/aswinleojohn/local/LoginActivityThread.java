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
 * Created by aswinleojohn on 20/06/17.
 */

public class LoginActivityThread extends AsyncTask {
    String responseString;
    Response response;
    OkHttpClient client;
    Request request;
    RequestBody formBody;
    String email;
    String password;
    String fcmtoken;
    String link="http://192.168.1.4/fr/loginuser.php";
    @Override
    protected String doInBackground(Object[] params) {
        email=(String) params[0];
        password=(String) params[1];
        fcmtoken=(String) params[2];
        client = new OkHttpClient();
        formBody = new FormBody.Builder()
                .add("email", email).add("password",password).add("fcmtoken",fcmtoken)
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
