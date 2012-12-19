package jie.java.android.demodictionaryoflac2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(check() != 0) {
        	this.finish();
        }
    }

    private int check() {  
		return 0;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_UP) {
			showDictionaryActivity();
		}
		
		return true;
	}

	private void showDictionaryActivity() {

	}
    
}
