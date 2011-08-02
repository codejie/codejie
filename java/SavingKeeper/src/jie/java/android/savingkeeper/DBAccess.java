/**
 * file   : DBAccess.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 2:48:07 PM
 */
package jie.java.android.savingkeeper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBAccess {
	
	private static final int DATABASE_VERSION			= 1;
	
	public static final String TABLE_NAME_BANK			= "Bank";
	public static final String TABLE_NAME_RATE			= "Rate";
	public static final String TABLE_NAME_SAVING		= "Saving";
	
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

	public static final String TABLE_NAME_TEST			= "Test";

	public static final String TABLE_COLUMN_STRING		= "string";
	public static final String TABLE_COLUMN_INTEGER		= "value";
	
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
	
	public static class TestData {
		public String str;
		public int value;
	}

	private SQLiteDatabase db = null;
	
	public DBAccess(final String dbfile) {
		onCreate(dbfile);
	}

	private void onCreate(String dbfile) {
		try {
			db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		}
		catch(SQLiteException e) {
			Log.w(GLOBAL.APP_TAG, e.toString());
		}
		db.setVersion(DATABASE_VERSION);
		
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SAVING + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_TITLE + " TEXT,"
		+ TABLE_COLUMN_AMOUNT + " REAL,"
		+ TABLE_COLUMN_CURRENCY + " INTEGER,"
		+ TABLE_COLUMN_CHECKIN + " TEXT,"
		+ TABLE_COLUMN_TYPE + " INTEGER,"
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
		+ TABLE_COLUMN_TYPE + " INTEGER, "
		+ TABLE_COLUMN_START + " INTEGER,"
		+ TABLE_COLUMN_END + " INTEGER,"
		+ TABLE_COLUMN_RATE + " REAL"
		+ ");";
		
		db.execSQL(sql);
		
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TEST + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_STRING + " TEXT,"
		+ TABLE_COLUMN_INTEGER + " INTEGER"
		+ ");";
		
		db.execSQL(sql);		
	}
	
	public void close (){
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
	
	public final String getBank(long id) {
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
	
	public int removeBank(long id) {
		if(checkBankUsed(id) > 0)
			return -1;
		return db.delete(TABLE_NAME_BANK, TABLE_COLUMN_ID + " = " + id, null);
	}
	
	public int checkBankUsed(long id) {
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_SAVING + " WHERE " + TABLE_NAME_BANK + " = " + id, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
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

	//
	public float getRate(int checkin, int type) {
		return 0.0f;
	}
	
	//
	public int insert(final TestData data) {
		
		ContentValues values = new ContentValues();
		
		values.put(TABLE_COLUMN_STRING, data.str);
		values.put(TABLE_COLUMN_INTEGER, data.value);
		
		if(db.insert(TABLE_NAME_TEST, null, values) == -1) {
			Log.w(GLOBAL.APP_TAG, "insert failed.");
			return -1;
		}
		
		return 0;
	}
	
	public Cursor query(){
		String col[] = new String[] { TABLE_COLUMN_ID, TABLE_COLUMN_STRING, TABLE_COLUMN_INTEGER }; 
		Cursor  cursor = db.query(TABLE_NAME_TEST, col, null, null, null, null, null, null);//"", new String[] {}, "", "", "", "");
		cursor.moveToFirst();
		return cursor;
	}
	
	
}
