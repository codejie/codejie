package jie.java.android.lingoshook;

import android.content.Context;

public final class Global {
	public static final String DATABASE_NAME		=	"/data/data/jie.java.android.lingoshook/LingosHook.db";
	public static final String APP_TITLE			=	"LingosHook";
	public static int APP_VERSION					=	1;
	
	public static int DB_FORMAT_VERSION				=	1;
	
	public static int initApplication(Context context) {
		DBAccess.init(DATABASE_NAME);
		Setting.init(context);
		Speaker.init(context);		
		Score.init();
		
		return 0;
	}
	
	public static void exitApplication() {
		Speaker.release();
		Setting.release();
		DBAccess.release();
		System.exit(0);
	}
}
