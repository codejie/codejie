package jie.java.android.lingoshook;

import android.content.Context;
import android.content.SharedPreferences;

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
	
	public static int init(Context context, SharedPreferences sharedPreferences) {
		
		String key = context.getString(R.string.set_key_word_numloadnew);
		numLoadNewWord = sharedPreferences.getInt(key, 0);	
		
		return 0;
	}
	
	public static void release() {
		
	}
		
}
