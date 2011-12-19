package jie.java.android.lingoshook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class WordDisplayActivity extends Activity implements OnClickListener {
	
	private static Score.WordData dataWord = null;
	
	private ResultDisplayActivity act = null;//new ResultDisplayActivity();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.word_display);
        initView();
        
//        loadWordData();
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
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		loadWordData();
		
		super.onResume();
	}

	private void initView() {
    	LinearLayout ll = (LinearLayout)this.findViewById(R.id.linearLayout2);
    	addFingerDrawView(this, ll);
    	addAdPanelView(this, ll);
    	
    	this.findViewById(R.id.radio1).setOnClickListener(this);
    	this.findViewById(R.id.radio2).setOnClickListener(this);
    	this.findViewById(R.id.radio3).setOnClickListener(this);
    	this.findViewById(R.id.radio4).setOnClickListener(this);
    }
    
    private void addFingerDrawView(Context context, LinearLayout parent) {
    	   	
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);    	
    	parent.addView(new FingerDrawView(context));//, params);
    }
    
    private void addAdPanelView(Context context, LinearLayout parent) {
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
    	parent.addView(new AdPanelView(context));//, params);    	    	
    }
    
    private void onRadioClick(int score) {
    	Intent intent = new Intent(this, ResultDisplayActivity.class);
    	intent.putExtra(Score.TAG_WORDID, dataWord.wordid);
    	intent.putExtra(Score.TAG_SRCID, dataWord.srcid);
    	intent.putExtra(Score.TAG_UPDATED, dataWord.updated);
    	intent.putExtra(Score.TAG_PRESCORE, dataWord.score);
    	intent.putExtra(Score.TAG_SCORE, score);
    	
    	this.startActivity(intent);    	
    }
    
    private int loadWordData() {
    	dataWord = Score.popWordData();
    	if(dataWord == null)
    		return -1;

    	((RadioButton)this.findViewById(R.id.radio1)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio2)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio3)).setChecked(false);
    	((RadioButton)this.findViewById(R.id.radio4)).setChecked(false);
    	
    	TextView tv = (TextView)this.findViewById(R.id.textWord);
    	tv.setText(dataWord.word);
    	
    	tv = (TextView)this.findViewById(R.id.textScore);
    	tv.setText(String.format("%d", ((dataWord.updated > 0) ? Score.deltaUpdated - dataWord.updated : dataWord.updated)));
    	
    	saveSrcData();
    	
    	return 0;
    }
    
    private int saveSrcData() {
    	
		try {
			File file = new File(Environment.getExternalStorageDirectory() + Score.CACHE_FILE);
    		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    		
    		bw.write(DBAccess.getHTML(dataWord.srcid));
    		
    		bw.close();
    	}
    	catch (IOException e) {
    		Log.e(Global.APP_TITLE, "io exception - " + e.toString());
    		return -1;
    	}
    	
    	return 0;
		
    }
}
