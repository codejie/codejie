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
	
	private static final int DIALOG_LOADMISTAKEWORD = 0;

	private FingerDrawView _viewDraw = null;
		
	private Score.WordDisplayData _dataWord = null;
	
	private static ResultDisplayActivity _result = null;
	
	private int _scoreWord	= -1;
	
	private Handler _handler = null;
	private Runnable _runnable = null;
	//private boolean _isDisplay = true;

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
        
        enableRefreshFingerView(true);
        
		Score.refreshData();

		//Log.d(Global.APP_TITLE, "Word Activity count : " + WordDisplayActivity.getInstanceCount());
    }
	
	private void runRunnable() {
		if(Setting.refeshFingerPanel) {
			//Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
			
			_viewDraw.clearCanvas();
			
			_handler.postDelayed(_runnable, Setting.intervalFingerPanel);
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
				
		showWord();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		
		enableRefreshFingerView(false);
		//_handler.removeCallbacks(_runnable);
		//_isDisplay = false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//Log.d(Global.APP_TITLE, "Word - back key.");
			if(Setting.loadMistakeWord && Score.getMistakeWordCount() > 0) {
				//Toast.makeText(this, "mistake word list", Toast.LENGTH_SHORT).show();
				this.showDialog(DIALOG_LOADMISTAKEWORD);
				return true;
			}
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
				onResume();
			}
			else {
				this.finish();
			}
			break;
		default:
			break;
		}		
	}

	private void enableRefreshFingerView(boolean enable) {
		if(enable) {
			//_isDisplay = true;
			_handler.postDelayed(_runnable, Setting.intervalFingerPanel);
		}
		else {
			//_isDisplay = false;
			_handler.removeCallbacks(_runnable);
		}
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
	
	private void showWord() {
		
		_viewDraw.clearCanvas();
		
		if(loadWordData() != 0) {
			enableViews(false);
		}
		
		enableRefreshFingerView(true);		
	}
 
	private void onRadioClick(int score) {
		
    	if(_dataWord == null)
    		return;
    	
    	_scoreWord = score;
    	
    	if(Setting.loadResultDisplay) {
    		Intent intent = new Intent(this, ResultDisplayActivity.class);
    		intent.putExtra(ResultDisplayActivity.ACTION, ResultDisplayActivity.ACTION_WORD);
    		this.startActivity(intent);
    	}
    	else {
    		updateWordData(Score.JUDGE_YES);
    		
    		showWord();
    	}
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
    		Toast.makeText(this, this.getString(R.string.str_nomoreword), Toast.LENGTH_LONG).show();
    		return -1;
    	}
    	
    	showWordData(_dataWord);
    	
    	showSrcData();
    	
    	if(Setting.loadSpeaker) {
    		speakWord(_dataWord.data.word);
    	}
    	
    	return 0;
    }
    
    private void showWordData(Score.WordDisplayData data) {
    	
    	((RadioButton)this.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio4)).setChecked(false);
    	
    	TextView tv = (TextView)this.findViewById(R.id.textWord);
    	tv.setText(data.data.word);
    	
    	tv = (TextView)this.findViewById(R.id.textScore);
    	//tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
    	tv.setText(String.format("%d", data.data.updated));
    	
    	tv = (TextView)this.findViewById(R.id.textType);
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
		
//    	if(_result != null) {
//    		_result.loadData();
//    	}
    	
    	return 0;		
    }
    
    private int updateWordData(int judge) {
    	return Score.updateWordData(_dataWord.data, _scoreWord, judge);   	
    }
    
    private void speakWord(final String word) {
    	Speaker.speak(word);
    }
}
