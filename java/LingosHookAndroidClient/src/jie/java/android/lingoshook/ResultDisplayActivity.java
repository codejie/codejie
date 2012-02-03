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

	public static String ACTION		=	"action";
	public static int ACTION_WORD	=	1;
	public static int ACTION_HELP	=	2;
	
	private boolean isWord			=	true;
	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result_display);
		
		web = (WebView)this.findViewById(R.id.webView);
		
//		web.setOnTouchListener(this);
		
//		WordDisplayActivity.setResultDisplay(this);
		
		Intent intent = this.getIntent();
		if(intent != null) {
			int act = intent.getExtras().getInt(ACTION);
			if(act == ACTION_WORD) {
				isWord = true;
				loadData();
			}
			else {
				isWord = false;
				loadHelp();
			}
		}
		
		initButtons(isWord);
/*		
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
*/		
		//loadData();
	}
/*	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		if(intent != null) {
			int act = intent.getExtras().getInt(ACTION);
			if(act == ACTION_WORD) {
				loadData();
			}
			else {
				web.loadUrl("file://android_asset/info.html");
			}
		}
		
		super.onNewIntent(intent);
	}
*/
/*
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		web.loadUrl("file://\\sdcard\\a.html");
		return false;
	}
*/
	
	private void loadHelp() {
		web.loadUrl("file:////ContentRoot/assets/info.html");
	}

	private void initButtons(boolean display) {
		if(display) {
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
		}
		else {
			this.findViewById(R.id.btnYes).setVisibility(View.GONE);
			this.findViewById(R.id.btnNo).setVisibility(View.GONE);
		}
	}

	public void loadData() {
		//web.loadUrl("file://android_asset/info.html");
		//web.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/info.html");
		web.loadUrl("file://" + Environment.getExternalStorageDirectory() + Score.CACHE_FILE);
		//web.loadUrl("file:///sdcard/t.html");
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "key - " + keyCode + " event - " + event.getKeyCode());
		
		if(isWord) {
			if(keyCode == KeyEvent.KEYCODE_BACK) {
				submitResult(Score.JUDGE_YES);
			}
		}
		
		return super.onKeyUp(keyCode, event);
	}	

	private void submitResult(int judge) {
			
		Intent intent = new Intent(this, WordDisplayActivity.class);
		intent.putExtra(Score.TAG_JUDGE, judge);
		this.startActivity(intent);
	}
	
}
