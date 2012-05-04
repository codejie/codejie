package jie.java.android.boxcatcher.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.StageData;
import jie.java.android.boxcatcher.StageData.Box;
import jie.java.android.boxcatcher.StageData.Setting;

public class AndroidDBAccess extends DBAccess {

	private static SQLiteDatabase db = null;
	
	@Override
	public void dispose() {
		if(db != null) {
			db.close();
		}
	}

	@Override
	protected int createDatabase(String file) {
		try {
			db = SQLiteDatabase.openOrCreateDatabase("/data/data/jie.java.android.boxcatcher/" + DATABASE_FILE, null);
			
			Gdx.app.log(Global.APP_TAG, "db initial - " + db.getMaximumSize());
		}
		catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	@Override
	protected int execSQL(String sql) {
		if(db == null)
			return -1;
		db.execSQL(sql);
		return 0;
	}

	@Override
	public int loadSetting(int id, Setting setting) {
		String sql = getLoadSettingSql(id);
		try {
			Cursor cursor = db.rawQuery(sql, null);// .query(TABLE_NAME_STAGES, new String[] { TABLE_COLUMN_TITLE,TABLE_COLUMN_MAXTIME, TABLE_COLUMN_GRAVITY_X, TABLE_COLUMN_GRAVITY_Y} , TABLE_COLUMN_INDEX + "=" + id, null, null, null, null);
			if(cursor == null || cursor.getCount() == 0) 
				return -1;
			cursor.moveToFirst();
			setting.title = cursor.getString(0);
			setting.maxStateTime = cursor.getInt(1);
			setting.gravity = new Vector2(cursor.getFloat(2), cursor.getFloat(3));
			cursor.close();
			
		} catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
	}

	@Override
	public int loadFrames(int id, ArrayList<Box> frames) {
		String sql = getLoadFramesSql(id);
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null || cursor.getCount() == 0)
				return -1;
			while(cursor.moveToNext()) {
				StageData.Box box = new StageData.Box();
				box.id = cursor.getInt(0);
				box.name = cursor.getString(1);
				box.race = covertBoxRace(cursor.getInt(2));
				box.type = covertBoxType(cursor.getInt(3));
				box.shape = convertBoxShape(cursor.getInt(4));
				box.width = cursor.getInt(5);
				box.height = cursor.getInt(6);
				box.angle = cursor.getFloat(7);
				box.density = cursor.getFloat(8);
				box.restitution = cursor.getFloat(9);
				box.friction = cursor.getFloat(10);
				box.filterBits = (short)cursor.getInt(11);
				box.texture = cursor.getInt(12);
				box.animation = cursor.getInt(13);
				box.x = cursor.getInt(14);
				box.y = cursor.getInt(15);
				
				frames.add(box);				
			}
			cursor.close();			
			
		} catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}

	@Override
	public int loadBoxes(int id, HashMap<Integer, ArrayList<Box>> boxes) {
		String sql = getLoadBoxesSql(id);
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null || cursor.getCount() == 0)
				return -1;
			while(cursor.moveToNext()) {
				StageData.Box box = new StageData.Box();
				box.id = cursor.getInt(0);
				box.name = cursor.getString(1);
				box.race = covertBoxRace(cursor.getInt(2));
				box.type = covertBoxType(cursor.getInt(3));
				box.shape = convertBoxShape(cursor.getInt(4));
				box.width = cursor.getInt(5);
				box.height = cursor.getInt(6);
				box.angle = cursor.getFloat(7);
				box.density = cursor.getFloat(8);
				box.restitution = cursor.getFloat(9);
				box.friction = cursor.getFloat(10);
				box.filterBits = (short)cursor.getInt(11);
				box.texture = cursor.getInt(12);
				box.animation = cursor.getInt(13);
				box.x = cursor.getInt(14);
				box.y = cursor.getInt(15);
				
				ArrayList<StageData.Box> array = boxes.get(cursor.getInt(16));
				if(array != null) {
					array.add(box);
				}
				else {
					array = new ArrayList<StageData.Box>();
					array.add(box);
					boxes.put(cursor.getInt(16), array);
				}				
			}
			cursor.close();
			
		} catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		return 0;
	}
	@Override
	public boolean isStageExist(int id) {
		String sql = getIsStageExistSql(id);
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor == null || cursor.getCount() == 0)
				return false;
			if(cursor.getInt(0) == 0) {
				cursor.close();
				return false;
			}
			else {
				cursor.close();
				return true;
			}
		} catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return false;
		}
	}


}
