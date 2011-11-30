package jie.android.java.whereisyourfinger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class WhereIsYourFingerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout ll = (LinearLayout)this.findViewById(R.id.root);
        ll.addView(new FingerDrawView(this), 0);
        
    }
}