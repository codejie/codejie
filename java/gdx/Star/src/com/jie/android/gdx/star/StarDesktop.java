/**
 * file:   StarDesktop.java
 * author: codejie (codejie@gmail.com)
 * date:   Jun 2, 2011 11:30:34 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class StarDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new JoglApplication(new StarGame(), "Star !", 480, 800, false);
	}

}
