package jie.java.android.boxcatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class BCGame extends Game {

	@Override
	public void create() {
		// TODO Auto-generated method stub
		this.setScreen(new TestScreen(this));
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
