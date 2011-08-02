/**
 * file   : AddSavingActivity.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 26, 2011 5:57:30 PM
 */
package jie.java.android.savingkeeper;

import java.util.Calendar;

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
	public static final int ACTION_REMOVE	=	2;
	public static final int ACTION_EDIT		=	3;
	
	protected static final int DIALOG_SHOWDATE	=	0;
	
	private int iAction = 0;
	private int iID = -1;
	
	private int iYear, iMonth, iDay;
	private long iType, iBank;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		iAction = this.getIntent().getExtras().getInt("ACTION");
		iID = this.getIntent().getExtras().getInt("ID");
		
		this.setContentView(R.layout.savingdetail);
		
		initView();
		
		////// 
		
		((Button)this.findViewById(R.id.btnCheckin)).setOnClickListener(this);
		//((Spinner)this.findViewById(R.id.listCurrency)).setOnItemClickListener(mMessageClickedHandler); 		
		
/*
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "RMB", "US", "EU" });
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner)this.findViewById(R.id.listCurrency)).setAdapter(adapter);
*/		
/*
		//if(this.action == ACTION_ADD) {
		if(this.getIntent().getExtras().getInt("ACTION") == ACTION_ADD) {
			Button btn = new Button(this);
			btn.setText(R.string.str_button_add);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			((LinearLayout)this.findViewById(R.id.mainLayout)).addView(btn, params);
			//View view = this.addContentView(view, params) .getContentView();
		}
*/		
	}
	
	private void initView() {
		
		iYear = Calendar.getInstance().get(Calendar.YEAR);
		iMonth = Calendar.getInstance().get(Calendar.MONTH);
		iDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);		

		ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency));
		adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner)this.findViewById(R.id.listCurrency)).setAdapter(adapterCurrency);
		
		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.type));
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner type = ((Spinner)this.findViewById(R.id.listType));
		type.setAdapter(adapterType);
		type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
				iType = id;
				Toast.makeText(SavingDetailActivity.this, "pos: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
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
				iBank = id;
				Toast.makeText(SavingDetailActivity.this, "pos: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		Button btn = (Button)this.findViewById(R.id.btnAction);
		
		switch(iAction) {
		case ACTION_ADD: {
			btn.setText(R.string.str_button_add);
		}
		break;
		case ACTION_EDIT: {
			btn.setText(R.string.str_button_edit);			
		}
		break;
		case ACTION_REMOVE: {
			btn.setText(R.string.str_button_remove);			
		}
		break;
		default:
			break;
		}
		
		((Button)this.findViewById(R.id.btnCheckin)).setOnClickListener(this);
		((Button)this.findViewById(R.id.btnAction)).setOnClickListener(this);
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
		final EditText checkin = (EditText)this.findViewById(R.id.textCheckin);
		DatePickerDialog.OnDateSetListener onCheckinListSelected = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				iYear = year;
				iMonth = monthOfYear;
				iDay = dayOfMonth;
				checkin.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
			
		};
		
		if(id == DIALOG_SHOWDATE) {
			return new DatePickerDialog(this, onCheckinListSelected, iYear, iMonth, iDay);
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
		if(iAction == ACTION_ADD || iAction == ACTION_EDIT) {
			
			String title = ((EditText)this.findViewById(R.id.textTitle)).getText().toString();
			float amont = Float.valueOf(((EditText)this.findViewById(R.id.textAmount)).getText().toString());
			
		}
		else if(iAction == ACTION_REMOVE) {
			
		}
		else {
			
		}
	}
	
}
