package jie.java.android.demodictionaryoflac2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jie.java.android.demodictionaryoflac2.data.AssetsHelper;
import jie.java.android.demodictionaryoflac2.data.DBAccess;
import jie.java.android.demodictionaryoflac2.data.Dictionary;
import jie.java.android.demodictionaryoflac2.data.Speaker;
import jie.java.android.demodictionaryoflac2.data.XmlTranslator;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnTouchListener {


	private static final int MSG_CHECKDATA_DONE		=	0;
	private static final int MSG_CHECKEXPIRE_FAIL	=	1;
	private static final int DIALOG_EXPIRE	=	1;
	private static final int DIALOG_ABOUT	=	2;
	
	private Handler handler = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
               
        initHandler();        
        
        if(checkData() != 0) {
        	this.finish();
        }
        
//        if(checkExpire() != 0) {
//
//			this.showDialog(DIALOG_EXPIRE);
//        }
                        
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
    		        if(checkExpire() != 0) {
    		        	MainActivity.this.showDialog(DIALOG_EXPIRE);
    		        }
    		        break;
    			case MSG_CHECKEXPIRE_FAIL:
    				MainActivity.this.finish();
    				break;
    			}
    			super.handleMessage(msg);
    		}    		
    	};    	
	}

	private int initData() {

    	if(DBAccess.init(Global.SDCARD_ROOT + Global.DATA_ROOT + Global.DB_FILE) != 0)
    		return -1;
    	try {
			XmlTranslator.init(this.getResources().getAssets().open("ld2.xsl"));
		} catch (IOException e) {
			return -1;
		}
		Dictionary.open(DBAccess.instance());
    	
//    	if(DataFileAccess.init(Global.SDCARD_ROOT + Global.DATA_ROOT + Global.LD2_FILE) != 0)
//    		return -1;
    	return 0;
	}


	private int checkExpire() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date expire = sf.parse("2013-06-31");
			Date now = new Date();
			if(now.compareTo(expire) > 0) {
				return -1;
			}
		} catch (ParseException e) {
			return -1;
		}

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
	
	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case DIALOG_EXPIRE:
			builder.setTitle("Demo has Expired");
			builder.setMessage("Please visit www.codejie.tk to get more information.");
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					handler.sendEmptyMessage(MSG_CHECKEXPIRE_FAIL);
				}
				
			});
			break;
		case DIALOG_ABOUT:
			builder.setTitle("Demo Dictionary of LAC");
			builder.setMessage("Please visit www.codejie.tk to get more information.");
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
				
			});
			break;
		}
		return builder.create();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.item_about) {
			this.showDialog(DIALOG_ABOUT);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
