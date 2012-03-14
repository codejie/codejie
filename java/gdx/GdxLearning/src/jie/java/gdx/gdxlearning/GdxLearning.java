package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class GdxLearning {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(new MyGame(), "Gdx Learning ! by Jie.", 800, 480, false);
	}

}
