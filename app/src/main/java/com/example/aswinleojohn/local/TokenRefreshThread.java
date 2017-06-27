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

public class TokenRefreshThread extends AsyncTask {
    String userid;
    String newfcmtoken;
    String responseString;
    Response response;
    OkHttpClient client;
    Request request;
    RequestBody formBody;
    String link="http://192.168.1.4/fr/registertoken.php";
    @Override
    protected String doInBackground(Object[] params) {
        userid=(String) params[0];
        newfcmtoken=(String) params[1];
        client = new OkHttpClient();
        formBody = new FormBody.Builder()
                .add("userid", userid).add("newfcmtoken",newfcmtoken)
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
