package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class WordDisplayActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.word_display);
        initView();
    }
    
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		DBAccess.release();
		super.finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) {
		case R.id.radio1:
		case R.id.radio2:
		case R.id.radio3:
		case R.id.radio4:
			onRadioClick();
			break;
		default:
			break;
		}
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
    
    private void onRadioClick() {
    	this.startActivity(new Intent(this, ResultDisplayActivity.class));
    }
}
