package jie.java.android.boxcatcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BoxCatcherDesktop {
	public static void main(String[] args) {
		new LwjglApplication(new BCGame(), "Box Catcher ! by Jie.", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, false);
	}
}
