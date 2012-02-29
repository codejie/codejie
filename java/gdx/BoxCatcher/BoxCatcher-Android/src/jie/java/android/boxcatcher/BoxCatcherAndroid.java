package jie.java.android.boxcatcher;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class BoxCatcherAndroid extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initialize(new BCGame(), false);
	}
	

}
