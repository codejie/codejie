package jie.java.android.boxcatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class BCGame extends Game {

	@Override
	public void create() {
		// TODO Auto-generated method stub
		//this.setScreen(new ImageScreen(this));
		//this.setScreen(new TestWorldScreen(this));
		
		Global.TEXTURE.load();
		
		this.setScreen(new WorldScreen(this, 0));
		
		Gdx.app.log("tag", "BCGame - constructor.");
		
	}
	@Override
	public void setScreen(Screen screen) {
		Screen old = this.getScreen();
		if(old != null && old != screen) {
			old.dispose();
		}
		super.setScreen(screen);
	}


}
