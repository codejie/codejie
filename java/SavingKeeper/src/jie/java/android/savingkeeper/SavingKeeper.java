package jie.java.android.savingkeeper;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SavingKeeper extends Activity {
    /** Called when the activity is first created. */
	private Button btn = null;
	
	private static final int DIALOG_CHG_PASSWD		=	1;
	private static final int DIALOG_ABOUT			=	2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GLOBAL.init();    	
       
        btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	        	String passwd = SavingKeeper.this.getPasswd();// text.getText().toString();
				String p = GLOBAL.DBACCESS.getConfigValue(DBAccess.CONFIG_ID_PASSWD);
				if(passwd.equals(p)) {
				
					Intent intent = new Intent(SavingKeeper.this, SavingListActivity.class);
					SavingKeeper.this.startActivity(intent);
					SavingKeeper.this.finish();
				}
				else {
					Toast.makeText(SavingKeeper.this, "Password is incorrect.", 1).show();
				}
			}
        	
        });
     
    }
    
    public String getPasswd() {
        EditText text = (EditText) this.findViewById(R.id.editText1);
        return text.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.item1:
    		break;
    	case R.id.item2:
        	GLOBAL.close();
        	System.exit(0);
    		break;
    	case R.id.item3:
    		break;
    	default:
    		break;
    	}
    	return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dlg = null;
    	
    	switch(id) {
    	case DIALOG_CHG_PASSWD: {
    		LayoutInflater factory = LayoutInflater.from(this);
    		final View v = factory.inflate(R.layout.chgpasswd, null);
    		
    		Builder build = new AlertDialog.Builder(this);
    		build.setTitle(R.string.title_chgPasswd);
    		build.setView(v);
    		
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
    		
    		build.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
    		
    		}
    		break;
    	case DIALOG_ABOUT: {
    		LayoutInflater factory = LayoutInflater.from(this);
    		final View v = factory.inflate(R.layout.about, null);
    		
    		Builder build = new AlertDialog.Builder(this);
    		build.setTitle(R.string.title_about);
    		build.setView(v);
    		
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
    		
    		}
    		break;
    	}
    	
    	return dlg;
    }
}