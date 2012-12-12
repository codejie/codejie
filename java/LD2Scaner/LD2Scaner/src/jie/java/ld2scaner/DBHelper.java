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
	
	private int execSQL(final String sql) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		
			stat.close();
		} catch (SQLException e) {			
			return -1;
		}
		return 0;
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
		String sql = "CREATE TABLE IF NOT EXISTS ld2_vicon_block_info (" 
				+ "idx INTEGER PRIMARY KEY,"
				+ "offset INTEGER,"
				+ "length INTEGER,"
				+ "start INTEGER,"
				+ "end INTEGER)";
		
		execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS ld2_vicon_word_index ("
				+ "idx INTEGER,"
				+ "offset INTEGER,"
				+ "length INTEGER,"
				+ "block1 INTEGER,"
				+ "block2 INTEGER)";
		execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS ld2_vicon_word_info ("
				+ "word TEXT PRIMARY KEY,"
				+ "idx INTEGER)";
		execSQL(sql);
	}

	public static DBHelper create(final String dbfile) {
		
		return new DBHelper(dbfile);
		
	}
	
	public void insertBaseInfo() {
		// TODO Auto-generated method stub
		
	}

	public void insertBlockInfo(final BlockData data) {
		String sql = "INSERT INTO ld2_vicon_block_info VALUES ("
					+ data.index + "," 
					+ data.offset + ","
					+ data.length + ","
					+ data.start + ","
					+ data.end + ")";
		execSQL(sql);
	}

	public void insertWordInfo(int index, String word) {
		String sql = "INSERT INTO ld2_vicon_word_info VALUES ('"
				+ word + "',"
				+ index +")";
		execSQL(sql);
	}
	
	public void insertWordIndex(int index, int offset, int length, int block1, int block2) {
		String sql = "INSERT INTO ld2_vicon_word_index VALUES ("
				+ index + ","
				+ offset + ","
				+ length + ","
				+ block1 + ","
				+ block2 + ")";
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

	public int getWordData(final WordData data) {
		
		String sql = "SELECT word FROM ld2_vicon_word_info WHERE idx=" + data.index;
		
		try {
			Statement stat = null;
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if(rs == null || rs.next() == false) {
				stat.close();
				return -1;
			}
			
			data.word = rs.getString(1);
			rs.close();
				
			sql = "SELECT offset, length, block1, block2 FROM ld2_vicon_word_index WHERE idx = " + data.index;
			rs = stat.executeQuery(sql);
			if(rs != null) {
				while(rs.next()) {
					WordIndex wi = new WordIndex();
					wi.offset = rs.getInt(1);
					wi.length = rs.getInt(2);
					wi.block1 = rs.getInt(3);
					wi.block2 = rs.getInt(4);
					
					data.block.add(wi);
				}
			}
			rs.close();
			stat.close();
			return 0;
		} catch (SQLException e) {
			return -1;
		}
	}

	public int getBlockDat(BlockData data) {
		String sql = "SELECT offset, length, start, end FROM ld2_vicon_block_info WHERE idx = " + data.index;
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if(rs == null || rs.next() == false) 
				return -1;
			
			data.offset = rs.getInt(1);
			data.length = rs.getInt(2);
			data.start = rs.getInt(3);
			data.end = rs.getInt(4);
			
			rs.close();
			stat.close();		
		} catch (SQLException e) {
			return -1;
		}

		return 0;
	}

}
