package com.jie.android.gdx.star;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class Star extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initialize(new StarGame(), false);
    }
}