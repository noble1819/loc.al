package com.example.aswinleojohn.local;

import android.content.Context;
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

public class MylocationThread extends AsyncTask {
    private Context context;
    OkHttpClient client;
    Request request;
    Response response;
    String responseString;
    String userid;
    Double latitude;
    Double longitude;
    String link="http://192.168.1.4/fr/savedb.php";

    @Override
    protected String doInBackground(Object[] params) {
        userid=(String) params[0];
        latitude = (Double) params[1];
        longitude = (Double) params[2];
        client=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userid", userid).add("latitude",Double.toString(latitude)).add("longitude",Double.toString(longitude))
                .build();
        Request request = new Request.Builder()
                .url(link)
                .post(formBody)
                .build();
        try {
            response = client.newCall(request).execute();
            responseString= response.body().string();
            Log.e(TAG, "Mylocation thread: Json"+responseString);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
