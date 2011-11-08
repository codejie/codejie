package jie.java.android.savingkeeper;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SavingTotalActivity extends Activity implements OnClickListener {
	
	private class StatsData {
		public int sum = 0;
		public float amount = 0.0f;
		public float now = 0.0f;
		public float end = 0.0f;
	}
	
	private StatsData _total = new StatsData();
	private StatsData _current = new StatsData();
	private StatsData _maturity = new StatsData();
	private StatsData _nonmaturity = new StatsData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Cursor cursor = GLOBAL.DBACCESS.querySaving();
		while(cursor.moveToNext()) {
			float amount = cursor.getFloat(2);
			int currency = cursor.getInt(3);
			int type = cursor.getInt(5);
			String checkin = cursor.getString(4);
		
			DataCalculator.CalcResult result = new DataCalculator.CalcResult();
			Date ci = TOOLKIT.String2Date(checkin);
			GLOBAL.CALCULATOR.calcMoney(ci, amount, currency, type, result);
			
			incStatsData(ci, amount, type, result);
		}
		cursor.close();
		
		this.setContentView(R.layout.showtotal);
		
		showStatsData();
		
		((Button)this.findViewById(R.id.button1)).setOnClickListener(this);
	}
	
	private void incStatsData(final Date checkin, float amount, int type, final DataCalculator.CalcResult result) {
		
		_total.sum ++;
		_total.amount += amount;
		_total.now += result.now;
		
		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			_current.sum ++;
			_current.amount += amount;
			_current.now += result.now;
		}
		else {
			
			boolean end = false;
			
			switch(type) {
			case DBAccess.SAVING_TYPE_FIXED_3_MONTH:
				checkin.setMonth(checkin.getMonth() + 3);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			case DBAccess.SAVING_TYPE_FIXED_6_MONTH:
				checkin.setMonth(checkin.getMonth() + 6);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			case DBAccess.SAVING_TYPE_FIXED_1_YEAR:
				checkin.setYear(checkin.getYear() + 1);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			case DBAccess.SAVING_TYPE_FIXED_2_YEAR:
				checkin.setYear(checkin.getYear() + 2);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			case DBAccess.SAVING_TYPE_FIXED_3_YEAR:
				checkin.setYear(checkin.getYear() + 3);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			case DBAccess.SAVING_TYPE_FIXED_5_YEAR:
				checkin.setYear(checkin.getYear() + 5);
				end = checkin.compareTo(GLOBAL.TODAY)  <= 0;
				break;
			default:
				break;
			}
			
			if(end) {
				_maturity.sum ++;
				_maturity.amount += amount;
				_maturity.now += result.now;
				_maturity.end += result.end;
			}
			else {
				_nonmaturity.sum ++;
				_nonmaturity.amount += amount;
				_nonmaturity.now += result.now;
				_nonmaturity.end += result.end;				
			}
		}
	}

	private void showStatsData() {
		//Total
		((TextView)this.findViewById(R.id.TextView18)).setText(String.format("%d", _total.sum));
		((TextView)this.findViewById(R.id.TextView21)).setText(String.format("%.02f", _total.amount));
		((TextView)this.findViewById(R.id.TextView19)).setText(String.format("%.02f", _total.now));
		
		//Current
		((TextView)this.findViewById(R.id.TextView12)).setText(String.format("%d", _current.sum));
		((TextView)this.findViewById(R.id.TextView15)).setText(String.format("%.02f", _current.amount));
		((TextView)this.findViewById(R.id.TextView13)).setText(String.format("%.02f", _current.now));

		//Maturity Fixed
		((TextView)this.findViewById(R.id.TextView06)).setText(String.format("%d", _maturity.sum));
		((TextView)this.findViewById(R.id.TextView09)).setText(String.format("%.02f", _maturity.amount));
		((TextView)this.findViewById(R.id.TextView07)).setText(String.format("%.02f", _maturity.now));
		((TextView)this.findViewById(R.id.TextView24)).setText(String.format("%.02f", _maturity.end));

		//Non-Maturity Fixed
		((TextView)this.findViewById(R.id.TextView3)).setText(String.format("%d", _nonmaturity.sum));
		((TextView)this.findViewById(R.id.TextView04)).setText(String.format("%.02f", _nonmaturity.amount));
		((TextView)this.findViewById(R.id.TextView02)).setText(String.format("%.02f", _nonmaturity.now));
		((TextView)this.findViewById(R.id.TextView26)).setText(String.format("%.02f", _nonmaturity.end));
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.button1) {
//	        Intent intent = new Intent(this, SavingListActivity.class);
//			this.startActivity(intent);
			this.finish();
		}
	}

}
