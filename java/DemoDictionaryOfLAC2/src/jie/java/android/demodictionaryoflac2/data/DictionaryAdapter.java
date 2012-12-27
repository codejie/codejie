package jie.java.android.demodictionaryoflac2.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DictionaryAdapter extends BaseAdapter {

	public static final class ItemData {
		public int index = -1;
		public String text = null;
		
		public ItemData(int index, final String text) {
			this.index = index;
			this.text = text;
		}
	}
	
	private Context context = null;	
	private DBAccess db = null;
	
	private Cursor cursor = null;
	private String condition = null;
	private int offset = 0;
	private int maxRows = 12;
	
	private ArrayList<ItemData> array = new ArrayList<ItemData>(); 
	
	public DictionaryAdapter(Context context, final DBAccess db) {
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
		return array.get(index).index;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		
		((TextView)view.findViewById(android.R.id.text1)).setText(array.get(index).text);
		
		return view;
	}

	public int load(final String condition) {
		
		this.condition = condition;
		this.offset = 0;
		
		return refresh();
	}

	public int refresh() {

		if(cursor != null) {
			cursor.close();
		}
		
		cursor = db.getItemData(condition, offset, maxRows);
		
		if(cursor == null || !cursor.moveToFirst()) {
			return -1;
		}
		
		offset += cursor.getCount();
		
		while(cursor.moveToNext()) {
			array.add(new ItemData(cursor.getInt(0), cursor.getString(1)));
		}
				
		this.notifyDataSetChanged();
		
		return offset;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	
	public void clear() {
		cursor = null;
		offset = 0;
		condition = null;
//		maxRows = 12;
		
		array.clear();
		
		this.notifyDataSetChanged();
	}	
}
