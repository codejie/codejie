package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

public class ResultDisplayActivity extends Activity /*implements OnTouchListener*/ {

	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result_display);
		
		web = (WebView)this.findViewById(R.id.webView1);
		
//		web.setOnTouchListener(this);

	}
/*
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		web.loadUrl("file://\\sdcard\\a.html");
		return false;
	}
*/
	
	public void loadData() {
		web.loadUrl("file://" + Environment.getExternalStorageDirectory() + Score.CACHE_FILE);
		//web.loadUrl("file:///sdcard/t.html");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Intent intent = this.getIntent();
		
		if(intent != null) {
			loadData();
		}
		
		super.onResume();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "key - " + keyCode + " event - " + event.getKeyCode());
		return super.onKeyUp(keyCode, event);
	}	

}
