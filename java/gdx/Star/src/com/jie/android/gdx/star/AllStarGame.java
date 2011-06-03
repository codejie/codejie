/**
 * file   : AllStarGame.java
 * author : codejie (codejie@gmail.com)
 * date   : Jun 3, 2011 5:35:09 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class AllStarGame extends Game {

	Screen screen = null;
	@Override
	public void create() {
		this.screen = new RomanticStarScreen(this);
		
		this.setScree(screen);
	}
	
	public void setScree(Screen screen) {
		if(this.screen != null && this.screen != screen)
			this.screen.dispose();

		this.screen = screen;
		super.setScreen(this.screen);
	}

}
