package jie.java.android.lingoshook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class PlayActivity extends Activity implements OnClickListener, OnTouchListener {

	private static final int DIALOG_LOADMISTAKEWORD = 0;
	
	private ViewSwitcher switcher;
	private TranslateAnimation aniResultIn = null;
	private TranslateAnimation aniWord = null;
	private TranslateAnimation aniResultOut = null;
	
	private FingerDrawView drawer = null;
	private WebView web = null;
	
	private Handler handler = null;
	private Runnable runRefreshDrawer = null;
	private Runnable runLoadWord = null;
	private Message msgRefreshDrawer = null;
	private Message msgLoadWord = null;
	
	private boolean isWordShow = true;
	
	private Score.WordDisplayData dataWord = null;
	private int scoreWord = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.play);

		initHandler();
		
		initAnination();
		
		initViews();
		
		initDrawer();
		
		initWordData();
	}

	private void initHandler() {
		
		msgRefreshDrawer = new Message();
		msgRefreshDrawer.what = 2;
		msgLoadWord = new Message();
		msgLoadWord.what = 1;
		
		handler = new Handler() {
	        public void handleMessage(Message msg) {
	            if(msg.what == 2) {
	        		if(msg.arg1 == 1) {
	        			handler.postDelayed(runRefreshDrawer, Setting.intervalFingerPanel);
	        		}
	        		else {
	        			handler.removeCallbacks(runRefreshDrawer);
	        		}
	            }
	            else if(msg.what == 1) {
	            	handler.post(runLoadWord);
	            }
	            
	        }			
		};
		
		runLoadWord = new Runnable() {

			@Override
			public void run() {
				loadWordData();
			}
			
		};	
		
		runRefreshDrawer = new Runnable() {

			@Override
			public void run() {
				refreshDrawer();
			}
		};		
	}
	
	private void initAnination() {
		
		Global.getScreenInfo(this);
		
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Global.SCREEN_HEIGHT = displaymetrics.heightPixels;
        Global.SCREEN_WIDTH = displaymetrics.widthPixels;
        
        if(Global.SCREEN_HEIGHT > Global.SCREEN_WIDTH) {		
			aniResultIn = new TranslateAnimation(Global.SCREEN_WIDTH, 0, 0, 0);
			aniResultIn.setDuration(700);
			aniWord = new TranslateAnimation(0, 0, 0, 0);
			aniWord.setDuration(700);
			aniResultOut = new TranslateAnimation(0, Global.SCREEN_WIDTH, 0, 0);
			aniResultOut.setDuration(500);
        }
        else {
        	aniResultIn = new TranslateAnimation(0, 0, Global.SCREEN_HEIGHT, 0);
			aniResultIn.setDuration(700);
			aniWord = new TranslateAnimation(0, 0, 0, 0);
			aniWord.setDuration(700);
			aniResultOut = new TranslateAnimation(0, 0, 0, Global.SCREEN_HEIGHT);
			aniResultOut.setDuration(500);
        }
		
	}

	private void initWordData() {
		Score.refreshData();		
		showWord();
	}

	private void initDrawer() {
		drawer = (FingerDrawView) switcher.findViewById(R.id.fingerDrawView1);
		
		handler.dispatchMessage(msgRefreshDrawer);
	}

	protected void refreshDrawer() {
		if(Setting.refeshFingerPanel) {
			drawer.clearCanvas();
		}
		handler.dispatchMessage(msgRefreshDrawer);
	}

	private void initViews() {
		switcher = (ViewSwitcher) this.findViewById(R.id.viewSwitcher1);
		
		setClickListener(true);
		
		web = (WebView) switcher.findViewById(R.id.webView);
		if(Setting.webclickable) {
			web.setOnTouchListener(this);
			switcher.findViewById(R.id.btnNo).setVisibility(View.GONE);
			Button btnYes = (Button)switcher.findViewById(R.id.btnYes);
			
			btnYes.setText(R.string.title_no);			
			btnYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					submitResult(Score.JUDGE_NO);
				}
				
			});
			
		}
		else {
			switcher.findViewById(R.id.btnYes).setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					submitResult(Score.JUDGE_YES);
				}
				
			});
			
			switcher.findViewById(R.id.btnNo).setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					submitResult(Score.JUDGE_NO);
				}
				
			});
		}
	}

	protected void submitResult(int judge) {
		updateWordScore(judge);
		
		showWord();
	}

	private void updateWordScore(int judge) {
		if(dataWord != null && dataWord.data != null) {
			Score.updateWordData(dataWord.data, scoreWord, judge);
		}
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
		if(Setting.webclickable) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				submitResult(Score.JUDGE_YES);
			}
		}
		return true;
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

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//Log.d(Global.APP_TITLE, "Word - back key.");
			if(Score.getMistakeWordCount() > 0) {
				//Toast.makeText(this, "mistake word list", Toast.LENGTH_SHORT).show();
				this.showDialog(DIALOG_LOADMISTAKEWORD);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void showWord() {

		//handler.dispatchMessage(msgLoadWord);
		
		if(!isWordShow) {
			isWordShow = true;

			switcher.clearAnimation();
			switcher.setInAnimation(aniWord);
			switcher.setOutAnimation(aniResultOut);
			switcher.showPrevious();		
		}		
		
		handler.dispatchMessage(msgLoadWord);
		
		drawer.clearCanvas();

		msgRefreshDrawer.arg1 = 1;
		handler.dispatchMessage(msgRefreshDrawer);
	}
	
	private void showResult() {
		if(isWordShow) {
			isWordShow = false;
			
			msgRefreshDrawer.arg1 = 2;
			handler.dispatchMessage(msgRefreshDrawer);			
			
			switcher.clearAnimation();
			switcher.setInAnimation(aniResultIn);
			switcher.setOutAnimation(aniWord);	
			switcher.showNext();
		}
	}
	
    private int loadWordData() {
	 	
    	dataWord = new Score.WordDisplayData();
    	
    	if(Score.popWordData(dataWord) != 0)
    	{
			if(Score.getMistakeWordCount() > 0) {
				//Toast.makeText(this, "mistake word list", Toast.LENGTH_SHORT).show();
				this.showDialog(DIALOG_LOADMISTAKEWORD);
				return -1;
			}
			else {
	    		Toast.makeText(this, this.getString(R.string.str_today_nomoreword), Toast.LENGTH_LONG).show();
	    		this.finish();
	    		return -1;
			}
    	}
    	
    	showWordData(dataWord);
    	    	
    	showSrcData();
    	
    	if(Setting.loadSpeaker) {
    		Speaker.speak(dataWord.data.word);
    	}   	
    	    	
    	return 0;
    }

    private void showWordData(Score.WordDisplayData data) {
    	    	
    	TextView tv = (TextView)switcher.findViewById(R.id.textWord);
    	tv.setText(data.data.word);
    	
//    	tv = (TextView)switcher.findViewById(R.id.textScore);
    	//tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
//    	tv.setText(String.format("%d", data.data.updated));
    	
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
    	
    	((RadioButton)switcher.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)switcher.findViewById(R.id.radio4)).setChecked(false);
    	
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
    	
    	scoreWord = score;
    	
    	if(Setting.loadResultDisplay) {
    		showResult();
    	}
    	else {
    		updateWordScore(Score.JUDGE_YES);
    		
    		showWord();
    	}
    }
    
	private void onWordClick() {
		if(dataWord == null)
			return;
   	
		Speaker.speak(dataWord.data.word);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_LOADMISTAKEWORD: {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.str_loadmistakeword);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDialogClick(DIALOG_LOADMISTAKEWORD, android.R.id.button1);
					dialog.dismiss();
					
				}
			});
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDialogClick(DIALOG_LOADMISTAKEWORD, android.R.id.button2);
					dialog.dismiss();					
				}
			});
			dlg = builder.create();
			}
			break;
		default:
			break;
			
		}
		return dlg;
	}
	
	protected void onDialogClick(int id, int which) {
		switch(id)
		{
		case DIALOG_LOADMISTAKEWORD:
			if(which == android.R.id.button1) {
				Score.loadMistakeWordData();
//				showWord();//onResume();
				loadWordData();
			}
			else {
				this.finish();
			}
			break;
		default:
			break;
		}		
	}	
}
