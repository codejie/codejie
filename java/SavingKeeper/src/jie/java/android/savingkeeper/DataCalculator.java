/**
 * file:   DataCalculator.java
 * author: codejie (codejie@gmail.com)
 * date:   Aug 23, 2011 12:04:20 AM
 */
package jie.java.android.savingkeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import android.util.Log;

public class DataCalculator {
	
	private static final Date END_DATE = new Date(9999 - 1900, 11, 31);
	
	public static final class CalcResult {
		public float now;
		public float end;
		public float next;
		
		public CalcResult() {
			now = 0.0f;
			end = 0.0f;
			next = 0.0f;
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
/*		
		for(RateData data : _rateData) {
			Log.d(GLOBAL.APP_TAG, "rate record: " + data.begin.toString() + " - " + data.end.toGMTString() + " - " + data.data[0][0]);
		}
		
		for(RateData data : _rateSplitData) {
			Log.d(GLOBAL.APP_TAG, "splitrate record: " + data.begin.toString() + " - " + data.end.toGMTString() + " - " + data.data[0][0]);
		}
*/		
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
		
		cursor.close();
		
		return 0;
	}
	
	private void splitRateData() {
		for(RateData data : _rateData) {
			
			Date cd = new Date(data.begin.getYear(), 5, 30);
			//Log.d(GLOBAL.APP_TAG, "close date(0):" + cd.toString());
			
			if(data.begin.compareTo(cd) <= 0 && data.end.compareTo(cd) <= 0) {
				_rateSplitData.add(data);
				continue;
			}
			
			if(data.begin.compareTo(cd) > 0) {
				cd.setYear(cd.getYear() + 1);
				//Log.d(GLOBAL.APP_TAG, "close date(1):" + cd.toString());
				
				if(data.begin.compareTo(cd) <= 0 && data.end.compareTo(cd) <= 0) {
					_rateSplitData.add(data);
					continue;
				}				
			}
			
			
			RateData d = new RateData();
			d.begin = data.begin;
			d.data = data.data;
			
			//Log.d(GLOBAL.APP_TAG, "Today:" + GLOBAL.TODAY.toString());
			while(GLOBAL.TODAY.compareTo(cd) >= 0) {
				d.end = cd;
				_rateSplitData.add(d);
				d = new RateData();
				d.begin = cd;
				d.data = data.data;

				cd = new Date(cd.getYear() + 1, 5, 30);
				//d.begin = cd;
				//Log.d(GLOBAL.APP_TAG, "close date(2):" + cd.toString());
			}
			
			d.end = data.end;
			_rateSplitData.add(d);
		}
	}
	
	public int calcMoney(final String checkin, float amount, int currency, int type, CalcResult result) {
/*		
		for(RateData data : _rateData) {
			Log.d(GLOBAL.APP_TAG, "begin: " + data.begin.toString() + " end:" + data.end.toString() + " data: " + data.data[0][1]);
		}
*/		
		Date ci = TOOLKIT.String2Date(checkin);
	
		return calcMoney(ci, amount, currency, type, result);
	}

	private long getDays(Date begin, Date end) {
		long d = ((end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24));
		if(d <= 0) {
			return 0;//Log.e(GLOBAL.APP_TAG, "error: " + d + " end:" + end.toString() + " begin:" + begin.toString());
		}
		return d;
	}	
	
	private float getCurrentAmount(Date checkin, float amount, int currency) {
		float result = amount;
		
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

		return result;
	}
	
	private float getFixedRate(Date checkin, int currency, int type) {
		
		for(RateData data : _rateData) {
			if(data.begin.compareTo(checkin) <= 0 && data.end.compareTo(checkin) > 0) {
				return  data.data[currency][type];
			}
		}
		
		return 0.0f;
	}
	
	private float getFixedMonthEstimateFixedAmount(Date checkin, int months, float amount, int currency, int type) {
		float rate = getFixedRate(checkin, currency, type);
		rate = rate / 12;
		return amount * (1 + rate * months);
	}
	
	private float getFixedYearEstimateFixedAmount(Date checkin, int years, float amount, int currency, int type) {
		float rate = getFixedRate(checkin, currency, type);
		return amount * (1 + rate * years);
	}
	
