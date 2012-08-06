package jie.java.android.lingoshook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

		private boolean showDate = true;
		public WordCursorAdapter(Context context, Cursor cursor, boolean showDate) {
			super(context, cursor);
			
			this.showDate = showDate;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView w = (TextView) view.findViewById(R.id.textView1);
			w.setText(cursor.getString(0));

			if(showDate) {
				w = (TextView) view.findViewById(R.id.textView2);
				w.setText(getDateByUpdated(cursor.getInt(2)));
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = LayoutInflater.from(WordListActivity.this).inflate(R.layout.word_list_item, null);
			return view;
		}		
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.word_list);
		
		textView = (TextView) this.findViewById(R.id.textView1);
		listView = (ListView) this.findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(WordListActivity.this, HtmlDisplayActivity.class);
	    		intent.putExtra(HtmlDisplayActivity.REQ, HtmlDisplayActivity.REQ_WORD);
	    		intent.putExtra(HtmlDisplayActivity.WORD, ((TextView)view.findViewById(R.id.textView1)).getText().toString());//view.getId());
	    		WordListActivity.this.startActivity(intent);					
			}			
		});
		
		//this.registerForContextMenu(listView);
		
		
		
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

	@Override
	protected void onDestroy() {
		if(cursor != null) {
			cursor.close();
		}
		super.onDestroy();
	}

	public String getDateByUpdated(int updated) {
		if(updated == 0)
			return "";
		
		Calendar c = Calendar.getInstance();
		c.setTime(DBAccess.CHECKIN);
		c.add(Calendar.DATE, updated);
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		
		return fmt.format(c.getTime());
	}

	private void loadCommonWords(int value) {
		if(value == 0) {
			//all
			textView.setText(R.string.str_all_words);
		}
		else if(value == 1) {
			//new
			textView.setText(R.string.str_all_newwords);
		}
		else if(value == 2) {
			textView.setText(R.string.str_all_oldwords);
		}
		
		cursor = DBAccess.getWords(0, value);
		ListAdapter adapter = new WordCursorAdapter(this, cursor, true);
		listView.setAdapter(adapter);
		//listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(WordListActivity.this, HtmlDisplayActivity.class);
	    		intent.putExtra(HtmlDisplayActivity.REQ, HtmlDisplayActivity.REQ_WORD);
	    		intent.putExtra(HtmlDisplayActivity.WORD, ((TextView)view.findViewById(R.id.textView1)).getText().toString());//view.getId());
	    		WordListActivity.this.startActivity(intent);					
			}			
		});
		//listView.setOnClickListener(this);
	}	

	private void loadSpecificWords(int value) {
		
		if(value != 0) {
			textView.setText(this.getString(R.string.str_all_wordson) +  getDateByUpdated(value));
		}
		else {
			textView.setText(R.string.str_all_newwords);
		}
		
		cursor = DBAccess.getWords(1, value);
		ListAdapter adapter = new WordCursorAdapter(this, cursor, false);
		listView.setAdapter(adapter);
	}

}