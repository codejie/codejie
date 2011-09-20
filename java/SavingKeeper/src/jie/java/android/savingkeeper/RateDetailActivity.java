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
			InsertData();
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
	
	private void InsertData() {
		
		String start = ((EditText)this.findViewById(R.id.editStartDate)).getText().toString();
		String end = ((EditText)this.findViewById(R.id.editEndDate)).getText().toString();
		
		int currency = DBAccess.CURRENCY_TYPE_RMB;
		float rate0 = Float.valueOf(((EditText)this.findViewById(R.id.EditText11)).getText().toString());
		float rate1 = Float.valueOf(((EditText)this.findViewById(R.id.EditText12)).getText().toString());
		float rate2 = Float.valueOf(((EditText)this.findViewById(R.id.EditText13)).getText().toString());
		float rate3 = Float.valueOf(((EditText)this.findViewById(R.id.EditText14)).getText().toString());
		float rate4 = Float.valueOf(((EditText)this.findViewById(R.id.EditText15)).getText().toString());
		float rate5 = Float.valueOf(((EditText)this.findViewById(R.id.EditText16)).getText().toString());
		float rate6 = Float.valueOf(((EditText)this.findViewById(R.id.EditText17)).getText().toString());
		
		GLOBAL.DBACCESS.insertRate(start, end, currency, rate0, rate1, rate2, rate3, rate4, rate5, rate6);
		
		
		currency = DBAccess.CURRENCY_TYPE_RMB;
		rate0 = Float.valueOf(((EditText)this.findViewById(R.id.EditText21)).getText().toString());
		rate1 = Float.valueOf(((EditText)this.findViewById(R.id.EditText22)).getText().toString());
		rate2 = Float.valueOf(((EditText)this.findViewById(R.id.EditText23)).getText().toString());
		rate3 = Float.valueOf(((EditText)this.findViewById(R.id.EditText24)).getText().toString());
		rate4 = Float.valueOf(((EditText)this.findViewById(R.id.EditText25)).getText().toString());
		rate5 = Float.valueOf(((EditText)this.findViewById(R.id.EditText26)).getText().toString());
		rate6 = Float.valueOf(((EditText)this.findViewById(R.id.EditText27)).getText().toString());
		
		GLOBAL.DBACCESS.insertRate(start, end, currency, rate0, rate1, rate2, rate3, rate4, rate5, rate6);
		
		
		currency = DBAccess.CURRENCY_TYPE_RMB;
		rate0 = Float.valueOf(((EditText)this.findViewById(R.id.EditText31)).getText().toString());
		rate1 = Float.valueOf(((EditText)this.findViewById(R.id.EditText32)).getText().toString());
		rate2 = Float.valueOf(((EditText)this.findViewById(R.id.EditText33)).getText().toString());
		rate3 = Float.valueOf(((EditText)this.findViewById(R.id.EditText34)).getText().toString());
		rate4 = Float.valueOf(((EditText)this.findViewById(R.id.EditText35)).getText().toString());
		rate5 = Float.valueOf(((EditText)this.findViewById(R.id.EditText36)).getText().toString());
		rate6 = Float.valueOf(((EditText)this.findViewById(R.id.EditText37)).getText().toString());
		
		GLOBAL.DBACCESS.insertRate(start, end, currency, rate0, rate1, rate2, rate3, rate4, rate5, rate6);
		
	}
	
}
