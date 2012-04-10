package jie.java.android.boxcatcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import jie.java.android.boxcatcher.BoxActor.BoxShape;
import jie.java.android.boxcatcher.StageData.BoxRace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class DBAccess {

	private final int DATABASE_VERSION	=	1;
	private final String DATABASE_FILE	=	"data/db.db";
	
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
	private final String TABLE_COLUMN_RESTITUTION	=	"restitution";
	private final String TABLE_COLUMN_FRICTION		=	"friction";
	private final String TABLE_COLUMN_FILTERBITS	=	"filterbits";
	private final String TABLE_COLUMN_TEXTUREINDEX	=	"textureidx";
	private final String TABLE_COLUMN_ANIMATIONINDEX	=	"animationidx";
	
	private final String TABLE_COLUMN_TITLE		=	"title";
	private final String TABLE_COLUMN_MAXTIME	=	"maxtime";
	private final String TABLE_COLUMN_GRAVITY_X	=	"gx";
	private final String TABLE_COLUMN_GRAVITY_Y	=	"gy";
	
	private final String TABLE_COLUMN_STAGEINDEX 	=	"stageidx";
	private final String TABLE_COLUMN_BOXINDEX 		=	"boxidx";
	private final String TABLE_COLUMN_PRESENTTIME	=	"pt";
	
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
//				+ TABLE_COLUMN_X + " INTEGER,"
//				+ TABLE_COLUMN_Y + " INTEGER,"
				+ TABLE_COLUMN_WIDTH + " INTEGER,"
				+ TABLE_COLUMN_HEIGHT + " INTEGER,"
				+ TABLE_COLUMN_ANGLE + " REAL,"
				+ TABLE_COLUMN_DENSITY + " REAL,"
				+ TABLE_COLUMN_RESTITUTION + " REAL,"
				+ TABLE_COLUMN_FRICTION + " REAL,"
				+ TABLE_COLUMN_FILTERBITS + " INTEGER,"
				+ TABLE_COLUMN_TEXTUREINDEX + " INTEGER,"
				+ TABLE_COLUMN_ANIMATIONINDEX + " INTEGER"
				+ ");";
		execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STAGES + " ("
				+ TABLE_COLUMN_INDEX + " INTEGER PRIMARY KEY,"
				+ TABLE_COLUMN_TITLE + " TEXT,"
				+ TABLE_COLUMN_MAXTIME + " INTEGER,"
				+ TABLE_COLUMN_GRAVITY_X + " REAL,"
				+ TABLE_COLUMN_GRAVITY_Y + " REAL"
				+ ");";
		execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STAGEBOX + " ("
				+ TABLE_COLUMN_STAGEINDEX + " INTEGER,"
				+ TABLE_COLUMN_BOXINDEX + " INTEGER,"
				+ TABLE_COLUMN_X + " INTEGER,"
				+ TABLE_COLUMN_Y + " INTEGER,"
				+ TABLE_COLUMN_PRESENTTIME + " INTEGER"
				+ ");";
		execSQL(sql);
		
	}

	private void execSQL(String sql) throws SQLException {
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
	}

	private ResultSet querySQL(String sql) throws SQLException {
		Statement stat = conn.createStatement();
		return stat.executeQuery(sql);
	}
	
	public int loadSetting(int id, StageData.Setting setting) {
		String sql = "SELECT " + TABLE_COLUMN_TITLE + "," + TABLE_COLUMN_MAXTIME + "," + TABLE_COLUMN_GRAVITY_X + "," + TABLE_COLUMN_GRAVITY_Y + " FROM " + TABLE_NAME_STAGES + " WHERE " + TABLE_COLUMN_INDEX + "=" + id;
		try {
			ResultSet res = querySQL(sql);
			if(res.next()) {
				setting.title = res.getString(1);
				setting.maxStateTime = res.getInt(2);
				setting.gravity = new Vector2(res.getFloat(3), res.getFloat(4));
				
				res.close();
			}
			else {
				res.close();
				return -1;
			}
		} catch (SQLException e) {
			Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	public int loadFrames(int id, ArrayList<StageData.Box> frames) {
		//SELECT StageBox.x, StageBox.y, Boxes.name FROM StageBox, Boxes WHERE StageBox.boxidx = Boxes.idx AND StageBox.stageidx=1
		String sql = "SELECT "// + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_INDEX + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_RACE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_TYPE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_SHAPE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_WIDTH + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_HEIGHT + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_ANGLE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_DENSITY + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_RESTITUTION + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_FRICTION + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_FILTERBITS + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_TEXTUREINDEX + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_ANIMATIONINDEX + ","
				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_X + ","
				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_Y// + ","
//				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_PRESENTTIME
				 + " FROM " + TABLE_NAME_BOXES + "," + TABLE_NAME_STAGEBOX
				 + " WHERE " + TABLE_NAME_BOXES + "." + TABLE_COLUMN_INDEX + "=" + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_BOXINDEX
				 + " AND " + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_STAGEINDEX + "=" + id
				 + " AND " + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_PRESENTTIME + "= -1";
		
		try {
			ResultSet res = querySQL(sql);
			
			while(res.next()) {
				StageData.Box box = new StageData.Box();
				box.id = res.getInt(1);
				box.name = res.getString(2);
				box.race = covertBoxRace(res.getInt(3));
				box.type = covertBoxType(res.getInt(4));
				box.shape = convertBoxShape(res.getInt(5));
				box.width = res.getInt(6);
				box.height = res.getInt(7);
				box.angle = res.getFloat(8);
				box.density = res.getFloat(9);
				box.restitution = res.getFloat(10);
				box.friction = res.getFloat(11);
				box.filterBits = (short)res.getInt(12);
				box.texture = res.getInt(13);
				box.animation = res.getInt(14);
				box.x = res.getInt(15);
				box.y = res.getInt(16);
				
				frames.add(box);
			}
			res.close();
			
		} catch (SQLException e) {
			Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
	}
	

	public int loadBoxes(int id, HashMap<Integer, ArrayList<StageData.Box>> boxes) {
		String sql = "SELECT "// + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_INDEX + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_RACE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_TYPE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_SHAPE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_WIDTH + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_HEIGHT + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_ANGLE + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_DENSITY + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_RESTITUTION + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_FRICTION + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_FILTERBITS + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_TEXTUREINDEX + ","
				 + TABLE_NAME_BOXES + "." + TABLE_COLUMN_ANIMATIONINDEX + ","
				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_X + ","
				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_Y + ","
				 + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_PRESENTTIME
				 + " FROM " + TABLE_NAME_BOXES + "," + TABLE_NAME_STAGEBOX
				 + " WHERE " + TABLE_NAME_BOXES + "." + TABLE_COLUMN_INDEX + "=" + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_BOXINDEX
				 + " AND " + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_STAGEINDEX + "=" + id
				 + " AND " + TABLE_NAME_STAGEBOX + "." + TABLE_COLUMN_PRESENTTIME + "!= -1";
		
		try {
			ResultSet res = querySQL(sql);
			
			while(res.next()) {
				StageData.Box box = new StageData.Box();
				box.id = res.getInt(1);
				box.name = res.getString(2);
				box.race = covertBoxRace(res.getInt(3));
				box.type = covertBoxType(res.getInt(4));
				box.shape = convertBoxShape(res.getInt(5));
				box.width = res.getInt(6);
				box.height = res.getInt(7);
				box.angle = res.getFloat(8);
				box.density = res.getFloat(9);
				box.restitution = res.getFloat(10);
				box.friction = res.getFloat(11);
				box.filterBits = (short)res.getInt(12);
				box.texture = res.getInt(13);
				box.animation = res.getInt(14);
				box.x = res.getInt(15);
				box.y = res.getInt(16);
				
				ArrayList<StageData.Box> array = boxes.get(res.getInt(17));
				if(array != null) {
					array.add(box);
				}
				else {
					array = new ArrayList<StageData.Box>();
					array.add(box);
					boxes.put(res.getInt(17), array);
				}
			}
			res.close();
			
		} catch (SQLException e) {
			Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
		
	}	

	private BoxShape convertBoxShape(int shape) {
		switch(shape) {
		case 0:
			return BoxShape.RECTANGLE;
		case 1:
			return BoxShape.CIRCLE;
		case 2:
			return BoxShape.TRIANGLE;
		case 3:
			return BoxShape.LINE;
		case 4:
			return BoxShape.RIGHT_TRIANGLE;
		default:
			return BoxShape.RECTANGLE;
		}
	}

	private BodyType covertBoxType(int type) {
		switch(type) {
		case 0:
			return BodyType.StaticBody;
		case 1:
			return BodyType.KinematicBody;
		case 2:
			return BodyType.DynamicBody;
		default:
			return BodyType.StaticBody;
		}
	}

	private BoxRace covertBoxRace(int race) {
		switch(race) {
		case 0:
			return StageData.BoxRace.UNKNOWN;
		case 1:
			return StageData.BoxRace.BOX;
		case 2:
			return StageData.BoxRace.DOCK;
		case 3:
			return StageData.BoxRace.BORDER;
		case 4:
			return StageData.BoxRace.DEADLINE;
		default:
			return StageData.BoxRace.UNKNOWN;			
		}
	}

	public boolean isStageExist(int id) {
		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME_STAGES + " WHERE " + TABLE_COLUMN_INDEX + "=" + id;
		
		try {
			ResultSet res = querySQL(sql);
			if(res.next()) {
				if(res.getInt(1) == 0)
					return false;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			Gdx.app.log("tag", "dbaccess exception - " + e.toString());
			return false;
		}
		
		return true;
	}



	
}
