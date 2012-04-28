package jie.java.android.boxcatcher.stagelayer;

import jie.java.android.boxcatcher.BCScreen;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.MaterialManager;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class EndGameLayer extends BaseStageLayer {

	private Label label = null;
	public EndGameLayer(BCScreen screen, MaterialManager materialManager) {
		super(screen, materialManager);

		label = new Label("GAME OVER!", materialManager.getBitmapFont().getStyle(1));
		//label.scaleX = 3.0f;
		//label.scaleY = 3.0f;
		label.x = Global.SCREEN_WIDTH / 2 - label.getText().length() * 32 / 2;
		label.y = Global.SCREEN_HEIGHT / 2;
		
		addActor(label);
	}	
	
}
