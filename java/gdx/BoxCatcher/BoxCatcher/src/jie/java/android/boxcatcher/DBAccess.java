package jie.java.android.boxcatcher;

import java.sql.*;

import com.badlogic.gdx.Gdx;

public class DBAccess {

	private final int DATABASE_VERSION	=	1;
	private final String DATABASE_FILE	=	"db.db";
	
	private final String TABLE_NAME_BOXES		=	"Boxes";
	private final String TABLE_NAME_STAGES		=	"Stages";
	private final String TABLE_NAME_STAGEBOX	=	"StageBox";
	
	private final String TABLE_COLUMN_INDEX		=	"idx";
	private final String TABLE_COLUMN_NAME		=	"name";
	private final String TABLE_COLUMN_RACE		=	"race";
	private final String TABLE_COLUMN_TYPE		=	"type";
	private final String TABLE_COLUMN_SHAPE		=	"shape";
	private final String TABLE_COLUMN_X			=	"x";
	private final String TABLE_COLUMN_Y			=	"y";
	private final String TABLE_COLUMN_WIDTH		=	"width";
	private final String TABLE_COLUMN_HEIGHT	=	"height";
	private final String TABLE_COLUMN_ANGLE		=	"angle";
	private final String TABLE_COLUMN_DENSITY	=	"density";
	private final String TABLE_COLUMN_RESTITUTION	=	"resti";
	private final String TABLE_COLUMN_FRICTION		=	"fric";
	private final String TABLE_COLUMN_FILTERBITS	=	"filter";
	
	private Connection conn = null;
	
	public DBAccess() {
	}
	
	public int init() {
		try {
			Class.forName("org.sqlite.JDBC");			
			
			createDatabase(DATABASE_FILE);
			
			createTables();
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
	
	private void createTables() throws SQLException {
		
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BOXES + " ("
				+ TABLE_COLUMN_INDEX + " INTEGER PRIMARY KEY,"
				+ TABLE_COLUMN_NAME + " TEXT," 
				+ TABLE_COLUMN_RACE + " INTEGER,"
				+ TABLE_COLUMN_TYPE + " INTEGER,"
				+ TABLE_COLUMN_SHAPE + " INTEGER,"
				+ TABLE_COLUMN_X + " INTEGER,"
				+ TABLE_COLUMN_Y + " INTEGER,"
				+ TABLE_COLUMN_WIDTH + " INTEGER,"
				+ TABLE_COLUMN_HEIGHT + " INTEGER,"
				+ TABLE_COLUMN_ANGLE + " REAL,"
				+ TABLE_COLUMN_DENSITY + " REAL,"
				+ TABLE_COLUMN_RESTITUTION + " REAL,"
				+ TABLE_COLUMN_FRICTION + " REAL,"
				+ TABLE_COLUMN_FILTERBITS + " INTEGER"
				+ ");";

		execSQL(sql);
		
		//sql = 
		
	}

	private void execSQL(String sql) throws SQLException {
        Statement stat = conn.createStatement();
        stat.execute(sql);
	}


	
}
