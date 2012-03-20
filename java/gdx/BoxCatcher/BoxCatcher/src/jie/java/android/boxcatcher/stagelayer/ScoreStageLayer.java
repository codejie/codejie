package jie.java.android.boxcatcher.stagelayer;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.MaterialManager;
import jie.java.android.boxcatcher.WorldScreen;

public class ScoreStageLayer extends BaseStageLayer {

	private Label level = null;
	private Label score = null;
	
	public ScoreStageLayer(WorldScreen screen, MaterialManager materialManager) {
		super(screen, materialManager);
		
		init();
	}
	
	private int init() {
		//LabelStyle style = 
		level = new Label("level", materialManager.getBitmapFont().getStyle(1));
		level.x = 10;
		level.y = Global.SCREEN_HEIGHT - 52;
		
		addActor(level);
		
		
		score = new Label("score", materialManager.getBitmapFont().getStyle(2));
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
	

}
