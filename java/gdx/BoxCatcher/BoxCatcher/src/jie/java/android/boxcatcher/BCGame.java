package jie.java.android.boxcatcher;

import com.badlogic.gdx.Game;

public class BCGame extends Game {

	@Override
	public void create() {
		// TODO Auto-generated method stub
		this.setScreen(new BCScreen(this, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, true));
	}

}
