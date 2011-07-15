/**
 * file   : DisplayActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 4:46:59 PM
 */
package jie.java.android.savingkeeper;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DisplayActivity extends ListActivity {
	
	protected class DataCursorAdapter extends SimpleCursorAdapter {

		public DataCursorAdapter(Context context, int layout, Cursor c,	String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(GLOBAL.APP_TAG, "DISPLAY ACTIVITY");
		
		this.setContentView(R.layout.diaplay_activity);// android.R.layout.activity_list_item);
		
		Log.d(GLOBAL.APP_TAG, "1");
		Cursor cursor = GLOBAL.DBACCESS.query();
		
		Log.d(GLOBAL.APP_TAG, "2");
//		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] { DBAccess.TABLE_COLUMN_STRING, DBAccess.TABLE_COLUMN_INTEGER }, new int[] { android.R.id.text1, android.R.id.text2});
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.display_activity_row, cursor, new String[] { DBAccess.TABLE_COLUMN_STRING, DBAccess.TABLE_COLUMN_INTEGER, DBAccess.TABLE_COLUMN_INTEGER }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3});		
		this.setListAdapter(adapter);
		Log.d(GLOBAL.APP_TAG, "3");
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
				.show();
	}
}
