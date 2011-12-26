package jie.java.android.lingoshook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {

	private static final int DIALOG_IMPORT		=	1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Global.initApplication(this);
        
        setContentView(R.layout.main);
        
        this.findViewById(R.id.relativeLayout1).setOnTouchListener(this);
        this.findViewById(R.id.relativeLayout1).setLongClickable(true);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Global.exitApplication();
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP) {
			Intent intent = new Intent(this, WordDisplayActivity.class);
			this.startActivity(intent);
			//this.finish();			
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
    	case R.id.menu_score_list:
    		onMenuScoreList();
    		break;
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
    		String file = "clientdata.db3";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.import_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
    		build.setView(v);
    		final CheckBox c = (CheckBox)v.findViewById(R.id.checkBox1);
    		
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				importData(d.getText().toString(), f.getText().toString(), c.isChecked());
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
		default:
			break;
		}
		return dlg;
	}    
	
	private void importData(final String dir, final String file, boolean overwrite) {
		if(DBAccess.importData(dir + "/" + file, (overwrite ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) != 0) {
			Toast.makeText(this, "Import data failed.", Toast.LENGTH_LONG).show();
		}
		else {
			Score.init();
		}
	}
	
	private void onMenuExit() {
		Global.exitApplication();
	}
	
	private void onMenuImport() {
		this.showDialog(DIALOG_IMPORT);
	}
	
	private void onMenuScoreList() {
		// TODO Auto-generated method stub
		this.startActivity(new Intent(this, ScoreListActivity.class));
	} 

}