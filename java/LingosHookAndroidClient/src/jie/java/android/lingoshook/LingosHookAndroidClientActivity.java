package jie.java.android.lingoshook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {

	private static final int DIALOG_IMPORT		=	1;
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

        showMainView();

        if(Global.STATE_CODING == 2) {
        	intiAdView();
        }
    }

	private void intiAdView() {
		
		Global.getScreenInfo(this);
		
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout2);
		//new AdPanelView(this, ll, 0, 80);
		new AdPanelView(this, ll, 0, Global.SCREEN_HEIGHT - 80);
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
				
				//showMainView();
				
				Intent intent = new Intent(this, PlayActivity.class);
				this.startActivity(intent);
				
				showMainView();
			}
			else {
				showListView();
				
				//hasListShowed = true;
			}
			//this.setContentView(R.layout.main_list);

            
            
			return true;
			
//			if(DBAccess.checkWord()) {
//				Intent intent = new Intent(this, PlayActivity.class);
//				this.startActivity(intent);
//			}
//			else {
//				this.showDialog(DIALOG_NOWORD);
//			}
		}
		return true;
	}
	
	private void showMainView() {
        //LayoutInflater factory = LayoutInflater.from(this);
        //final View v = factory.inflate(R.layout.main_title, null);
        
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
        
        int n = DBAccess.getScoreCount(true, -1);
        int o = DBAccess.getScoreCount(false, Score.getDeltaUpdated());
        
        if(n > Setting.numLoadNewWord) {
        	n = Setting.numLoadNewWord;
        }
        
        if(Setting.numLoadOldWord != 0 && o > Setting.numLoadOldWord) {
        	o = Setting.numLoadOldWord;
        }        
                
		TextView text = ((TextView)this.findViewById(R.id.textView2));
		text.setText(String.format("%d", (n)));

		text = ((TextView)this.findViewById(R.id.textView3));
		text.setText(String.format("%d", (o)));
       
        //p.addView(LayoutInflater.from(this).inflate(R.layout.main_list, null));
        
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
    	case R.id.menu_import:
    		onMenuImport();
    		break;
    	case R.id.menu_setting:
    		onMenuSetting();
    		break;
    	case R.id.menu_about:
    		onMenuAbout();
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
		case DIALOG_IMPORT: {
    		final Builder builder = new AlertDialog.Builder(this);
    		builder.setIcon(android.R.drawable.ic_dialog_info);
    		String dir = Environment.getExternalStorageDirectory().getPath();
    		String file = "LH_Export.db3";
            LayoutInflater factory = LayoutInflater.from(this);
            final View v = factory.inflate(R.layout.import_dialog, null);
            final EditText d = (EditText) v.findViewById(R.id.editText1);
            d.setText(dir);
            final EditText f = (EditText) v.findViewById(R.id.editText2);
            f.setText(file);
            v.findViewById(R.id.progressBar1).setVisibility(View.GONE);
    		builder.setView(v);
    		final CheckBox c = (CheckBox)v.findViewById(R.id.checkBox1);
    		
    		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(final DialogInterface dialog, int which) {
    				v.findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
    				
    				final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							dialog.dismiss();
							//super.handleMessage(msg);
						}    					
    				};

    				Runnable r = new Runnable() {

						@Override
						public void run() {
							importData(d.getText().toString(), f.getText().toString(), c.isChecked());
							handler.sendEmptyMessage(0);
						}    					
    				};
    				
    				handler.post(r);
    			}    			
    		});
    		builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();					
				}
			});
    		
    		dlg = builder.create();
			
			}
			break;
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
	
	private void importData(final String dir, final String file, boolean overwrite) {
		if(DBAccess.importData(dir + "/" + file, (overwrite ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) != 0) {
			Toast.makeText(this, "Import data failed.", Toast.LENGTH_LONG).show();
		}
		else {
			Score.init();
		}
	}
	
	private void onMenuExit() {
		Global.exitApplication();
		this.finish();
	}
	
	private void onMenuImport() {
		this.showDialog(DIALOG_IMPORT);
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
}