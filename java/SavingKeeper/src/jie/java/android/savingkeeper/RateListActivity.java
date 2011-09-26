/**
 * file   : RateListActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Aug 4, 2011 5:39:17 PM
 */
package jie.java.android.savingkeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class RateListActivity extends ExpandableListActivity {
	private static final String P_TITLE		=	"TITLE";
	private static final String C_TITLE		=	"TITLE";
	private static final String C_RATE_RMB	=	"RMB";
	private static final String C_RATE_US	=	"US";
	private static final String C_RATE_EU	=	"EU";

	private static final String C_TITLE_0	=	"TITLE_0";
	private static final String C_TITLE_1	=	"TITLE_1";
	private static final String C_TITLE_2	=	"TITLE_2";
	private static final String C_TITLE_3	=	"TITLE_3";
	private static final String C_TITLE_4	=	"TITLE_4";
	private static final String C_TITLE_5	=	"TITLE_5";
	private static final String C_TITLE_6	=	"TITLE_6";
	
	private static final String C_RATE_0	=	"RATE_0";
	private static final String C_RATE_1	=	"RATE_1";
	private static final String C_RATE_2	=	"RATE_2";
	private static final String C_RATE_3	=	"RATE_3";
	private static final String C_RATE_4	=	"RATE_4";
	private static final String C_RATE_5	=	"RATE_5";
	private static final String C_RATE_6	=	"RATE_6";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<Map<String, String>> titleData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		
		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				this,
				titleData,
				android.R.layout.simple_expandable_list_item_1,
				new String[] { P_TITLE },
				new int[] { android.R.id.text1 },
				childData,
				//android.R.layout.simple_expandable_list_item_1,
				R.layout.ratedatadetail,
				new String[] { C_TITLE, C_TITLE_0, C_TITLE_1, C_TITLE_2, C_TITLE_3, C_TITLE_4, C_TITLE_5, C_TITLE_6, 
						C_RATE_0, C_RATE_1, C_RATE_2, C_RATE_3, C_RATE_4, C_RATE_5, C_RATE_6 },
				//new int[] { android.R.id.text1}
				new int[] { R.id.rate_title, R.id.rate_title_0, R.id.rate_title_1, R.id.rate_title_2, R.id.rate_title_3, R.id.rate_title_4, R.id.rate_title_5, R.id.rate_title_6,
						R.id.rate_0, R.id.rate_1, R.id.rate_2, R.id.rate_3, R.id.rate_4, R.id.rate_5, R.id.rate_6});
		
		initData(titleData, childData);
		this.setListAdapter(adapter);
		
		this.getExpandableListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				RateListActivity.this.onItemLongClick(parent, view, position, id);
				//Toast.makeText(BankListActivity.this, "pos:" + position + " id:" + id, Toast.LENGTH_SHORT).show();
				return true;
			}
			
		});		
		
		//this.registerForContextMenu(this.getExpandableListView());
	}
	
	private void initData(List<Map<String, String>> titleData, List<List<Map<String, String>>> childData) {
		
		Cursor cursor = GLOBAL.DBACCESS.queryRate();
		
		int count = 0;
		List<Map<String, String>> c = new ArrayList<Map<String, String>>();	
		Map<String, String> m = null;
		while(cursor.moveToNext()) {
			
			Log.d(GLOBAL.APP_TAG, "start : " + cursor.getString(1) + " currency : " + cursor.getString(3));
			if(count == 0)
			{
				m = new HashMap<String, String>();
				m.put(C_TITLE, cursor.getString(1) + " - " + cursor.getString(2));
				titleData.add(m);
			}

			if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_RMB) {
				m = new HashMap<String, String>();
				m.put(C_TITLE, RCLoader.getCurrency(this, 0));
				
				m.put(C_TITLE_0, RCLoader.getType(this, 0));
				m.put(C_TITLE_1, RCLoader.getType(this, 1));
				m.put(C_TITLE_2, RCLoader.getType(this, 2));
				m.put(C_TITLE_3, RCLoader.getType(this, 3));
				m.put(C_TITLE_4, RCLoader.getType(this, 4));
				m.put(C_TITLE_5, RCLoader.getType(this, 5));
				m.put(C_TITLE_6, RCLoader.getType(this, 6));
				
				m.put(C_RATE_0, String.format("%.3f%%", cursor.getFloat(4)));
				m.put(C_RATE_1, String.format("%.3f%%", cursor.getFloat(5)));
				m.put(C_RATE_2, String.format("%.3f%%", cursor.getFloat(6)));
				m.put(C_RATE_3, String.format("%.3f%%", cursor.getFloat(7)));
				m.put(C_RATE_4, String.format("%.3f%%", cursor.getFloat(8)));
				m.put(C_RATE_5, String.format("%.3f%%", cursor.getFloat(9)));
				m.put(C_RATE_6, String.format("%.3f%%", cursor.getFloat(10)));
				c.add(m);				
//				childData.add(c);
			}
			else if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_US) {
				m = new HashMap<String, String>();
				m.put(C_TITLE, RCLoader.getCurrency(this, 1));
				
				m.put(C_TITLE_0, RCLoader.getType(this, 0));
				m.put(C_TITLE_1, RCLoader.getType(this, 1));
				m.put(C_TITLE_2, RCLoader.getType(this, 2));
				m.put(C_TITLE_3, RCLoader.getType(this, 3));
				m.put(C_TITLE_4, RCLoader.getType(this, 4));
				m.put(C_TITLE_5, RCLoader.getType(this, 5));
				m.put(C_TITLE_6, RCLoader.getType(this, 6));				
				
				m.put(C_RATE_0, String.format("%.3f%%", cursor.getFloat(4)));
				m.put(C_RATE_1, String.format("%.3f%%", cursor.getFloat(5)));
				m.put(C_RATE_2, String.format("%.3f%%", cursor.getFloat(6)));
				m.put(C_RATE_3, String.format("%.3f%%", cursor.getFloat(7)));
				m.put(C_RATE_4, String.format("%.3f%%", cursor.getFloat(8)));
				m.put(C_RATE_5, String.format("%.3f%%", cursor.getFloat(9)));
				m.put(C_RATE_6, String.format("%.3f%%", cursor.getFloat(10)));
				c.add(m);
				
//				childData.add(c);
			}
			else if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_EU) {
				m = new HashMap<String, String>();
				m.put(C_TITLE, RCLoader.getCurrency(this, 2));		
				
				m.put(C_TITLE_0, RCLoader.getType(this, 0));
				m.put(C_TITLE_1, RCLoader.getType(this, 1));
				m.put(C_TITLE_2, RCLoader.getType(this, 2));
				m.put(C_TITLE_3, RCLoader.getType(this, 3));
				m.put(C_TITLE_4, RCLoader.getType(this, 4));
				m.put(C_TITLE_5, RCLoader.getType(this, 5));
				m.put(C_TITLE_6, RCLoader.getType(this, 6));
				
				m.put(C_RATE_0, String.format("%.3f%%", cursor.getFloat(4)));
				m.put(C_RATE_1, String.format("%.3f%%", cursor.getFloat(5)));
				m.put(C_RATE_2, String.format("%.3f%%", cursor.getFloat(6)));
				m.put(C_RATE_3, String.format("%.3f%%", cursor.getFloat(7)));
				m.put(C_RATE_4, String.format("%.3f%%", cursor.getFloat(8)));
				m.put(C_RATE_5, String.format("%.3f%%", cursor.getFloat(9)));
				m.put(C_RATE_6, String.format("%.3f%%", cursor.getFloat(10)));
				c.add(m);
				
//				childData.add(c);
			}
			
			++ count;
			
			if(count == 3) {
				childData.add(c);
				c = new ArrayList<Map<String, String>>();
				count = 0;
			}
		}		
	}
	
	public void refreshList() {
		this.getExpandableListView().removeAllViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.ratelist, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_addrate:
			onMenuAddRate();
			break;
		default:
			return false;
		}
		return true;
	}
	
	private void onMenuAddRate() {
        Intent intent = new Intent(this, RateDetailActivity.class);
		this.startActivity(intent);
		this.finish();
		//GLOBAL.DBACCESS.insertRate("2010.08.02", "9999.12.31", 0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
		//GLOBAL.DBACCESS.insertRate("2010.08.02", "9999.12.31", 1, 1.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
		//GLOBAL.DBACCESS.insertRate("2010.08.02", "9999.12.31", 2, 2.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
	}
	
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		Log.d(GLOBAL.APP_TAG, "contextmenu:" + menu.toString() + " view:" + v.toString() + " menuinfo:" + menuInfo.toString());
//		Log.d(GLOBAL.APP_TAG, "pos: " + ((ExpandableListView.ExpandableListContextMenuInfo)menuInfo).packedPosition + " id:" + ((ExpandableListView.ExpandableListContextMenuInfo)menuInfo).id);
//		this.getExpandableListView().getItemAtPosition(position)
//		this.getMenuInflater().inflate(R.menu.ratelist_remove, menu);
//		this.getMenuInflater().inflate(R.menu.banklist_context, menu);
//	}

	
	public void onItemLongClick(AdapterView<?> parent, View child, int position, long id) {
		if(parent == this.getExpandableListView()) {
			Log.d(GLOBAL.APP_TAG, "position:" + position + " id:" + id);
			//this.getExpandableListView().removeViewAt(position);
		}
	}
	
}
