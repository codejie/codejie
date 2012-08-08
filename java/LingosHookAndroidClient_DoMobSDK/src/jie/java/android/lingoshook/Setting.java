package jie.java.android.lingoshook;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Setting {

	public static int numLoadNewWord	=	3;
	public static int numLoadOldWord	=	3;
	public static boolean loadMistakeWord	=	true;	
	public static boolean loadResultDisplay	=	true;
	public static boolean loadSpeaker		=	true;
	
	public static boolean refeshFingerPanel	=	true;
	public static int intervalFingerPanel	=	5;
	public static int colorFingerPanelPen	=	0;
	public static int widthFingerPanelPen	=	0;
	
	public static boolean webclickable		=	true;
	
	public static int init(Context context) {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		numLoadNewWord = Integer.parseInt(preferences.getString(context.getString(R.string.set_key_word_numloadnew), "20"));
		numLoadOldWord = Integer.parseInt(preferences.getString(context.getString(R.string.set_key_word_numloadold), "0"));
		loadMistakeWord = preferences.getBoolean(context.getString(R.string.set_key_word_loadmistake), true);
		loadResultDisplay = preferences.getBoolean(context.getString(R.string.set_key_word_loadresult), true);
		loadSpeaker = preferences.getBoolean(context.getString(R.string.set_key_word_loadspeaker), true);
		
		refeshFingerPanel = preferences.getBoolean(context.getString(R.string.set_key_finger_refresh), true);
		intervalFingerPanel = Integer.parseInt(preferences.getString(context.getString(R.string.set_key_finger_interval), "2000"));
		colorFingerPanelPen = Integer.parseInt(preferences.getString(context.getString(R.string.set_key_finger_pencolor), "FFFFFF"), 16);
		widthFingerPanelPen = Integer.parseInt(preferences.getString(context.getString(R.string.set_key_finger_penwidth), "6"));
		
		webclickable = preferences.getBoolean(context.getString(R.string.set_key_web_clickable), true);
		
		return 0;
	}
	
	public static void release() {
		
	}
		
}
