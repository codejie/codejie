package jie.java.android.lingoshook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jie.java.android.lingoshook.DataFormat.Data;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {

	private static final int DIALOG_ABOUT 		=	2;
	private static final int DIALOG_NOWORD		=	3;	

	private boolean hasListShowed 				=	false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Toast.makeText(this, "state:" + (savedInstanceState != null ? "yes" : "no"), Toast.LENGTH_SHORT).show();
        
        Global.initApplication(this);

        setContentView(R.layout.main);              
        this.findViewById(R.id.linearLayout1).setOnTouchListener(this);
        //this.findViewById(R.id.linearLayout1).setLongClickable(true);

        if(Global.STATE_CODING == 2) {
        	intiAdView();
        }
    }

	@Override
	protected void onResume() {
		Score.init();

        showMainView();
		super.onResume();
	}

	private void intiAdView() {
		
		Global.getScreenInfo(this);
		
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout2);
		//new AdPanelView(this, ll, 0, 80);
		new AdPanelView(this, ll, 0, Global.SCREEN_HEIGHT - AdPanelView.BANNER_WIDTH);
	}

	@Override
	protected void onDestroy() {
		Global.exitApplication();
		//AdPanelView.release();
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			
			if(hasListShowed) {				
				if(Score.TODAY_NEW != 0 || Score.TODAY_OLD != 0) {
					Intent intent = new Intent(this, PlayActivity.class);
					this.startActivity(intent);
				}
				else if(Score.WORD_TOTAL == 0)
				{
					this.showDialog(DIALOG_NOWORD);
				}
				else {
					Toast.makeText(this, this.getString(R.string.str_today_nomoreword), Toast.LENGTH_LONG).show();
					return true;
				}
				
//				showMainView();
			}
			else {
				if(Score.WORD_TOTAL == 0) {
					//Toast.makeText(this, this.getString(R.string.str_nomoreword), Toast.LENGTH_LONG).show();
					this.showDialog(DIALOG_NOWORD);
					return true;
				}
				
				showListView();
			}
		}
		return true;
	}
	
	private void showMainView() {

		LinearLayout p = (LinearLayout) this.findViewById(R.id.linearLayout1);
        p.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.main_title, p);
        //p.addView(v);
        
        hasListShowed = false;
	}

	private void showListView() {
        //LayoutInflater factory = LayoutInflater.from(this);
        //final View v = factory.inflate(R.layout.main_list, null);
        
        LinearLayout p = (LinearLayout) this.findViewById(R.id.linearLayout1);
        p.removeAllViews();
        LayoutInflater.from(this).inflate(R.layout.main_list, p);
            
		TextView text = ((TextView)this.findViewById(R.id.textView2));
		text.setText(String.format("%d", (Score.TODAY_NEW)));

		text = ((TextView)this.findViewById(R.id.textView3));
		text.setText(String.format("%d", (Score.TODAY_OLD)));

		text = ((TextView)this.findViewById(R.id.textView1));
		text.setText(String.format("%d", (Score.WORD_TOTAL)));		
        
        hasListShowed = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
    	switch(item.getItemId()) {
    	case R.id.menu_score_list:
    		onMenuScoreList();
    		break;
    	case R.id.menu_exit:
    		onMenuExit();
    		break;
//    	case R.id.menu_import:
//    		onMenuImport();
//    		break;
    	case R.id.menu_setting:
    		onMenuSetting();
    		break;
    	case R.id.menu_about:
    		//onMenuAbout();
    		this.importXml("a.xml", true);
    		break;
    	case R.id.menu_import_local:
    		onMenuImportLocal();
    		break;
    	case R.id.menu_import_remote:
    		onMenuImportRemote();
    		break;
    	default:
    		break;
    	}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_ABOUT: {
			Builder builder = new AlertDialog.Builder(this);
			builder.setView(LayoutInflater.from(this).inflate(R.layout.about_dialog, null));
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			builder.setNeutralButton("Help", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//LingosHookAndroidClientActivity.this.startActivity(new Intent(LingosHookAndroidClientActivity.this, HtmlDisplayActivity.class));
		    		Intent intent = new Intent(LingosHookAndroidClientActivity.this, HtmlDisplayActivity.class);
		    		intent.putExtra(HtmlDisplayActivity.REQ, HtmlDisplayActivity.REQ_HELP);
		    		LingosHookAndroidClientActivity.this.startActivity(intent);					
					dialog.dismiss();
					
				}
			});
			
			dlg = builder.create();
			}
			break;
		case DIALOG_NOWORD:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.str_nowordindb);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dlg = builder.create();
			break;
		default:
			break;
		}
		return dlg;
	}    
		
	private void onMenuExit() {
		Global.exitApplication();
		this.finish();
	}
	
	private void onMenuImportLocal() {
		this.startActivity(new Intent(this, ImportDBActivity.class));
		//this.showDialog(DIALOG_IMPORT);
	}
	
	private void onMenuImportRemote() {
		this.startActivity(new Intent(this, HttpdActivity.class));
	}
	
	private void onMenuScoreList() {
		this.startActivity(new Intent(this, ScoreListActivity.class));
	}
	
	private void onMenuSetting() {
		this.startActivity(new Intent(this, SettingActivity.class));
	}

	private void onMenuAbout() {
		this.showDialog(DIALOG_ABOUT);		
	}
	
