package jie.java.android.demodictionaryoflac2;

import jie.java.android.demodictionaryoflac2.data.DBAccess;
import jie.java.android.demodictionaryoflac2.data.DictionaryAdapter;
import jie.java.android.demodictionaryoflac2.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class DictionaryActivity extends Activity {

	private EditText input = null;
	private RefreshListView list = null;
	
	private DictionaryAdapter adapter = null;
	
	private String inputString = null;
	
	private Handler handler = null;
	Thread thread = null;
	private boolean checkRun = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_dictionary);
		
		initView();
		
		initData();
		
		initCheckThread();
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
				//onInputChange(s.toString());
				inputString = s.toString();
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
	
	private void initCheckThread() {

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if(inputString != null) {
					onInputChange(inputString);
					inputString = null;
				}
				Log.d("======", "handlemessage.....");				
			}		
		};
		
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(checkRun) {
					try {
						Thread.sleep(Global.INPUT_CHECK_PEROID);
						handler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		thread.start();
	}

	protected void onInputChange(String string) {
		if(adapter != null) {
			adapter.clear();
			adapter.load("word like '" + string.toString() + "%'");
		}
	}

	
	protected void onRefreshData(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(adapter != null) {
			adapter.setMaxRows(visibleItemCount + 1);
			adapter.refresh();
		}		
	}

	@Override
	protected void onDestroy() {
		checkRun = false;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		checkRun = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		checkRun = true;
		super.onResume();
	}
	
}
