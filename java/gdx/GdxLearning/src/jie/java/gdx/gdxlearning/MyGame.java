package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.Game;

public class MyGame extends Game {

	@Override
	public void create() {
		this.setScreen(new FontScreen(this));
	}

}
