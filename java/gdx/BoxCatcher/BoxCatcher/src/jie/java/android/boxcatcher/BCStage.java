package jie.java.android.boxcatcher;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BCStage extends Stage {
	
	protected String title = null;
	
	public BCStage(String title) {
		super(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, true);
		this.title = title;
	}
}
