package com.example.aswinleojohn.local;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearbyOnline.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearbyOnline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbyOnline extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG ="Main Activity" ;
    SharedPreferences pref,tokenpref;
    SharedPreferences.Editor editor;
    LocationActivity locationActivity;
    MylocationThread mylocationThread;
    JSONObject jObject = null;
    JSONArray userd;
    Activity activity;
    ListView listView;
    View mView;
    NearbyList nearbyList;
    Double latitude=0.0;
    Double longitude=0.0;
    String userid;
    String fcmtoken;
    String responseString;
    String[] usersid;
    String[] name;
    String[] fcmtokenget;

    private OnFragmentInteractionListener mListener;

    public NearbyOnline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyOnline.
     */
    // TODO: Rename and change types and number of parameters
    public static NearbyOnline newInstance(String param1, String param2) {
        NearbyOnline fragment = new NearbyOnline();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("loginstatus", MODE_PRIVATE);
        tokenpref = getActivity().getSharedPreferences("tokenstat", MODE_PRIVATE);
        activity=getActivity();
        editor = pref.edit();
        userid = pref.getString("userid", null);
        fcmtoken=tokenpref.getString("fcmtoken",null);
        Log.e(TAG, "Main Activity:User Id from Signup " + userid);
        Log.e(TAG, "Main Activity:FCM token " + fcmtoken);
        mylocationThread = new MylocationThread();
        Log.e(TAG, "After getting my location:latitude:" + latitude + " longitude:" + longitude);
        try {
            responseString = (String) mylocationThread.execute(userid, latitude, longitude).get();
            try {
                jObject = new JSONObject(responseString);
                JSONArray userd = jObject.getJSONArray("users");
                Log.e(TAG, "Parsed JSON Data");
                usersid=new String[userd.length()];
                name=new String[userd.length()];
                fcmtokenget=new String[userd.length()];
                for (int i = 0; i < userd.length(); i++) {
                    JSONObject person = userd.getJSONObject(i);
                    usersid[i] = person.getString("userid");
                    name[i] = person.getString("name");
                    fcmtokenget[i]=person.getString("token");
                    Log.e(TAG, "userid:" + usersid[i] + " name:" + name[i]+" fcm token"+fcmtokenget[i]);

                }
                nearbyList = new NearbyList(activity, usersid, name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.nearby_online, container, false);
        this.mView=rootView;
        listView = (ListView) mView.findViewById(R.id.listView);
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

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
