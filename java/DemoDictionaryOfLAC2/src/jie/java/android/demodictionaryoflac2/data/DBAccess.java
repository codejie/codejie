package jie.java.android.demodictionaryoflac2.data;

import java.util.ArrayList;

import jie.java.android.demodictionaryoflac2.Global;
import jie.java.android.demodictionaryoflac2.Magic;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public class DBAccess {

	public static final class XmlIndex {
		public int offset = 0;
		public int length = 0;
		public int block1 = 0;
		public int block2 = 0;		
	}
	
	public static final class WordData {
		public int index = 0;
		public String word = null;
		public ArrayList<XmlIndex> xmlIndex = new ArrayList<XmlIndex>();
	}	
	
	public static final class BlockData {

		public int index = 0;
		public int offset = 0;
		public int length = 0;
		public int start = 0;
		public int end = 0;
	}

	private static final DBAccess instance = new DBAccess();
	
	private SQLiteDatabase db = null;
	
	public static DBAccess instance() {
		return instance;
	}
	
	public int init() {
		try {
			db = SQLiteDatabase.openOrCreateDatabase(Global.DB_FILE, null);
		}
		catch (SQLiteException e) {
			return -1;
		}
		return 0;
	}
	
	public int getWordData(final WordData data) {
		String sql = "SELECT word FROM ld2_vicon_word_info WHERE idx=" + data.index;
		
		Cursor cursor = db.rawQuery(sql, null);
		
		if(cursor == null || cursor.isFirst() == false)
			return -1;
		
		data.word = cursor.getString(0);
		
		cursor.close();
		
		return getWordXmlIndex(data.index, data.xmlIndex);
	}
	
	public int getWordXmlIndex(final int index, final ArrayList<XmlIndex> xmlIndex) {
		String sql = "SELECT offset, length, block1, block2 FROM ld2_vicon_word_index WHERE idx = " + index;
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor == null || cursor.isFirst() == false)
			return -1;

		while(cursor.moveToNext()) {
			XmlIndex xi = new XmlIndex();
			xi.offset = cursor.getInt(0) + Magic.getMagicNumber();
			xi.length = cursor.getInt(1);
			xi.block1 = cursor.getInt(2);
			xi.block2 = cursor.getInt(3);
			
			xmlIndex.add(xi);
		}
		cursor.close();
		return 0;
	}
	
	public int getBlockData(final BlockData data) {
		String sql = "SELECT offset, length, start, end FROM ld2_vicon_block_info WHERE idx = " + data.index;
		
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor == null || cursor.isFirst() == false) 
			return -1;
		data.offset = cursor.getInt(0);
		data.length = cursor.getInt(1);
		data.start = cursor.getInt(2);
		data.end = cursor.getInt(3);
		
		cursor.close();
		return 0;		
	}

	public Cursor getItemData(String condition, int offset, int maxRows) {
		String sql = "SELECT idx, word FROM ld2_vicon_word_info WHERE " + condition;
		
		return db.rawQuery(sql, null);
	}
	

	
}
