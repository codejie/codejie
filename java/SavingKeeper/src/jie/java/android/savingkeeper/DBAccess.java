/**
 * file   : DBAccess.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 2:48:07 PM
 */
package jie.java.android.savingkeeper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBAccess {
	
	private static final int DATABASE_VERSION			= 1;
	
	public static final String TABLE_NAME_BANK			= "Bank";
	public static final String TABLE_NAME_RATE			= "Rate";
	public static final String TABLE_NAME_SAVING		= "Saving";
	private static final String TABLE_NAME_CONFIG		= "Config";
	
	public static final String TABLE_COLUMN_ID			= "_id";
	public static final String TABLE_COLUMN_TITLE		= "Title";
	public static final String TABLE_COLUMN_AMOUNT		= "Amount";
	public static final String TABLE_COLUMN_CURRENCY	= "Currency";
	public static final String TABLE_COLUMN_CHECKIN		= "CheckIn";
	public static final String TABLE_COLUMN_TYPE		= "Type";
	public static final String TABLE_COLUMN_RATE		= "Rate";
	public static final String TABLE_COLUMN_BANK		= "Bank";
	public static final String TABLE_COLUMN_NOTE		= "Note";	
	public static final String TABLE_COLUMN_START		= "Start";
	public static final String TABLE_COLUMN_END			= "End";
	public static final String TABLE_COLUMN_RATE_RMB	= "Rate_RMB";
	public static final String TABLE_COLUMN_RATE_US		= "Rate_US";
	public static final String TABLE_COLUMN_RATE_EU		= "Rate_EU";
	public static final String TABLE_COLUMN_RATE_0		= "Rate_0";
	public static final String TABLE_COLUMN_RATE_1		= "Rate_1";
	public static final String TABLE_COLUMN_RATE_2		= "Rate_2";
	public static final String TABLE_COLUMN_RATE_3		= "Rate_3";
	public static final String TABLE_COLUMN_RATE_4		= "Rate_4";
	public static final String TABLE_COLUMN_RATE_5		= "Rate_5";
	public static final String TABLE_COLUMN_RATE_6		= "Rate_6";
	public static final String TABLE_COLUMN_VALUE		= "Value";
	
	public static final int SAVING_TYPE_CURRENT			= 0;
	public static final int SAVING_TYPE_FIXED_3_MONTH	= 1;
	public static final int SAVING_TYPE_FIXED_6_MONTH	= 2;
	public static final int SAVING_TYPE_FIXED_1_YEAR	= 3;
	public static final int SAVING_TYPE_FIXED_2_YEAR	= 4;
	public static final int SAVING_TYPE_FIXED_3_YEAR	= 5;
	public static final int SAVING_TYPE_FIXED_5_YEAR	= 6;
	
	public static final int CURRENCY_TYPE_RMB			= 0;
	public static final int CURRENCY_TYPE_US			= 1;
	public static final int CURRENCY_TYPE_EU			= 2;
	
	public static int CONFIG_ID_VERSION				= 1;
	public static int CONFIG_ID_PASSWD					= 2;
	
	private SQLiteDatabase db = null;
	
	public DBAccess() {
		//onCreate(dbfile);
	}

	public int init(final String dbfile) {
		try {
			db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		}
		catch(SQLiteException e) {
			Log.w(GLOBAL.APP_TAG, e.toString());
			
			return -1;
		}
		db.setVersion(DATABASE_VERSION);
		
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SAVING + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_TITLE + " TEXT,"
		+ TABLE_COLUMN_AMOUNT + " REAL,"
		+ TABLE_COLUMN_CURRENCY + " INTEGER,"
		+ TABLE_COLUMN_CHECKIN + " INTEGER,"
		+ TABLE_COLUMN_TYPE + " TEXT,"
		+ TABLE_COLUMN_BANK + " INTEGER,"
		+ TABLE_COLUMN_NOTE + " TEXT"
		+ ");";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BANK + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_TITLE + " TEXT"
		+ ");";
		
		db.execSQL(sql);

		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_RATE + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_START + " TEXT,"
		+ TABLE_COLUMN_END + " TEXT,"
		+ TABLE_COLUMN_CURRENCY + " INTEGER, "
		+ TABLE_COLUMN_RATE_0 + " REAL,"
		+ TABLE_COLUMN_RATE_1 + " REAL,"
		+ TABLE_COLUMN_RATE_2 + " REAL,"
		+ TABLE_COLUMN_RATE_3 + " REAL,"
		+ TABLE_COLUMN_RATE_4 + " REAL,"
		+ TABLE_COLUMN_RATE_5 + " REAL,"
		+ TABLE_COLUMN_RATE_6 + " REAL"
		+ ");";		
		db.execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CONFIG + "("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_VALUE + " TEXT"
		+ ");";
		db.execSQL(sql);
		
		return 0;
	}
	
	public void release() {
		this.close();
	}
	
	protected void close (){
		if(db != null) {
			db.close();
		}
	}
	
	public int initData(Activity act) {
		initConfigData();
		initRateData();
		initBankData(act);
		
		return 0;
	}
	
	public int insertBank(final String title) {		
		ContentValues values = new ContentValues();
		//values.put(TABLE_COLUMN_NO, no);
		values.put(TABLE_COLUMN_TITLE, title);
		
		if(db.insert(TABLE_NAME_BANK, null, values) == -1) {
			return -1;
		}
		return 0;
	}
	
	public String getBank(int id) {
		Cursor cursor = db.query(TABLE_NAME_BANK, new String[] { TABLE_COLUMN_TITLE }, TABLE_COLUMN_ID + "=" + id, null, null, null, null);
		String ret = "Unknown";
		if(cursor.moveToFirst())
			ret = cursor.getString(0);
		cursor.close();
		return ret;
	}
	
	public Cursor queryBank() {
		Cursor cursor = db.query(TABLE_NAME_BANK, new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE }, null, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public int removeBank(int id) {
		if(checkBankUsed(id) > 0)
			return -1;
		return db.delete(TABLE_NAME_BANK, TABLE_COLUMN_ID + " = " + id, null);
	}
	
	public int checkBankUsed(int id) {
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_SAVING + " WHERE " + TABLE_NAME_BANK + " = " + id, null);
		int ret = -1;
		if(cursor.moveToFirst())
			ret = cursor.getInt(0);
		cursor.close();
		return ret;
	}
	
	private int initBankData(Activity act) {
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME_BANK, null);
		if(cursor.moveToFirst()) {
			if(cursor.getInt(0) > 0) {
				cursor.close();
				return -1;
			}
		}
		cursor.close();		
		
		String bank[] = act.getResources().getStringArray(R.array.bank);
		for(String str : bank) {
			insertBank(str);
		}

		return 0;
	}

	//
	public Cursor querySaving() {
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE, TABLE_COLUMN_AMOUNT, TABLE_COLUMN_CURRENCY, TABLE_COLUMN_CHECKIN, TABLE_COLUMN_TYPE, TABLE_COLUMN_BANK, TABLE_COLUMN_NOTE }; 
		Cursor  cursor = db.query(TABLE_NAME_SAVING, col, null, null, null, null, null, null);//"", new String[] {}, "", "", "", "");
		//cursor.moveToFirst();
		return cursor;		
	}
	
	public Cursor querySaving(int id) {
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE, TABLE_COLUMN_AMOUNT, TABLE_COLUMN_CURRENCY, TABLE_COLUMN_CHECKIN, TABLE_COLUMN_TYPE, TABLE_COLUMN_BANK, TABLE_COLUMN_NOTE }; 
		Cursor  cursor = db.query(TABLE_NAME_SAVING, col, TABLE_COLUMN_ID + " = " + id, null, null, null, null, null);
		//cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor querySaving(final String title) {
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE, TABLE_COLUMN_AMOUNT, TABLE_COLUMN_CURRENCY, TABLE_COLUMN_CHECKIN, TABLE_COLUMN_TYPE, TABLE_COLUMN_BANK, TABLE_COLUMN_NOTE }; 
		Cursor  cursor = db.query(TABLE_NAME_SAVING, col, TABLE_COLUMN_TITLE + " = '" + title + "'", null, null, null, null, null);
		//cursor.moveToFirst();
		return cursor;
	}
	
	public int insertSaving(final String title, float amount, int currency, final String checkin, int type, int bank, final String note) {
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_TITLE, title);
		values.put(TABLE_COLUMN_AMOUNT, amount);
		values.put(TABLE_COLUMN_CURRENCY, currency);
		values.put(TABLE_COLUMN_CHECKIN, checkin);
		values.put(TABLE_COLUMN_TYPE, type);
		values.put(TABLE_COLUMN_BANK, bank);
		values.put(TABLE_COLUMN_NOTE, note);
		
		if(db.insert(TABLE_NAME_SAVING, null, values) == -1)
			return -1;
		return 0;
	}
	
	public int updateSaving(int id, final String title, float amount, int currency, final String checkin, int type, int bank, final String note) {
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_TITLE, title);
		values.put(TABLE_COLUMN_AMOUNT, amount);
		values.put(TABLE_COLUMN_CURRENCY, currency);
		values.put(TABLE_COLUMN_CHECKIN, checkin);
		values.put(TABLE_COLUMN_TYPE, type);
		values.put(TABLE_COLUMN_BANK, bank);
		values.put(TABLE_COLUMN_NOTE, note);	
		
		if(db.update(TABLE_NAME_SAVING, values, TABLE_COLUMN_ID + " = " + id, null) == -1)
			return -1;
		
		return 0;
	}
	
	public int removeSaving(int id) {
		db.delete(TABLE_NAME_SAVING, TABLE_COLUMN_ID + "=" + id, null);
		return 0;
	}

	//
	public int insertRate(final String start, final String end, int currency, float rate0, float rate1, float rate2, float rate3, float rate4, float rate5, float rate6) {		
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_START, start);
		values.put(TABLE_COLUMN_END, end);
		values.put(TABLE_COLUMN_CURRENCY, currency);
		values.put(TABLE_COLUMN_RATE_0, rate0);
		values.put(TABLE_COLUMN_RATE_1, rate1);
		values.put(TABLE_COLUMN_RATE_2, rate2);
		values.put(TABLE_COLUMN_RATE_3, rate3);
		values.put(TABLE_COLUMN_RATE_4, rate4);
		values.put(TABLE_COLUMN_RATE_5, rate5);
		values.put(TABLE_COLUMN_RATE_6, rate6);
		
		if(db.insert(TABLE_NAME_RATE, null, values) == -1) {
			//Log.w(GLOBAL.APP_TAG, "insert rate data failed.");
			return -1;
		}
		
		return 0;
	}
	
	public Cursor queryRate() {
						
		String[] col = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_START, TABLE_COLUMN_END, TABLE_COLUMN_CURRENCY,
						TABLE_COLUMN_RATE_0, TABLE_COLUMN_RATE_1, TABLE_COLUMN_RATE_2, TABLE_COLUMN_RATE_3, TABLE_COLUMN_RATE_4, TABLE_COLUMN_RATE_5, TABLE_COLUMN_RATE_6 };
		Cursor cursor = db.query(TABLE_NAME_RATE, col, null, null, null, null, TABLE_COLUMN_START);
		//cursor.moveToFirst();
		return cursor;
	}
	
	public int removeRate(final String start, final String end) {
		//Log.d(GLOBAL.APP_TAG, "start:" + start + "end:" + end);
		return (db.delete(TABLE_NAME_RATE, TABLE_COLUMN_START + "='" + start + "' and " + TABLE_COLUMN_END + "='" + end + "'", null) > 0 ? 0 : -1);
	}
	
	private int initRateData() {
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME_RATE, null);
		if(cursor.moveToFirst()) {
			if(cursor.getInt(0) > 0) {
				cursor.close();
				return -1;
			}
		}
		cursor.close();
		
		//http://hi.baidu.com/shuangxiuliangzi/blog/item/c3d826faa8c80e8e9e51468f.html
		//http://www.boc.cn/finadata/lilv/fd33/
		insertRate("2006.08.19", "2007.03.17", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0180f, 0.0225f, 0.0252f, 0.0306f, 0.0369f, 0.0414f);
		insertRate("2006.08.19", "2007.03.17", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2006.08.19", "2007.03.17", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		insertRate("2007.03.18", "2007.05.18", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0198f, 0.0243f, 0.0279f, 0.0333f, 0.0396f, 0.0441f);
		insertRate("2007.03.18", "2007.05.18", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.03.18", "2007.05.18", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		insertRate("2007.05.19", "2007.07.20", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0207f, 0.0261f, 0.0306f, 0.0369f, 0.0441f, 0.0495f);
		insertRate("2007.05.19", "2007.07.20", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.05.19", "2007.07.20", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2007.07.21", "2007.08.21", DBAccess.CURRENCY_TYPE_RMB, 0.0081f, 0.0234f, 0.0288f, 0.0333f, 0.0396f, 0.0468f, 0.0522f);
		insertRate("2007.07.21", "2007.08.21", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.07.21", "2007.08.21", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2007.08.22", "2007.09.14", DBAccess.CURRENCY_TYPE_RMB, 0.0081f, 0.0261f, 0.0315f, 0.0360f, 0.0423f, 0.0495f, 0.0549f);
		insertRate("2007.08.22", "2007.09.14", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.08.22", "2007.09.14", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2007.09.15", "2007.12.20", DBAccess.CURRENCY_TYPE_RMB, 0.0081f, 0.0288f, 0.0342f, 0.0387f, 0.0450f, 0.0522f, 0.0576f);
		insertRate("2007.09.15", "2007.12.20", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.09.15", "2007.12.20", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);		
		
		insertRate("2007.12.21", "2008.10.08", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0333f, 0.0378f, 0.0414f, 0.0468f, 0.0540f, 0.0585f);
		insertRate("2007.12.21", "2008.10.08", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2007.12.21", "2008.10.08", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);	
	
		insertRate("2008.10.09", "2008.10.29", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0315f, 0.0351f, 0.0387f, 0.0441f, 0.0513f, 0.0558f);
		insertRate("2008.10.09", "2008.10.29", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2008.10.09", "2008.10.29", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2008.10.30", "2008.11.26", DBAccess.CURRENCY_TYPE_RMB, 0.0072f, 0.0288f, 0.0324f, 0.0360f, 0.0414f, 0.0477f, 0.0513f);
		insertRate("2008.10.30", "2008.11.26", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2008.10.30", "2008.11.26", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		insertRate("2008.11.27", "2008.12.22", DBAccess.CURRENCY_TYPE_RMB, 0.0036f, 0.0198f, 0.0225f, 0.0252f, 0.0306f, 0.0360f, 0.0387f);
		insertRate("2008.11.27", "2008.12.22", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2008.11.27", "2008.12.22", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2008.12.23", "2010.10.19", DBAccess.CURRENCY_TYPE_RMB, 0.0036f, 0.0171f, 0.0198f, 0.0225f, 0.0279f, 0.0333f, 0.0360f);
		insertRate("2008.12.23", "2010.10.19", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2008.12.23", "2010.10.19", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2010.10.20", "2010.12.25", DBAccess.CURRENCY_TYPE_RMB, 0.0036f, 0.0191f, 0.0220f, 0.0250f, 0.0325f, 0.0385f, 0.0420f);
		insertRate("2010.10.20", "2010.12.25", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2010.10.20", "2010.12.25", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2010.12.26", "2011.02.08", DBAccess.CURRENCY_TYPE_RMB, 0.0036f, 0.0225f, 0.0250f, 0.0275f, 0.0355f, 0.0415f, 0.0455f);
		insertRate("2010.12.26", "2011.02.08", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2010.12.26", "2011.02.08", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2011.02.09", "2011.04.05", DBAccess.CURRENCY_TYPE_RMB, 0.0040f, 0.0260f, 0.0280f, 0.0300f, 0.0390f, 0.0450f, 0.0500f);
		insertRate("2011.02.09", "2011.04.05", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2011.02.09", "2011.04.05", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2011.04.06", "2011.07.06", DBAccess.CURRENCY_TYPE_RMB, 0.0050f, 0.0285f, 0.0305f, 0.0325f, 0.0415f, 0.0475f, 0.0525f);
		insertRate("2011.04.06", "2011.07.06", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2011.04.06", "2011.07.06", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

		insertRate("2011.07.07", "2999.12.31", DBAccess.CURRENCY_TYPE_RMB, 0.0050f, 0.0310f, 0.0330f, 0.0350f, 0.0440f, 0.0500f, 0.0550f);
		insertRate("2011.07.07", "2999.12.31", DBAccess.CURRENCY_TYPE_US, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		insertRate("2011.07.07", "2999.12.31", DBAccess.CURRENCY_TYPE_EU, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		return 0;
	}
	
	private int initConfigData() {
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME_CONFIG, null);
		if(cursor.moveToFirst()) {
			
			if(cursor.getInt(0) > 0) {
				cursor.close();
				return -1;
			}
			cursor.close();
			
			ContentValues values = new ContentValues();
			values.put(TABLE_COLUMN_ID, CONFIG_ID_VERSION);
			values.put(TABLE_COLUMN_VALUE, "0.11.10.25");
			
			db.insert(TABLE_NAME_CONFIG, null, values);
			
			values.clear();
			values.put(TABLE_COLUMN_ID, CONFIG_ID_PASSWD);
			values.put(TABLE_COLUMN_VALUE, "");
			
			db.insert(TABLE_NAME_CONFIG, null, values);
		}

		return 0;
	}
	
	public String getConfigValue(int id) {
		Cursor cursor = db.query(TABLE_NAME_CONFIG, new String[] { TABLE_COLUMN_VALUE }, TABLE_COLUMN_ID + "=" + id, null, null, null, null);
		String ret = "";
		if(cursor.moveToFirst()) {
			ret = cursor.getString(0);
		}
		cursor.close();
		return ret;
	}
	
	public int updateConfigValue(int id, final String value) {
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_ID, id);
		values.put(TABLE_COLUMN_VALUE, value);
		if(db.update(TABLE_NAME_CONFIG, values, TABLE_COLUMN_ID + " = " + id, null) == -1)
			return -1;
		return 0;
	}
	
	public void resetBankData(Activity act) {
		String sql = "DELETE FROM " + TABLE_NAME_BANK;		
		db.execSQL(sql);
		
		initBankData(act);
	}
	
	public void resetRateData() {
		String sql = "DELETE FROM " + TABLE_NAME_RATE;
		db.execSQL(sql);
		
		initRateData();
	}
	
}
