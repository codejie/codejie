package jie.java.android.gdx.mooneclipse;

//import android.app.Activity;
import com.badlogic.gdx.backends.android.AndroidApplication;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MoonEclipse extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize(new MoonEclipseGame(), false);
        //setContentView(R.layout.main);
        
        //http://code.google.com/p/libgdx/wiki/AdMobInLibgdx
        RelativeLayout layout = new RelativeLayout(this);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        View gameView = this.initializeForView(new MoonEclipseGame(), false);
        layout.addView(gameView);

        initAdView(layout);
        
        this.setContentView(layout);        
    }

	private void initAdView(RelativeLayout layout) {
		// TODO Auto-generated method stub
		
	}
    
    
}