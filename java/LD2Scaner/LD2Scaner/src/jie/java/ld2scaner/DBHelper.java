package jie.java.ld2scaner;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jie.java.ld2scaner.FileScan.BlockData;
import jie.java.ld2scaner.LD2Scaner.WordData;
import jie.java.ld2scaner.LD2Scaner.WordIndex;

public class DBHelper {

	private Connection conn = null;
	
	public static DBHelper create(final String dbfile) {
		return new DBHelper(dbfile);	
	}	
	
	public DBHelper(final String dbfile) {
		
		init(dbfile);
		createTables();
		
	}
	
	private void init(String dbfile) {
		try {
			
			Class.forName("org.sqlite.JDBC");
			
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbfile);
			conn.setAutoCommit(false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}		
	
	private void execSQL(final String sql) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);	
			stat.close();
		} catch (SQLException e) {			
			return;
		}
	}	
	
	private int execSQLWithReturn(final String sql) {
		int last = -1;
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);

			last = stat.getGeneratedKeys().getInt(1);
			stat.close();
		} catch (SQLException e) {			
			return -1;
		}
		return last;
	}
	
	private ResultSet querySQL(final String sql) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			ResultSet ret = stat.executeQuery(sql);
//			stat.close();
			return ret;
		} catch (SQLException e) {			
			return null;
		}
	}	 

	private void createTables() {
		
		String sql = "CREATE TABLE IF NOT EXISTS dict_info ("
				+ " idx INTEGER PRIMARY KEY,"
				+ " title TEXT,"
				+ " file TEXT,"
				+ " offset INTEGER,"
				+ " d_decoder INTGER,"
				+ " x_decoder INTEGER)";
		execSQL(sql);
		
//		sql = "CREATE TABLE IF NOT EXISTS block_info ("
//				+ " dictid INTEGER,"
//				+ " idx INTEGER,"
//				+ " offset INTEGER,"
//				+ " length INTEGER,"
//				+ " start INTEGER,"
//				+ " end INTEGER)";
//		execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS word_info ("
				+ " idx INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " word TEXT,"
				+ " flag INTEGER)";
		execSQL(sql);
		
//		sql = "CREATE TABLE IF NOT EXISTS word_index ("
//				+ " wordid INTEGER PRIMARY KEY,"
//				+ " dictid INTEGER,"
//				+ " idx INTEGER,"
//				+ " offset INTEGER,"
//				+ " length INTEGER,"
//				+ " block1 INTEGER)";//,"
//				//+ " block2 INTEGER)";
//		execSQL(sql);
				
//		sql = "CREATE TABLE IF NOT EXISTS reference_index ("
//				+ " wordid INTEGER,"
//				+ " dictid INTEGER,"
//				+ " idx INTEGER,"
//				+ " ref_idx INTEGER)";
//		execSQL(sql);
	}
	
	public void createDictionaryTables(int dictid) {
		//block info
		String sql = "CREATE TABLE IF NOT EXISTS block_info_" + dictid + " ("
				+ " idx INTEGER PRIMARY KEY,"
				+ " offset INTEGER,"
				+ " length INTEGER,"
				+ " start INTEGER,"
				+ " end INTEGER)";
		execSQL(sql);
		//word index
		sql = "CREATE TABLE IF NOT EXISTS word_index_" + dictid + " ("
				+ " wordid INTEGER,"
				+ " idx INTEGER,"
				+ " offset INTEGER,"
				+ " length INTEGER,"
				+ " block1 INTEGER)";//,"
				//+ " block2 INTEGER)";
		execSQL(sql);
		
		sql = "CREATE INDEX index_word_index_" + dictid + "_idx ON word_index_" + dictid + " (idx ASC)";
		execSQL(sql);
		//reference index
		sql = "CREATE TABLE IF NOT EXISTS ref_index_" + dictid + " ("
				+ " wordid INTEGER,"
				+ " idx INTEGER,"
				+ " ref_idx INTEGER)";
		execSQL(sql);
		sql = "CREATE INDEX index_ref_index_" + dictid + "_wordid ON ref_index_" + dictid + " (wordid ASC)";		
		execSQL(sql);
		sql = "CREATE INDEX index_ref_index_" + dictid + "_ref_idx ON ref_index_" + dictid + " (ref_idx ASC)";		
		execSQL(sql);		
	}
		
	public void insertBaseInfo(int dictid, final String title, final String file, int offset) {
		String sql = "INSERT INTO dict_info (idx,title,file,offset) VALUES ("
				+ dictid + ",\""
				+ title + "\",\""
				+ file + "\","
				+ offset + ")";
		execSQL(sql);
	}


	public void updateDecoderInfo(int dictid, int indexWordDecoder, int indexXmlDecoder) {
		String sql = "UPDATE dict_info SET d_decoder=" + indexWordDecoder + ",x_decoder=" + indexXmlDecoder
				+ " WHERE idx=" + dictid;
		execSQL(sql);
	}
	
	public void insertBlockInfo(int dictid, final BlockData data) {
		String sql = "INSERT INTO block_info_" + dictid + " VALUES ("
					+ data.index + "," 
					+ data.offset + ","
					+ data.length + ","
					+ data.start + ","
					+ data.end + ")";
		execSQL(sql);
	}

	public int insertWordInfo(int index, String word, int flag) {
		String sql = "INSERT INTO word_info (word, flag) VALUES (\""
				+ word+"\","
				+ flag + ")";
		return execSQLWithReturn(sql);
	}
	
	public void insertWordIndex(int wordid, int dictid, int index, int offset, int length, int block1, int block2) {
		
		if(block2 != -1)
			block1 = -block1;
		
		String sql = "INSERT INTO word_index_" + dictid + " VALUES ("
				+ wordid + ","
				+ index + ","
				+ offset + ","
				+ length + ","
				+ block1 + ")";//,"
				//+ block2 + ")";
		execSQL(sql);
	}
	
