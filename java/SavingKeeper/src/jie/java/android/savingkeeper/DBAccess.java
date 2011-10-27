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
		
		//
		initConfigData();
		initRateData();
		
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
	
	public int insertBank(final String title) {		
		ContentValues values = new ContentValues();
		//values.put(TABLE_COLUMN_NO, no);
		values.put(TABLE_COLUMN_TITLE, title);
		
		if(db.insert(TABLE_NAME_BANK, null, values) == -1) {
			return -1;
		}
		return 0;
	}
	
	public final String getBank(int id) {
		Cursor cursor = db.query(TABLE_NAME_BANK, new String[] { TABLE_COLUMN_TITLE }, TABLE_COLUMN_ID + "=" + id, null, null, null, null);
		if(cursor.getCount() == 0)
			return "Unknown";
		cursor.moveToFirst();
		return cursor.getString(0);
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
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	//
	public Cursor querySaving() {
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE, TABLE_COLUMN_AMOUNT, TABLE_COLUMN_CURRENCY, TABLE_COLUMN_CHECKIN, TABLE_COLUMN_TYPE, TABLE_COLUMN_BANK, TABLE_COLUMN_NOTE }; 
		Cursor  cursor = db.query(TABLE_NAME_SAVING, col, null, null, null, null, null, null);//"", new String[] {}, "", "", "", "");
		cursor.moveToFirst();
		return cursor;		
	}
	
	public Cursor querySaving(int id) {
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_TITLE, TABLE_COLUMN_AMOUNT, TABLE_COLUMN_CURRENCY, TABLE_COLUMN_CHECKIN, TABLE_COLUMN_TYPE, TABLE_COLUMN_BANK, TABLE_COLUMN_NOTE }; 
		Cursor  cursor = db.query(TABLE_NAME_SAVING, col, TABLE_COLUMN_ID + " = " + id, null, null, null, null, null);
		cursor.moveToFirst();
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
		return -1;
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
			Log.w(GLOBAL.APP_TAG, "insert rate data failed.");
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
		Log.d(GLOBAL.APP_TAG, "start:" + start + "end:" + end);
		return (db.delete(TABLE_NAME_RATE, TABLE_COLUMN_START + "='" + start + "' and " + TABLE_COLUMN_END + "='" + end + "'", null) > 0 ? 0 : -1);
	}
	
	public float getRate(int checkin, int currency, int type) {
		return 0.0f;
	}
	
	private int initRateData() {
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME_RATE, null);
		cursor.moveToFirst();
		int c = cursor.getInt(0);
		
		Log.d(GLOBAL.APP_TAG, "rate row count:" + c);
		
		if(cursor.getInt(0) > 0) {
			return -1;
		}
		
		//http://www.abchina.com/cn/PublicPlate/Quotation/bwbll/201012/t20101213_45404.htm
		//http://www.icbc.com.cn/icbc/é‡‘èž�ä¿¡æ�¯/å­˜è´·æ¬¾åˆ©çŽ‡è¡¨/äººæ°‘å¸�å­˜æ¬¾åˆ©çŽ‡è¡¨/
		
		return 0;
	}
	
	private int initConfigData() {
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME_CONFIG, null);
		cursor.moveToFirst();
		
		if(cursor.getInt(0) > 0) {
			return -1;
		}
		
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_ID, CONFIG_ID_VERSION);
		values.put(TABLE_COLUMN_VALUE, "0.11.10.25");
		
		db.insert(TABLE_NAME_CONFIG, null, values);
		
		values.clear();
		values.put(TABLE_COLUMN_ID, CONFIG_ID_PASSWD);
		values.put(TABLE_COLUMN_VALUE, "");
		
		db.insert(TABLE_NAME_CONFIG, null, values);
		
		return 0;
	}
	
	public String getConfigValue(int id) {
		Cursor cursor = db.query(TABLE_NAME_CONFIG, new String[] { TABLE_COLUMN_VALUE }, TABLE_COLUMN_ID + "=" + id, null, null, null, null);
		if(cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return new String("");
	}
	
	public int updateConfigValue(int id, final String value) {
		ContentValues values = new ContentValues();
		values.put(TABLE_COLUMN_ID, id);
		values.put(TABLE_COLUMN_VALUE, value);
		if(db.update(TABLE_NAME_CONFIG, values, TABLE_COLUMN_ID + " = " + id, null) == -1)
			return -1;
		return 0;
	}
}
