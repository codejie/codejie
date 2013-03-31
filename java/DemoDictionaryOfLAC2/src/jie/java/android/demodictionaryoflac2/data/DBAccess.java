package jie.java.android.demodictionaryoflac2.data;

import java.util.ArrayList;
import java.util.HashMap;

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
		
		public WordData(int idx) {
			index = idx;
		}
		
		public WordData(int idx, final String word) {
			this.index = idx;
			this.word = word;
		}
	}	
	
	public static final class BlockData {

		public int index = 0;
		public int offset = 0;
		public int length = 0;
		public int start = 0;
		public int end = 0;
	}
	
	HashMap<Integer, BlockData> mapBlockData = new HashMap<Integer, BlockData>();

	private static DBAccess instance = null;//new DBAccess();
	
	private SQLiteDatabase db = null;
	
	public static DBAccess instance() {
		return instance;
	}
	
	public static int init(final String dbfile) {
		if(instance != null) {
			return 0;
		}
		instance = new DBAccess();
		
		try {
			instance.db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);//Global.SDCARD_ROOT + Global.DATA_ROOT + Global.DB_FILE, null);
		}
		catch (SQLiteException e) {
			return -1;
		}
		return 0;//instance.loadBlockData();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(db != null) {
			db.close();
		}
		super.finalize();
	}
	
	private int loadBlockData() {
		Cursor cursor = db.query("ld2_vicon_block_info", new String[] { "idx", "offset", "length", "start", "end" }, null, null, null, null, null);
		if(cursor != null && cursor.moveToFirst()) {
			do {
				
				BlockData data = new BlockData();
				data.index = cursor.getInt(0);
				data.offset = cursor.getInt(1);
				data.length = cursor.getInt(2);
				data.start = cursor.getInt(3);
				data.end = cursor.getInt(4);
				
				mapBlockData.put(data.index, data);
				
			} while(cursor.moveToNext());
			cursor.close();
		}
		return 0;
	}
	
	public final BlockData getBlockData(int index) {
		return mapBlockData.get(index);
	}
	
	public int getWordData(final WordData data) {
		String sql = "SELECT word FROM ld2_vicon_word_info WHERE idx=" + data.index;
		
		Cursor cursor = db.rawQuery(sql, null);
		
		if(cursor == null || cursor.moveToFirst() == false)
			return -1;
		
		data.word = cursor.getString(0);
		
		cursor.close();
		
		return getWordXmlIndex(data.index, data.xmlIndex);
	}
	
	public int getWordXmlIndex(final int index, final ArrayList<XmlIndex> xmlIndex) {
		String sql = "SELECT offset, length, block1, block2 FROM ld2_vicon_word_index WHERE idx = " + index;
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor == null || cursor.moveToFirst() == false)
			return -1;

		do {
			XmlIndex xi = new XmlIndex();
			xi.offset = cursor.getInt(0) + Magic.getMagicNumber();
			xi.length = cursor.getInt(1);
			xi.block1 = cursor.getInt(2);
			xi.block2 = cursor.getInt(3);
			
			xmlIndex.add(xi);
		} while(cursor.moveToNext());
		cursor.close();
		return 0;
	}
	
	public int getBlockData(final BlockData data) {
		String sql = "SELECT offset, length, start, end FROM ld2_vicon_block_info WHERE idx = " + data.index;
		
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor == null || cursor.moveToFirst() == false) 
			return -1;
		data.offset = cursor.getInt(0);
		data.length = cursor.getInt(1);
		data.start = cursor.getInt(2);
		data.end = cursor.getInt(3);
		
		cursor.close();
		return 0;		
	}

	public Cursor getItemData(String condition, int offset, int maxRows) {
		String sql = "SELECT idx, word, flag FROM word_info";
		if(condition != null) {
			sql += " WHERE " + condition;
		}
		sql += " LIMIT " + maxRows + " OFFSET " + offset ;
		
		return db.rawQuery(sql, null);
	}

	public Cursor queryBlockData(int dictid) {
		return db.query("block_info_" + dictid, new String[] { "offset", "length", "start", "end" }, null, null, null, null, "idx");
	}

	public Cursor queryDictionary() {
		return db.query("dict_info", new String[] { "idx", "title", "file", "offset", "d_decoder", "x_decoder" }, null, null, null, null, "idx");
	}

	public Cursor queryWordXmlIndex(int dictid, int wordid) {
		return db.query("word_index_" + dictid, new String[] { "offset", "length", "block1" }, "wordid=?", new String[] { String.valueOf(wordid) }, null, null, null);
	}
	

	
}
