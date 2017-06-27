package com.example.aswinleojohn.local;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.internal.zzt;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.BrokenBarrierException;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    StringTokenizer stringTokenizer;
    ListView listView;
    View mView;
    NearbyList nearbyList;
    Activity activity;
    String fcmtokenget[];
    String usersid[];
    String name[];
    String key;
    int i=0;
    Map<String, ?> keys;
    Bundle savedInstanceStates;
    LayoutInflater inflaters;
    ViewGroup containers;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    View rootView;

    private OnFragmentInteractionListener mListener;

    public ChatView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatView.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatView newInstance(String param1, String param2) {
        ChatView fragment = new ChatView();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceStates=savedInstanceState;
        pref = getActivity().getSharedPreferences("chatview", MODE_PRIVATE);
        activity=getActivity();
        editor = pref.edit();
        keys = pref.getAll();
        refresh(keys);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("chatviewreload")) {
                    // new push notification is received
                    keys=pref.getAll();
                    refresh(keys);


                    //Toast.makeText(context, "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflaters=inflater;
        containers=container;
        rootView = inflater.inflate(R.layout.chat_view, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(nearbyList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, Message.class);
                intent.putExtra("fcmusertoken", fcmtokenget[i]);
                intent.putExtra("userid",usersid[i]);
                intent.putExtra("name",name[i]);
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void refresh(Map<String, ?> keys){
        int i=0;
        activity=getActivity();
        Map<String,?> map = new TreeMap<>(keys);
        name=new String[keys.size()];
        usersid=new String[keys.size()];
        fcmtokenget=new String[keys.size()];
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            key = entry.getKey();
            stringTokenizer=new StringTokenizer(key,"--");
            while(stringTokenizer.hasMoreTokens()){
                usersid[i]=stringTokenizer.nextToken();
                name[i]=stringTokenizer.nextToken();
            }
            fcmtokenget[i]=(String)entry.getValue();
            Log.e(TAG, "Chat view:name:"+name[i]+" userid:"+usersid[i]+" fcmtoken:"+fcmtokenget[i] );
            i++;
        }

        nearbyList = new NearbyList(activity, usersid, name);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.e(TAG, "onStart: inside on start of chatview" );
    }
    @Override
    public void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("chatviewreload"));
        keys=pref.getAll();
        refresh(keys);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.actions , menu);
    }
    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            case R.id.cnt_mnu_clear:
                SharedPreferences settings = getContext().getSharedPreferences(usersid[info.position]+"_message", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Toast.makeText(getContext(), name[info.position]+"'s Messages Cleared" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.cnt_mnu_delete:
                SharedPreferences settings2 = getContext().getSharedPreferences(usersid[info.position]+"_message", Context.MODE_PRIVATE);
                settings2.edit().clear().commit();
                SharedPreferences settings1 = getContext().getSharedPreferences("chatview", Context.MODE_PRIVATE);
                settings1.edit().remove(usersid[info.position]+"--"+name[info.position]).commit();
                Toast.makeText(getContext(), name[info.position]+"'s Messages deleted" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.cnt_mnu_block:
                Toast.makeText(getContext(), "delete selected"+name[info.position]  , Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
