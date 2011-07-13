package jie.java.android.savingkeeper;

public final class GLOBAL {

	public static final String APP_TAG 			= "SavingKeeper";
	
	public static final String DATABASE_NAME	= "/data/data/jie.java.android.savingkeeper/savingkeeper.db";

	public static DBAccess DBACCESS = null;
	
	public static void Init() {
		DBACCESS = new DBAccess(DATABASE_NAME);
	}
}
