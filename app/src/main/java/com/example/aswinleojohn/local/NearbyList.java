package com.example.aswinleojohn.local;

/**
 * Created by aswinleojohn on 18/06/17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class NearbyList extends ArrayAdapter<String> {
    private String[] userid;
    private String[] name;

    private Activity context;

    public NearbyList(Activity context, String[] userid, String[] name) {
        super(context, R.layout.nearbylist_view, userid);
        this.context = context;
        this.userid = userid;
        this.name = name;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.nearbylist_view, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.textViewId);


        textViewName.setText(name[position]);
        textViewId.setText(userid[position]);

        return  listViewItem;
    }
}
