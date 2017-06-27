package com.example.aswinleojohn.local;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * Created by aswinleojohn on 20/06/17.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new NearbyOnline();
            case 1:
                // Games fragment activity
                return new ChatView();
            case 2:
                // Movies fragment activity
                return new SettingsActivity();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}