package com.jie.android.gdx.demo;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DemoDraftDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new JoglApplication(new DemoDraft(), "GDX-DemoDraft", (int)GLOBAL.SCREEN_WIDTH, (int)GLOBAL.SCREEN_HEIGHT, false);
	}

}