/*
	<LAC>
	<Version>1.0</Version>
	<DefaultDict>test</DefaultDict>
	<WordList>
		<Item>
			<Dict></Dict>
			<Word></Word>
			<Symbol></Symbol>
			<DataList>
				<Item>
					<Category></Category>
					<Meaning></Meaning>
				</Item>
			</DataList>
		</Item>
	</WordList>
	</LAC>	
 */
	
	private void importXml(final String file, boolean overwrite) {
//		File ifile = new File(file);
//		if(!ifile.exists()) {
//			return;
//		}
		
		try {
			//FileInputStream is = new FileInputStream(ifile);
			InputStream is = this.getAssets().open(file);
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xp = factory.newPullParser();
			xp.setInput(is, "UTF-8");
			
			DataFormat.Data data = new DataFormat.Data();
			String defDict = "Default Dictionary";
			
			int type = xp.getEventType();
			while(type != XmlPullParser.END_DOCUMENT) {
				if(type == XmlPullParser.START_TAG) {
					if(xp.getName().equals("DefaultDict")) {
						defDict = xp.getText();
					}
					else if(xp.getName().equals("WordList")) {
						type = xp.nextTag();
						if(type == XmlPullParser.START_TAG) {
							while(xp.getName().equals("Item")) {
								getItem(xp, data);
								checkData(data, defDict);
//								type = xp.next();
								type = xp.nextTag();
								type = xp.nextTag();
							}
						}
					}
				}
				type = xp.next();
			}
			is.close();
			
			
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	private int checkData(Data data, String defDict) {
		return 0;		
	}

	private int getItem(XmlPullParser xp, DataFormat.Data data) {
		try {
			//Dict			
			int type = xp.nextTag();
			if(xp.getName().equals("Dict")) {
				type = xp.next();
				data.dict = xp.getText();
				type = xp.next();
			}
			//Word
			type = xp.nextTag();
			if(xp.getName().equals("Word")) {
				type = xp.next();
				data.word = xp.getText();
				type = xp.next();
			}
			else {
				return -1;
			}
			
			//Symbol
			type = xp.nextTag();
			if(xp.getName().equals("Symbol")) {
				type = xp.next();
				data.symbol = xp.getText();
				type = xp.next();
			}
			//DataList
			type = xp.nextTag();
			if(xp.getName().equals("DataList")) {
				type = xp.nextTag();
				if(type == XmlPullParser.START_TAG) {
					while(xp.getName().equals("Item")) {
						getWordData(xp, data);
						checkWordData(data);
						//type = xp.next();
						type = xp.nextTag();
						type = xp.nextTag();
					}
				}
			}
			else {
				return -1;
			}
			
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int checkWordData(Data data) {
		return 0;
	}

	private int getWordData(XmlPullParser xp, Data data) {
		try {
			//Category
			int type = xp.nextTag();			
			if(xp.getName().equals("Category")) {
				type = xp.next();
				data.category.add(xp.getText());
				type = xp.next();
			}
			else {
				return -1;
			}
			//Meaning
			type = xp.nextTag();
			if(xp.getName().equals("Meaning")) {
				type = xp.next();
				data.meaning.add(xp.getText());
				type = xp.next();
			}
			else {
				return -1;
			}
			
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;		
	}
	
	
	
}