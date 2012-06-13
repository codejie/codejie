package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WordListActivity extends Activity {

	public static final String REQ_TYPE		=	"type";
	public static final String REQ_VALUE	=	"value";
	
	private TextView textView = null;
	private ListView listView = null;
	private Cursor cursor = null;
	
	protected class WordCursorAdapter extends CursorAdapter {

		public WordCursorAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView w = (TextView) view.findViewById(R.id.textView1);
			w.setText(cursor.getString(0));
			
			if(cursor.getInt(2) != 0) {
				w = (TextView) view.findViewById(R.id.textView2);
				//w.setText(text)
			}
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = LayoutInflater.from(WordListActivity.this).inflate(R.layout.word_list_item, null);
			view.setId(cursor.getInt(1));
			return view;
		}
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.word_list);
		
		textView = (TextView) this.findViewById(R.id.textView1);
		listView = (ListView) this.findViewById(R.id.listView1);
		
		Intent intent = this.getIntent();
		if(intent != null) {
			int type = intent.getExtras().getInt(REQ_TYPE);
			int value = intent.getExtras().getInt(REQ_VALUE);
		
			if(type == 0) {
				//all, new, old
				loadCommonWords(value);
			}
			else {
				//specific date
				loadSpecificWords(value);
			}
		}
		
	}

	private void loadCommonWords(int value) {
		if(value == 0) {
			//all
			textView.setText("All Words");
		}
		else if(value == 1) {
			//new
			textView.setText("All New Words");
		}
		else if(value == 2) {
			textView.setText("All Old Words");
		}
		
		cursor = DBAccess.getWords(0, 0);
		ListAdapter adapter = new WordCursorAdapter(this, cursor);
		listView.setAdapter(adapter);
		
	}	

	private void loadSpecificWords(int value) {
		// TODO Auto-generated method stub
		
	}
	


}
