package jie.java.android.lingoshook;

import jie.java.android.savingkeeper.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {

	private static final int DIALOG_IMPORT		=	1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initObjects();
        
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
    	switch(item.getItemId()) {
    	case R.id.menu_exit:
    		onMenuExit();
    		break;
    	case R.id.menu_import:
    		onMenuImport();
    		break;
    	default:
    		break;
    	}
		return super.onMenuItemSelected(featureId, item);
	}
    
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dlg = null;
		switch(id) {
		case DIALOG_IMPORT: {
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_dialog_info);
    		String dir = Environment.getExternalStorageDirectory().getPath();
    		String file = "lingoshook.db";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.file_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
    		build.setView(v);
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				_backupPath = d.getText().toString();
    				_backupFilename = f.getText().toString();
    				exportSavingList();
    				dialog.dismiss();
    			}    			
    		});
    		build.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();					
				}
			});
    		
    		dlg = build.create();
			
			}
			break;
		}
		return super.onCreateDialog(id);
	}    
    
	private int initObjects() {
		if(DBAccess.init(Global.DATABASE_NAME) != 0)
			return -1;
		return 0;
	}
	
	private void onMenuExit() {
		Global.exitApplication();
	}
	
	private void onMenuImport() {
		
        LayoutInflater factory = LayoutInflater.from(this);
        final View v = factory.inflate(R.layout.import_dialog, null);
        
		
		DBAccess.importData(DBAccess.IMPORTTYPE_OVERWRITE, );
	}

}