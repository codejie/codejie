package jie.java.android.boxcatcher;

import java.sql.*;

import com.badlogic.gdx.Gdx;

public class DBAccess {

	private final int DATABASE_VERSION	=	1;
	private final String DATABASE_FILE	=	"db.db";
	
	private final String TABLE_NAME_BOXES		=	"Boxes";
	
	private final String TABLE_COLUMN_INDEX		=	"id";
	
	private Connection conn = null;
	
	public DBAccess() {
	}
	
	public int init() {
		try {
			Class.forName("org.sqlite.JDBC");			
			
			createDatabase(DATABASE_FILE);
			
			createTable(TABLE_NAME_BOXES);
		}
		catch (SQLException e) {
			Gdx.app.log("tag", "dbaccess sql exception - " + e.toString());
			return -1;
		}
		catch (Exception e) {
			Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	public void dispose() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			}
		}
	}
	
	private void createDatabase(String file) throws SQLException {
		conn = DriverManager.getConnection("jdbc:sqlite:" + file);
	}	
	
	private void createTable(String name) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("CREATE TABLE IF NOT EXISTS " + name + " ("
				+ TABLE_COLUMN_INDEX + " INTEGER PRIMARY KEY"
				+ ");");

	}


	
}
