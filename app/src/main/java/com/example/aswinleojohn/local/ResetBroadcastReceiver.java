package com.example.aswinleojohn.local;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aswinleojohn on 22/06/17.
 */
public class ResetBroadcastReceiver extends BroadcastReceiver {
    SharedPreferences chatpref,usermessagepref;
    SharedPreferences.Editor usermessageeditor,chateditor;
    StringTokenizer stringTokenizer;
    Map<String,?> keys;
    String key;
    String usersid[],name[];
    int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Broadcast Receiver at 12" );
        chatpref=context.getSharedPreferences("chatview", MODE_PRIVATE);
        keys = chatpref.getAll();
        usersid=new String[keys.size()];
        name=new String[keys.size()];
        Map<String, ?> map = new TreeMap<>(keys);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            key = entry.getKey();
            stringTokenizer=new StringTokenizer(key,"--");
            while(stringTokenizer.hasMoreTokens()){
                usersid[i]=stringTokenizer.nextToken();
                name[i]=stringTokenizer.nextToken();
            }
            Log.e(TAG, "Broadcast Receiver at 12: userid:"+usersid[i]+" name:"+name[i] );
            i++;
        }
        for(int j=0;j<usersid.length;j++){
            usermessagepref=context.getSharedPreferences(usersid[j]+"_message", MODE_PRIVATE);
            usermessageeditor=usermessagepref.edit();
            usermessageeditor.clear().commit();
        }
    }
}