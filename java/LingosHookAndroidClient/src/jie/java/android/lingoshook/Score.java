package jie.java.android.lingoshook;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

public final class Score {

	private static final float rateTable[][] = {
			{ 1.75f, 0.80f, 0.45f, 0.17f },
			{ 1.50f, 1.25f, 0.55f, 0.20f },
			{ 1.00f, 0.80f, 0.45f, 0.20f },
			{ 0.80f, 0.50f, 0.30f, 0.17f }
	};
	
	private static final int judgeTable[][] = {
		{ 0, 1, 1, 2 },
		{ 2, 2, 3, 3 } 
	};
		
	public static final int SCORE_0		=	0;
	public static final int SCORE_1		=	1;
	public static final int SCORE_2		=	2;
	public static final int SCORE_3		=	3;
	
	public static final int JUDGE_YES	= 0;
	public static final int JUDGE_NO 	= 1;
	
	public static final String TAG_WORDID		=	"wordid";
	public static final String TAG_SRCID		=	"srcid";
	public static final String TAG_UPDATED		=	"updated";
	public static final String TAG_PRESCORE		=	"prescore";
	public static final String TAG_SCORE		=	"score";
	public static final String TAG_JUDGE		=	"judge";
	
	public static final String CACHE_FILE		=	"/lhc_cache.html";

	public static final int WORD_NEW	=	0;
	public static final int WORD_OLD	=	1;
	
	private static final int WORD_LIMIT_NEW	=	0;
	private static final int WORD_LIMIT_OLD	=	30;

	public static final long UPDATED_START	=	30; 
	
	public static long deltaUpdated = 0;
	
	private static int offsetNewWord = 0;
	private static int offsetOldWord = 0;		
	
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
		
		Log.d(Global.APP_TITLE, "score deltaUpdated : " + deltaUpdated);
		
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
		cursor.close();
		
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
		cursor.close();
		
		return 0;		
	}
	
	public static WordData popWordData() {
		if(listWord.isEmpty())
			return null;
		return listWord.remove(0);
	}
	
	public static int updateWordData(final WordData data, int score, int judge) {
		
		Log.d(Global.APP_TITLE, data.word + " old score : " + data.score + " new score : " + score + " judge : " + judge);
		
		int check = judgeTable[judge][score];
		
		Log.d(Global.APP_TITLE, data.word + " check : " + check);
		
		int result = (int)(((((data.updated != 0) ? (deltaUpdated - data.updated) : UPDATED_START)) * rateTable[data.score][check]));
		
		Log.d(Global.APP_TITLE, data.word + "old score: " + data.score + " new score : " + result);
		
		return DBAccess.updateScoreData(data.wordid, data.updated + result, check);
	}
}
