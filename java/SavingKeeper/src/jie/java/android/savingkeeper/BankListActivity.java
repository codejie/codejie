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
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
	
	private long idBank = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cursor = GLOBAL.DBACCESS.queryBank();
		this.startManagingCursor(cursor);
		
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] {  DBAccess.TABLE_COLUMN_TITLE }, new int[] { android.R.id.text1 });
		this.setListAdapter(adapter);
		
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				BankListActivity.this.onListItemLongClick(view, position, id);
				//Toast.makeText(BankListActivity.this, "pos:" + position + " id:" + id, Toast.LENGTH_SHORT).show();
				return true;
			}
			
		});
		
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
	public void onListItemLongClick(View v, int position, long id) {
		idBank = id;
		this.showDialog(DIALOG_REMOVE_BANK);		
	}
/*	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this, "id:" + id, Toast.LENGTH_SHORT).show();
	}
*/	
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_ADD_BANK: {			
	            LayoutInflater factory = LayoutInflater.from(this);
	            final View textEntryView = factory.inflate(R.layout.addbank, null);			
				
				Builder build = new AlertDialog.Builder(this);
				build.setTitle(R.string.title_add_bank);
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
			}
			break;
		case DIALOG_REMOVE_BANK: {
				Builder build = new AlertDialog.Builder(this);
				build.setIcon(android.R.drawable.ic_delete);
				build.setTitle(R.string.title_remove_bank);
				build.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//dialog.dismiss();
						if(GLOBAL.DBACCESS.removeBank(idBank) == -1) {
							Builder b = new AlertDialog.Builder(BankListActivity.this);
							b.setIcon(android.R.drawable.ic_dialog_alert);
							b.setMessage(R.string.error_remove_bank);
							b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {				
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							b.create().show();
						}
						else {
							cursor.requery();
						}
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
	
	protected void onMenuAddBank() {
		this.showDialog(DIALOG_ADD_BANK);
	}
}
