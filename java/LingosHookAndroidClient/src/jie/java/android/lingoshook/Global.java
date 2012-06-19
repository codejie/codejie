package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public final class Global {
	public static final String DATABASE_NAME			=	"/data/data/jie.java.android.lingoshook/LingosHook.db";
	public static final String APP_TITLE				=	"LingosHook";
	public static final int APP_VERSION					=	1;
	
	public static final int DB_FORMAT_VERSION			=	1;
	
	public static final int STATE_CODING				=	2;//0:Debug; 1: Coding; 2: Release
	
	public static int SCREEN_WIDTH						=	480;
	public static int SCREEN_HEIGHT						=	800; 
	
	public static void getScreenInfo(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Global.SCREEN_HEIGHT = displaymetrics.heightPixels;
        Global.SCREEN_WIDTH = displaymetrics.widthPixels;		
	}
	
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
		//System.exit(0);
	}
}
