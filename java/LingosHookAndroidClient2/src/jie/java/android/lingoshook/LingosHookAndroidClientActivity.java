package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class LingosHookAndroidClientActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Global.init(savedInstanceState);
		
		this.setContentView(R.layout.lingoshookandroidclient);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lingoshookandroidclient, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.item1: {
				this.startActivity(new Intent(this, DictionaryActivity.class));
				break;
			}
			case R.id.item2: {
				this.startActivity(new Intent(this, TestActivity.class));
				break;
			}
			default:;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
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
	
}
