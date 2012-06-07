package jie.java.android.lingoshook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class PlayActivity extends Activity implements OnClickListener, OnTouchListener {

	private ViewSwitcher switcher;
	private FingerDrawView drawer = null;
	private WebView web = null;
	
	private Handler handler = null;
	private Runnable runnable = null;
	
	private boolean isWordShow = true;
	
	private Score.WordDisplayData dataWord = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.play);

		initViews();
		
		initDrawer();
		
		initWordData();
	}

	private void initWordData() {
		Score.refreshData();
	
		showWord();
	}

	private void initDrawer() {
		drawer = (FingerDrawView) switcher.findViewById(R.id.fingerDrawView1);
		
		handler = new Handler();
		
		runnable = new Runnable() {

			@Override
			public void run() {
				refreshDrawer();
			}
		};
		
		enableRefreshDrawer(true);		
	}

	private void enableRefreshDrawer(boolean enable) {
		if(enable) {
			handler.postDelayed(runnable, Setting.intervalFingerPanel);
		}
		else {
			handler.removeCallbacks(runnable);
		}
	}

	protected void refreshDrawer() {
		if(Setting.refeshFingerPanel) {
			drawer.clearCanvas();
			handler.postDelayed(runnable, Setting.intervalFingerPanel);
		}
	}

	private void initViews() {
		switcher = (ViewSwitcher) switcher.findViewById(R.id.viewSwitcher1);
		web = (WebView) switcher.findViewById(R.id.webView);
		
		setClickListener(true);
	}

	private void setClickListener(boolean enable) {
		if(enable) {
			switcher.findViewById(R.id.radio1).setOnClickListener(this);
			switcher.findViewById(R.id.radio2).setOnClickListener(this);
			switcher.findViewById(R.id.radio3).setOnClickListener(this);
			switcher.findViewById(R.id.radio4).setOnClickListener(this);
	    	
			switcher.findViewById(R.id.textWord).setOnClickListener(this);
		}
		else {
			switcher.findViewById(R.id.radio1).setOnClickListener(null);
			switcher.findViewById(R.id.radio2).setOnClickListener(null);
			switcher.findViewById(R.id.radio3).setOnClickListener(null);
			switcher.findViewById(R.id.radio4).setOnClickListener(null);
	    	
			switcher.findViewById(R.id.textWord).setOnClickListener(null);			
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			switcher.showNext();
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.radio1:
			onRadioClick(Score.SCORE_0);
			break;
		case R.id.radio2:
			onRadioClick(Score.SCORE_1);
			break;
		case R.id.radio3:
			onRadioClick(Score.SCORE_2);
			break;
		case R.id.radio4:
			onRadioClick(Score.SCORE_3);
			break;
		case R.id.textWord:
			onWordClick();
			break;
		default:
			break;
		}
	}

	private void showWord() {
		if(!isWordShow) {
			switcher.showPrevious();
		}
		
		drawer.clearCanvas();
		
		if(loadWordData() != 0) {
			setClickListener(false);
		}
		
		enableRefreshDrawer(true);
		
	}
	
    private int loadWordData() {
	 	
    	dataWord = new Score.WordDisplayData();
    	
    	if(Score.popWordData(dataWord) != 0)
    	{
    		Toast.makeText(this, this.getString(R.string.str_nomoreword), Toast.LENGTH_LONG).show();
    		return -1;
    	}
    	
    	showWordData(dataWord);
    	
    	if(Setting.loadSpeaker) {
    		Speaker.speak(dataWord.data.word);
    	}
    	
    	showSrcData();    	
    	
    	return 0;
    }

    private void showWordData(Score.WordDisplayData data) {
    	
    	((RadioButton)switcher.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio4)).setChecked(false);
    	
    	TextView tv = (TextView)switcher.findViewById(R.id.textWord);
    	tv.setText(data.data.word);
    	
    	tv = (TextView)switcher.findViewById(R.id.textScore);
    	//tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
    	tv.setText(String.format("%d", data.data.updated));
    	
    	tv = (TextView)switcher.findViewById(R.id.textType);
    	if(data.type == Score.WordType.NEW) {
    		tv.setText(this.getString(R.string.str_newword) + String.format("%d", data.rest));
    	}
    	else if(data.type == Score.WordType.OLD) {
    		tv.setText(this.getString(R.string.str_oldword) + String.format("%d", data.rest));
    	}
    	else if(data.type == Score.WordType.MISTAKE) {
    		tv.setText(this.getString(R.string.str_mistakeword) + String.format("%d", data.rest));
    	}
    	else {
    		tv.setText("NULL");
    	}    	
    }
    
    private int showSrcData() {
    	
    	if(!Setting.loadResultDisplay)
    		return 0;
    	
    	web.loadDataWithBaseURL(null, DBAccess.getHTML(dataWord.data.srcid), "text/html", "utf-8", null);
    	
    	return 0;		
    }  
	private void onRadioClick(int score) {
		
    	if(dataWord == null)
    		return;
    	
    	//_scoreWord = score;
    	
    	if(Setting.loadResultDisplay) {
//    		Intent intent = new Intent(this, ResultDisplayActivity.class);
//    		intent.putExtra(ResultDisplayActivity.ACTION, ResultDisplayActivity.ACTION_WORD);
//    		this.startActivity(intent);
    		switcher.showNext();
//    		showSrcView();
//    		this.setContentView(R.layout.result_display);
    	}
    	else {
 //   		updateWordData(Score.JUDGE_YES);
    		
    		showWord();
    	}
    }
    
	private void onWordClick() {
		if(dataWord == null)
			return;
   	
		Speaker.speak(dataWord.data.word);
	}    
}
