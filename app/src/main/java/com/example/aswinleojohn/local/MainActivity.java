package com.example.aswinleojohn.local;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.support.v7.app.ActionBarActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements
         android.support.v7.app.ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsAdapter mAdapter;
    android.support.v7.app.ActionBar actionBar;
    private static final String TAG = "Main Activity";
    SharedPreferences pref, tokenpref,chatpref;
    SharedPreferences.Editor editor,chateditor;
    Boolean loggedin;
    TokenRefreshThread tokenRefreshThread;
    String userid;
    String fcmtoken;
    Map<String,?> keys;
    String responseString;
    // Tab titles
    private String[] tabs = {"Nearby", "Chat", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("loginstatus", MODE_PRIVATE);
        tokenpref = getSharedPreferences("tokenstat", MODE_PRIVATE);
        chatpref = getSharedPreferences("chatview", MODE_PRIVATE);
        editor = pref.edit();
        chateditor=chatpref.edit();
        //chateditor.clear().commit();
        chateditor.putString("0--Chat Support","jsdagdjagdfjsagdusagudyfadeufdshdf").commit();

        loggedin = pref.getBoolean("loggedinstat", false);
        Log.e(TAG, "very start:shared pref loggedin val:" + loggedin);
        if (loggedin) {
            setContentView(R.layout.activity_main);
            fcmtoken=tokenpref.getString("fcmtoken",null);

            // Initilization
            viewPager = (ViewPager) findViewById(R.id.pager);
            actionBar = getSupportActionBar();
            mAdapter = new TabsAdapter(getSupportFragmentManager());

            viewPager.setAdapter(mAdapter);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Adding Tabs
            for (String tab_name : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tab_name)
                        .setTabListener(this));
            }

            /**
             * on swiping the viewpager make respective tab selected
             * */
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    // on changing the page
                    // make respected tab selected
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND,20);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
            Log.e(TAG, "calender time: " +cal.getTime().toString());
            Intent intent = new Intent(MainActivity.this, ResetBroadcastReceiver.class);
            PendingIntent pintent = PendingIntent.getBroadcast(MainActivity.this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);



        }
        else
        {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {

        return;
    }
    void refreshToken(String refreshedToken){
        tokenRefreshThread=new TokenRefreshThread();

        try {

            tokenRefreshThread.execute(userid, refreshedToken);
        } catch (Exception e){

        }
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
        Log.e(TAG, "onTabSelected: "+tab.getPosition() );
        if(tab.getPosition()==1){
            Intent chatviewreload = new Intent("chatviewreload");
            Log.e(TAG, "onTabSelected: " );
            LocalBroadcastManager.getInstance(this).sendBroadcast(chatviewreload);
        }
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}