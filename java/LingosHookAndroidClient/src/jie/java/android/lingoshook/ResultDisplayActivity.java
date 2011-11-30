package jie.java.android.lingoshook;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

public class ResultDisplayActivity extends Activity implements OnTouchListener {

	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.result_display);
		
		web = (WebView)this.findViewById(R.id.webView1);
		web.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		web.loadUrl("file://\\sdcard\\a.html");
		return false;
	}

}
