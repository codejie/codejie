package jie.java.android.demodictionaryoflac2;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
			InputStream is = this.getAssets().open("data/lac2.db");
			int i = is.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Global.ASSETS_PATH += this.getPackageName();
        
        setContentView(R.layout.activity_main);
        
        if(check() != 0) {
        	this.finish();
        }
        
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.relativeLayout);
        rl.setOnTouchListener(this);
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
		this.startActivity(new Intent(this, DictionaryActivity.class));
		this.finish();
	}
    
}
