package jie.java.android.lingoshook;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public final class DBAccess {

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
		if(initData() != 0)
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
		return -1;
	}
	
	private static int initData() {
		return -1;
	}
	
}
