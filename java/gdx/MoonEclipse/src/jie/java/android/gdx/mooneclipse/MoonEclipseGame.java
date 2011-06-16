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
	Screen screen = null;
	@Override
	public void create() {
		float width = GLOBAL.PORTRAIT_SCREEN_WIDTH;
		float height = GLOBAL.PORTRAIT_SCREEN_HEIGHT;
/*			
			if(portrait == false) {
				width = GLOBAL.LANDSCAPE_SCREEN_WIDTH;
				height = GLOBAL.LANDSCAPE_SCREEN_HEIGHT;
			}
*/			
	
		this.screen = new MoonEclipseScreen(this, width, height, true);
		
		this.setScreen(screen);
	}
	
	public void setScreen(Screen screen) {
		if(this.screen != null && this.screen != screen)
			this.screen.dispose();
		this.screen = screen;
		super.setScreen(this.screen);
	}

}
