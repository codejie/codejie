/**
 * file   : AddSavingActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 26, 2011 5:57:30 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SavingDetailActivity extends Activity {
	
	public static final int ACTION_ADD		=	1;
	public static final int ACTION_REMOVE	=	2;
	public static final int ACTION_EDIT		=	3;
	
	private int action = 0;
	
	public SavingDetailActivity(int act) {
		super();
		
		this.action = act;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.savingdetail);
		
		if(this.action == ACTION_ADD) {
			Button btn = new Button(this);
			btn.setText(R.string.str_button_add);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			((LinearLayout)this.findViewById(R.id.mainLayout)).addView(btn, params);
			//View view = this.addContentView(view, params) .getContentView();
		}
	}
	
	
	
}
