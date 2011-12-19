package jie.java.android.lingoshook;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public final class Score {

	private static final float rate[][] = {
			{ 1.75f, 0.80f, 0.45f, 0.17f },
			{ 1.50f, 1.25f, 0.55f, 0.20f },
			{ 1.00f, 0.80f, 0.45f, 0.20f },
			{ 0.80f, 0.50f, 0.30f, 0.17f }
	};
		
	public static int SCORE_0		=	0;
	public static int SCORE_1		=	1;
	public static int SCORE_2		=	2;
	public static int SCORE_3		=	3;
	
	public static String TAG_WORDID		=	"wordid";
	public static String TAG_SRCID		=	"srcid";
	public static String TAG_UPDATED	=	"updated";
	public static String TAG_PRESCORE	=	"prescore";
	public static String TAG_SCORE		=	"score";
	
	public static String CACHE_FILE		=	"/lhc_cache.html";

	public static final int WORD_NEW	=	0;
	public static final int WORD_OLD	=	1;
	
	private static final int WORD_LIMIT_NEW	=	30;
	private static final int WORD_LIMIT_OLD	=	30;

	private static int offsetNewWord = 0;
	private static int offsetOldWord = 0;	
	
	public static long deltaUpdated = 0;
	
	public static final class WordData {
		public long wordid;
		public long srcid;
		public long updated;
		public int score;
		public String word = new String();
	}
	
	private static List<WordData> listWord = new ArrayList<WordData>();
	
	public static int init() {
		deltaUpdated = DBAccess.getDeltaUpdate();
		return loadWordData();
	}
	
	public static void setDeltaUpdated(long delta) {
		deltaUpdated = delta;
	}
	
	private static int loadWordData() {
		listWord.clear();
		
		loadNewWordData();
		loadOldWordData();
		return 0;
	}
	
	private static int loadNewWordData() {
		Cursor cursor = DBAccess.getWordData(Score.WORD_NEW, WORD_LIMIT_NEW, offsetNewWord);
		if(cursor == null)
			return -1;
		
		while(cursor.moveToNext()) {
			WordData data = new WordData();
			
			data.wordid = cursor.getLong(0);
			data.srcid = cursor.getLong(1);
			data.updated = cursor.getLong(3);
			data.score = cursor.getInt(4);
			data.word = cursor.getString(2);
			
			listWord.add(data);
			
			++ offsetNewWord;
		}
		
		return 0;
	}
	
	private static int loadOldWordData() {
		Cursor cursor = DBAccess.getWordData(Score.WORD_OLD, WORD_LIMIT_OLD, offsetOldWord);
		if(cursor == null)
			return -1;
		
		while(cursor.moveToNext()) {
			WordData data = new WordData();
			
			data.wordid = cursor.getLong(0);
			data.srcid = cursor.getLong(1);
			data.updated = cursor.getLong(3);
			data.score = cursor.getInt(4);
			data.word = cursor.getString(2);
			
			listWord.add(data);
			
			++ offsetOldWord;
		}
		
		return 0;		
	}
	
	public static WordData popWordData() {
		if(listWord.isEmpty())
			return null;
		return listWord.remove(0);
	}
	
	public static int calcScore() {
		return -1;
	}
}
