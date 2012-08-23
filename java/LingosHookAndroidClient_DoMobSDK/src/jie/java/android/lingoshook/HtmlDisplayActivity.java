package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class HtmlDisplayActivity extends Activity implements OnTouchListener {

	public static String REQ		=	"req";
	public static String ID			=	"id";
	public static String SRCID		=	"srcid";
	public static String WORD		=	"w";
	public static int REQ_HELP		=	0;
	public static int REQ_WORD		=	1;
	
	private WebView web = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.html_display);
		
        if(Global.STATE_CODING == 2) {
        	intiAdView();
        }
		
		web = (WebView) this.findViewById(R.id.webView1);		
		
		Intent intent = this.getIntent();
		if(intent != null) {
			if(intent.getExtras().getInt(REQ) == REQ_WORD) {
				web.setOnTouchListener(this);
				//loadWordData(intent.getExtras().getString(WORD));
				loadWordSrc(Integer.parseInt(intent.getExtras().getString(SRCID)));
			}
			else {
				loadHelpInfo();
			}
		}
		else {
			loadHelpInfo();
		}
	}

	private void loadWordData(final String word) {
		web.loadDataWithBaseURL(null, DBAccess.getHTMLbyWord(word), "text/html", "utf-8", null);
	}
	
	private void loadWordSrc(int srcid) {
		web.loadDataWithBaseURL(null, DBAccess.getHTML(srcid), "text/html", "utf-8", null);
	}

	private void loadHelpInfo() {
		web.loadUrl("file:///android_asset/info.html");
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		this.finish();
		return true;
	}

	private void intiAdView() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout1);
		new AdPanelView(this, ll, 0, Global.SCREEN_HEIGHT - 80);
	}	
}
