/**
 * project:		DemoDraft
 * file:		TOOLKIT.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 25, 2011
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.math.Vector2;

public final class TOOLKIT {
	
	public static Vector2 Screen2World(final Vector2 vct) {
		return new Vector2(vct.x / GLOBAL.WORLD_SCALE, vct.y / GLOBAL.WORLD_SCALE);
	}

	public static Vector2 Screen2World(final float x, final float y) {
		return new Vector2(x / GLOBAL.WORLD_SCALE, y / GLOBAL.WORLD_SCALE);
	}
	
	public static float Screen2World(final float v) {
		return v / GLOBAL.WORLD_SCALE;
	}
	
	public static Vector2 getWorldBoxCenter(final Vector2 top, final Vector2 size) {
		return new Vector2(top.x + size.x / 2, top.y + size.y / 2);
	}
	
	public static Vector2 Frame2Screen(final int x, final int y) {
		return new Vector2(x, GLOBAL.SCREEN_HEIGHT - y);
	}
}
