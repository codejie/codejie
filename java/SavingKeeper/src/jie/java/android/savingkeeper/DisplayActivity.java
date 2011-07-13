/**
 * file   : DisplayActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 4:46:59 PM
 */
package jie.java.android.savingkeeper;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class DisplayActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(GLOBAL.APP_TAG, "DISPLAY ACTIVITY");
	}
	
}
