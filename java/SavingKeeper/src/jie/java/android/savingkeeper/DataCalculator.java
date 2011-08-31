/**
 * file:   DataCalculator.java
 * author: codejie (codejie@gmail.com)
 * date:   Aug 23, 2011 12:04:20 AM
 */
package jie.java.android.savingkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.database.Cursor;
import android.util.Log;

public class DataCalculator {

	public static final class CalcResult {
		public float now;
		public float end;
		
		public CalcResult() {
			now = 0;
			end = 0;
		}
	}
	
	private final class RateData {
		public Date begin = new Date();
		public Date end = new Date();
		public float[][] data = new float[3][7];
	}
	
	ArrayList<RateData> RateData = new ArrayList<RateData>();
	
	public int init() {
		loadRateData();
		return 0;
	}
	
	public void release() {
		
	}
	
	protected int loadRateData() {
		Cursor cursor = GLOBAL.DBACCESS.queryRate();
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");

		RateData data = new RateData();
		
		int count = 0;
		while(cursor.moveToNext()) {
			
			if(count == 0) {
				try {
					data.begin = fmt.parse(cursor.getString(1));
					data.end = fmt.parse(cursor.getString(2));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return -1;
				}
			}
			
			if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_RMB) {
				data.data[0][0] = cursor.getFloat(4);
				data.data[0][1] = cursor.getFloat(5);
				data.data[0][2] = cursor.getFloat(6);
				data.data[0][3] = cursor.getFloat(7);
				data.data[0][4] = cursor.getFloat(8);
				data.data[0][5] = cursor.getFloat(9);
				data.data[0][6] = cursor.getFloat(10);
			}
			else if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_US) {
				data.data[1][0] = cursor.getFloat(4);
				data.data[1][1] = cursor.getFloat(5);
				data.data[1][2] = cursor.getFloat(6);
				data.data[1][3] = cursor.getFloat(7);
				data.data[1][4] = cursor.getFloat(8);
				data.data[1][5] = cursor.getFloat(9);
				data.data[1][6] = cursor.getFloat(10);
			}
			else if(cursor.getInt(3) == DBAccess.CURRENCY_TYPE_EU) {
				data.data[2][0] = cursor.getFloat(4);
				data.data[2][1] = cursor.getFloat(5);
				data.data[2][2] = cursor.getFloat(6);
				data.data[2][3] = cursor.getFloat(7);
				data.data[2][4] = cursor.getFloat(8);
				data.data[2][5] = cursor.getFloat(9);
				data.data[2][6] = cursor.getFloat(10);
			}
			
			++ count;
			if(count == 3) {
				RateData.add(data);
				
				data = new RateData();			
				count = 0;
			}
			
		}
		
		return 0;
	}
	
	public int calcMoney(final String checkin, float amount, int currency, int type, CalcResult result) {
		
		for(RateData data : RateData) {
			Log.d(GLOBAL.APP_TAG, "begin: " + data.begin.toString() + " end:" + data.end.toString() + " data: " + data.data[0][1]);
		}
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
		Date ci = null;
		try {
			ci = fmt.parse(checkin);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
		
		result.end = calcEndMoney(ci, amount, currency, type);
		result.now = calcNowMoney(ci, amount, currency, type);
		
		
		
		result.end = RateData.get(0).data[currency][type] * amount;// 10.0f;
		result.now = RateData.get(0).data[currency][type + 1] * amount;
		
		return 0;
	}
	
	private float calcEndMoney(Date checkin, float amount, int currency, int type) {
		
		float result = 0.0f;
		
		Date endDate =null;//Calendar.getInstance().getTime();
		if(type == GLOBAL.DBACCESS.SAVING_TYPE_CURRENT) {
			endDate = Calendar.getInstance().getTime();
		}
		else if(type == GLOBAL.DBACCESS.SAVING_TYPE_FIXED_3_MONTH) {
			endDate.setMonth(endDate.getMonth() + 3);
		}
		else {
			return result;
		}
		for(RateData data : RateData) {
			
		}
		
		return result;
	}
	
	private float calcNowMoney(Date checkin, float amount, int currency, int type) {
		
		return 0.0f;
	}
}
