package com.example.aswinleojohn.local;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by aswinleojohn on 18/06/17.
 */

public class MessageThread extends AsyncTask {
    Response response;
    OkHttpClient client;
    Request request;
    RequestBody formBody;
    JSONObject jsonObject;
    JSONObject data;
    JSONObject notification;
    String fcmusertoken;
    String message;
    String json;
    String responseString;
    String senderid;
    String link="https://fcm.googleapis.com/fcm/send";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected Object doInBackground(Object[] params) {
        fcmusertoken=(String) params[0];
        message=(String) params[1];
        senderid=(String) params[2];
        jsonObject = new JSONObject();
        data = new JSONObject();
        //notification=new JSONObject();
        try {
            data.put("username", senderid);
            data.put("datamessage",message);
            //notification.put("title",senderid);
            //notification.put("text",message);
            jsonObject.put("data", data);
            //jsonObject.put("notification",notification);
            jsonObject.put("to", fcmusertoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        json=jsonObject.toString();
        Log.e(TAG, "Message thread: request json body: "+json );
        client=new OkHttpClient();
        formBody = RequestBody.create(JSON, json);
        request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","key=AAAAX9rX-hU:APA91bFxCbi4apiIVbyT6N4HBb4hCgmeqOoxe8PgxzW3N07KYaDTVC7UO4ns4lsAnBLCJZAOzvoguDjZobUeGrQ7IK3XsSwa9tkJm52sJ-L6AwzwlJPsZFBPB4PmuoQntyGD7R_ZjU-7")
                .url(link)
                .post(formBody)
                .build();
        try {
            response = client.newCall(request).execute();
            responseString= response.body().string();
            Log.e(TAG, "Message thread: Json"+responseString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
