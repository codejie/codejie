package jie.java.android.lingoshook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordDisplayActivity extends Activity implements OnClickListener {
	
	private FingerDrawView _viewDraw = null;
	
	private Score.WordData _dataWord = null;
	private static ResultDisplayActivity _result = null;
	
	private int _scoreWord	= -1;
	
	private Handler _handler = null;
	private Runnable _runnable = null;
	private boolean _isDisplay = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.word_display);
		
        initView();
        
        _runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub	
		if(_result != null)
			_result.finish();
	
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub	
		loadWordData();

		_isDisplay = true;
		_handler.post(_runnable);
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		_handler.removeCallbacks(_runnable);
		_isDisplay = false;
		
		super.onPause();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(Global.APP_TITLE, "Word - back key.");
			//this.finish();
			//return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		//Intent intent = this.getIntent();
		if(intent != null) {
			int judge = intent.getIntExtra(Score.TAG_JUDGE, -1);
			if(judge != -1) {
				updateWordData(judge);
			}
		}
				
		super.onNewIntent(intent);
	}

	public static void setResultDisplay(Activity activity) {
		_result = (ResultDisplayActivity) activity;
	}
	
	private void initView() {
    	LinearLayout ll = null;//(LinearLayout)this.findViewById(R.id.linearLayout2);

    	addFingerDrawView(this, ll);
    	addAdPanelView(this, ll);
    	
    	this.findViewById(R.id.radio1).setOnClickListener(this);
    	this.findViewById(R.id.radio2).setOnClickListener(this);
    	this.findViewById(R.id.radio3).setOnClickListener(this);
    	this.findViewById(R.id.radio4).setOnClickListener(this);
    	
    	this.findViewById(R.id.textWord).setOnClickListener(this);
    }
    
    private void addFingerDrawView(Context context, LinearLayout parent) {
    	
    	//XmlPullParser parser = this.getResources().getXml(R.layout.fingerdrawview);
    	//AttributeSet attributes = Xml.asAttributeSet(parser);
    	//LayoutParams params = new LinearLayout.LayoutParams(this, attributes);
    	
    	
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	
    	
    	_viewDraw = (FingerDrawView)this.findViewById(R.id.fingerDrawView1);// new FingerDrawView(context);
    	//parent.addView(_viewDraw, params);
    }
    
    private void addAdPanelView(Context context, LinearLayout parent) {
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
/*    	
        <TextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="TextView" />

</LinearLayout>
*/
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		//parent.addView(new AdPanelView(context), params);    	    	
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
		// TODO Auto-generated method stub
		if(_dataWord == null)
			return;
		speakWord(_dataWord.word);
	}
    
    private int loadWordData() {
    	_dataWord = Score.popWordData();
    	if(_dataWord == null)
    	{
    		Toast.makeText(this, "No any word in db now.", Toast.LENGTH_LONG).show();
    		return -1;
    	}

    	((RadioButton)this.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio4)).setChecked(false);
    	
    	TextView tv = (TextView)this.findViewById(R.id.textWord);
    	tv.setText(_dataWord.word);
    	
    	tv = (TextView)this.findViewById(R.id.textScore);
    	//tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
    	tv.setText(String.format("%d", _dataWord.updated));
    	 	
    	saveSrcData();
    	
    	if(_result != null) {
    		_result.loadData();
    	}
    	
    	speakWord(_dataWord.word);
    	
    	return 0;
    }
    
    private int saveSrcData() {
    	
		try {
			File file = new File(Environment.getExternalStorageDirectory() + Score.CACHE_FILE);
    		BufferedWriter bw = new BufferedWriter(new FileWriter(file), 4096);
    		
    		bw.write(DBAccess.getHTML(_dataWord.srcid));
    		
    		bw.close();
    	}
    	catch (IOException e) {
    		Log.e(Global.APP_TITLE, "io exception - " + e.toString());
    		return -1;
    	}
    	
    	return 0;		
    }
    
    private int updateWordData(int judge) {
    	return Score.updateWordData(_dataWord, _scoreWord, judge);   	
    }
    
    private void speakWord(final String word) {
    	Speaker.speak(word);
    }
}
