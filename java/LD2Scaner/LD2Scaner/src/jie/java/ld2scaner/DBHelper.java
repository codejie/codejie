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
			// TODO Auto-generated catch block
			return -1;
		}
		return 0;
	}

	private void createTables() {
		String sql = "CREATE TABLE IF NOT EXISTS ld2_vicon_block_index (" 
				+ "index INTEGER PRIMARY KEY,"
				+ "offset INTEGER,"
				+ "length INTEGER,"
				+ "start INTEGER,"
				+ "end INTEGER)";
		
		execSQL(sql);

	}

	public static DBHelper create(final String dbfile) {
		
		return new DBHelper(dbfile);
		
	}
	
	public void insertBaseInfo() {
		// TODO Auto-generated method stub
		
	}

	public void insertBlockInfo(final BlockData data) {
		// TODO Auto-generated method stub
		
	}

	public void insertWordInfo(int index, String word) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertWordIndex(int index, int offset, int length, int block1, int block2) {
		// TODO Auto-generated method stub
		
	}

}
