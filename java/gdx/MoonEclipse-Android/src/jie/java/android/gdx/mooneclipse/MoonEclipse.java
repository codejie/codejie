package jie.java.android.gdx.mooneclipse;

//import android.app.Activity;
import com.badlogic.gdx.backends.android.AndroidApplication;

import android.os.Bundle;

public class MoonEclipse extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initialize(new MoonEclipseGame(), false);
    }    
}