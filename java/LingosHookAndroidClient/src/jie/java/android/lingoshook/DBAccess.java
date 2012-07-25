package jie.java.android.lingoshook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class DBAccess {

	private static final String TABLE_INFO		=	"Info";
	private static final String TABLE_WORD		=	"Word";
	private static final String TABLE_DATA		=	"Data";
	private static final String TABLE_SCORE		=	"Score";
	
	private static final String COLUMN_ID		=	"id";
	private static final String COLUMN_VALUE	=	"value";
	private static final String COLUMN_WORD		=	"word";
	private static final String COLUMN_SRCID	=	"srcid";
	private static final String COLUMN_HTML		=	"html";
	private static final String COLUMN_WORDID	=	"wordid";
	private static final String COLUMN_LAST		=	"last";//latest update
	private static final String COLUMN_NEXT		=	"next";//next update
	private static final String COLUMN_SCORE	=	"score";
	
	private static final int INFOTAG_VERSION	=	1;
	private static final int INFOTAG_CHECKIN	=	2;
	private static final String INFOVALUE_VERSION	=	"0.0.1";
	
	public static final int IMPORTTYPE_OVERWRITE	=	0;
	public static final int IMPORTTYPE_APPEND		=	1;
	
	public static Date CHECKIN		= null;
	
	private static SQLiteDatabase db = null;
	
	public static int init(final String file) {
		
		try {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
			
			Log.i(Global.APP_TITLE, "db initial - " + db.getMaximumSize());
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
					+ COLUMN_LAST + " INTEGER,"
					+ COLUMN_NEXT + " INTEGER,"
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
	
	public static int importData(Handler handler, int msgcode, final String file, int type) {
		if(type == IMPORTTYPE_OVERWRITE) {
			if(clearData() != 0)
				return -1;
		}
		
		try {
			long srcid = -1, wordid = -1;
			long p = -1, n = -1;
	
			ContentValues values = new ContentValues();
			
			SQLiteDatabase src = SQLiteDatabase.openDatabase(file, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			String sql = "SELECT word.srcid, word.word, src.html FROM word, src WHERE word.srcid = src.srcid ORDER BY word.srcid";
			
			int offset = 0;
			while(true) {
				String s = sql + " LIMIT 10 OFFSET " + offset;
				
				Cursor cursor = src.rawQuery(s, null);
				if(cursor.getCount() == 0) {
					cursor.close();
					break;
				}
				while(cursor.moveToNext()) {
					n = cursor.getInt(0);
					if(p != n) {
						p = n;
						values.clear();
						values.put(COLUMN_HTML, cursor.getString(2));
						srcid = db.insert(TABLE_DATA, null, values);
					}
					values.clear();
					values.put(COLUMN_SRCID, srcid);
					values.put(COLUMN_WORD, cursor.getString(1));
					wordid = db.insert(TABLE_WORD, null, values);
					
					values.clear();
					values.put(COLUMN_WORDID, wordid);
					values.put(COLUMN_UPDATED, 0);//
					values.put(COLUMN_SCORE, Score.SCORE_UNKNOWN);
					db.insert(TABLE_SCORE, null, values);
					
					handler.sendMessage(Message.obtain(handler, msgcode, cursor.getString(1)));
				}
				cursor.close();
				
				offset += 10;
				
				Log.d(Global.APP_TITLE, "offset:" + offset);
			}		
			
			src.close();
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return -1;
		}
		
		if(type == IMPORTTYPE_OVERWRITE || (checkInfoTable(INFOTAG_CHECKIN) == false)) {
			try {
				Date today = Calendar.getInstance().getTime();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, INFOTAG_CHECKIN);
				values.put(COLUMN_VALUE, fmt.format(today));
				db.insert(TABLE_INFO, null, values);
			}
			catch (SQLException e) {
				Log.e(Global.APP_TITLE, "db exception - " + e.toString());
				return -1;
			}
		}		
		return 0;
	}
	
	private static boolean checkInfoTable(int tag) {
		try {
			Cursor cursor = db.query(TABLE_INFO, new String[] { COLUMN_ID }, COLUMN_ID + "=" + tag, null, null, null, null);
			if(cursor == null)
				return false;
			if(cursor.getCount() == 0) {
				cursor.close();
				return false;
			}
			cursor.close();
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db - exception - " + e.toString());
		}
		return true;
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
	
	public static long getDeltaUpdate() {
		try {
			Cursor cursor = db.query(TABLE_INFO, new String[] { COLUMN_VALUE }, COLUMN_ID + "=" + INFOTAG_CHECKIN, null, null, null, null);
			if(cursor == null)
				return 0;
			if(cursor.getCount() == 0) {
				cursor.close();
				return 0;
			}
			cursor.moveToFirst();
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			//String s = cursor.getString(0);
			//Date checkin = fmt.parse(cursor.getString(0));
			CHECKIN = fmt.parse(cursor.getString(0));
			Date today = Calendar.getInstance().getTime();
			cursor.close();
			
			return (((today.getTime() - CHECKIN.getTime()) / (1000 * 60 * 60 * 24)) + 1);
		}
		catch (SQLiteException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(Global.APP_TITLE, "parser exception - " + e.toString());
			//e.printStackTrace();
			return 0;
		}
	}
	
	public static String getHTML(long srcid) {
		try {
			Cursor cursor = db.query(TABLE_DATA, new String[] { COLUMN_HTML }, COLUMN_ID + "=" + srcid, null, null, null, null);
			if(cursor.getCount() == 0)
				return null;
			cursor.moveToFirst();
			String ret = cursor.getString(0);
			cursor.close();
			return ret;
		}
		catch (SQLiteException e) {
			return null;
		}
	}
	
	public static String getHTMLbyWord(final String word) {
		try {
			String sql = "SELECT " + TABLE_DATA + "." + COLUMN_HTML + " FROM " + TABLE_DATA + "," + TABLE_WORD + " WHERE " + TABLE_DATA + "." + COLUMN_ID + "=" + TABLE_WORD + "." + COLUMN_SRCID + " AND " + TABLE_WORD + "." + COLUMN_WORD + "='" + word + "'";
			
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.getCount() == 0)
				return null;
			cursor.moveToFirst();
			String ret = cursor.getString(0);
			cursor.close();
			return ret;
		}
		catch (SQLiteException e) {
			return null;
		}		
	}
	
	private static void clearTables() {
		try {
			String sql = "DROP TABLE " + TABLE_INFO;
			db.execSQL(sql);
			
			sql = "DROP TABLE " + TABLE_DATA;
			db.execSQL(sql);
			
			sql = "DROP TABLE " + TABLE_WORD;
			db.execSQL(sql);
			
			sql = "DROP TABLE " + TABLE_SCORE;
			db.execSQL(sql);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
		}
	}
	
	private static int clearData() {
		try {
			db.delete(TABLE_DATA, null, null);
			db.delete(TABLE_WORD, null, null);
			db.delete(TABLE_SCORE, null, null);
			db.delete(TABLE_INFO, COLUMN_ID + "=" + INFOTAG_CHECKIN, null);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
		}
		return 0;
	}
	
	public static Cursor getWordData(int type, int limit, int offset) {
		try {
			//select Word.srcid, Word.word, Score.updated, Score.score from Word, Score where Word.id = Score.wordid and Score.updated = 0 limit 10 offset 10
			String sql = "SELECT " + TABLE_WORD + "." + COLUMN_ID + "," + TABLE_WORD + "." + COLUMN_SRCID + "," + TABLE_WORD + "." + COLUMN_WORD + "," + TABLE_SCORE + "." + COLUMN_UPDATED + "," + TABLE_SCORE + "." + COLUMN_SCORE + " FROM " + TABLE_WORD + "," + TABLE_SCORE;
			sql += " WHERE " + TABLE_WORD + "." + COLUMN_ID + "=" + TABLE_SCORE + "." + COLUMN_WORDID + " AND " + TABLE_SCORE + "." + COLUMN_UPDATED;
			if(type == Score.WORD_TYPE_NEW)
				sql += "=0 AND " + TABLE_SCORE + "." + TABLE_SCORE + "=" + Score.SCORE_UNKNOWN;
			else
				sql += ">0 and " + TABLE_SCORE + "." + COLUMN_UPDATED;
			sql += " ORDER BY " + TABLE_SCORE + "." + COLUMN_UPDATED + " LIMIT " + limit + " OFFSET " + offset; 
			return db.rawQuery(sql, null);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return null;
		}
	}
	
	public static int updateScoreData(long wordid, long updated, int score) {
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_UPDATED, updated);
			values.put(COLUMN_SCORE, score);
			return ((db.update(TABLE_SCORE, values, COLUMN_WORDID + "=" + wordid, null) > 0) ? 0 : -1);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return -1;
		}
	}

	public static Cursor getScoreStat() {
		try {
			String sql = "SELECT " + COLUMN_UPDATED + ", COUNT(" + COLUMN_UPDATED + ") FROM " + TABLE_SCORE + " GROUP BY " + COLUMN_UPDATED;
			return db.rawQuery(sql, null);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return null;	
		}
	}
	
	public static int getWordCount() {
		try {
			String sql = "SELECT COUNT(*) FROM " + TABLE_WORD;
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null)
				return -1;
			cursor.moveToFirst();
			int ret = cursor.getInt(0);
			cursor.close();
			return ret;
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return -1;	
		}
	}
	
	public static int getScoreCount(boolean newword, long updated) {
		try {
			String sql = "SELECT COUNT(" + COLUMN_SCORE + ") FROM " + TABLE_SCORE;
			if(newword) {
				sql += " WHERE " + COLUMN_SCORE + "=" + Score.SCORE_UNKNOWN;
			}
			else {
				sql += " WHERE " + COLUMN_SCORE + "<>" + Score.SCORE_UNKNOWN;
				if(updated != -1) {
					sql += " AND " + COLUMN_UPDATED + " <= " + updated;
				}
			}

			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null)
				return -1;
			cursor.moveToFirst();
			int ret = cursor.getInt(0);
			cursor.close();
			return ret;
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return -1;	
		}
	}

	public static boolean checkWord() {
		try {
			String sql = "SELECT COUNT(" + COLUMN_SCORE + ") FROM " + TABLE_SCORE;
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null)
				return false;
			cursor.moveToFirst();
			int ret = cursor.getInt(0);
			cursor.close();
			if(ret == 0)
				return false;
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return false;	
		}
		return true;
	}

	public static Cursor getWords(int type, int value) {
		
		String sql = "SELECT Word.word, Word.srcid AS _id, Score.updated FROM Word, Score WHERE (Word.id = Score.wordid)";
		
		if(type == 0) {
			if(value == 1) {
				//new
				sql += " AND (Score.updated = 0)";
			}
			else if(value == 2) {
				//old
				sql += " AND (Score.updated > 0)";
			}
			else {
				//all
			}
		}
		else {
			sql += (" AND (Score.updated = " + value + ")");
		}
		
		try {
			return db.rawQuery(sql, null);
		}
		catch (SQLException e) {
			Log.e(Global.APP_TITLE, "db exception - " + e.toString());
			return null;	
		}
	}

}
