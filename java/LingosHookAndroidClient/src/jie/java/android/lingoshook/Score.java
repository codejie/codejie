package jie.java.android.lingoshook;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

public final class Score {

	public static final class WordData {
		public int wordid;
		public int srcid;
		public int last;
		public int next;
		public int score;
		public String word;
	}
	
	public static enum WordType {
		NULL, NEW, OLD, MISTAKE
	}
		
	public static final class WordDisplayData {
		public WordType type = WordType.NULL;
		public WordData data = null;
		public int rest = -1;
	}	
		
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
	
	//public static final String CACHE_FILE		=	"/lhc_cache.html";

	public static final int WORD_TYPE_ALL	=	0;
	public static final int WORD_TYPE_NEW	=	1;
	public static final int WORD_TYPE_OLD	=	2;
	public static final int WORD_TYPE_TODAY	=	3;
	
	//private static final int WORD_LIMIT_NEW	=	3;
	//private static final int WORD_LIMIT_OLD	=	3;

	public static final int UPDATED_START		=	7;//30;
	public static final int DEFAULT_LIMIT_OLD	=	30;
	
	public static int TODAY_NEW		=	0;
	public static int TODAY_OLD		=	0;
	public static int WORD_TOTAL	=	0;
	
	private static int deltaUpdated = 0;
	
	private static int offsetNewWord = 0;
	private static int offsetOldWord = 0;		
	
	private static WordType typeWord = WordType.NULL;	
	private static int numRestWord = -1;
	private static List<WordData> listWord = new ArrayList<WordData>();
	private static List<WordData> listMistakeWord = new ArrayList<WordData>();
	
	
	public static int init() {
		deltaUpdated = DBAccess.getDeltaUpdate();
		
		WORD_TOTAL = DBAccess.getWordCount();
        TODAY_NEW = DBAccess.getScoreCount(true, -1);
        TODAY_OLD = DBAccess.getScoreCount(false, Score.getDeltaUpdated());
        
        if(TODAY_NEW > Setting.numLoadNewWord) {
        	TODAY_NEW = Setting.numLoadNewWord;
        }
        
        if(Setting.numLoadOldWord != 0 && TODAY_OLD > Setting.numLoadOldWord) {
        	TODAY_OLD = Setting.numLoadOldWord;
        }
        
        listWord.clear();
        listMistakeWord.clear();

		Log.d(Global.APP_TITLE, "score deltaUpdated : " + deltaUpdated);
		
		return 0;//loadWordData();
	}
	
	public static void setDeltaUpdated(int delta) {
		deltaUpdated = delta;
	}
	
	public static int getDeltaUpdated() {
		return deltaUpdated;
	}

	private static int loadNewWordData(int limit) {
		if(limit > 0) {
			numRestWord = limit;//DBAccess.getScoreCount(true, -1);
			Cursor cursor = DBAccess.getWordData(WORD_TYPE_NEW, limit, offsetNewWord);
			if(cursor == null)
				return -1;
			
			while(cursor.moveToNext()) {
				WordData data = new WordData();
				
				data.wordid = cursor.getInt(0);
				data.srcid = cursor.getInt(1);
				data.last = cursor.getInt(3);
				data.next = cursor.getInt(4);
				data.score = cursor.getInt(5);
				if(data.score == SCORE_UNKNOWN)
					data.score = SCORE_3;
				data.word = cursor.getString(2);
				
				listWord.add(data);
				
				++ offsetNewWord;
			}
			cursor.close();
		}
		typeWord = WordType.NEW;

		return 0;
	}
	
	private static int loadOldWordData(int limit) {
		if(limit > 0) {
			numRestWord = limit;// DBAccess.getScoreCount(false, -1);
			
			Cursor cursor = DBAccess.getWordData(WORD_TYPE_OLD, limit, offsetOldWord);
			if(cursor == null)
				return -1;
			
			while(cursor.moveToNext()) {
				WordData data = new WordData();
				
				data.wordid = cursor.getInt(0);
				data.srcid = cursor.getInt(1);
				data.last = cursor.getInt(3);
				data.next = cursor.getInt(4);
				data.score = cursor.getInt(5);
				data.word = cursor.getString(2);
				
				listWord.add(data);
				
				++ offsetOldWord;
			}
			cursor.close();
		}
		typeWord = WordType.OLD;
		
		return 0;		
	}
	
