package jie.java.ld2scaner;

public class LD2Scaner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DBHelper db = DBHelper.create("./data/db.db");
		
		if(FileScan.scan("./data/3GPP.ld2", null) != 0) {
			System.out.println("FAILED.");
		}
			
		
	}

}
