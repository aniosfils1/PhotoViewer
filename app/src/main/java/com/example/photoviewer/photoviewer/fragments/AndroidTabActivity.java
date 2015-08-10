package com.example.photoviewer.photoviewer.fragments;

/**
 * Created by Isaac on 8/10/2015.
 */

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.photoviewer.photoviewer.R;
import com.example.photoviewer.photoviewer.TimelineActivity;

public class AndroidTabActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, TimelineActivity.class);

        spec = tabHost
                .newTabSpec("Widget")
                .setIndicator("Home",
                        res.getDrawable(android.R.drawable.ic_menu_today))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab2.class);
        spec = tabHost
                .newTabSpec("Form")
                .setIndicator("Tab 2",
                        res.getDrawable(android.R.drawable.ic_menu_manage))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab3.class);
        spec = tabHost
                .newTabSpec("onglet3")
                .setIndicator("Tab 3", res.getDrawable(android.R.drawable.ic_menu_mapmode))
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

    }
}