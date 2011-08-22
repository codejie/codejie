package jie.java.android.savingkeeper;

public final class GLOBAL {

	public static final String APP_TAG 			= "SavingKeeper";
	
	public static final String DATABASE_NAME	= "/data/data/jie.java.android.savingkeeper/savingkeeper.db";

	public static DBAccess DBACCESS = null;
	public static DataCalculator CALCULATOR = null;
	
	public static void init() {
		DBACCESS = new DBAccess();
		DBACCESS.init(DATABASE_NAME);
		
		CALCULATOR = new DataCalculator();
		CALCULATOR.init();
	}
	
	public static void close() {
		
		if(CALCULATOR != null) {
			CALCULATOR.release();
		}
		
		if(DBACCESS != null) {
			DBACCESS.release();
		}
	}
}
