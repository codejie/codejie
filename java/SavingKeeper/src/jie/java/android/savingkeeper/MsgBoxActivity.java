/**
 * file   : MsgBoxActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 12, 2011 5:22:44 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MsgBoxActivity extends Activity {
	protected void OnCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.msgbox_activity);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
	}
}
