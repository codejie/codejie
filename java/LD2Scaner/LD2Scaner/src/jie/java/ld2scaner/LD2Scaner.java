package jie.java.ld2scaner;

import java.util.ArrayList;

public class LD2Scaner {

	public static final class WordIndex {
		public int offset = 0;
		public int length = 0;
		public int block1 = 0;
		public int block2 = 0;		
	}
	public static final class WordData {
		public int index = 0;
		public String word = null;
		public ArrayList<WordIndex> block = new ArrayList<WordIndex>();
	}
	
	private final static String ld2File = "./data/3GPP.ld2"; 

	public static void main(String[] args) {
		
		DBHelper db = DBHelper.create("./data/db.db");
		
//		if(FileScan.scan(ld2File, db) != 0) {
//			System.out.println("FAILED.");
//		}
		
		verifyData(db, ld2File, 100);
		
		db.close();		
	}

	private static void verifyData(DBHelper db, String ld2file2, int index) {
		
		final WordData data = new WordData();
		data.index = index;
		
		if(db.getWordData(data) != 0)
			return;
		
		return;
	}

}
