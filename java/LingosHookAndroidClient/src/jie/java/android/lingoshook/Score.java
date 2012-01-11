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
	public static final int SCORE_UNKNOWN	=	99;
	
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
	
	//private static final int WORD_LIMIT_NEW	=	3;
	//private static final int WORD_LIMIT_OLD	=	3;

	public static final long UPDATED_START	=	30; 
	
	public static long deltaUpdated = 0;
	
	private static int offsetNewWord = 0;
	private static int offsetOldWord = 0;		
	
	public static final class WordData {
		public WordData(WordData data) {
			this.wordid = data.wordid;
			this.srcid = data.srcid;
			this.updated = data.updated;
			this.score = data.score;
			this.word = data.word;
		}
		
		public WordData() {
			word = new String();		
		}
		public long wordid;
		public long srcid;
		public long updated;
		public int score;
		public String word = null;
	}
	
	public static enum WordType {
		NULL, NEW, OLD, MISTAKE
	}
	
	private static WordType _typeWord = WordType.NULL;
	
	private static boolean loadNewWord = true;
	
	private static List<WordData> listWord = new ArrayList<WordData>();
	
	public static int init() {
		deltaUpdated = DBAccess.getDeltaUpdate();
		
		loadNewWord = true;
		
		Log.d(Global.APP_TITLE, "score deltaUpdated : " + deltaUpdated);
		
		return 0;//loadWordData();
	}
	
	public static void setDeltaUpdated(long delta) {
		deltaUpdated = delta;
	}
	
	private static int loadWordData() {
		//listWord.clear();
		
		if(loadNewWord) {
			if(loadNewWordData() != 0)
				return -1;
			loadNewWord = false;
		}
		else {		
			if(loadOldWordData() != 0)
				return -1;
		}
		
		return 0;
	}
	
	private static int loadNewWordData() {
		Cursor cursor = DBAccess.getWordData(Score.WORD_NEW, Setting.numLoadNewWord, offsetNewWord);
		if(cursor == null)
			return -1;
		
		while(cursor.moveToNext()) {
			WordData data = new WordData();
			
			data.wordid = cursor.getLong(0);
			data.srcid = cursor.getLong(1);
			data.updated = cursor.getLong(3);
			data.score = cursor.getInt(4);
			if(data.score == SCORE_UNKNOWN)
				data.score = SCORE_0;
			data.word = cursor.getString(2);
			
			listWord.add(data);
			
			++ offsetNewWord;
		}
		cursor.close();
		
		return 0;
	}
	
	private static int loadOldWordData() {
		Cursor cursor = DBAccess.getWordData(Score.WORD_OLD, Setting.numLoadOldWord, offsetOldWord);
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
	
	private static int loadMistakeWordData() {
		return -1;
	}
	
	public static WordData popWordData(WordType type) {
		
	}
	
	public static WordType popWordData(WordData data) {
		if(listWord.isEmpty()) {
			if(_typeWord == WordType.NULL) {
				if(loadNewWordData() == 0) {
					_typeWord = WordType.NEW;
				}
				else {
					return WordType.NULL;
				}
			}
			else if(_typeWord == WordType.NEW) {
				if(loadOldWordData() == 0) {
					_typeWord = WordType.OLD;
				}
				else {
					return WordType.NULL;
				}
			}
			else if(_typeWord == WordType.OLD) {
				if(loadMistakeWordData() == 0) {
					_typeWord = WordType.MISTAKE;
				}
				else {
					return WordType.NULL;
				}
			}
		}
		if(listWord.isEmpty())
			return WordType.NULL;
		
		//data = new WordData();
		data = new WordData(listWord.remove(0));
		return _typeWord;
	}
	
	
	public static WordData popWordData() {
		if(listWord.isEmpty()) {
			if(loadWordData() != 0) {
				return null;
			}
			if(listWord.isEmpty()) {
				return null;
			}
		}
		return listWord.remove(0);
	}
	
	public static int updateWordData(final WordData data, int score, int judge) {
		
		Log.d(Global.APP_TITLE, data.word + " old score : " + data.score + " new score : " + score + " judge : " + judge);
		
		int check = judgeTable[judge][score];
		
		Log.d(Global.APP_TITLE, data.word + " check : " + check);
		
		int result = (int)(((((data.updated != 0) ? data.updated : UPDATED_START)) * rateTable[data.score][check]));
		
		Log.d(Global.APP_TITLE, data.word + "old score: " + data.score + " new score : " + result);
		
		return DBAccess.updateScoreData(data.wordid, data.updated + result, check);
	}
}
