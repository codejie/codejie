package jie.java.android.demodictionaryoflac2.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import jie.java.android.demodictionaryoflac2.Global;
import jie.java.android.demodictionaryoflac2.data.Word.XmlData;

import android.database.Cursor;
import android.util.Log;

public class Dictionary {

	public static final class BlockData {

		//public int index = 0;
		public int offset = 0;
		public int length = 0;
		public int start = 0;
		public int end = 0;
	}
	
	public static final class XmlIndex {
		public int offset = 0;
		public int length = 0;
		public int block1 = 0;
		public int block2 = 0;		
	}
	
	public static final class Item {
		
		private final int id;
		private final String title;	
		private final String fileName;
		private final int offsetMagic;
		private final int wordDecoder, xmlDecoder;
		
		private final ArrayList<BlockData> blockData = new ArrayList<BlockData>();
		
		private RandomAccessFile file = null;
		
		public Item(int id, final String title, final String fileName, int offsetMagic, int wordDecoder, int xmlDecoder) {
			this.id = id;
			this.title = title;
			this.fileName = fileName;
			this.offsetMagic = offsetMagic;
			this.wordDecoder = wordDecoder;
			this.xmlDecoder = xmlDecoder;
		}
		
		@Override
		protected void finalize() throws Throwable {
			close();
			super.finalize();
		}
		
		public int init(DBAccess db) {
			try {
				file = new RandomAccessFile(Global.SDCARD_ROOT + Global.DATA_ROOT + fileName, "r");
				
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
		
		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}		
		
		private int loadBlockData(DBAccess db) {
			Cursor cursor = db.queryBlockData(id);
			if(cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						BlockData block = new BlockData();
						block.offset = cursor.getInt(0);
						block.length = cursor.getInt(1);
						block.start = cursor.getInt(2);
						block.end = cursor.getInt(3);
						
						blockData.add(block);
					} while (cursor.moveToNext());
				}
				cursor.close();
			} else {
				return -1;
			}
			return 0;
		}
		
		public final ArrayList<String> getWordXmlData(DBAccess db, int wordid) {
			ArrayList<String> ret = new ArrayList<String>();
			//get self xml
			Log.d("===", "self xml start.");
			getSelfXml(db, wordid, ret);
			Log.d("===", "self xml over.");
			//get ref xml
			//getRefXml(db, wordid, ret);
			Log.d("===", "ref xml over.");

			if (ret.size() > 0) {
				return ret;
			} else {
				return null;
			}
		}

		private void getSelfXml(DBAccess db, int wordid, ArrayList<String> ret) {
			//get from word_index
			Cursor cursor = db.queryWordXmlIndex(id, wordid);
			Log.d("===", "4.");
			if(cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						do {
							Log.d("===", "5.");
							XmlIndex index = new XmlIndex();
							index.offset = cursor.getInt(0);
							index.length = cursor.getInt(1);
							index.block1 = cursor.getInt(2);
							if(index.block1 < 0) {
								index.block1 = -index.block1;
								index.block2 = index.block2 + 1;
							} else {
								index.block2 = -1;
							}
							
							String xml = getXmlData(index);
							if (xml != null) {
								ret.add(xml);
							}
						} while(cursor.moveToNext());
					}
				} finally {
					cursor.close();
				}
			}
		}

		private void getRefXml(DBAccess db, int wordid, ArrayList<String> ret) {
			//get ref index
			Cursor cursor = db.queryRefXmlIndex(id, wordid);
			if(cursor != null) {
				Log.d("===", "1.");
				try {
					if(cursor.moveToFirst()) {
						Log.d("===", "2.");
						do {
							XmlIndex index = new XmlIndex();
							index.offset = cursor.getInt(0);
							index.length = cursor.getInt(1);
							index.block1 = cursor.getInt(2);
							if(index.block1 < 0) {
								index.block1 = -index.block1;
								index.block2 = index.block2 + 1;
							} else {
								index.block2 = -1;
							}
							String xml = getXmlData(index);
							if (xml != null) {
								ret.add(xml);
							}
							
						} while(cursor.moveToNext());
					}
				} finally {
					cursor.close();
				}
			}
		}
		
