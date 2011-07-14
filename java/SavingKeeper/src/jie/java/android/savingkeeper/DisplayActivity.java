/**
 * file   : DisplayActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 4:46:59 PM
 */
package jie.java.android.savingkeeper;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class DisplayActivity extends ListActivity {
	
	//private Cursor cursor = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(GLOBAL.APP_TAG, "DISPLAY ACTIVITY");
		
		//this.setContentView(android.R.layout.activity_list_item);
		
		Log.d(GLOBAL.APP_TAG, "1");
		Cursor cursor = GLOBAL.DBACCESS.query();
		
		Log.d(GLOBAL.APP_TAG, "2");
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] { DBAccess.TABLE_COLUMN_STRING, DBAccess.TABLE_COLUMN_INTEGER }, new int[] { android.R.id.text1, android.R.id.text2});
		this.setListAdapter(adapter);
		Log.d(GLOBAL.APP_TAG, "3");
	}
	
}
