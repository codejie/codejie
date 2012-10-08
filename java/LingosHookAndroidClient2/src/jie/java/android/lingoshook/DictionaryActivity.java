package jie.java.android.lingoshook;

import jie.java.android.lingoshook.data.DBAccess;
import jie.java.android.lingoshook.view.RefreshListView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class DictionaryActivity extends Activity {

	
	private EditText input = null;
	private RefreshListView list = null;
	private WebView web = null;
	private DictionaryAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.dictionary);
		
		initView();
		
		initData();
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
		
		adapter = new DictionaryAdapter(this, DBAccess.instance);
		
		list = (RefreshListView) this.findViewById(R.id.refreshListView1);
		list.setAdapter(adapter);
		
		web = (WebView) this.findViewById(R.id.webView1);
		web.loadDataWithBaseURL(null, "<HTML><BODY>This is a test for <B>Dictionay</B> data.</BODY></HTML>", "text/html", "utf-8", null);
	}
	
	private void initData() {
		adapter.load(null);
	}

}
