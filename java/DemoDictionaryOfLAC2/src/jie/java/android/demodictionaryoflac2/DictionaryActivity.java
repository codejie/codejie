package jie.java.android.demodictionaryoflac2;

import jie.java.android.demodictionaryoflac2.data.DBAccess;
import jie.java.android.demodictionaryoflac2.data.DictionaryAdapter;
import jie.java.android.demodictionaryoflac2.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DictionaryActivity extends Activity {

	private EditText input = null;
	private RefreshListView list = null;
	
	private DictionaryAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_dictionary);
		
		initView();
		
		initData();		
	}
	
	private void initView() {
		
		adapter = new DictionaryAdapter(this, DBAccess.instance());
		
		input = (EditText) this.findViewById(R.id.editText1);
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int count,	int after) {
				onInputChange(s.toString());
			}
			
		});
		
		list = (RefreshListView) this.findViewById(R.id.refreshListView1);
		list.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
			
			@Override
			public void onStopRefresh() {
			}
			
			@Override
			public void onRefresh(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				onRefreshData(firstVisibleItem, visibleItemCount, totalItemCount);
			}
			
			@Override
			public void onEndRefresh() {
			}
			
			@Override
			public void onBeginRefresh() {
			}
		});		
		list.setAdapter(adapter);
		
	}

	private void initData() {
		adapter.load(null);
	}

	protected void onInputChange(String string) {
		// TODO Auto-generated method stub
		
	}

	
	protected void onRefreshData(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
}
