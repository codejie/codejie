package jie.java.android.demodictionaryoflac2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jie.java.android.demodictionaryoflac2.data.DBAccess;
import jie.java.android.demodictionaryoflac2.data.Dictionary;
import jie.java.android.demodictionaryoflac2.data.Word;
import jie.java.android.demodictionaryoflac2.data.WordListAdapter;
import jie.java.android.demodictionaryoflac2.data.Word.XmlData;
import jie.java.android.demodictionaryoflac2.data.WordListAdapter.ItemData;
import jie.java.android.demodictionaryoflac2.data.XmlTranslator;
import jie.java.android.demodictionaryoflac2.view.RefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class DictionaryActivity extends Activity {
	
	private ViewSwitcher switcher = null;
	
	private EditText input = null;
	private ImageButton button = null;
	private RefreshListView list = null;
	private WebView web = null;
	
	private TranslateAnimation aniResultIn = null;
	private TranslateAnimation aniWord = null;
	private TranslateAnimation aniResultOut = null;		
	
	private WordListAdapter adapter = null;
	
	private String inputString = null;
	
	private Handler handler = null;
	Thread thread = null;
	private boolean checkRun = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_dictionary2);
				
		initAnination();		
		initView();		
		initData();
		
		initHandler();
		
		//initCheckThread();
	}
	
	private void initAnination() {
		
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        
        int SCREEN_HEIGHT = displaymetrics.heightPixels;
        int SCREEN_WIDTH = displaymetrics.widthPixels;        
        
        if(SCREEN_HEIGHT > SCREEN_WIDTH) {		
			aniResultIn = new TranslateAnimation(SCREEN_WIDTH, 0, 0, 0);
			aniResultIn.setDuration(400);
			aniWord = new TranslateAnimation(0, 0, 0, 0);
			aniWord.setDuration(400);
			aniResultOut = new TranslateAnimation(0, SCREEN_WIDTH, 0, 0);
			aniResultOut.setDuration(400);
        }
        else {
        	aniResultIn = new TranslateAnimation(0, 0, SCREEN_HEIGHT, 0);
			aniResultIn.setDuration(400);
			aniWord = new TranslateAnimation(0, 0, 0, 0);
			aniWord.setDuration(700);
			aniResultOut = new TranslateAnimation(0, 0, 0, SCREEN_HEIGHT);
			aniResultOut.setDuration(400);
        }        
        
	}	
	private void initView() {
		
		switcher = (ViewSwitcher) this.findViewById(R.id.viewSwitcher1);
		web = (WebView) switcher.findViewById(R.id.webView);
		web.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motion) {
				onWebTouch(motion);
				return true;
			}
			
		});
		
		adapter = new WordListAdapter(this, DBAccess.instance());
		
		input = (EditText) this.findViewById(R.id.keyword);
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
		
		button = (ImageButton) this.findViewById(R.id.clear);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClearButton();
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
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick(view, position, id);
			}
			
		});

		list.setAdapter(adapter);
		
	}

	private void initData() {		
		adapter.load(null);
	}
	
	private void initHandler() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if(inputString != null) {
					onInputChange(inputString);
					inputString = null;
				}
			}		
		};
	}
	
	private void initCheckThread() {	
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				checkRun = true;
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
			list.setSelection(0);
		}
	}

	
	protected void onRefreshData(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(adapter != null) {
//			adapter.setMaxRows(visibleItemCount + 1);
			adapter.refresh();
		}		
	}
	
	protected void onListItemClick(View view, int position, long id) {
//		Toast.makeText(this, "index = " + id, Toast.LENGTH_SHORT).show();
		
		ItemData item = (ItemData) list.getItemAtPosition(position);
		
		onWordItemClick(new Word(item.getIndex(), item.getText(), item.getFlag()));
	}
	

	private void onWordItemClick(Word word) {
		
		InputStream xmlFile = null;
		try {
			xmlFile = this.getResources().getAssets().open("a.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream xsltFile = null;
		try {
			xsltFile = this.getResources().getAssets().open("a.xsl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		XmlTranslator.test(xmlFile, xsltFile);
		
		
		Dictionary.getWordData(DBAccess.instance(), word);

		for(final XmlData data : word.getXmlData()) {
			Log.d("======", "Dict = " + data.getDictid());
			for(final String xml : data.getXml()) {
				Log.d("=====", "XML = " + xml);
			}
		}
		
//		String html = HtmlMaker.make(word, Dictionary);
//		web.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		
		showResultView();
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
		super.onResume();

		initCheckThread();
	}

	protected void onClearButton() {
		input.setText(null);
	}

	private void showWordListView() {
		switcher.clearAnimation();
		switcher.setInAnimation(aniWord);
		switcher.setOutAnimation(aniResultOut);
		switcher.showPrevious();		
	}
	
	private void showResultView() {
		switcher.clearAnimation();
		switcher.setInAnimation(aniResultIn);
		switcher.setOutAnimation(aniWord);	
		switcher.showNext();		
	}
	
	protected void onWebTouch(MotionEvent motion) {
		if(motion.getAction() == MotionEvent.ACTION_UP) {
			showWordListView();
		}
	}	
}
