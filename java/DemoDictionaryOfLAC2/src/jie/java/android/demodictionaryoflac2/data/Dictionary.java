package jie.java.android.demodictionaryoflac2.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import jie.java.android.demodictionaryoflac2.data.Word.XmlData;

import android.database.Cursor;

public class Dictionary {

	public static final class BlockData {

		//public int index = 0;
		public int offset = 0;
		public int length = 0;
		public int start = 0;
		public int end = 0;
	}
	
	public static final class Item {
		
		private final int id;
		private final String title;	
		private final String fileName;
		private final int offsetMagic;
		
		private final ArrayList<BlockData> blockData = new ArrayList<BlockData>();
		
		private RandomAccessFile file = null;
		
		public Item(int id, final String title, final String fileName, int offsetMagic) {
			this.id = id;
			this.title = title;
			this.fileName = fileName;
			this.offsetMagic = offsetMagic;
		}
		
		@Override
		protected void finalize() throws Throwable {
			close();
			super.finalize();
		}
		
		public int init(DBAccess db) {
			try {
				file = new RandomAccessFile(fileName, "r");
				
				return loadBlockData(db);
				
			} catch (FileNotFoundException e) {
				return -1;
			}
		}
		
		private void close() throws IOException {
			if(file != null) {
				file.close();
			}
		}
		
		private int loadBlockData(DBAccess db) {
			Cursor cursor = db.queryBlockData(id);
			if(cursor != null && cursor.moveToFirst()) {
				do {
					BlockData block = new BlockData();
					block.offset = cursor.getInt(0);
					block.length = cursor.getInt(1);
					block.start = cursor.getInt(2);
					block.end = cursor.getInt(3);
					
					blockData.add(block);
				} while (cursor.moveToNext());
			} else {
				return -1;
			}
			return 0;
		}
		
		public final String getXmlData(DBAccess db, int wordid) {
			//get word index
			//get ref
			//get xml
			return null;
		}

		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		
	}
	
	private static DBAccess db = null;
	private static final HashMap<Integer, Item> dictMap = new HashMap<Integer, Item>();	

	public static int open(DBAccess db) {
		if(load(db) != 0)
			return -1;
		 for(final Item dict : dictMap.values()) {
			 if(dict.init(db) != 0) {
				 return -1;
			 }
		 }
		 
		 Dictionary.db = db;
		
		return 0;
	}

	private static void close() throws IOException {
		for(final Item dict : dictMap.values()) {
			dict.close();
		}
	}

	
	private static int load(DBAccess db) {
		Cursor cursor = db.queryDictionary();
		if(cursor != null && cursor.moveToFirst()) {
			do {
				dictMap.put(cursor.getInt(0), new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
			} while (cursor.moveToNext());
		}
		return 0;
	}	
	
	public static int getXmlData(int wordid, ArrayList<XmlData> xmlData) {
		for(final Item dict : dictMap.values()) { 
			String xml = dict.getXmlData(db, wordid);
			if(xml != null) {
				xmlData.add(new XmlData(dict.getId(), xml));
			}
		}
		return 0;
	}
	
	
}
