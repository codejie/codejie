package jie.java.android.boxcatcher;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class BoxCatcherDesktop {
	public static void main(String[] args) {
		new JoglApplication(new BCGame(), "Box Catcher ! by Jie.", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, false);
	}
}
