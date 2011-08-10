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
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;

public class RateListActivity extends ExpandableListActivity {
	private static final String P_TITLE		=	"TITLE";
	private static final String C_TITLE		=	"TITLE";
	private static final String C_RATE_RMB	=	"RMB";
	private static final String C_RATE_US	=	"US";
	private static final String C_RATE_EU	=	"EU";
	
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
				R.layout.ratedetail,
				new String[] { C_TITLE },
				//new int[] { android.R.id.text1}
				new int[] { R.id.textView1 }
				);
		
		initData(titleData, childData);
		this.setListAdapter(adapter);
	}
	
	private void initData(List<Map<String, String>> titleData, List<List<Map<String, String>>> childData) {
		
		Cursor cursor = GLOBAL.DBACCESS.queryRate();
		
		while(cursor.moveToNext()) {
			
			Log.d(GLOBAL.APP_TAG, "start : " + cursor.getString(1) + " currency : " + cursor.getString(3));
			
		}
		
		
		Map<String, String> m = new HashMap<String, String>();
		m.put(P_TITLE, "p_title");
		titleData.add(m);
		m = new HashMap<String, String>();
		m.put(P_TITLE, "p_title");
		if(titleData.indexOf(m) == -1)
			titleData.add(m);
		
		List<Map<String, String>> c = new ArrayList<Map<String, String>>();
		m = new HashMap<String, String>();
		m.put(C_TITLE, "C_RATE_RMB");
		c.add(m);
		m = new HashMap<String, String>();
		m.put(C_TITLE, "C_RATE_US");
		c.add(m);
		m = new HashMap<String, String>();
		m.put(C_TITLE, "C_RATE_EU");
		c.add(m);		
		
		childData.add(c);
//	
		c = new ArrayList<Map<String, String>>();
		m = new HashMap<String, String>();
		m.put(C_TITLE, "C_RATE_RMB");
		c.add(m);
	
		
		childData.add(c);		
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
		GLOBAL.DBACCESS.insertRate("2011-08-01", "9999-12-31", 0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
		GLOBAL.DBACCESS.insertRate("2011-08-01", "9999-12-31", 1, 1.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
		GLOBAL.DBACCESS.insertRate("2011-08-01", "9999-12-31", 2, 2.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f);
	}
}