	public static int loadMistakeWordData() {
	
		numRestWord = listMistakeWord.size();
		if(numRestWord == 0)
			return -1;
		
		if(!listWord.isEmpty())
			listWord.clear();
		
		listWord.addAll(listMistakeWord);// . .s  = listMistakeWord;
		listMistakeWord.clear();
		
		typeWord = WordType.MISTAKE;
		
		return 0;
	}
	
	private static int loadWordData() {
		if(typeWord == WordType.NULL) {
			if(loadOldWordData(TODAY_OLD) != 0) {
				return -1;
			}
			if(listWord.isEmpty())
				return loadWordData();
		}
		else if(typeWord == WordType.OLD) {
			if(loadNewWordData(TODAY_NEW) != 0) {
				return -1;
			}
			if(listWord.isEmpty())
				return loadWordData();
		}
		else if(typeWord == WordType.NEW) {
			if(Setting.loadMistakeWord) { 
				if(loadMistakeWordData() != 0) {
					return -1;
				}
			}
		}
		else if(typeWord == WordType.MISTAKE) {
			if(loadMistakeWordData() != 0) {
				return -1;
			}
			if(listWord.isEmpty())
				return loadWordData();			
		}
		else {
			return -1;
		}
		
		return 0;
	}
	
//	private static int loadWordData() {
//		if(typeWord == WordType.NULL) {
//			if(loadNewWordData(TODAY_NEW) != 0) {
//				return -1;
//			}
//			if(listWord.isEmpty())
//				return loadWordData();
//		}
//		else if(typeWord == WordType.NEW) {
//			if(loadOldWordData(TODAY_OLD) != 0) {
//				return -1;
//			}
//			if(listWord.isEmpty())
//				return loadWordData();
//		}
//		else if(typeWord == WordType.OLD) {
////			if(Setting.numLoadOldWord == 0) {
////				if(loadOldWordData(DEFAULT_LIMIT_OLD) != 0)
////					return -1;
////			}
////			else
//			if(Setting.loadMistakeWord) { 
//				if(loadMistakeWordData() != 0) {
//					return -1;
//				}
//			}
//		}
//		else {
//			return -1;
//		}
//		
//		return 0;
//	}
	
	public static int popWordData(WordDisplayData data) {
		if(listWord.isEmpty()) {
			if(loadWordData() != 0)
				return -1;
		}
		if(listWord.isEmpty())
			return -1;

		data.data = listWord.remove(0);
		data.type = typeWord;
		data.rest = numRestWord --;
		return 0;
	}
	
	public static int updateWordData(final WordData data, int score, int judge) {
		
		if(judge == JUDGE_NO) {
			listMistakeWord.add(data);
		}
		
		Log.d(Global.APP_TITLE, data.word + " old score : " + data.score + " new score : " + score + " judge : " + judge);
		
		int check = judgeTable[judge][score];
		
		Log.d(Global.APP_TITLE, data.word + " check : " + check);

		data.next = (data.last != 0) ? deltaUpdated - data.last : UPDATED_START;
		data.next *= rateTable[data.score][check];
		data.next += (deltaUpdated + 1);
		
//		int result = ((((data.updated != 0) ? data.updated : UPDATED_START)) * rateTable[data.score][check]);

//		result = ((data.updated + result) > deltaUpdated) ? (data.updated + result) : (deltaUpdated + result);
		
//		Log.d(Global.APP_TITLE, data.word + "old score: " + data.score + " new score : " + result);
		
		return DBAccess.updateScoreData(data.wordid, deltaUpdated, data.next, check);
	}
	
	public static int getMistakeWordCount() {
		return listMistakeWord.size();
	}
	
	public static void refreshData() {
		offsetNewWord = 0;
		offsetOldWord = 0;		
		
		typeWord = WordType.NULL;	
		numRestWord = -1;
		listWord.clear();
		listMistakeWord.clear();		
	}

}
