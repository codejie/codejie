/**
 * file:   RateDetailActivity.java
 * author: codejie (codejie@gmail.com)
 * date:   Aug 30, 2011 11:00:04 PM
 */
package jie.java.android.savingkeeper;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class RateDetailActivity extends Activity  implements OnClickListener{

	private static int DIALOG_SHOWSTARTDATE		=	1;
	private static int DIALOG_SHOWENDDATE		=	2;
	
	private int iYear, iMonth, iDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.ratedetail);
		
		((Button)this.findViewById(R.id.button01)).setOnClickListener(this);
		((Button)this.findViewById(R.id.button02)).setOnClickListener(this);
		((Button)this.findViewById(R.id.button03)).setOnClickListener(this);
		
		((Button)this.findViewById(R.id.btnStartDate)).setOnClickListener(this);
		((Button)this.findViewById(R.id.btnEndDate)).setOnClickListener(this);
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
		else if(v.getId() == R.id.btnStartDate) {
			this.showDialog(DIALOG_SHOWSTARTDATE);
		}
		else if(v.getId() == R.id.btnEndDate) {
			this.showDialog(DIALOG_SHOWENDDATE);
		}
		else if(v.getId() == R.id.btnInsert) {
			
		}
		else {
			
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
		if(id == DIALOG_SHOWSTARTDATE) {
			final EditText edit = (EditText)this.findViewById(R.id.editStartDate);
			
			DatePickerDialog.OnDateSetListener onCheckinListSelected = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					edit.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth);
				}
				
			};

			return new DatePickerDialog(this, onCheckinListSelected, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));			
		}
		else if(id == DIALOG_SHOWENDDATE) {
			final EditText edit = (EditText)this.findViewById(R.id.editEndDate);
			
			DatePickerDialog.OnDateSetListener onCheckinListSelected = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					edit.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth);
				}
				
			};

			return new DatePickerDialog(this, onCheckinListSelected, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		}
		else {
			return null;
		}
		
  }	
	
}
