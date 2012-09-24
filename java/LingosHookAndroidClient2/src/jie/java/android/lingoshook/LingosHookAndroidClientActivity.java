package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LingosHookAndroidClientActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.lingoshookandroidclient);
		//this.setContentView(R.layout.test);
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
	
}
