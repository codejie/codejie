package jie.java.android.boxcatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class BCGame extends Game {

	private MaterialManager materialManager = null;
	
	private static Screen screen = null;
	@Override
	public void create() {
		
		init();
		
		//Global.TEXTURE.load();
		
		this.setScreen(new WorldScreen(this, 0));
		
		Gdx.app.log("tag", "BCGame - constructor.");
				
	}
	
	public int init() {
		materialManager = new MaterialManager();
		materialManager.load();
		
		return 0;
	}
	
	@Override
	public void dispose() {
		if(materialManager != null) {
			materialManager.dispose();
		}
		super.dispose();
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
		
		super.setScreen(BCGame.screen);
	}

	public static WorldScreen getWorldScreen() {
		if(BCGame.screen.getClass() == Screen.class) {
			return (WorldScreen)BCGame.screen;
		}
		return null;
	}

}
