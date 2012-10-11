package jie.java.android.lingoshook;

import java.util.ArrayList;

import jie.java.android.lingoshook.data.DBAccess;
import jie.java.android.lingoshook.data.DataStructure;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DictionaryAdapter extends BaseAdapter {

	private ArrayList<DataStructure.Test> array = new ArrayList<DataStructure.Test>();
	private Context context = null;
	
	private DBAccess db = null;
	private Cursor cursor = null;
	private String condition = null;
	private int offset = 0;
	private int maxRows = 12;
	
	public DictionaryAdapter(Context context, DBAccess db) {
		this.context = context;
		this.db = db;
	}
	
	@Override
	public int getCount() {
		return array.size();
	}

	@Override
	public Object getItem(int index) {
		return array.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		TextView v = (TextView)convertView.findViewById(android.R.id.text1);
		v.setText(Integer.toString(array.get(index).id));
		
		return convertView;
	}
	
	public int load(final String condition) {
		if(cursor != null) {
			cursor.close();
		}
		
		this.condition = condition;
		this.offset = 0;
		
		return refresh();
	}
	
	public int load(final String condition, int maxRows) {
		this.maxRows = maxRows;
		return load(condition);
	}

	private int refresh() {
		cursor = db.getTestData(this.condition, offset, maxRows);
		
		if(cursor == null)
			return -1;
		if(!cursor.moveToFirst())
			return -1;
		
		while(cursor.moveToNext()) {
			array.add(new DataStructure.Test(cursor.getInt(0), cursor.getString(1)));
		}
		
		offset += cursor.getCount();
		
		this.notifyDataSetChanged();
		
		return offset;		
	}
	
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

}
