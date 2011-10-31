/**
 * file   : AddSavingActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 26, 2011 5:57:30 PM
 */
package jie.java.android.savingkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SavingDetailActivity extends Activity implements OnClickListener {
	
	public static final int ACTION_ADD		=	1;
	public static final int ACTION_EDIT		=	3;
	
	protected static final int DIALOG_SHOWDATE	=	0;
	
	private int _iAction = 0;
	private int _iID = -1;
	
	private int _iYear, _iMonth, _iDay;
	private int _iCurrency = 0, _iType = 0, _iBank = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_iAction = this.getIntent().getExtras().getInt("ACTION");
		_iID = this.getIntent().getExtras().getInt("ID");
		
		this.setContentView(R.layout.savingdetail);
		
		initView();
		
		initData();
	}		
	
	private void initView() {

		ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency));
		adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner currency = ((Spinner)this.findViewById(R.id.listCurrency));
		currency.setAdapter(adapterCurrency);
		currency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
				_iCurrency = (int)id;
				showRate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}
			
		});
		
		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.type));
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner type = ((Spinner)this.findViewById(R.id.listType));
		type.setAdapter(adapterType);
		type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
				_iType = (int)id;
				showRate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		
		Cursor cursor = GLOBAL.DBACCESS.queryBank();
		
		SimpleCursorAdapter adapterBank = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, new String[] { DBAccess.TABLE_COLUMN_TITLE }, new int[] { android.R.id.text1 });
		adapterBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner bank = ((Spinner)this.findViewById(R.id.listBank));
		bank.setAdapter(adapterBank);
		bank.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
				_iBank = (int)id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		((Button)this.findViewById(R.id.btnCheckin)).setOnClickListener(this);
		((Button)this.findViewById(R.id.btnAction)).setOnClickListener(this);
	}
	
	private void initData() {

		_iYear = Calendar.getInstance().get(Calendar.YEAR);
		_iMonth = Calendar.getInstance().get(Calendar.MONTH);
		_iDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		_iCurrency = 0;
		_iType = 0;
		_iBank = -1;
					
		String title = null;
		String amount = new String("0.00");
		String note = null;
		
		int btnTitle = R.string.str_button_add;
		
		if(_iAction == ACTION_EDIT) {			
			Cursor cursor = GLOBAL.DBACCESS.querySaving(_iID);
			if(cursor.moveToFirst()) {
				title = cursor.getString(1);
				amount = cursor.getString(2);
				_iCurrency = cursor.getInt(3);
				
				String checkin = cursor.getString(4);
				Date ci = TOOLKIT.String2Date(checkin);
				_iYear = ci.getYear() + 1900;
				_iMonth = ci.getMonth();
				_iDay = ci.getDay();
				
				_iType = cursor.getInt(5);
				_iBank = cursor.getInt(6);
				
				note = cursor.getString(7);
				btnTitle = R.string.str_button_edit;
			}
			else {
				Toast.makeText(this, "Load saving(" + _iID + ") data from db failed.", Toast.LENGTH_LONG).show();
				this.finish();
			}			
		}

		EditText textTitle = (EditText)this.findViewById(R.id.textTitle);
		textTitle.setText(title);
		
		EditText textAmount = (EditText)this.findViewById(R.id.textAmount);
		textAmount.setText(amount);		
		
		EditText checkin = (EditText)this.findViewById(R.id.textCheckin);
		checkin.setText(_iYear + "." + (_iMonth + 1) + "." + _iDay);

		Spinner currency = ((Spinner)this.findViewById(R.id.listCurrency));
		currency.setSelection(_iCurrency);
		
		Spinner type = ((Spinner)this.findViewById(R.id.listType));
		type.setSelection(_iType);

		if(_iBank != -1) {
			Spinner bank = ((Spinner)this.findViewById(R.id.listBank));
			bank.setSelection(_iBank);
		}
		
		Button btn = (Button)this.findViewById(R.id.btnAction);	
		btn.setText(btnTitle);
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
		final EditText checkin = (EditText)this.findViewById(R.id.textCheckin);
		DatePickerDialog.OnDateSetListener onCheckinListSelected = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				_iYear = year;
				_iMonth = monthOfYear;
				_iDay = dayOfMonth;
				checkin.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth);
			}
			
		};
		
		if(id == DIALOG_SHOWDATE) {
			return new DatePickerDialog(this, onCheckinListSelected, _iYear, _iMonth, _iDay);
		}
        return null;
    }	
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnCheckin) {
			//Toast.makeText(this, "checkin button", Toast.LENGTH_SHORT).show();
			this.showDialog(DIALOG_SHOWDATE);
		}
		else if(v.getId() == R.id.btnAction) {
			onBtnAction();
		}
	}
	
	private void onBtnAction() {
		if(_iAction == ACTION_ADD || _iAction == ACTION_EDIT) {
			
			String title = ((EditText)this.findViewById(R.id.textTitle)).getText().toString();
			float amount = Float.valueOf(((EditText)this.findViewById(R.id.textAmount)).getText().toString());
			String checkin = ((EditText)this.findViewById(R.id.textCheckin)).getText().toString();
			String note = ((EditText)this.findViewById(R.id.textNote)).getText().toString();
			
			if(_iAction == ACTION_ADD) {
				GLOBAL.DBACCESS.insertSaving(title, amount, _iCurrency, checkin, _iType, _iBank, note);
			}
			else {
				GLOBAL.DBACCESS.updateSaving(_iID, title, amount, _iCurrency, checkin, _iType, _iBank, note);
			}
			
		}
		else {
			
		}
		this.finish();
	}
	
	private void showRate() {
		//String checkin = ((EditText)this.findViewById(R.id.textCheckin)).getText().toString();
		float rate = GLOBAL.CALCULATOR.getLatestRate(_iCurrency, _iType);
		((EditText)this.findViewById(R.id.textRate)).setText(String.format("%.03f%%", rate * 100));
	}
	
}
