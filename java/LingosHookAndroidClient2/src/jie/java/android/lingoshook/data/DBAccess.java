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
	
	private static final String TABLE_TEST		=	"Test";
	
	private static final String COLUMN_ID		=	"id";
	private static final String COLUMN_VALUE	=	"value";
		
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
//		if(initData() != 0)
//			return -1;
		
		return 0;		

	}
	
	@Override
	protected int release() {
		if(db != null) {
			db.close();
		}
		return 0;
	}
	
	protected int initTables() {
		
		try {
			String sql = "CREATE TABLE IF NOT EXISTS Test ("
					+ "id INTEGER PRIMARY KEY,"
					+ "value TEXT)";
			db.execSQL(sql);
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
			values.put(COLUMN_VALUE, String.format("This is %d", i));
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
