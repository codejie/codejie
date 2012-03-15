package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MyGame extends Game {

	private Screen screen = null;
	@Override
	public void create() {
		this.setScreen(new FontScreen(this));
	}

	@Override
	public void setScreen(Screen screen) {
		if(this.screen != null && this.screen != screen) {
			this.screen.dispose();
		}
		this.screen = screen;

		super.setScreen(this.screen);
	}

}
