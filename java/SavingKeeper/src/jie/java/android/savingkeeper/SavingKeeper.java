package jie.java.android.savingkeeper;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SavingKeeper extends Activity {
    /** Called when the activity is first created. */
	private Button btn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GLOBAL.init();    	

        //test();
        
        //Intent intent = new Intent(this, RateListActivity.class);
        Intent intent = new Intent(this, SavingListActivity.class);
		this.startActivity(intent);
		this.finish();
		//Test.startActivity(BankListActivity.this);
		//this.finish();
/*
        btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SavingKeeper.this, SavingListActivity.class);
				SavingKeeper.this.startActivity(intent);	        	
			}
        	
        });
*/        
    }
    
    protected void finalize() {
    	//GLOBAL.close();
    }
    
    private void test() {
    	Date endDate = Calendar.getInstance().getTime();
    	Log.d(GLOBAL.APP_TAG, "date = " + endDate.toString());
    	endDate.setMonth(endDate.getMonth() + 3);
    	Log.d(GLOBAL.APP_TAG, "date = " + endDate.toString());
    	endDate.setMonth(endDate.getMonth() + 3);
    	Log.d(GLOBAL.APP_TAG, "date = " + endDate.toString());
    }
}