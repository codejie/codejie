package jie.java.android.lingoshook.data;

import jie.java.android.lingoshook.Global;
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
		
//		if(initTables() != 0)
//			return -1;
//		if(initInfoData() != 0)
//			return -1;
//		
		return 0;		

	}

	@Override
	protected int release() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
