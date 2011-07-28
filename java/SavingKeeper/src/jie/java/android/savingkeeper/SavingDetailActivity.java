/**
 * file   : AddSavingActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 26, 2011 5:57:30 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SavingDetailActivity extends Activity implements OnClickListener {
	
	public static final int ACTION_ADD		=	1;
	public static final int ACTION_REMOVE	=	2;
	public static final int ACTION_EDIT		=	3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.savingdetail);
		
		initView(this.getIntent().getExtras().getInt("ACTION"));
		
		initViewData(this.getIntent().getExtras().getInt("ACTION"));
		
		//////
		
		((Button)this.findViewById(R.id.btnCheckin)).setOnClickListener(this);
		((Spinner)this.findViewById(R.id.listCurrency)).setOnItemClickListener(mMessageClickedHandler); 		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "RMB", "US", "EU" });
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner)this.findViewById(R.id.listCurrency)).setAdapter(adapter);
		

		//if(this.action == ACTION_ADD) {
		if(this.getIntent().getExtras().getInt("ACTION") == ACTION_ADD) {
			Button btn = new Button(this);
			btn.setText(R.string.str_button_add);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			((LinearLayout)this.findViewById(R.id.mainLayout)).addView(btn, params);
			//View view = this.addContentView(view, params) .getContentView();
		}
	}
	
	private void initView(int action) {
		
	}
	
	private void initViewData(int action) {
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnCheckin) {
			Toast.makeText(this, "checkin button", Toast.LENGTH_SHORT).show();
		}
	}
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		@Override
	    public void onItemClick(AdapterView parent, View v, int position, long id)
	    {
	        // Display a messagebox.
	        Toast.makeText(SavingDetailActivity.this,"v.id: " + v.getId() + " pos: " + position + " id: " + id,Toast.LENGTH_SHORT).show();
	    }

	};


	
}
