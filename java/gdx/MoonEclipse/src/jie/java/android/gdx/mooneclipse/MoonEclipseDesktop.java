/**
 *   File: MoonEclipseDesktop.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class MoonEclipseDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JoglApplication(new MoonEclipseGame(), "Moon Eclipse on June 16, 2011 !", GLOBAL.PORTRAIT_SCREEN_WIDTH, GLOBAL.PORTRAIT_SCREEN_HEIGHT, false);
	}

}
