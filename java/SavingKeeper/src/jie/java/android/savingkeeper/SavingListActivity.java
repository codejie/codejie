/**
 * file   : SavingListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 15, 2011 5:29:13 PM
 */
package jie.java.android.savingkeeper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SavingListActivity extends ListActivity {

	private int _id	=	-1;
	private String _title = null;
	private Cursor _cursor = null;
	
	private static final int DIALOG_REMOVE_SAVING		=	1;
	
	protected class DataCursorAdapter extends CursorAdapter {

		public DataCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}
	
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			SavingListView view = new SavingListView(context);
			view.setId(cursor.getPosition());
			
			return view;
		}
		
		public void bindView(View view, Context context, Cursor cursor) {
			String title = cursor.getString(1);
			float amount = cursor.getFloat(2);
			int currency = cursor.getInt(3);
			String checkin = cursor.getString(4);
			int type = cursor.getInt(5);
			int bank = cursor.getInt(6);
			String note = cursor.getString(7);
			
			DataCalculator.CalcResult result = new DataCalculator.CalcResult();
			GLOBAL.CALCULATOR.calcMoney(checkin, amount, currency, type, result);
			
			Log.d(GLOBAL.APP_TAG, "end = " + String.format("%.2f", result.end) + " now = " + result.now);
			//((SavingListView)view).setTitle(cursor.getString(0), cursor.getString(1));

			((SavingListView)view).setContent(cursor.getInt(0), title, String.format("%.2f", amount), RCLoader.getCurrency(SavingListActivity.this, currency),
					checkin, RCLoader.getType(SavingListActivity.this, type), GLOBAL.DBACCESS.getBank(bank), note, String.format("%.2f", result.end), String.format("%.2f", result.now));
		}
	}
	
	protected class SavingListView extends LinearLayout {
		
		private TextView textTitle;
		private TextView textAmount;
		private TextView textEnd;
		private TextView textNow;
		
		private LinearLayout tlayout;
		
		private TextView textBank;
		private TextView textCheckin;
		private TextView textType;
		private TextView textCurrency;
		private TextView textNote;
		
		public SavingListView(Context context) {
			super(context);	
			
			this.setOrientation(VERTICAL);
			this.setPadding(10, 0, 10, 0);
			
			//Layout2 - according to display_activity_row.xml
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);			
			
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);					
			textTitle = new TextView(context);
			textTitle.setTextSize(32);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT;
			layout.addView(textTitle, params);
			
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textEnd = new TextView(context);
			textEnd.setTextSize(20);
			//viewText2.setVisibility(GONE);
			params.weight = 0.0f;
			params.gravity = Gravity.RIGHT;
			layout.addView(textEnd, params);
			
			this.addView(layout);
		
			//layout1
			layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);

			textAmount = new TextView(context);
			textAmount.setTextSize(20);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT | Gravity.BOTTOM;
			layout.addView(textAmount, params);
			
			textNow = new TextView(context);
			textNow.setTextSize(24);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 0.0f;
			params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			layout.addView(textNow, params);
			
			this.addView(layout);
			
			TextView tv = null;
			
			//TableLayout
			tlayout = new LinearLayout(context);
			tlayout.setOrientation(VERTICAL);
			tlayout.setPadding(40, 10, 0, 10);
			tlayout.setVisibility(GONE);
			
			//currency
			layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);
			
			tv = new TextView(context);
			tv.setText(R.string.str_currency);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 2.0f;
			//params.gravity = Gravity.RIGHT;
			layout.addView(tv, params);
			
			textCurrency = new TextView(context);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT;
			layout.addView(textCurrency, params);			
			
			tlayout.addView(layout);
			
			//checkin
			layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);
			
			tv = new TextView(context);
			tv.setText(R.string.str_checkin);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 2.0f;			
			//params.gravity = Gravity.LEFT;
			layout.addView(tv, params);
			
			textCheckin = new TextView(context);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT;
			layout.addView(textCheckin, params);			
			
			tlayout.addView(layout);
			
			//type
			layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);
			
			tv = new TextView(context);
			tv.setText(R.string.str_type);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 2.0f;			
			//params.gravity = Gravity.RIGHT;
			layout.addView(tv, params);
			
			textType = new TextView(context);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT;
			layout.addView(textType, params);			
			
			tlayout.addView(layout);
			
			//bank
			layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);
			
			tv = new TextView(context);
			tv.setText(R.string.str_bank);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 2.0f;			
			//params.gravity = Gravity.RIGHT;
			layout.addView(tv, params);
			
			textBank = new TextView(context);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.gravity = Gravity.LEFT;
			layout.addView(textBank, params);			
			
			tlayout.addView(layout);			
			
			//note
			tv = new TextView(context);
			tv.setText(R.string.str_note);
			tlayout.addView(tv, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			textNote = new TextView(context);
			textNote.setPadding(20, 0, 0, 0);
			tlayout.addView(textNote, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
		
			this.addView(tlayout, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}
		
		public void setContent(int id, final String title, final String amount, final String currency, final String checkin, final String type, final String bank, final String note, final String end, final String now) {
			this.setId(id);
			
			textTitle.setText(title);
			textAmount.setText(amount);
			textCurrency.setText(currency);
			textCheckin.setText(checkin);
			textType.setText(type);
			textBank.setText(bank);
			textNote.setText(note);
			textEnd.setText(end);
			textNow.setText(now);
		}
		
		public void setExpanded() {
			if(tlayout.getVisibility() == VISIBLE)
				tlayout.setVisibility(GONE);
			else
				tlayout.setVisibility(VISIBLE);
		}
		
		public String getTitle() {
			return this.textTitle.getText().toString();
		}
	}

	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_cursor = GLOBAL.DBACCESS.querySaving();
		this.startManagingCursor(_cursor);
		ListAdapter adapter = new DataCursorAdapter(this, _cursor);		
		this.setListAdapter(adapter);
		
		
		this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SavingListActivity.this.onListItemLongClick(parent, view, position, id);
				return true;
			}
			
		});
		
		//this.registerForContextMenu(this.getListView());
	}
	
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {    
    	((SavingListView)v).setExpanded();
    	//Toast.makeText(this, "view : " + v.getId() + " position : " + position + " id : " + id, 0).show();
       //((SpeechListAdapter)getListAdapter()).toggle(position);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.savinglist, menu);
    	return true;    	
    }
