package jie.java.android.boxcatcher.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.StageData;
import jie.java.android.boxcatcher.StageData.Box;
import jie.java.android.boxcatcher.StageData.Setting;

public class DesktopDBAccess extends DBAccess {

	private Connection conn = null;
	
	@Override
	public int init() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}			
		
		if(createDatabase(DATABASE_FILE) != 0)
			return -1;
		
		if(createTables() != 0)
			return -1;
		return 0;
	}

	@Override
	public void dispose() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			}
		}
	}

	@Override
	protected int createDatabase(String file) {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + file);
		} catch (SQLException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess sql exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	@Override
	protected int execSQL(String sql) {
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess sql exception - " + e.toString());
			return -1;
		}
		return 0;
	}
	
	private ResultSet querySQL(String sql) throws SQLException {
		Statement stat = conn.createStatement();
		return stat.executeQuery(sql);
	}

	@Override
	public int loadSetting(int id, Setting setting) {
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
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	@Override
	public int loadFrames(int id, ArrayList<Box> frames) {
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
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
	}

	@Override
	public int loadBoxes(int id, HashMap<Integer, ArrayList<Box>> boxes) {
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
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
	}

	@Override
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
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return false;
		}
		
		return true;
	}

}
