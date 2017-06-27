package com.example.aswinleojohn.local;

/**
 * Created by aswinleojohn on 18/06/17.
 */

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.concurrent.ExecutionException;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    String refreshedToken;
    String userid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MainActivity ma;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        pref = getSharedPreferences("tokenstat", MODE_PRIVATE);
        editor = pref.edit();
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        editor.clear();
        editor.putString("fcmtoken",refreshedToken);
        editor.commit();
        //ma=new MainActivity();
        //ma.refreshToken(refreshedToken);



    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }

}