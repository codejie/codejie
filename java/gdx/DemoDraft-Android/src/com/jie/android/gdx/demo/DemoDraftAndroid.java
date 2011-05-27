package com.jie.android.gdx.demo;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;


public class DemoDraftAndroid extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new DemoDraft(), false);
        //setContentView(R.layout.main);
    }
}