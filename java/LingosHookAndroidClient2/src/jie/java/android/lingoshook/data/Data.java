package jie.java.android.lingoshook.data;

import android.os.Bundle;

public class Data {

	public static DBAccess db = null;
	
	public static int init(Bundle savedInstanceState) {
		create();
		
		db.init(savedInstanceState);
		
		return 0;
	}
	
	private static void create() {
		db = new DBAccess();		
	}
}
