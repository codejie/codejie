package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ScoreListActivity extends Activity {

	private class ScoreDataAdapter extends BaseAdapter {

		private Context _context = null;
		private Cursor _cursor = null;
		
		public ScoreDataAdapter(Context context) {
			_context = context;
		}

		public void close() {
			if(_cursor != null)
				_cursor.close();
		}
		
		public int initData() {
			_cursor = DBAccess.getScoreStat();
			if(_cursor == null)
				return -1;
			if(_cursor.moveToFirst() == false)
				return -1;
			return 0;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _cursor.getCount() ;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
				convertView = LayoutInflater.from(_context).inflate(R.layout.score_item, null);
			}
			TextView u = (TextView)convertView.findViewById(R.id.textUpdated);
			TextView a = (TextView)convertView.findViewById(R.id.textAmount);	
			
			_cursor.moveToPosition(position);
			
			u.setText(_cursor.getString(0));
			a.setText(_cursor.getString(1));
			
			return convertView;
		}
	}
	
	private ScoreDataAdapter adapter = null;//new ScoreDataAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.score_list);

		loadTotalData();
		loadScoreList();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(adapter != null)
			adapter.close();
		
		super.finish();
	}
	
	private void loadTotalData() {
		int n = DBAccess.getScoreCount(true);
		int o = DBAccess.getScoreCount(false);
		
		((TextView)this.findViewById(R.id.textView1)).setText(String.format("%d", (n + o)));
		((TextView)this.findViewById(R.id.textView2)).setText(String.format("%d", n));
		((TextView)this.findViewById(R.id.textView3)).setText(String.format("%d", o));		
	}
	
	private void loadScoreList() {
		adapter = new ScoreDataAdapter(this);
		adapter.initData();
		
		GridView grid = (GridView)this.findViewById(R.id.gridScore);
		//Cursor cursor = DBAccess.getScoreStatData();
		grid.setAdapter(adapter);
		
		
	}

}
