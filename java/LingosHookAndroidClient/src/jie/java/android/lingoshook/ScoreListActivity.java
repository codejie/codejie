package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreListActivity extends Activity implements OnClickListener {

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
			
			if(_cursor.getInt(0) != 0) {
				u.setText(String.format("%d", (_cursor.getInt(0) - Score.deltaUpdated)));
			}
			else {
				u.setText("New");
			}
			a.setText(_cursor.getString(1));
			
			convertView.setId(_cursor.getInt(0));
			
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
		int n = DBAccess.getScoreCount(true, -1);
		int o = DBAccess.getScoreCount(false, -1);
		
		TextView text = ((TextView)this.findViewById(R.id.textView1));
		text.setText(String.format("%d", (n + o)));
		text.setOnClickListener(this);
		
		text = ((TextView)this.findViewById(R.id.textView2));
		text.setText(String.format("%d", (n)));
		text.setOnClickListener(this);

		text = ((TextView)this.findViewById(R.id.textView3));
		text.setText(String.format("%d", (o)));
		text.setOnClickListener(this);
		
		((TextView)this.findViewById(R.id.textTotal)).setOnClickListener(this);
		((TextView)this.findViewById(R.id.textNew)).setOnClickListener(this);
		((TextView)this.findViewById(R.id.textOld)).setOnClickListener(this);
	}
	
	private void loadScoreList() {
		adapter = new ScoreDataAdapter(this);
		adapter.initData();
		
		GridView grid = (GridView)this.findViewById(R.id.gridScore);
//		TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
//        int mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
//        a.recycle();		
//		grid.setBackgroundResource(mGalleryItemBackground);

		//Cursor cursor = DBAccess.getScoreStatData();
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ScoreListActivity.this, WordListActivity.class);
				intent.putExtra(WordListActivity.REQ_TYPE, 1);
				intent.putExtra(WordListActivity.REQ_VALUE, view.getId());

				//Toast.makeText(ScoreListActivity.this, "pos: " + position + " id: " + id + "  viewid: " + view.getId(), Toast.LENGTH_LONG).show();
				
				ScoreListActivity.this.startActivity(intent);
			}
			
		});
		//.setOnClickListener(this);		
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(this, WordListActivity.class);
		
		if(view == this.findViewById(R.id.textView1) || view == this.findViewById(R.id.textTotal)) {
			intent.putExtra(WordListActivity.REQ_TYPE, 0);
			intent.putExtra(WordListActivity.REQ_VALUE, 0);
		}
		else if(view == this.findViewById(R.id.textView2) || view == this.findViewById(R.id.textNew)) {
			intent.putExtra(WordListActivity.REQ_TYPE, 0);
			intent.putExtra(WordListActivity.REQ_VALUE, 1);
		}
		else if(view == this.findViewById(R.id.textView3) || view == this.findViewById(R.id.textOld)) {
			intent.putExtra(WordListActivity.REQ_TYPE, 0);
			intent.putExtra(WordListActivity.REQ_VALUE, 2);
		}
		else
		{
			return;
		}
		
		this.startActivity(intent);
	}

}
