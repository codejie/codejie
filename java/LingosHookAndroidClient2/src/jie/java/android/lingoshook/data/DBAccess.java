package jie.java.android.lingoshook.data;

import jie.java.android.lingoshook.Global;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

public class DBAccess extends BaseDataObject {
	
	public static final DBAccess instance = new DBAccess();
	
	private static final String DATABASE_FILE			=	"/data/data/jie.java.android.lingoshook/LingosHook.db";;
	public static final int DATABASE_FORMAT_VERSION		=	2;
	
	private static final String TABLE_TEST			=	"Test";
	private static final String TABLE_WORD			=	"Word";
	private static final String TABLE_SRC_INDEX		=	"Src_Index";
	private static final String TABLE_SRC_TEXT_INDEX	=	"Src_Text_Index";
	private static final String TABLE_SRC_TEXT_MEANING	=	"Src_Text_Meaning";
	private static final String TABLE_SRC_HTML		=	"Src_Html";
	private static final String TABLE_DICTIONARY	=	"Dictionary";
	private static final String TABLE_SCORE			=	"Score";
	
	private static final String COLUMN_ID		=	"id";
	private static final String COLUMN_VALUE	=	"value";
		
	private static final String COL_WORDID		=	"wordid";
	private static final String COL_WORD		=	"word";
	private static final String COL_FLAG		=	"flag";
	private static final String COL_SRCID		=	"srcid";
	private static final String COL_FMT			=	"fmt";
	private static final String COL_ORIG		=	"orig";
	private static final String COL_DICTID		=	"dictid";
	private static final String COL_SYMBOL		=	"symbol";
	private static final String COL_CATEGORY	=	"category";
	private static final String COL_MEANING		=	"meaning";
	private static final String COL_HTML		=	"html";
	private static final String COL_TITLE		=	"title";
	private static final String COL_LAST		=	"last";
	private static final String COL_NEXT		=	"next";//next update
	private static final String COL_SCORE		=	"score";	
	
	private static SQLiteDatabase db = null;

	@Override
	public int init(Bundle savedInstanceState) {

		try {
			db = SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE, null);
			
			Log.i(Global.APP_TITLE, "db initial - " + db.getMaximumSize());
		}
		catch (SQLiteException e) {
			return -1;
		}
		
		if(initTables() != 0)
			return -1;
		if(initData() != 0)
			return -1;
		
		return 0;		

	}
	
	@Override
	protected void release() {
		if(db != null) {
			db.close();
		}
	}
		
	protected int initTables() {
		
		try {
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WORD + " (" 
						+ COL_WORDID + " INTEGER PRIMARY KEY," 
						+ COL_WORD + " TEXT,"
						+ COL_FLAG + " INTEGER)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SRC_INDEX + " ("
						+ COL_SRCID + " INTEGER PRIMARY KEY,"					
						+ COL_WORDID + " INTEGER,"
						+ COL_FMT + " INTEGER,"
						+ COL_ORIG + " INTEGER)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SRC_TEXT_INDEX + " ("
						+ COL_SRCID + " INTEGER,"
						+ COL_DICTID + " INTEGER,"
						+ COL_SYMBOL + " TEXT)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SRC_TEXT_MEANING + " ("
						+ COL_SRCID + " INTEGER,"
						+ COL_CATEGORY + " TEXT,"
						+ COL_MEANING + " TEXT)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SRC_HTML + " ("
						+ COL_SRCID + " INTEGER,"
						+ COL_HTML + " TEXT)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DICTIONARY + " ("
						+ COL_DICTID + " INTEGER,"
						+ COL_TITLE + " TEXT)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + " ("
						+ COL_WORDID + " INTEGER,"
						+ COL_LAST + " INTEGER,"
						+ COL_NEXT + " INTEGER,"
						+ COL_SCORE + " SCORE)");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS Test (id INTEGER PRIMARY KEY,value TEXT)");
		}
		catch (SQLiteException e) {
			return -1;
		}
		
		
		return 0;
	}
	
	protected int initData() {
		
		ContentValues values = new ContentValues();
		
		for(int i = 0; i < 24; ++ i) {
			values.clear();
			
			values.put(COLUMN_ID, String.valueOf(i));
			values.put(COLUMN_VALUE, String.format("%d - This is %d", i, i));
			db.insert(TABLE_TEST, null, values);
		}
		
		return 0;
	}

	public Cursor getTestData(String condition, int offset, int maxRows) {
		String sql = "SELECT id, value FROM Test";
		if(condition != null) {
			sql += " WHERE " + condition;
		}
		sql += " LIMIT " + maxRows + " OFFSET " + offset ;
		
		return db.rawQuery(sql, null);
	}
	
	
}
