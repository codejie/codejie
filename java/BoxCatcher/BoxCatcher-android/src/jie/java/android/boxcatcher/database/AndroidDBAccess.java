package jie.java.android.boxcatcher.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.StageData.Box;
import jie.java.android.boxcatcher.StageData.Setting;

public class AndroidDBAccess extends DBAccess {

	private static SQLiteDatabase db = null;
	
	@Override
	public int init() {
		
		if(createDatabase(DATABASE_FILE) != 0)
			return -1;
		
		if(createTables() != 0)
			return -1;
		
		return 0;
	}

	@Override
	public void dispose() {
		if(db != null) {
			db.close();
		}
	}

	@Override
	protected int createDatabase(String file) {
		try {
			db = SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE, null);
			
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
		try {
			
			Cursor cursor = db.query(TABLE_NAME_STAGES, new String[] { TABLE_COLUMN_TITLE,TABLE_COLUMN_MAXTIME, TABLE_COLUMN_GRAVITY_X, TABLE_COLUMN_GRAVITY_Y} , TABLE_COLUMN_INDEX + "=" + id, null, null, null, null);
			if(cursor == null) 
			
		} catch (SQLiteException e) {
			Gdx.app.log(Global.APP_TAG, "dbaccess exception - " + e.toString());
			return -1;
		}
		
		return 0;
	}

	@Override
	public int loadFrames(int id, ArrayList<Box> frames) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int loadBoxes(int id, HashMap<Integer, ArrayList<Box>> boxes) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isStageExist(int id) {
		// TODO Auto-generated method stub
		return false;
	}


}
