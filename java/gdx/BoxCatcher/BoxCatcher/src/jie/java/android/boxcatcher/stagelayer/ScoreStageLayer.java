package jie.java.android.boxcatcher.stagelayer;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import jie.java.android.boxcatcher.BCScreen;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.MaterialManager;

public class ScoreStageLayer extends BaseStageLayer {

	private Label level = null;
	private Label score = null;
	
	public ScoreStageLayer(BCScreen screen, MaterialManager materialManager) {
		super(screen, materialManager);
		
		init();
	}
	
	private int init() {
		//LabelStyle style = 
		level = new Label("Level", materialManager.getBitmapFont().getStyle(1));
		level.x = 10;
		level.y = Global.SCREEN_HEIGHT - 52;
		
		addActor(level);
		
		
		score = new Label("Score", materialManager.getBitmapFont().getStyle(2));
		score.x = Global.SCREEN_WIDTH - score.getText().length() * 32 - 20;// .getMaxWidth();
		score.y = Global.SCREEN_HEIGHT - 52;
		addActor(score);
		
		return 0;
	}
	
	public void setLevel(int value) {
		level.setText(String.format("L:%d", value));
	}
	
	public void setScore(int value) {
		score.setText(String.format("S:%d", value));
		score.x = Global.SCREEN_WIDTH - score.getText().length() * 32 - 20;
	}

	public void showResult() {
		ImageButton btn = new ImageButton(materialManager.getTexture().getRegion(2), materialManager.getTexture().getRegion(1));
		btn.x = 100;
		btn.y = 100;
		addActor(btn);
	}
	
	
}
