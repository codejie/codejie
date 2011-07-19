/**
 * file   : SavingListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 15, 2011 5:29:13 PM
 */
package jie.java.android.savingkeeper;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SavingListActivity extends ListActivity {

	protected class DataCursorAdapter extends CursorAdapter {

		public DataCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}
	
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			SavingListView view = new SavingListView(context);
			
			return view;
		}
		
		public void bindView(View view, Context context, Cursor cursor) {
			((SavingListView)view).setTitle(cursor.getString(0), cursor.getString(1));
		}
	}
	
	protected class SavingListView extends LinearLayout {
		
		private TextView viewText;
		private TextView viewText2;
		
		private TextView textTitle;
		private TextView textAmount;
		private TextView textEnd;
		private TextView textNow;
		
		private TextView textBank;
		private TextView textType;
		private TextView textRate;
		private TextView textNote;
		
		public SavingListView(Context context) {
			super(context);	
			
			this.setOrientation(VERTICAL);
			
			LinearLayout lLayout2 = new LinearLayout(context);
			textTitle = new TextView(context);
			textTitle.setTextSize(22);
			textTitle.setLayoutWeight() .setLayoutParams(LayoutParams)
			lLayout2.addView(lLayout2, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			
			
			viewText = new TextView(context);
			viewText.setId(100);
			addView(viewText, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			viewText2 = new TextView(context);
			viewText2.setId(101);
			addView(viewText2, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			viewText2.setVisibility(GONE);
		}
		
		public void setTitle(String title, String value) {
			viewText.setText(title);
			viewText2.setText(value);
		}
		
		public void setExpanded() {
			if(viewText2.getVisibility() == VISIBLE)
				viewText2.setVisibility(GONE);
			else
				viewText2.setVisibility(VISIBLE);
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Cursor cursor = GLOBAL.DBACCESS.query();
		ListAdapter adapter = new DataCursorAdapter(this, cursor);		
		this.setListAdapter(adapter);
		
		this.registerForContextMenu(this.getListView());
	}
	
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {    
    	((SavingListView)v).setExpanded();
       //((SpeechListAdapter)getListAdapter()).toggle(position);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_savinglist, menu);
    	return true;    	
    }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_savinglist_context, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Toast.makeText(this, "" + item.getTitle().toString(), 1).show();// item.getTitle()item.toString();
    	return true;
    	
    	//return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	Toast.makeText(this, "" + item.getTitle().toString(),Toast.LENGTH_SHORT).show();
    	return true;
    }
}
