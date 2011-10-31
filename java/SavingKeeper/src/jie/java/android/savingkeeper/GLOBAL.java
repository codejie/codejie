package jie.java.android.savingkeeper;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.util.Log;

public final class GLOBAL {

	public static final String APP_TAG 			= "SavingKeeper";

	public static final int APP_ACTION_EXIT		=	0;
	
	public static final String DATABASE_NAME	= "/data/data/jie.java.android.savingkeeper/savingkeeper.db";

	public static Date TODAY					= Calendar.getInstance().getTime();
	
	public static DBAccess DBACCESS = null;
	public static DataCalculator CALCULATOR = null;
	
	public static void init(Activity act) {
		DBACCESS = new DBAccess();
		DBACCESS.init(DATABASE_NAME);
		
		CALCULATOR = new DataCalculator();
		CALCULATOR.init();
		
		DBACCESS.initData(act);
	}
	
	public static void close() {
		
		if(CALCULATOR != null) {
			CALCULATOR.release();
		}
		
		if(DBACCESS != null) {
			DBACCESS.release();
		}
		
		Log.d(APP_TAG, "GLOBAL close.");
	}
}
