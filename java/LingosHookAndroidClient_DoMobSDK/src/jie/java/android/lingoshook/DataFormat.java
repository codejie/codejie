package jie.java.android.lingoshook;

import java.util.Vector;



public class DataFormat {

	public static final int FORMAT_HTML		=	1;
	public static final int FORMAT_TEXT		=	2;
	
	public static final int TYPE_DICT		=	1;
	public static final int TYPE_MEMORY		=	2;
	
	
	public static class Data {
		public String dict = null;
		public String word = null;
		public String symbol = null;
		public Vector<String> category = new Vector<String>();
		public Vector<String> meaning = new Vector<String>();
		
		public Data() {
		}
	}
	
}
