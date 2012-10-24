package jie.java.android.lingoshook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import jie.java.android.lingoshook.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
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
		
		parseXml();
		
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
	
	void parseXml() {
		String str = "<c><w>abandon</w><d>1</d><e>abandons|abandoned|abandoning</e><f><s>a·ban·don || ə'bændən</s><i><c>n.</c><m>  放纵, 放任; 狂热</m></i><i><c>v.</c><m>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</m></i></f></c>";
		
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = new ByteArrayInputStream(str.getBytes());
		try {
			parser.setInput(is, "UTF-8");
			
			int type = parser.getEventType();
			while(type != XmlPullParser.END_DOCUMENT) {
				
	          if(type == XmlPullParser.START_DOCUMENT) {
	              System.out.println("Start document");
	          } else if(type == XmlPullParser.END_DOCUMENT) {
	              System.out.println("End document");
	          } else if(type == XmlPullParser.START_TAG) {
	              System.out.println("Start tag "+parser.getName());
	          } else if(type == XmlPullParser.END_TAG) {
	              System.out.println("End tag "+parser.getName());
	          } else if(type == XmlPullParser.TEXT) {
	              System.out.println("Text "+parser.getText());
	          }
				
				type = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
