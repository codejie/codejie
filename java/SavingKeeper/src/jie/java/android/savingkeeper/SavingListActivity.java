/**
 * file   : SavingListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 15, 2011 5:29:13 PM
 */
package jie.java.android.savingkeeper;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SavingListActivity extends ListActivity {

	protected class DataCursorAdapter extends SimpleCursorAdapter {

		public DataCursorAdapter(Context context, int layout, Cursor c,	String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
		@Override
		public void setViewText(TextView v, String text) {
			if(v.getId() == R.id.textView1)
				text += "this is view1";
			else
				text += "unknown";
			super.setViewText(v, text);
		}
		
	}
	
	protected class SavingView extends LinearLayout {
		
	}
	
}