/*	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_savinglist_context, menu);
    }
 */   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.menu_add_saving:
    		onMenuAddSaving();
    		break;
    	case R.id.menu_ratelist:
    		onMenuRateList();
    		break;
    	case R.id.menu_banklist:
    		onMenuBankList();
    		break;
    	case R.id.menu_exit:
    		onMenuExit();
    		break;
    	default:
    		return false;
    	}
    	
 //   	Toast.makeText(this, "" + item.getTitle().toString(), 1).show();// item.getTitle()item.toString();
    	return true;
    	
    	//return super.onOptionsItemSelected(item);
    }
/*    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	Toast.makeText(this, "" + item.getTitle().toString(),Toast.LENGTH_SHORT).show();
    	return true;
    }
*/    
    ////////////
    protected void onMenuAddSaving() {
    	SavingDetailActivity act = new SavingDetailActivity();
		Intent intent = new Intent(this, act.getClass());
		intent.putExtra("ACTION", SavingDetailActivity.ACTION_ADD);
		intent.putExtra("ID", -1);
		this.startActivity(intent);
    }
    
    protected void onMenuRateList() {
		Intent intent = new Intent(this, RateListActivity.class);
		this.startActivity(intent);   	
    }
    
    protected void onMenuBankList() {
		Intent intent = new Intent(this, BankListActivity.class);
		this.startActivity(intent);
    }
    
    protected void onMenuExit() {
    	System.exit(0);
    }
    
    protected void onListItemLongClick(AdapterView<?> parent, View child, int position, long id) {
    	Log.d(GLOBAL.APP_TAG, "VIEW: " + child.getId() + " pos:" + position + " id:" + id);
    	_id = child.getId();
    	_title = ((SavingListView)child).getTitle();
    	Log.d(GLOBAL.APP_TAG, "Saving title:" + _title);
    	
    	this.showDialog(DIALOG_REMOVE_SAVING);
//    	GLOBAL.DBACCESS.removeSaving(_id);
//    	_cursor.requery();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dlg = null;
    	switch(id) {
    	case DIALOG_REMOVE_SAVING: {   		
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_delete);
    		build.setTitle("Remove " + _title + " ?");
    		build.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
			    	GLOBAL.DBACCESS.removeSaving(_id);
			    	_cursor.requery();					
				}
			});
    		build.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});    		
    		
    		dlg = build.create();
    		break;
    	}
    	default:
    		break;
    	}
    	return dlg;
    }
}
