package jie.java.ld2scaner;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import jie.java.ld2scaner.FileScan.BlockData;

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

}
