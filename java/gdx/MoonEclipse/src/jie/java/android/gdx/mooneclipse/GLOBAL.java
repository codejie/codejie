/**
 *   File: GLOBAL.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */
package jie.java.android.gdx.mooneclipse;

import java.util.Vector;

public class GLOBAL {

	public static int LANDSCAPE_SCREEN_WIDTH		=	800;
	public static int LANDSCAPE_SCREEN_HEIGHT		=	480;
	
	public static int PORTRAIT_SCREEN_WIDTH			=	LANDSCAPE_SCREEN_HEIGHT;
	public static int PORTRAIT_SCREEN_HEIGHT		=	LANDSCAPE_SCREEN_WIDTH;

	public static int currentFrame = 0;
	public static float delta = 0.0f;
	public static float speed = 0.1f;
	public static float scale = 1.2f;
	public static boolean isPause = false;
	public static boolean isShift = false;
	
	public static boolean isMini = false;
	
	public static class StarData {
		public boolean isStar = true;
		public float x = 0.0f, y = 0.0f;
		public float scale = 1.0f;
		public float rotate = 0.0f;
		public boolean needFall = false;
	}
	public static Vector<StarData> starList = new Vector<StarData>();
	
}
