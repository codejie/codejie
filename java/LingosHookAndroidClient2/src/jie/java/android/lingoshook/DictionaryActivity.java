package jie.java.android.lingoshook;

import jie.java.android.lingoshook.view.RefreshListView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class DictionaryActivity extends Activity {

	private class CursorRefreshAdapter extends BaseAdapter {

		public CursorRefreshAdapter(Context context) {
			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}		
	}
	
	
	private EditText input = null;
	private RefreshListView list = null;
	private CursorRefreshAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.dictionary);
		
		initView();
	}

	private void initView() {
		input = (EditText) this.findViewById(R.id.editText1);		
		input.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable arg0) {
				//onTextAfterChange()
				Log.d("this", "afterTextChanged():string:" + input.getText().toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Log.d("this", "beforeTextChanged():string:" + s.toString() + " - s=" + start + " c=" + count + " a=" + after);
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
				Log.d("this", "onTextChanged():string:" + s.toString() + " - s=" + start + " c=" + count + " a=" + after);
			}			
		});
		
		adapter = new CursorRefreshAdapter(this);
		
		list = (RefreshListView) this.findViewById(R.id.refreshListView1);
		list.setAdapter(adapter);
	}

}
