package jie.java.android.lingoshook;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

public class HelpActivity extends Activity implements OnTouchListener {

	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.help);
		
		web = (WebView) this.findViewById(R.id.webView1);
		web.setOnTouchListener(this);
		
		loadHelpInfo();
	}

	private void loadHelpInfo() {
		web.loadUrl("file:///android_asset/info.html");
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		this.finish();
		return true;
	}

}
