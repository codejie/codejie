package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.findViewById(R.id.relativeLayout1).setOnTouchListener(this);
        this.findViewById(R.id.relativeLayout1).setLongClickable(true);
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP) {
			Intent intent = new Intent(this, WordDisplayActivity.class);
			this.startActivity(intent);
			this.finish();			
		}
		return false;
	}
}