//	public void insertReferenceInfo(int index, final String word) {
//		String sql = "INSERT INTO reference_info VALUES ("
//				+ index + ",'"
//				+ word +"')";
//		execSQL(sql);		
//	}
	
	public void insertReferenceIndex(int wordid, int dictid, int index, int refindex) {
		String sql = "INSERT INTO ref_index_" + dictid + " VALUES ("
				+ wordid + ","
				+ index + ","
				+ refindex +")";
		execSQL(sql);	
		
	}
	
	public void close() {
		if(conn != null) {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
 

//	
//	public int getWordData(final WordData data) {
//		
//		String sql = "SELECT word FROM ld2_vicon_word_info WHERE idx=" + data.index;
//		
//		try {
//			Statement stat = null;
//			stat = conn.createStatement();
//			ResultSet rs = stat.executeQuery(sql);
//			if(rs == null || rs.next() == false) {
//				stat.close();
//				return -1;
//			}
//			
//			data.word = rs.getString(1);
//			rs.close();
//				
//			sql = "SELECT offset, length, block1, block2 FROM ld2_vicon_word_index WHERE idx = " + data.index;
//			rs = stat.executeQuery(sql);
//			if(rs != null) {
//				while(rs.next()) {
//					WordIndex wi = new WordIndex();
//					wi.offset = rs.getInt(1);
//					wi.length = rs.getInt(2);
//					wi.block1 = rs.getInt(3);
//					wi.block2 = rs.getInt(4);
//					
//					data.block.add(wi);
//				}
//			}
//			rs.close();
//			stat.close();
//			return 0;
//		} catch (SQLException e) {
//			return -1;
//		}
//	}
//
//	public int getBlockDat(BlockData data) {
//		String sql = "SELECT offset, length, start, end FROM ld2_vicon_block_info WHERE idx = " + data.index;
//		
//		try {
//			Statement stat = conn.createStatement();
//			ResultSet rs = stat.executeQuery(sql);
//			if(rs == null || rs.next() == false) 
//				return -1;
//			
//			data.offset = rs.getInt(1);
//			data.length = rs.getInt(2);
//			data.start = rs.getInt(3);
//			data.end = rs.getInt(4);
//			
//			rs.close();
//			stat.close();		
//		} catch (SQLException e) {
//			return -1;
//		}
//
//		return 0;
//	}

}
