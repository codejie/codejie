package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;

public class ResultDisplayActivity extends Activity /*implements OnTouchListener*/ {

	private static final String Button = null;
	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result_display);
		
		web = (WebView)this.findViewById(R.id.webView);
		
//		web.setOnTouchListener(this);
		
		WordDisplayActivity.setResultDisplay(this);
		
		((Button)this.findViewById(R.id.btnYes)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				submitResult(Score.JUDGE_YES);
			}			
		});
		
		((Button)this.findViewById(R.id.btnNo)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				submitResult(Score.JUDGE_NO);
			}
			
		});
		
		loadData();
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
		Log.d(Global.APP_TITLE, "Result Activity count : " + ResultDisplayActivity.getInstanceCount());
		
		super.onResume();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "key - " + keyCode + " event - " + event.getKeyCode());
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			submitResult(Score.JUDGE_YES);
		}
		
		return super.onKeyUp(keyCode, event);
	}	

	private void submitResult(int judge) {
			
		Intent intent = new Intent(this, WordDisplayActivity.class);
		intent.putExtra(Score.TAG_JUDGE, judge);
		this.startActivity(intent);
	}
	
}
