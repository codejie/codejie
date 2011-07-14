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
	
	public static final String TABLE_NAME_BANKBOOK		= "BankBook";
	
	public static final String TABLE_COLUMN_NO			= "No";
	public static final String TABLE_COLUMN_TITLE		= "Title";
	public static final String TABLE_COLUMN_AMOUNT		= "Amount";
	public static final String TABLE_COLUMN_CHECKIN		= "CheckIn";
	public static final String TABLE_COLUMN_TYPE		= "Type";
	public static final String TABLE_COLUMN_RATE		= "Rate";
	public static final String TABLE_COLUMN_BANK		= "Bank";
	public static final String TABLE_COLUMN_NOTE		= "Note";	

	public static final String TABLE_NAME_TEST			= "Test";
	public static final String TABLE_COLUMN_ID			= "_id";
	public static final String TABLE_COLUMN_STRING		= "string";
	public static final String TABLE_COLUMN_INTEGER		= "value";
	
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
		
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BANKBOOK + " ("
		+ TABLE_COLUMN_NO + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_TITLE + " TEXT,"
		+ TABLE_COLUMN_AMOUNT + " REAL,"
		+ TABLE_COLUMN_CHECKIN + " INTEGER,"
		+ TABLE_COLUMN_TYPE + " INTEGER,"
		+ TABLE_COLUMN_RATE + " REAL,"
		+ TABLE_COLUMN_BANK + " INTEGER,"
		+ TABLE_COLUMN_NOTE + " TEXT"
		+ ");";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TEST + " ("
		+ TABLE_COLUMN_ID + " INTEGER PRIMARY KEY,"
		+ TABLE_COLUMN_STRING + " TEXT,"
		+ TABLE_COLUMN_INTEGER + " INTEGER"
		+ ");";
		
		db.execSQL(sql);		
	}
	
	protected void finalize (){
		if(db != null) {
			db.close();
		}
	}
	
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
