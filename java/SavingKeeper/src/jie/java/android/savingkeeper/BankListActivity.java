/**
 * file   : BankListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 21, 2011 10:41:40 AM
 */
package jie.java.android.savingkeeper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BankListActivity extends ListActivity {
	
	private Cursor cursor = null;
	private static final int DIALOG_ADD_BANK		=	1;
	private static final int DIALOG_REMOVE_BANK		=	2;
	
	protected class DataCursorAdapter extends SimpleCursorAdapter {
		public DataCursorAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2, cursor, new String[] { DBAccess.TABLE_COLUMN_ID, DBAccess.TABLE_COLUMN_TITLE }, new int[] { android.R.id.text1, android.R.id.text2 });
		}
		
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cursor = GLOBAL.DBACCESS.queryBank();
		this.startManagingCursor(cursor);
		
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] { DBAccess.TABLE_COLUMN_ID, DBAccess.TABLE_COLUMN_TITLE }, new int[] { android.R.id.text1, android.R.id.text2 });
		this.setListAdapter(adapter);
		
//		this.registerForContextMenu(this.getListView());
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.banklist, menu);
		return true;
	}
/*	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		this.getMenuInflater().inflate(R.menu.banklist_context, menu);
	}
*/	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_addbank:
			onMenuAddBank();
			break;
		default:
			return false;
		}
		return true;
	}
/*	
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_removebank:
			TextView view = (TextView)this.getListView().getSelectedView().findViewById(android.R.id.text1);
			Toast.makeText(this, "id:" + view.getText(), Toast.LENGTH_SHORT).show();
			onMenuRemoveBank();
			break;
		default:
			return false;
		}
		return true;
	}
*/	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this, "id:" + id, Toast.LENGTH_SHORT).show();
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_ADD_BANK:			
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.addbank, null);			
			
			Builder build = new AlertDialog.Builder(this);
			build.setTitle("Input Bank Title");
			build.setView(textEntryView);
			final EditText text = (EditText)textEntryView.findViewById(R.id.editText1);
			build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					GLOBAL.DBACCESS.insertBank(text.getText().toString());
					cursor.requery();
				}
			});
			build.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dlg = build.create();			
			break;
		case DIALOG_REMOVE_BANK:

			break;
		default:
			break;			
		}
		return dlg;
	}
	
	protected void onMenuAddBank() {
		this.showDialog(DIALOG_ADD_BANK);
	}
	
	protected void onMenuRemoveBank() {

	}
	
}
