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
import java.util.GregorianCalendar;

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
	
	private ArrayList<RateData> _rateData = new ArrayList<RateData>();
	private ArrayList<RateData> _rateSplitData = new ArrayList<RateData>();
	
	public int init() {
		return loadRateData();
	}
	
	public void release() {
		_rateData.clear();
	}
	
	public int reloadData() {
		release();
		return loadRateData();
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
				_rateData.add(data);
				
				data = new RateData();			
				count = 0;
			}			
		}
		
		return 0;
	}
	
	private void splitRateData() {
		Date closedate = new Date(0, 5, 30);
		for(RateData data : _rateData) {
			closedate.setYear(data.begin.getYear());
			if(data.begin.compareTo(closedate) <= 0) {
				if(data.end.compareTo(closedate) )
			}
			else {
				
			}
		}
	}
	
	
	
	public int calcMoney(final String checkin, float amount, int currency, int type, CalcResult result) {
		
		for(RateData data : _rateData) {
			Log.d(GLOBAL.APP_TAG, "begin: " + data.begin.toString() + " end:" + data.end.toString() + " data: " + data.data[0][1]);
		}
		
		Date ci = TOOLKIT.String2Date(checkin);
/*		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
		Date ci = null;
		try {
			ci = fmt.parse(checkin);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
*/		
		result.end = calcEndMoney(ci, amount, currency, type);
		result.now = calcNowMoney(ci, amount, currency, type);
		
		//result.end = RateData.get(0).data[currency][type] * amount;// 10.0f;
		//result.now = RateData.get(0).data[currency][type + 1] * amount;
		
		return 0;
	}
	
	private float calcEndMoney(Date checkin, float amount, int currency, int type) {
		
		float result = 0.0f;
		
		//Date endDate =null;//Calendar.getInstance().getTime();
		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			
			Log.d(GLOBAL.APP_TAG, "today:" + GLOBAL.TODAY.toString() + " checkin:" + checkin.toString());
			
			long days = (GLOBAL.TODAY.getTime() - checkin.getTime()) / (1000 * 60 * 60 * 24);			
			float rate = getRate(checkin, currency, type);
			
			result = amount * days * (rate / 360.f);
			
			//int delta = _current.compareTo(checkin);
		}
		else if(type == DBAccess.SAVING_TYPE_FIXED_3_MONTH) {
			//endDate.setMonth(endDate.getMonth() + 3);
		}
		else {
			return result;
		}
		for(RateData data : _rateData) {
			
		}
		
		return result;
	}
	
	private float calcNowMoney(Date checkin, float amount, int currency, int type) {
		float result = amount;

		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			
			Log.d(GLOBAL.APP_TAG, "today:" + GLOBAL.TODAY.toString() + " checkin:" + checkin.toString());
/*			
			for(RateData data : _rateData) {
				if(data.begin.compareTo(checkin) <= 0) {
					if(data.end.compareTo(checkin) <= 0) {
						result = calc(data.begin, data.end, result, data.data); 
					}
					else {
						result = calc(data.begin, checkin, result, data.data);
					}
				}
				else {
					
				}
			}
*/			
			
			long days = (GLOBAL.TODAY.getTime() - checkin.getTime()) / (1000 * 60 * 60 * 24);
			
			float rate = getRate(checkin, currency, type);
			
			result = amount * days * (rate / 360.f);
			
			//int delta = _current.compareTo(checkin);
		}		
		
		return result;
	
	}
	
	public float getRate(Date checkin, int currency, int type) {
		if(_rateData.size() == 0)
			return 0.0f;
		
		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			return _rateData.get(_rateData.size() - 1).data[currency][0];
		}
		return 0.0f;
	}
	
	public float getLatestRate(int currency, int type) {
		if(_rateData.size() == 0)
			return 0.0f;
		
		return _rateData.get(_rateData.size() - 1).data[currency][type];
	}
	
	///
	private float calc(final Date begin, final Date end, final float amount, final float rate) {
		float result = amount;
		
		Date closedate = new Date();
		closedate.setMonth(Calendar.JUNE);
		closedate.setDate(30);
		
		while(begin.compareTo(end) < 0) {
			closedate.setYear(begin.getYear());
			Log.d(GLOBAL.APP_TAG, "closedate:" + closedate.toString());
			
			
			
		}
		
		return amount;
	}
}
