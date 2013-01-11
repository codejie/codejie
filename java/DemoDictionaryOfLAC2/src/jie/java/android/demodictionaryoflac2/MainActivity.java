package jie.java.android.demodictionaryoflac2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jie.java.android.demodictionaryoflac2.data.AssetsHelper;
import jie.java.android.demodictionaryoflac2.data.DBAccess;
import jie.java.android.demodictionaryoflac2.data.DataFileAccess;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener {

	private static final int MSG_CHECKDATA_DONE		=	0;
	private static final int MSG_CHECKEXPIRE_FAIL	=	1;
	
	private Handler handler = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        initHandler();        
        
        if(checkData() != 0) {
        	this.finish();
        }
                        
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.relativeLayout);
        rl.setOnTouchListener(this);
    }

    private void initHandler() {
    	handler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			switch(msg.what) {
    			case MSG_CHECKDATA_DONE:
    		        initData();
    		        checkExpire();
    		        break;    		        
    			}
    			super.handleMessage(msg);
    		}    		
    	};    	
	}

	private int initData() {
    	if(DBAccess.init(Global.SDCARD_ROOT + Global.DATA_ROOT + Global.DB_FILE) != 0)
    		return -1;
    	if(DataFileAccess.init(Global.SDCARD_ROOT + Global.DATA_ROOT + Global.LD2_FILE) != 0)
    		return -1;
    	return 0;
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
		Global.SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
		final String sdroot = Global.SDCARD_ROOT + Global.DATA_ROOT;
		File f = new File(sdroot);
		if(f.exists() && f.isDirectory()) {
			f = new File(sdroot + Global.DB_FILE);
			if(f.exists() && f.isFile()) {
				handler.sendEmptyMessage(MSG_CHECKDATA_DONE);
				return 0;
			}
		} else {
			if(!f.mkdirs()) {
				return -1;
			}
		}
		
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setMessage("Unzipping data...");
		dlg.show();

		try {

			final InputStream input = this.getAssets().open(Global.ASSETS_FILE);
			
			new Thread() {
				public void run() {				
					AssetsHelper.UnzipTo(input, sdroot);
					dlg.dismiss();
					handler.sendEmptyMessage(MSG_CHECKDATA_DONE);
				}
			}.start();
		
		} catch (IOException e) {
			return -1;
		}		
		
		return 0;
	}
}
