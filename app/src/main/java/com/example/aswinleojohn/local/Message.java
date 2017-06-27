package com.example.aswinleojohn.local;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by aswinleojohn on 18/06/17.
 */

public class Message extends Activity {
    SharedPreferences pref,chatpref,senderpref;
    SharedPreferences.Editor editor,chateditor;
    SimpleDateFormat dateFormat,dateFormat12;
    MessageThread messageThread;
    String fcmtoken;
    String userid;
    String formattedDate;
    String formattedDate12;
    String key;
    String name;
    ImageView sendbtn;
    CharSequence messsageinput;
    LinearLayout llsend;
    LinearLayout llreceive;
    LinearLayout rlayout;
    TextView txts;
    TextView txtr;
    EditText inputtxt;
    ImageView imgs;
    ImageView imgs1;
    ImageView imgr;
    ImageView imgr1;
    Map<String, ?> keys;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    LinearLayout.LayoutParams txtsLayoutParams;
    LinearLayout.LayoutParams txtrLayoutParams;
    LinearLayout.LayoutParams lprams;
    LinearLayout.LayoutParams rprams;
    LinearLayout.LayoutParams mprams;
    LinearLayout.LayoutParams imgsParams;
    LinearLayout.LayoutParams imgs1Params;
    LinearLayout.LayoutParams imgrParams;
    LinearLayout.LayoutParams imgr1Params;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    String message;
    String senderid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        sendbtn = (ImageView) findViewById(R.id.sendbtn);
        fcmtoken = getIntent().getExtras().getString("fcmusertoken");
        userid = getIntent().getExtras().getString("userid");
        name=getIntent().getExtras().getString("name");
        pref = getSharedPreferences(userid + "_message", MODE_PRIVATE);
        editor = pref.edit();
        senderpref=getSharedPreferences("loginstatus", MODE_PRIVATE);
        chatpref=getSharedPreferences("chatview", MODE_PRIVATE);
        chateditor=chatpref.edit();
        dateFormat = new SimpleDateFormat("HH:mm:ss aa");
        dateFormat12 = new SimpleDateFormat("hh:mm:ss aa");
        formattedDate = dateFormat.format(new Date()).toString();
        rlayout = (LinearLayout) findViewById(R.id.relative);
        //mprams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        //mprams.gravity=Gravity.BOTTOM;
        //rlayout.setLayoutParams(mprams);
        lprams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rprams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lprams.gravity = Gravity.RIGHT;
        rprams.gravity = Gravity.LEFT;
        txtsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtsLayoutParams.setMargins(0, 10, 0, 0);
        txtrLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtrLayoutParams.setMargins(0, 15, 0, 0);
        imgsParams = new LinearLayout.LayoutParams(15,15);
        imgsParams.topMargin=15;
        imgsParams.leftMargin=0;
        imgsParams.weight=1;
        imgs1Params = new LinearLayout.LayoutParams(80,80);
        imgs1Params.topMargin=0;
        imgs1Params.weight=1;
        imgrParams = new LinearLayout.LayoutParams(15, 15);
        imgrParams.topMargin=20;
        imgrParams.rightMargin=0;
        imgrParams.weight=1;
        imgr1Params = new LinearLayout.LayoutParams(80,80);
        imgr1Params.topMargin=0;
        imgr1Params.weight=3;
        keys = pref.getAll();
        viewAllMessages(keys);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                 if (intent.getAction().equals("pushNotification")) {
                    // new push notification is received
                     String msg=intent.getStringExtra("message");
                     Log.e(TAG, "onReceive: "+msg );
                     keys=pref.getAll();
                     viewAllMessages(keys);


                     Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

       /* keys = pref.getAll();
        Map<String, ?> map = new TreeMap<>(keys);
        for (Map.Entry<String, ?> entry : map.entrySet()) {

            key = entry.getKey();
            llsend = new LinearLayout(this);
            llsend.setOrientation(LinearLayout.HORIZONTAL);
            llsend.setLayoutParams(lprams);
            llreceive = new LinearLayout(this);
            llreceive.setOrientation(LinearLayout.HORIZONTAL);
            llreceive.setLayoutParams(rprams);
            imgs=new ImageView(this);
            imgs.setLayoutParams(imgsParams);
            imgs.setBackgroundResource(R.drawable.arrow_bg1);
            imgs1=new ImageView(this);
            imgs1.setLayoutParams(imgs1Params);
            imgs1.setBackgroundResource(R.drawable.user_pacific);
            imgr=new ImageView(this);
            imgr.setLayoutParams(imgrParams);
            imgr.setBackgroundResource(R.drawable.arrow_bg2);
            imgr1=new ImageView(this);
            imgr1.setLayoutParams(imgr1Params);
            imgr1.setBackgroundResource(R.drawable.user_pratikshya);


            if (key.contains("_S")) {

                txts = new TextView(Message.this);
                txts.setText((CharSequence) entry.getValue());
                txts.append("\n");
                txts.append(key.replace("_S",""));
                txts.setLayoutParams(txtsLayoutParams);
                txts.setPadding(1000, 1000, 1000, 1000);
                txts.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner));
                txts.setTextColor(Color.rgb(0, 0, 0));

                llsend.addView(txts);
                llsend.addView(imgs);
                llsend.addView(imgs1);
                rlayout.addView(llsend);
            } else {
                llreceive.setGravity(Gravity.LEFT);
                txtr = new TextView(Message.this);
                txtr.setText((CharSequence) entry.getValue());
                txtr.append("\n");
                txtr.append(key.replace("_R",""));
                txtr.setLayoutParams(txtrLayoutParams);
                txtr.setPadding(500, 500, 500, 500);
                txtr.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner1));
                txtr.setTextColor(Color.rgb(0, 0, 0));
                txtr.setGravity(Gravity.BOTTOM);
                llreceive.addView(imgr1);
                llreceive.addView(imgr);
                llreceive.addView(txtr);
                rlayout.addView(llreceive);
            }
        }
*/

        sendbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                keys = pref.getAll();
                inputtxt = (EditText) findViewById(R.id.inputtxt);
                formattedDate = dateFormat.format(new Date()).toString();
                message = inputtxt.getText().toString();
                inputtxt.setText("");
                Log.e(TAG, "Message: inside btn message:"+message+" fcmtoken:"+fcmtoken+"userid:"+userid);

                if (message.length() > 0) {
                    chateditor.putString(userid+"--"+name,fcmtoken);
                    chateditor.commit();
                    editor.putString(formattedDate + "_S",  message);
                    editor.commit();
                   /* llsend = new LinearLayout(Message.this);
                    llsend.setOrientation(LinearLayout.HORIZONTAL);
                    llsend.setLayoutParams(lprams);
                    txts = new TextView(Message.this);
                    txts.setText(message);
                    txts.append("\n");
                    txts.append(formattedDate);
                    txts.setLayoutParams(txtsLayoutParams);
                    txts.setPadding(1000, 1000, 1000, 1000);
                    txts.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner));
                    txts.setTextColor(Color.rgb(0, 0, 0));
                    if(txts.getParent()!=null)
                        ((ViewGroup)txts.getParent()).removeView(txts); // <- fix
                    llsend.addView(txts);
                    llsend.addView(imgs);
                    llsend.addView(imgs1);
                    rlayout.addView(llsend);*/
                   senderid=senderpref.getString("userid",null);
                    messageThread = new MessageThread();
                    messageThread.execute(fcmtoken, message, senderid);
                    keys=pref.getAll();

                    viewAllMessages(keys);
                }
                }

        });

    }
    void viewAllMessages(Map<String,?> keys){
        if( rlayout.getChildCount() > 0)
            rlayout.removeAllViews();
        Map<String, ?> map = new TreeMap<>(keys);
        for (Map.Entry<String, ?> entry : map.entrySet()) {

            key = entry.getKey();
            llsend = new LinearLayout(this);
            llsend.setOrientation(LinearLayout.HORIZONTAL);
            llsend.setLayoutParams(lprams);
            llreceive = new LinearLayout(this);
            llreceive.setOrientation(LinearLayout.HORIZONTAL);
            llreceive.setLayoutParams(rprams);
            imgs=new ImageView(this);
            imgs.setLayoutParams(imgsParams);
            imgs.setBackgroundResource(R.drawable.arrow_bg1);
            imgs1=new ImageView(this);
            imgs1.setLayoutParams(imgs1Params);
            imgs1.setBackgroundResource(R.drawable.user_pacific);
            imgr=new ImageView(this);
            imgr.setLayoutParams(imgrParams);
            imgr.setBackgroundResource(R.drawable.arrow_bg2);
            imgr1=new ImageView(this);
            imgr1.setLayoutParams(imgr1Params);
            imgr1.setBackgroundResource(R.drawable.user_pratikshya);


            if (key.contains("_S")) {

                txts = new TextView(Message.this);
                txts.setText((CharSequence) entry.getValue());
                txts.append("\n");
                key=key.replace("_S","");
                try {
                    formattedDate12=dateFormat12.format(dateFormat12.parse(key));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txts.append(formattedDate12);
                txts.setLayoutParams(txtsLayoutParams);
                txts.setPadding(1000, 1000, 1000, 1000);
                txts.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner));
                txts.setTextColor(Color.rgb(0, 0, 0));

                llsend.addView(txts);
                llsend.addView(imgs);
                llsend.addView(imgs1);
                rlayout.addView(llsend);
            } else {
                llreceive.setGravity(Gravity.LEFT);
                txtr = new TextView(Message.this);
                txtr.setText((CharSequence) entry.getValue());
                txtr.append("\n");
                key=key.replace("_R","");
                try {
                    formattedDate12=dateFormat12.format(dateFormat12.parse(key));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtr.append(formattedDate12);
                txtr.setLayoutParams(txtrLayoutParams);
                txtr.setPadding(500, 500, 500, 500);
                txtr.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner1));
                txtr.setTextColor(Color.rgb(0, 0, 0));
                txtr.setGravity(Gravity.BOTTOM);
                llreceive.addView(imgr1);
                llreceive.addView(imgr);
                llreceive.addView(txtr);
                rlayout.addView(llreceive);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("pushNotification"));
        keys=pref.getAll();
        viewAllMessages(keys);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.sview));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

}


        /*messagetv = (TextView) findViewById(R.id.messageview);
        messagetv.setMovementMethod(new ScrollingMovementMethod());
        messageEt = (EditText) findViewById(R.id.messageinput);
        sendbtn = (Button) findViewById(R.id.sendbtn);
        Map<String, ?> keys;
        keys = pref.getAll();
        Map<String, ?> map = new TreeMap<>(keys);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            key = entry.getKey();
            if (key.contains("_S")) {
                messagetv.append("Me:");
                messagetv.append("\n");
            } else {
                messagetv.append(userid + ":");
                messagetv.append("\n");
            }
            messagetv.append((CharSequence) entry.getValue());
            messagetv.append("\n");
            messagetv.append(entry.getKey());
            messagetv.append("\n");
            messagetv.append("\n");

        }
        sendbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dateFormat = new SimpleDateFormat("hh:mm:ss aa");
                formattedDate = dateFormat.format(new Date()).toString();
                messsageinput = messageEt.getText().toString();
                if (messsageinput.length() > 0) {
                    Log.e(TAG, "Message :onClick: message:" + messsageinput + " time:" + formattedDate);
                    messagetv.setText(messsageinput);
                    messageEt.setText("");
                    editor.putString(formattedDate + "_S", (String) messsageinput);
                    editor.commit();
                    Map<String, ?> keys;
                    keys = pref.getAll();
                    Map<String, ?> map = new TreeMap<>(keys);
                    messagetv.setText("");
                    for (Map.Entry<String, ?> entry : map.entrySet()) {
                        key = entry.getKey();
                        if (key.contains("_S")) {
                            messagetv.append("Me:");
                            messagetv.append("\n");
                        } else {
                            messagetv.append(userid+":");
                            messagetv.append("\n");
                        }
                        messagetv.append((CharSequence) entry.getValue());
                        messagetv.append("\n");
                        messagetv.append(entry.getKey());
                        messagetv.append("\n");
                        messagetv.append("\n");
                    }
                    messageThread = new MessageThread();
                    messageThread.execute(fcmtoken, messsageinput, userid);
                }
            }

        });*/



