package jie.java.ld2scaner;

public class LD2Scaner {

	public final class WordData {
		public final int index = 0;
		public final int offset = 0;
		public final int length = 0;
		public final int start = 0;
		public final int end = 0;
		public final String word = null;
	}
	
	private final static String ld2File = "./data/3GPP.ld2"; 

	public static void main(String[] args) {
		
		DBHelper db = DBHelper.create("./data/db.db");
		
		if(FileScan.scan(ld2File, db) != 0) {
			System.out.println("FAILED.");
		}
		
		verifyData(db, ld2File, 100);
		
		db.close();		
	}

	private static void verifyData(DBHelper db, String ld2file2, int index) {
		
		WordData data = new WordData();
		
		db.getWordData(index, data);
		
	}

}
