package jie.java.android.boxcatcher.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.StageData;
import jie.java.android.boxcatcher.BoxActor.BoxShape;
import jie.java.android.boxcatcher.StageData.BoxRace;

public abstract class DBAccess {
	protected final int DATABASE_VERSION	=	1;
	protected final String DATABASE_FILE	=	"db.db";
	
	protected final String TABLE_NAME_SYSTEM	=	"System";
	protected final String TABLE_NAME_BOXES		=	"Boxes";
	protected final String TABLE_NAME_STAGES	=	"Stages";
	protected final String TABLE_NAME_STAGEBOX	=	"StageBox";
	
	protected final String TABLE_COLUMN_ATTRIB		=	"attr";
	protected final String TABLE_COLUMN_VALUE_INT	=	"val_int";
	protected final String TABLE_COLUMN_VALUE_STR	=	"val_str";
	
	protected final String TABLE_COLUMN_INDEX		=	"idx";
	protected final String TABLE_COLUMN_NAME		=	"name";
	protected final String TABLE_COLUMN_RACE		=	"race";
	protected final String TABLE_COLUMN_TYPE		=	"type";
	protected final String TABLE_COLUMN_SHAPE		=	"shape";
	protected final String TABLE_COLUMN_X			=	"x";
	protected final String TABLE_COLUMN_Y			=	"y";
	protected final String TABLE_COLUMN_WIDTH		=	"width";
	protected final String TABLE_COLUMN_HEIGHT		=	"height";
	protected final String TABLE_COLUMN_ANGLE		=	"angle";
	protected final String TABLE_COLUMN_DENSITY		=	"density";
	protected final String TABLE_COLUMN_RESTITUTION	=	"restitution";
	protected final String TABLE_COLUMN_FRICTION	=	"friction";
	protected final String TABLE_COLUMN_FILTERBITS	=	"filterbits";
	protected final String TABLE_COLUMN_TEXTUREINDEX	=	"textureidx";
	protected final String TABLE_COLUMN_ANIMATIONINDEX	=	"animationidx";
	
	protected final String TABLE_COLUMN_TITLE		=	"title";
	protected final String TABLE_COLUMN_MAXTIME		=	"maxtime";
	protected final String TABLE_COLUMN_GRAVITY_X	=	"gx";
	protected final String TABLE_COLUMN_GRAVITY_Y	=	"gy";
	
	protected final String TABLE_COLUMN_STAGEINDEX 	=	"stageidx";
	protected final String TABLE_COLUMN_BOXINDEX 	=	"boxidx";
	protected final String TABLE_COLUMN_PRESENTTIME	=	"pt";
	
	public DBAccess() {
	}
	
	public int init() {
		if(createDatabase(DATABASE_FILE) != 0)
			return -1;
		
		if(createTables() != 0)
			return -1;
		
		if(initSystemSetting() != 0)
			return -1;
		
		return 0;
	}	
	
	
	protected int createTables() {
		
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SYSTEM + " ("
				+ TABLE_COLUMN_ATTRIB + " INTEGER,"
				+ TABLE_COLUMN_VALUE_INT + " INTEGER,"
				+ TABLE_COLUMN_VALUE_STR + " TEXT"
				+ ")";
		if(execSQL(sql) != 0)
			return -1;
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BOXES + " ("
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
		if(execSQL(sql) != 0)
			return -1;
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STAGES + " ("
				+ TABLE_COLUMN_INDEX + " INTEGER PRIMARY KEY,"
				+ TABLE_COLUMN_TITLE + " TEXT,"
				+ TABLE_COLUMN_MAXTIME + " INTEGER,"
				+ TABLE_COLUMN_GRAVITY_X + " REAL,"
				+ TABLE_COLUMN_GRAVITY_Y + " REAL"
				+ ");";
		if(execSQL(sql) != 0)
			return -1;
		
		sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STAGEBOX + " ("
				+ TABLE_COLUMN_STAGEINDEX + " INTEGER,"
				+ TABLE_COLUMN_BOXINDEX + " INTEGER,"
				+ TABLE_COLUMN_X + " INTEGER,"
				+ TABLE_COLUMN_Y + " INTEGER,"
				+ TABLE_COLUMN_PRESENTTIME + " INTEGER"
				+ ");";
		if(execSQL(sql) != 0)
			return -1;
		return 0;
	}
	
	protected String getInitSysSettingSql() {
		return "SELECT " + TABLE_COLUMN_ATTRIB + "," + TABLE_COLUMN_VALUE_INT + "," + TABLE_COLUMN_VALUE_STR + " FROM " + TABLE_NAME_SYSTEM;
	}
	
	protected String getLoadSettingSql(int id) {
		return "SELECT " + TABLE_COLUMN_TITLE + "," + TABLE_COLUMN_MAXTIME + "," + TABLE_COLUMN_GRAVITY_X + "," + TABLE_COLUMN_GRAVITY_Y + " FROM " + TABLE_NAME_STAGES + " WHERE " + TABLE_COLUMN_INDEX + "=" + id;
	}
	
	protected String getLoadFramesSql(int id) {
		//SELECT StageBox.x, StageBox.y, Boxes.name FROM StageBox, Boxes WHERE StageBox.boxidx = Boxes.idx AND StageBox.stageidx=1		
		return "SELECT "// + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
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
	}
	
	protected String getLoadBoxesSql(int id) {
		return "SELECT "// + TABLE_NAME_BOXES + "." + TABLE_COLUMN_NAME + ","
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
	}
	
	protected String getIsStageExistSql(int id) {
		return "SELECT COUNT(*) FROM " + TABLE_NAME_STAGES + " WHERE " + TABLE_COLUMN_INDEX + "=" + id;
	}
	
	protected BoxShape convertBoxShape(int shape) {
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

	protected BodyType covertBoxType(int type) {
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

	protected BoxRace covertBoxRace(int race) {
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
	
	public abstract void dispose();
	
	protected abstract int createDatabase(String file);
	protected abstract int execSQL(String sql);
	protected abstract int initSystemSetting();
	
	public abstract int loadSetting(int id, StageData.Setting setting);
	public abstract int loadFrames(int id, ArrayList<StageData.Box> frames);
	public abstract int loadBoxes(int id, HashMap<Integer, ArrayList<StageData.Box>> boxes);
	public abstract boolean isStageExist(int id);
	
}
