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
import android.os.Bundle;
import android.view.Menu;
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
	
}
