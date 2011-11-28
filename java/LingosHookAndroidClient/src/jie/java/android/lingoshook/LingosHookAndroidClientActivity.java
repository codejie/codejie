package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LingosHookAndroidClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initView();
    }
    
    void initView() {
    	LinearLayout ll = (LinearLayout)this.findViewById(R.id.linearLayout2);
    	
//    	if(ll.getOrientation() == LinearLayout.HORIZONTAL) {
//    		LinearLayout p = (LinearLayout)this.findViewById(R.id.linearLayout1);
//    		p.setOrientation(LinearLayout.HORIZONTAL);
//    	}
//    	else {
//    		LinearLayout p = (LinearLayout)this.findViewById(R.id.linearLayout1);
//    		p.setOrientation(LinearLayout.VERTICAL);    		
//    	}    	
    	
    	addFingerDrawView(this, ll);
    	addAdPanelView(this, ll);
    }
    
    void addFingerDrawView(Context context, LinearLayout parent) {
    	parent.addView(new FingerDrawView(context));
    }
    
    void addAdPanelView(Context context, LinearLayout parent) {
    	parent.addView(new AdPanelView(context));    	    	
    }
}