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
		if(loadRateData() != 0)
			return -1;
		
		splitRateData();
		
		for(RateData data : _rateData) {
			Log.d(GLOBAL.APP_TAG, "rate record: " + data.begin.toString() + " - " + data.end.toGMTString() + " - " + data.data[0][0]);
		}
		
		for(RateData data : _rateSplitData) {
			Log.d(GLOBAL.APP_TAG, "splitrate record: " + data.begin.toString() + " - " + data.end.toGMTString() + " - " + data.data[0][0]);
		}
		
		return 0;
	}
	
	public void release() {
		_rateData.clear();
		_rateSplitData.clear();
	}
	
	public int reloadData() {
		release();
		return init();
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
		for(RateData data : _rateData) {
			
			Date cd = new Date(data.begin.getYear(), 5, 30);
			Log.d(GLOBAL.APP_TAG, "close date(0):" + cd.toGMTString());
			
			if(data.begin.compareTo(cd) <= 0 && data.end.compareTo(cd) <= 0) {
				_rateSplitData.add(data);
				continue;
			}
			
			if(data.begin.compareTo(cd) > 0) {
				cd.setYear(cd.getYear() + 1);
				Log.d(GLOBAL.APP_TAG, "close date(1):" + cd.toGMTString());
				
				if(data.begin.compareTo(cd) <= 0 && data.end.compareTo(cd) <= 0) {
					_rateSplitData.add(data);
					continue;
				}				
			}
			
			
			RateData d = new RateData();
			d.begin = data.begin;
			d.data = data.data;
			
			while(data.end.compareTo(cd) > 0 && data.end.compareTo(GLOBAL.TODAY) < 0) {
				d.end = cd;
				_rateSplitData.add(d);
				
				d.begin = cd;
				d.begin.setDate(d.begin.getDate() + 1);
				cd.setYear(cd.getYear() + 1);
				Log.d(GLOBAL.APP_TAG, "close date(2):" + cd.toGMTString());
			}
			
			d.end = data.end;
			_rateSplitData.add(d);
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
	
	private long getDays(Date begin, Date end) {
		return ((end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24)); 
	}
	
	private float calcNowMoney(Date checkin, float amount, int currency, int type) {
		float result = amount;

		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			
			Log.d(GLOBAL.APP_TAG, "today:" + GLOBAL.TODAY.toString() + " checkin:" + checkin.toString());
			
			for(RateData data : _rateSplitData) {
				if(data.end.compareTo(checkin) > 0) {
					float r = (data.data[currency][0] / 360.0f);
					if(data.end.compareTo(GLOBAL.TODAY) > 0) {
						if(data.begin.compareTo(checkin) >= 0) {
							result *= (1 + r * getDays(data.begin, GLOBAL.TODAY));
						}
						else {
							result *= (1 + r * getDays(checkin, GLOBAL.TODAY));
						}
						break;
					}
					else {
						if(data.begin.compareTo(checkin) >= 0) {
							result *= (1 + r * getDays(data.begin, data.end));
						}
						else {
							result *= (1 + r * getDays(checkin, data.end));
						}
					}
				}
			}
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
