package jie.java.android.demodictionaryoflac2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jie.java.android.demodictionaryoflac2.data.AssetsHelper;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
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
        
        setContentView(R.layout.activity_main);
        
        if(checkData() != 0) {
        	this.finish();
        }
        
        if(checkExpire() != 0) {
        	this.finish();
        }
        
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.relativeLayout);
        rl.setOnTouchListener(this);
    }

    private int checkExpire() {  
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
    
	private int checkData() {
		Global.DATA_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + Global.SD_ROOT;
		File f = new File(Global.DATA_ROOT);
		if(f.exists()) {
			return 0;
		}
		f.mkdirs();
		
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setMessage("Unzipping data...");
		dlg.show();

		try {

			final InputStream input = this.getAssets().open(Global.ASSETS_FILE);
			
			new Thread() {
				public void run() {				
					AssetsHelper.UnzipTo(input, Global.DATA_ROOT);
					dlg.dismiss();
				}
			}.start();
		
		} catch (IOException e) {
			return -1;
		}		
		
		return 0;
	}
}
