/**
 * file:   RateDetailActivity.java
 * author: codejie (codejie@gmail.com)
 * date:   Aug 30, 2011 11:00:04 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class RateDetailActivity extends Activity  implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.ratedetail);
		
		((Button)this.findViewById(R.id.button01)).setOnClickListener(this);
		((Button)this.findViewById(R.id.button02)).setOnClickListener(this);
		((Button)this.findViewById(R.id.button03)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button01) {
			View tl = this.findViewById(R.id.tableLayout01);
			if(tl.getVisibility() == View.GONE) {
				tl.setVisibility(View.VISIBLE);
			}
			else if(tl.getVisibility() == View.VISIBLE) {
				tl.setVisibility(View.GONE);
			}
		}
		else if(v.getId() == R.id.button02) {
			View tl = this.findViewById(R.id.tableLayout02);
			if(tl.getVisibility() == View.GONE) {
				tl.setVisibility(View.VISIBLE);
			}
			else if(tl.getVisibility() == View.VISIBLE) {
				tl.setVisibility(View.GONE);
			}
		}
		else if(v.getId() == R.id.button03) {
			View tl = this.findViewById(R.id.tableLayout03);
			if(tl.getVisibility() == View.GONE) {
				tl.setVisibility(View.VISIBLE);
			}
			else if(tl.getVisibility() == View.VISIBLE) {
				tl.setVisibility(View.GONE);
			}
		}
		else if(v.getId() == R.id.btnInsert) {
			
		}
		else {
			
		}
		
	}
	
}
