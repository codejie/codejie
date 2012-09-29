package jie.java.android.lingoshook;

import jie.java.android.lingoshook.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;


public class TestActivity extends Activity {

	private RefreshListView listview = null;
	
	private ArrayAdapter<String> adapter = null;
	private SlidingDrawer drawer = null;
	private TextView tv = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.test);
		
		initListView();
	}

	private void initListView() {
		
		drawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer1);
		tv = (TextView) this.findViewById(R.id.textView1);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);		
		
		listview = (RefreshListView) this.findViewById(R.id.refreshListView1);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String str = adapter.getItem(position);
				tv.setText(str);
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
