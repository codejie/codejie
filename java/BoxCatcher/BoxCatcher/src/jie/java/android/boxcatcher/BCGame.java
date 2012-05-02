package jie.java.android.boxcatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import jie.java.android.boxcatcher.database.DBAccess;

public class BCGame extends Game {

	private DBAccess dbAccess = null;
	private MaterialManager materialManager = null;
	
	private static Screen screen = null;
	
	private int stageID = 1;
	
	public BCGame(DBAccess dbaccess) {
		dbAccess = dbaccess;
	}
	
	@Override
	public void create() {
		
		init();
			
		this.setScreen(new WorldScreen(this, stageID));
		//setScreen(new ShowStageScreen(this));
		
		Gdx.app.log(Global.APP_TAG, "BCGame - constructor.");
				
	}
	
	public int init() {
//		dbAccess = new DBAccess();
		dbAccess.init();
		
		materialManager = new MaterialManager();
		materialManager.load();
		
		return 0;
	}
	
	@Override
	public void dispose() {
		if(dbAccess != null) {
			dbAccess.dispose();
		}
		
		if(materialManager != null) {
			materialManager.dispose();
		}
		super.dispose();
	}
	
	public DBAccess getDBAccess() {
		return dbAccess;
	}
	
	public MaterialManager getMaterialManager() {
		return materialManager;
	}
	
	
	@Override
	public void setScreen(Screen screen) {
		if(BCGame.screen != null && BCGame.screen != screen) {
			BCGame.screen.dispose();
		}
		
		BCGame.screen = screen;
		if(BCGame.screen != null) {
			super.setScreen(BCGame.screen);
		}
	}

	public static WorldScreen getWorldScreen() {
		if(BCGame.screen.getClass() == Screen.class) {
			return (WorldScreen)BCGame.screen;
		}
		return null;
	}
	
	public int nextScreen() {
		++ stageID;
		if(dbAccess.isStageExist(stageID)) {
			setScreen(new WorldScreen(this, stageID));
		}
		else {
			setScreen(new ShowStageScreen(this));
		}
		return 0;
	}
}