//
//		private XmlIndex getWordXmlIndex(DBAccess db, int wordid) {
//			Cursor cursor = db.queryWordXmlIndex(id, wordid);
//			if(cursor != null) {
//				try {
//					if (cursor.moveToFirst()) {
//						XmlIndex ret = new XmlIndex();
//						ret.offset = cursor.getInt(0);
//						ret.length = cursor.getInt(1);
//						ret.block1 = cursor.getInt(2);
//						if(ret.block1 < 0) {
//							ret.block1 = -ret.block1;
//							ret.block2 = ret.block2 + 1;
//						} else {
//							ret.block2 = -1;
//						}
//						
//						return ret;
//					}
//				} finally {
//					cursor.close();
//				}
//			}
//			return null;
//		}

		private String getXmlData(XmlIndex index) {
			if(index.block1 > blockData.size())
				return null;
			final BlockData block = blockData.get(index.block1);
			
			int start = block.start;
			int offset = block.offset;
			int size = block.length;
			if(index.block2 != -1) {
				size += blockData.get(index.block2).length;
			}			
			
			if(fileAccess.setBlockCache(id, file, index.block1, start, offset, size) != 0)
				return null;
			
			return fileAccess.getXml(xmlDecoder, this.offsetMagic + index.offset, index.length);
		}	
		
	}
	
//	private static DBAccess db = null;
	private static final HashMap<Integer, Item> dictMap = new HashMap<Integer, Item>();
	
	private static DictFileAccess fileAccess = new DictFileAccess();

	public static int open(DBAccess db) {
		if(load(db) != 0)
			return -1;
		 for(final Item dict : dictMap.values()) {
			 if(dict.init(db) != 0) {
				 return -1;
			 }
		 }
		 
//		 Dictionary.db = db;
		
		return 0;
	}

	public static void close() throws IOException {
		for(final Item dict : dictMap.values()) {
			dict.close();
		}
	}

	
	private static int load(DBAccess db) {
		Cursor cursor = db.queryDictionary();
		if(cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					do {
						dictMap.put(cursor.getInt(0), new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
					} while (cursor.moveToNext());
				}
			} finally {
				cursor.close();
			}
		}
		return 0;
	}	
	
	public static int getWordData(DBAccess db, Word word) {
		for(final Item dict : dictMap.values()) {
			Log.d("===", "getWordData start.");
			ArrayList<String> xml = dict.getWordXmlData(db, word.getIndex());
			Log.d("===", "getWordData end.");
			if(xml != null) {
				word.addXmlData(dict.getId(), xml);
//				xmlData.add(new XmlData(dict.getId(), xml));
			}
		}
		return 0;
	}

	public static final String assembleXml(Word word) {
		if(word.getXmlData().size() == 0)
			return null;
		
		String ret = "<LAC><LAC-W>" + word.getText() + "</LAC-W>";
		for (final XmlData data : word.getXmlData()) {
			ret += "<LAC-R><LAC-D>" +  dictMap.get(data.getDictid()).getTitle() + "</LAC-D>";
			for(final String xml : data.getXml()) {
				ret += xml;
			}
			
//			for(final String xml : data.getXml()) {
//				ret += xml;
//			}
//			
			ret += "</LAC-R>";
		}
//		for (final XmlData data : word.getXmlData()) {
//			ret += "<LAC-R><LAC-D>" +  dictMap.get(data.getDictid()).getTitle() + "</LAC-D>";
//			for(final String xml : data.getXml()) {
//				ret += xml;
//			}
//			ret += "</LAC-R>";
//		}
		
		ret += "</LAC>";
		
		return ret;
	}
	
	
}
