package jie.java.android.lingoshook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordDisplayActivity extends Activity implements OnClickListener {
	
	private FingerDrawView _viewDraw = null;
		
	private Score.WordDisplayData _dataWord = null;
	
	private static ResultDisplayActivity _result = null;
	
	private int _scoreWord	= -1;
	
	private Handler _handler = null;
	private Runnable _runnable = null;
	private boolean _isDisplay = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//this.getPreferences(MODE_PRIVATE).getBoolean("r_checkbox", true);
		
		this.setContentView(R.layout.word_display);
		
        initView();
        
        _runnable = new Runnable() {
			@Override
			public void run() {
				
				runRunnable();
			}
        };
        
        _handler = new Handler();
        _handler.postDelayed(_runnable, 1000);
        
		//Log.d(Global.APP_TITLE, "Word Activity count : " + WordDisplayActivity.getInstanceCount());
    }
	
	private void runRunnable() {
		if(_isDisplay) {
			//Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
			
			_viewDraw.clearCanvas();
			
			_handler.postDelayed(_runnable, 3000);
		}
	}

	@Override
	protected void onDestroy() {
			
		if(_result != null)
			_result.finish();
	
		super.onDestroy();
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
	protected void onResume() {
			
		super.onResume();
		
		_viewDraw.clearCanvas();
		
		if(loadWordData() != 0) {
			enableViews(false);
		}
		
		_isDisplay = true;
		_handler.post(_runnable);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		
		_handler.removeCallbacks(_runnable);
		_isDisplay = false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(Global.APP_TITLE, "Word - back key.");
			if(Setting.loadMistakeWord && Score.getMistakeWordCount() > 0) {
				Toast.makeText(this, "mistake word list", Toast.LENGTH_SHORT).show();
				Score.loadMistakeWordData();
				onResume();
				return false;
			}
			
			//this.finish();
			//return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		
		//Intent intent = this.getIntent();
		if(intent != null) {
			int judge = intent.getIntExtra(Score.TAG_JUDGE, -1);
			if(judge != -1) {
				updateWordData(judge);
			}
		}
				
		super.onNewIntent(intent);
	}
	
	private void enableViews(boolean enable) {
		if(enable) {
	    	this.findViewById(R.id.radio1).setOnClickListener(this);
	    	this.findViewById(R.id.radio2).setOnClickListener(this);
	    	this.findViewById(R.id.radio3).setOnClickListener(this);
	    	this.findViewById(R.id.radio4).setOnClickListener(this);
	    	
	    	this.findViewById(R.id.textWord).setOnClickListener(this);
		}
		else {
		   	this.findViewById(R.id.radio1).setOnClickListener(null);
	    	this.findViewById(R.id.radio2).setOnClickListener(null);
	    	this.findViewById(R.id.radio3).setOnClickListener(null);
	    	this.findViewById(R.id.radio4).setOnClickListener(null);
	    	
	    	this.findViewById(R.id.textWord).setOnClickListener(null);			
		}			
	}

	public static void setResultDisplay(Activity activity) {
		_result = (ResultDisplayActivity) activity;
	}
	
	private void initView() {
    	
		_viewDraw = (FingerDrawView)this.findViewById(R.id.fingerDrawView1);
		
		enableViews(true);
    }
 
	private void onRadioClick(int score) {
		
    	if(_dataWord == null)
    		return;
    	
    	_scoreWord = score;
    	
    	Intent intent = new Intent(this, ResultDisplayActivity.class);
//    	intent.putExtra(Score.TAG_WORDID, dataWord.wordid);
//    	intent.putExtra(Score.TAG_SRCID, dataWord.srcid);
//    	intent.putExtra(Score.TAG_UPDATED, dataWord.updated);
//    	intent.putExtra(Score.TAG_PRESCORE, dataWord.score);
//    	intent.putExtra(Score.TAG_SCORE, score);
    	
    	this.startActivity(intent);
    }
    
	private void onWordClick() {
		
		if(_dataWord == null)
			return;
		speakWord(_dataWord.data.word);
	}
    
    private int loadWordData() {
    	 	
    	_dataWord = new Score.WordDisplayData();
    	if(Score.popWordData(_dataWord) != 0)
    	{
    		Toast.makeText(this, "No any word in db now.", Toast.LENGTH_LONG).show();
    		return -1;
    	}

    	((RadioButton)this.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio4)).setChecked(false);
    	
    	TextView tv = (TextView)this.findViewById(R.id.textWord);
    	tv.setText(_dataWord.data.word);
    	
    	tv = (TextView)this.findViewById(R.id.textScore);
    	//tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
    	tv.setText(String.format("%d", _dataWord.data.updated));
    	
    	tv = (TextView)this.findViewById(R.id.textType);
    	if(_dataWord.type == Score.WordType.NEW) {
    		tv.setText(this.getString(R.string.str_newword) + String.format("%d", _dataWord.rest));
    	}
    	else if(_dataWord.type == Score.WordType.OLD) {
    		tv.setText(this.getString(R.string.str_oldword) + String.format("%d", _dataWord.rest));
    	}
    	else if(_dataWord.type == Score.WordType.MISTAKE) {
    		tv.setText(this.getString(R.string.str_mistakeword) + String.format("%d", _dataWord.rest));
    	}
    	else {
    		tv.setText("NULL");
    	}
    	
    	saveSrcData();
    	
    	if(_result != null) {
    		_result.loadData();
    	}
    	
    	speakWord(_dataWord.data.word);
    	
    	return 0;
    }
    
    private int saveSrcData() {
    	
		try {
			File file = new File(Environment.getExternalStorageDirectory() + Score.CACHE_FILE);
    		BufferedWriter bw = new BufferedWriter(new FileWriter(file), 4096);
    		
    		bw.write(DBAccess.getHTML(_dataWord.data.srcid));
    		
    		bw.close();
    	}
    	catch (IOException e) {
    		Log.e(Global.APP_TITLE, "io exception - " + e.toString());
    		return -1;
    	}
    	
    	return 0;		
    }
    
    private int updateWordData(int judge) {
    	return Score.updateWordData(_dataWord.data, _scoreWord, judge);   	
    }
    
    private void speakWord(final String word) {
    	Speaker.speak(word);
    }
}
