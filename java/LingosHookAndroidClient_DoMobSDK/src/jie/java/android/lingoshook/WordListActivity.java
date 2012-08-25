package jie.java.android.lingoshook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WordListActivity extends Activity {

	public static final String REQ_TYPE		=	"type";
	public static final String REQ_VALUE	=	"value";
	
	private static final int DIALOG_REMOVEWORD = 1;
	
	private TextView textView = null;
	private ListView listView = null;
	private Cursor cursor = null;

	private String word = null;
	private int wordid = -1;
	private int srcid = -1;
	
	private int type = -1;
	private int value = -1;
	
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

			w = (TextView) view.findViewById(R.id.textView3);//wordid
			w.setText(cursor.getString(1));
			
			w = (TextView) view.findViewById(R.id.textView4);//srcid
			w.setText(cursor.getString(2));			
			
			if(showDate) {
				w = (TextView) view.findViewById(R.id.textView2);
				w.setText(getDateByUpdated(cursor.getInt(3)));
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
//		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(WordListActivity.this, HtmlDisplayActivity.class);
	    		intent.putExtra(HtmlDisplayActivity.REQ, HtmlDisplayActivity.REQ_WORD);
	//    		intent.putExtra(HtmlDisplayActivity.WORD, ((TextView)view.findViewById(R.id.textView1)).getText().toString());//view.getId());
	    		intent.putExtra(HtmlDisplayActivity.SRCID, ((TextView)view.findViewById(R.id.textView4)).getText().toString());//view.getId());	    		
	    		WordListActivity.this.startActivity(intent);					
			}			
		});
		
		//this.registerForContextMenu(listView);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				setWordData(((TextView)view.findViewById(R.id.textView1)).getText().toString(), ((TextView)view.findViewById(R.id.textView3)).getText().toString(), ((TextView)view.findViewById(R.id.textView4)).getText().toString());
				WordListActivity.this.showDialog(DIALOG_REMOVEWORD);
//				Toast.makeText(WordListActivity.this, "wordid:" + ((TextView)view.findViewById(R.id.textView3)).getText().toString(), Toast.LENGTH_SHORT).show();
				return true;
			}			
		});		
		
		//this.registerForContextMenu(listView);
		
		Intent intent = this.getIntent();
		if(intent != null) {
			type = intent.getExtras().getInt(REQ_TYPE);
			value = intent.getExtras().getInt(REQ_VALUE);
			
			loadWords();
//		
//			if(type == 0) {
//				//all, new, old
//				loadCommonWords(value);
//			}
//			else {
//				//specific date
//				loadSpecificWords(value);
//			}
		}		
	}
	
	private void loadWords() {
		if(type == 0) {
			//all, new, old
			loadCommonWords(value);
		}
		else {
			//specific date
			loadSpecificWords(value);
		}
	}

	protected void setWordData(String word, String wordid, String srcid) {
		this.word = word;
		this.wordid = Integer.parseInt(wordid);
		this.srcid = Integer.parseInt(srcid);
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
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
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

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_REMOVEWORD: {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getString(R.string.str_query_removeword, this.word));
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeData();
					dialog.dismiss();
				}
			});
			
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			dlg = builder.create();
			break;
		}
		default:;
		}
		return dlg;
	}

	protected void removeData() {
		if(DBAccess.removeWordData(this.wordid, this.srcid) == 0) {
			loadWords();
//			listView.removeView(this.subView);
		}
		else{
			Toast.makeText(this, "Remove '" + this.word + "' failed.", Toast.LENGTH_SHORT).show();
		}
	}

}
