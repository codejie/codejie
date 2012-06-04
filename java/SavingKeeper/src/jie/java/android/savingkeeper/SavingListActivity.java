/**
 * file   : SavingListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 15, 2011 5:29:13 PM
 */
package jie.java.android.savingkeeper;

import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SavingListActivity extends ListActivity {

	private int _id	=	-1;
	private String _title = null;
	private Cursor _cursor = null;
	
	private static final int DIALOG_REMOVE_SAVING		=	1;
	private static final int DIALOG_FILE_EXPORT			=	2;
	private static final int DIALOG_FILE_IMPORT_ALL		=	3;
	private static final int DIALOG_FILE_IMPORT_PART	=	4;
	
	private String _backupPath = null;
	private String _backupFilename = null;
	
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
			Date ci = TOOLKIT.String2Date(checkin);
			GLOBAL.CALCULATOR.calcMoney(ci, amount, currency, type, result);
			
			SavingListView v = (SavingListView)view;
			v.setId(cursor.getInt(0));
	
			v.textTitle.setText(title + " - " + checkin);
			int state = getState(type, ci);
			v.textTitle.setTextColor(getColor(state));
			
			v.textAmount.setText(String.format("%.2f", amount));
			v.textCurrency.setText(RCLoader.getCurrency(SavingListActivity.this, currency));
			v.textCheckin.setText(checkin);
			v.textType.setText(RCLoader.getType(SavingListActivity.this, type));
			v.textBank.setText(GLOBAL.DBACCESS.getBank(bank));
			v.textNote.setText(note);
			if(state != 1)
				v.textEnd.setText(String.format("%.2f", result.end));
			else
				v.textEnd.setText(String.format("%.2f/%.2f", result.end, result.next));
			v.textNow.setText(String.format("%.2f", result.now));			
		}
		
		//0: current; 1: fixed - over one period; 1: fixed - in one period
		private int getState(int type, final Date ci) {
			Date tmp = (Date) ci.clone();
			
			switch(type) {
			case DBAccess.SAVING_TYPE_CURRENT:
				return 0;
			case DBAccess.SAVING_TYPE_FIXED_3_MONTH:
				tmp.setMonth(tmp.getMonth() + 3);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;
			case DBAccess.SAVING_TYPE_FIXED_6_MONTH:
				tmp.setMonth(tmp.getMonth() + 6);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;
			case DBAccess.SAVING_TYPE_FIXED_1_YEAR:
				tmp.setYear(tmp.getYear() + 1);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;
			case DBAccess.SAVING_TYPE_FIXED_2_YEAR:
				tmp.setYear(tmp.getYear() + 2);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;
			case DBAccess.SAVING_TYPE_FIXED_3_YEAR:
				tmp.setYear(tmp.getYear() + 3);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;				
			case DBAccess.SAVING_TYPE_FIXED_5_YEAR:
				tmp.setYear(tmp.getYear() + 5);
				if(tmp.compareTo(GLOBAL.TODAY) <= 0)
					return 1;
			default:
				break;
			}
			
			return 2;			
		}
		
		private int getColor(int state) {
			switch(state) {
			case 0:
				return Color.GREEN;
			case 1:
				return Color.YELLOW;
			default:
				return Color.BLUE;
			}
		}
		
		private int getColor(int type, final Date ci) {
			//if(type == DBAccess.SAVING_TYPE_CURRENT)
			//	return Color.GREEN;
			
			switch(type) {
			case DBAccess.SAVING_TYPE_CURRENT:
				return Color.GREEN;
			case DBAccess.SAVING_TYPE_FIXED_3_MONTH:
				ci.setMonth(ci.getMonth() + 3);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;
			case DBAccess.SAVING_TYPE_FIXED_6_MONTH:
				ci.setMonth(ci.getMonth() + 6);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;
			case DBAccess.SAVING_TYPE_FIXED_1_YEAR:
				ci.setYear(ci.getYear() + 1);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;
			case DBAccess.SAVING_TYPE_FIXED_2_YEAR:
				ci.setYear(ci.getYear() + 2);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;
			case DBAccess.SAVING_TYPE_FIXED_3_YEAR:
				ci.setYear(ci.getYear() + 3);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;				
			case DBAccess.SAVING_TYPE_FIXED_5_YEAR:
				ci.setYear(ci.getYear() + 5);
				if(ci.compareTo(GLOBAL.TODAY) <= 0)
					return Color.YELLOW;
			default:
				break;
			}
			
			return Color.BLUE;
		}
	}
	
	protected class SavingListView extends LinearLayout {
		
		public TextView textTitle;
		public TextView textAmount;
		public TextView textEnd;
		public TextView textNow;
		
		public LinearLayout tlayout;
		
		public TextView textBank;
		public TextView textCheckin;
		public TextView textType;
		public TextView textCurrency;
		public TextView textNote;
		
		public SavingListView(Context context) {
			super(context);	
			
			this.setOrientation(VERTICAL);
			this.setPadding(10, 0, 10, 0);
			
			//Layout2 - according to display_activity_row.xml
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(HORIZONTAL);			
			
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);					
			textTitle = new TextView(context);
			textTitle.setTextSize(24);
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
		
/*		
		this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SavingListActivity.this.onListItemLongClick(parent, view, position, id);
				return true;
			}
			
		});
*/		
		this.registerForContextMenu(this.getListView());
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
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
    	
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)(menuInfo);
    	_id = (int) info.id;
    	_title = ((SavingListView)info.targetView).getTitle();
    	
    	//Log.d(GLOBAL.APP_TAG, "VIEW: " + _id);
    	//Log.d(GLOBAL.APP_TAG, "Saving title:" + _title);
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savinglist_context, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.menu_showtotal:
    		onMenuShowTotal();
    		break;
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
    	case R.id.menu_export:
    		onMenuExport();
    		break;
    	case R.id.menu_import_all:
    		onMenuImportAll();
    		break;
    	case R.id.menu_import_part:
    		onMenuImportPart();
    		break;
    	default:
    		return false;
    	}
    	
 //   	Toast.makeText(this, "" + item.getTitle().toString(), 1).show();// item.getTitle()item.toString();
    	return true;
    	
    	//return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	//Toast.makeText(this, "" + item.getTitle().toString(),Toast.LENGTH_SHORT).show();
    	switch(item.getItemId()) {
    	case R.id.menu_saving_edit:
    		onMenuSavingEdit();
    		break;
    	case R.id.menu_saving_remove:
    		onMenuSavingRemove();
    	default:
    		break;
    	}
    	return true;
    }
    
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
 //   	Intent intent = new Intent(this, SavingKeeper.class);
 //   	intent.putExtra("ACTION", GLOBAL.APP_ACTION_EXIT);
 //   	this.startActivity(intent);
 //   	this.finish();
    	GLOBAL.close();
    	System.exit(0);
    }
 /*   
    protected void onListItemLongClick(AdapterView<?> parent, View child, int position, long id) {
    	Log.d(GLOBAL.APP_TAG, "VIEW: " + child.getId() + " pos:" + position + " id:" + id);
    	_id = child.getId();
    	_title = ((SavingListView)child).getTitle();
    	Log.d(GLOBAL.APP_TAG, "Saving title:" + _title);
    	
    	this.showDialog(DIALOG_REMOVE_SAVING);
//    	GLOBAL.DBACCESS.removeSaving(_id);
//    	_cursor.requery();
    }
*/    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dlg = null;
    	switch(id) {
    	case DIALOG_REMOVE_SAVING: {   		
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_delete);
    		build.setTitle(SavingListActivity.this.getResources().getString(R.string.title_savingremove) + "'" + _title + "' ?");
    		build.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
			    	GLOBAL.DBACCESS.removeSaving(_id);
			    	_cursor.requery();					
				}
			});
    		build.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});    		
    		
    		dlg = build.create();
    		break;
    	}
    	case DIALOG_FILE_EXPORT: {
    		
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_dialog_info);
    		String dir = Environment.getExternalStorageDirectory().getPath();
    		String file = "sk_backup.xml";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.file_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
    		build.setView(v);
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				_backupPath = d.getText().toString();
    				_backupFilename = f.getText().toString();
    				exportSavingList();
    				dialog.dismiss();
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
    	case DIALOG_FILE_IMPORT_ALL: {
    		
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_dialog_info);
    		String dir = Environment.getExternalStorageDirectory().getPath();
    		String file = "sk_backup.xml";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.file_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
    		build.setView(v);
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				_backupPath = d.getText().toString();
    				_backupFilename = f.getText().toString();
    				importSavingList(false);
    				dialog.dismiss();
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
    	case DIALOG_FILE_IMPORT_PART: {
    		
    		Builder build = new AlertDialog.Builder(this);
    		build.setIcon(android.R.drawable.ic_dialog_info);
    		String dir = Environment.getExternalStorageDirectory().getPath();
    		String file = "sk_backup.xml";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.file_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
    		build.setView(v);
    		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				_backupPath = d.getText().toString();
    				_backupFilename = f.getText().toString();
    				importSavingList(true);
    				dialog.dismiss();
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
    
    private void onMenuSavingEdit() {
    	SavingDetailActivity act = new SavingDetailActivity();
		Intent intent = new Intent(this, act.getClass());
		intent.putExtra("ACTION", SavingDetailActivity.ACTION_EDIT);
		intent.putExtra("ID", _id);
		this.startActivity(intent);
    }
    
    private void onMenuSavingRemove() {
    	this.showDialog(DIALOG_REMOVE_SAVING);
    }
    
    private void onMenuShowTotal() {
    	Intent intent = new Intent(this, SavingTotalActivity.class);
		this.startActivity(intent);
    }
    
    private void onMenuExport() {
    	this.showDialog(DIALOG_FILE_EXPORT);
    }
    
    private void onMenuImportAll() {
    	this.showDialog(DIALOG_FILE_IMPORT_ALL);
    }
    
    private void onMenuImportPart() {
    	this.showDialog(DIALOG_FILE_IMPORT_PART);
    }
    
    private void exportSavingList() {
    	if(BackupManager.exportSavingList(_backupPath + "/" + _backupFilename) != 0) {
    		Toast.makeText(this, "Export Saving List failed.", Toast.LENGTH_LONG).show();
    	}
    }
    
    private void importSavingList(boolean checkexist) {
    	if(BackupManager.importSavingList(_backupPath + "/" + _backupFilename, checkexist) != 0) {
    		Toast.makeText(this, "Import Saving List failed.", Toast.LENGTH_LONG).show();
    	}
    	_cursor.requery(); 	
    }
}
