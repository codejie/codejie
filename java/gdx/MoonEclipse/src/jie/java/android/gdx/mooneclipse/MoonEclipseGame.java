/**
 *   File: MoonEclipseGame.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MoonEclipseGame extends Game {

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	
	private Screen screen = null;

	@Override
	public void create() {
		float width = GLOBAL.PORTRAIT_SCREEN_WIDTH;
		float height = GLOBAL.PORTRAIT_SCREEN_HEIGHT;
	
		this.setScreen(new MoonEclipseScreen(this, width, height, true));
	}
	
	public void setScreen(Screen screen) {
		if(this.screen != null && this.screen != screen)
			this.screen.dispose();
		this.screen = null;
		this.screen = screen;
		super.setScreen(this.screen);
	}
}
