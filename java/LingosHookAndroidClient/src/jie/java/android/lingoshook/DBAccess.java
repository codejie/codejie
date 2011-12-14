package jie.java.android.lingoshook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public final class DBAccess {

	private static final String TABLE_INFO		=	"Info";
	private static final String TABLE_WORD		=	"Word";
	private static final String TABLE_DATA		=	"Data";
	private static final String TABLE_SCORE		=	"Score";
	
	private static final String COLUMN_ID		=	"_id";
	private static final String COLUMN_VALUE	=	"value";
	private static final String COLUMN_WORD		=	"word";
	private static final String COLUMN_SRCID	=	"srcid";
	private static final String COLUMN_HTML		=	"html";
	private static final String COLUMN_WORDID	=	"wordid";
	private static final String COLUMN_UPDATED	=	"updated";
	private static final String COLUMN_SCORE	=	"score";
	
	private static final int INFOTAG_VERSION	=	1;
	private static final int INFOTAG_CHECKIN	=	2;
	private static final String INFOVALUE_VERSION	=	"0.0.1";
	
	public static final int IMPORTTYPE_OVERWRITE	=	0;
	public static final int IMPORTTYPE_APPEND		=	1;
	
	private static String dbFile = null;
	private static SQLiteDatabase db = null;
	
	public static int init(final String file) {
		
		try {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
			
			Log.i(Global.APP_TITLE, "db initial.");
		}
		catch (SQLiteException e) {
			return -1;
		}
		
		if(initTables() != 0)
			return -1;
		if(initInfoData() != 0)
			return -1;
		
		return 0;
	}
	
	public static void release() {
		if(db != null) {
			db.close();
			Log.i(Global.APP_TITLE, "db release.");
		}
	}
	
	private static int initTables() {
		
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_INFO + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY,"
					+ COLUMN_VALUE + " TEXT"
					+ ")";
			db.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_HTML + " TEXT"
					+ ")";
			db.execSQL(sql);		
			
			sql = "CREATE TABLE IF NOT EXISTS " + TABLE_WORD + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_SRCID + " INTEGER,"
					+ COLUMN_WORD + " TEXT"
					+ ")";
			db.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + " ("
					+ COLUMN_WORDID + " INTEGER PRIMARY KEY,"
					+ COLUMN_UPDATED + " INTEGER,"
					+ COLUMN_SCORE + " INTEGER"
					+ ")";
			db.execSQL(sql);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "create tables failed.");
			return -1;
		}
		return 0;
	}
	
	public static int importData(final String file, int type) {
		if(type == IMPORTTYPE_OVERWRITE) {
			clearData();
		}
		
		try {
			
			SQLiteDatabase src = SQLiteDatabase.openDatabase(file, null, 0);

			
		}
		catch (SQLException e) {
			return -1;
		}
		
		return 0;//transData(file);
	}
	
	private static int initInfoData() {
		
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_INFO, null);
		if(cursor.moveToFirst()) {
			if(cursor.getInt(0) > 0) {
				cursor.close();
				return 0;
			}
		}
		cursor.close();
	
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, INFOTAG_VERSION);
		values.put(COLUMN_VALUE, INFOVALUE_VERSION);
		db.insert(TABLE_INFO, null, values);
		
		return 0;
	}
	
	public static int refreshScore() {
		return -1;
	}
	
	public static int setScore(long wordid) {
		return -1;
	}
	
	
	public static String getHTML(long wordid) {
		return null;
	}
	
	private static void clearTables() {
		String sql = "DROP TABLE " + TABLE_INFO;
		db.execSQL(sql);
		
		sql = "DROP TABLE " + TABLE_DATA;
		db.execSQL(sql);
		
		sql = "DROP TABLE " + TABLE_WORD;
		db.execSQL(sql);
		
		sql = "DROP TABLE " + TABLE_SCORE;
		db.execSQL(sql);
	}
	
	private static void clearData() {
		db.delete(TABLE_DATA, null, null);
		db.delete(TABLE_WORD, null, null);
		db.delete(TABLE_SCORE, null, null);
	}
	
	private static int transData() {
		
		
		return -1;
	}
	
}
