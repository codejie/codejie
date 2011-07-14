package jie.java.android.savingkeeper;

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
        
        GLOBAL.Init();    	
    

        btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Test.insertData();
				Toast.makeText(v.getContext(), "hello", 2).show();
				//Test.queryData();
				Test.startActivity(SavingKeeper.this);
				//Test.queryData();
				//Test.insertData();
/*				
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage("alert...........");
				builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
*/				
/*		        Intent intent = new Intent();
		        intent.setClassName("jie.java.android.savingkeeper", "MsgBoxActivity");
		        startActivity(intent);
*/		        	
			}
        	
        });
        
    }
}