package jie.java.android.lingoshook;

import jie.java.android.lingoshook.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;


public class TestActivity extends Activity {

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d("tag", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d("tag", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	private RefreshListView listview = null;
	
	private ArrayAdapter<String> adapter = null;
	private SlidingDrawer drawer = null;
	private TextView tv = null;
	private WebView web = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("tag", "onCreate()");
		
		this.setContentView(R.layout.test);
		
		initListView();
	}

	private void initListView() {
		
		drawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer1);
		web = (WebView) this.findViewById(R.id.webView1);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);		
		
		listview = (RefreshListView) this.findViewById(R.id.refreshListView1);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str = adapter.getItem(position);
				web.loadData(str, "text/text", "utf-8");
				popDrawer(true);
				listview.setSelection(position);
			}
			
		});
		
		listview.setAdapter(adapter);
		
		for(int i = 0; i < 24; ++ i)  {
			adapter.add(Integer.toString(i));
		}
		
		adapter.notifyDataSetChanged();
	}
	
	private void popDrawer(boolean open) {
		if(!drawer.isOpened()) {
			drawer.animateOpen();
		}
	}

}