	private int getFixedMonthAmount(Date checkin, float amount, int currency, int type, CalcResult result) {
		int months = 0;
		switch(type) {
		case DBAccess.SAVING_TYPE_FIXED_3_MONTH:
			months = 3;
			break;
		case DBAccess.SAVING_TYPE_FIXED_6_MONTH:
			months = 6;
			break;
		default:
			break;
		}
		
		Date t = new Date(checkin.getYear(), checkin.getMonth(), checkin.getDay());
		t.setMonth(t.getMonth() + months);
		
		if(t.compareTo(GLOBAL.TODAY) > 0) {
			result.now = getCurrentAmount(checkin, amount, currency);
			result.end = getFixedMonthEstimateFixedAmount(checkin, months, amount, currency, type);
			result.next = result.end;
			return 0;
		}
		
		result.end = amount;
		result.now = amount;
		result.next = amount;
		float rate = 0.0f;
		while(t.compareTo(GLOBAL.TODAY) <= 0) {
			rate = getFixedRate(t, currency, type);
			rate = rate / 12;
			result.end = result.end * (1 + rate * months);
			t.setMonth(t.getMonth() + months);
		}
		rate = getFixedRate(t, currency, type);
		result.next = result.end * (1 + rate * months);
		
		t.setMonth(t.getMonth() - months);
		
		result.now = getCurrentAmount(t, result.end, currency);
		
		return 0;
	}
	
	private int getFixedYearAmount(Date checkin, float amount, int currency, int type, CalcResult result) {
		int years = 0;
		switch(type) {
		case DBAccess.SAVING_TYPE_FIXED_1_YEAR:
			years = 1;
			break;
		case DBAccess.SAVING_TYPE_FIXED_2_YEAR:
			years = 2;
			break;
		case DBAccess.SAVING_TYPE_FIXED_3_YEAR:
			years = 3;
			break;
		case DBAccess.SAVING_TYPE_FIXED_5_YEAR:
			years = 5;
			break;
		default:
			break;
		}
		
		Date t = new Date(checkin.getYear(), checkin.getMonth(), checkin.getDay());//checkin;
		t.setYear(t.getYear() + years);
		
		if(t.compareTo(GLOBAL.TODAY) > 0) {
			result.now = getCurrentAmount(checkin, amount, currency);
			result.end = getFixedYearEstimateFixedAmount(checkin, years, amount, currency, type);//amount;
			result.next = result.end;
			return 0;
		}
		
		result.end = amount;
		result.now = amount;
		float rate = 0.0f;
		while(t.compareTo(GLOBAL.TODAY) <= 0) {
			rate = getFixedRate(t, currency, type);
			result.end = result.end * (1 + rate * years);
			
			t.setYear(t.getYear() + years);
		}
		rate = getFixedRate(t, currency, type);
		result.next = result.end * (1 + rate * years);
		
		t.setYear(t.getYear() - years);
		
		result.now = getCurrentAmount(t, result.end, currency);		
		
		return 0;
	}
	
	public int calcMoney(final Date checkin, float amount, int currency, int type, CalcResult result) {		
		//Date endDate =null;//Calendar.getInstance().getTime();
		if(type == DBAccess.SAVING_TYPE_CURRENT) {
			//Log.d(GLOBAL.APP_TAG, "today:" + GLOBAL.TODAY.toString() + " checkin:" + checkin.toString());
			float r = getCurrentAmount(checkin, amount, currency);
			result.end = r;
			result.now = r;
			result.next = r;
			return 0;
		}
		else if(type == DBAccess.SAVING_TYPE_FIXED_3_MONTH || type == DBAccess.SAVING_TYPE_FIXED_6_MONTH) {
			return getFixedMonthAmount(checkin, amount, currency, type, result);
		}
		else if(type == DBAccess.SAVING_TYPE_FIXED_1_YEAR || type == DBAccess.SAVING_TYPE_FIXED_2_YEAR
				|| type == DBAccess.SAVING_TYPE_FIXED_3_YEAR || type == DBAccess.SAVING_TYPE_FIXED_5_YEAR) {
			return getFixedYearAmount(checkin, amount, currency, type, result);
		}
		else {
			return -1;
		}
	}
	
	public float getLatestRate(int currency, int type) {
		if(_rateData.size() == 0)
			return 0.0f;
		
		return _rateData.get(_rateData.size() - 1).data[currency][type];
	}
}
