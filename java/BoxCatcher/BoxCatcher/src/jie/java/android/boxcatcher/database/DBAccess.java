package jie.java.android.boxcatcher.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jie.java.android.boxcatcher.StageData;
import jie.java.android.boxcatcher.BoxActor.BoxShape;
import jie.java.android.boxcatcher.StageData.BoxRace;

public abstract class DBAccess {
	protected final int DATABASE_VERSION	=	1;
	protected final String DATABASE_FILE	=	"db.db";
	
	protected final String TABLE_NAME_BOXES		=	"Boxes";
	protected final String TABLE_NAME_STAGES		=	"Stages";
	protected final String TABLE_NAME_STAGEBOX	=	"StageBox";
	
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
	
	protected int createTables() {
		
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
	
	public abstract int init();
	public abstract void dispose();
	
	protected abstract int createDatabase(String file);
	protected abstract int execSQL(String sql);
	
	public abstract int loadSetting(int id, StageData.Setting setting);
	public abstract int loadFrames(int id, ArrayList<StageData.Box> frames);
	public abstract int loadBoxes(int id, HashMap<Integer, ArrayList<StageData.Box>> boxes);
	public abstract boolean isStageExist(int id);
	
}
