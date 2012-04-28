package jie.java.android.boxcatcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BoxCatcher";
		cfg.useGL20 = true;
		cfg.width = Global.SCREEN_WIDTH;
		cfg.height = Global.SCREEN_HEIGHT;
		
		new LwjglApplication(new BCGame(), cfg);
	}
}
