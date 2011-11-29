package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class WordDisplayActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        initView();
    }
    
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

	}
	
    void initView() {
    	LinearLayout ll = (LinearLayout)this.findViewById(R.id.linearLayout2);
    	addFingerDrawView(this, ll);
    	addAdPanelView(this, ll);
    }
    
    void addFingerDrawView(Context context, LinearLayout parent) {
    	   	
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);    	
    	parent.addView(new FingerDrawView(context));//, params);
    }
    
    void addAdPanelView(Context context, LinearLayout parent) {
    	//LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
    	parent.addView(new AdPanelView(context));//, params);    	    	
    }	
}
