package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class LingosHookAndroidClientActivity extends Activity implements OnTouchListener {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initObjects();
        
        setContentView(R.layout.main);
        
        this.findViewById(R.id.relativeLayout1).setOnTouchListener(this);
        this.findViewById(R.id.relativeLayout1).setLongClickable(true);
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP) {
			Intent intent = new Intent(this, WordDisplayActivity.class);
			this.startActivity(intent);
			this.finish();			
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
    	switch(item.getItemId()) {
    	case R.id.menu_exit:
    		onMenuExit();
    		break;
    	case R.id.menu_import:
    		onMenuImport();
    		break;
    	default:
    		break;
    	}
		return super.onMenuItemSelected(featureId, item);
	}
    
	private int initObjects() {
		if(DBAccess.init(Global.DATABASE_NAME) != 0)
			return -1;
		return 0;
	}
	
	private void onMenuExit() {
		Global.exitApplication();
	}
	
	private void onMenuImport() {
		DBAccess.importData(DBAccess.IMPORTTYPE_OVERWRITE);
	}
}