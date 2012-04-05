package jie.java.android.boxcatcher.stagelayer;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import jie.java.android.boxcatcher.BCScreen;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.MaterialManager;

public class EndStageLayer extends BaseStageLayer {

	private Label label = null;
	
	private Image left = null, right = null;
	
	
	public EndStageLayer(BCScreen screen, MaterialManager materialManager) {
		super(screen, materialManager);

		init();
		
	}
	
	private void init() {
		left = new Image(materialManager.getTexture().getRegion(2));
		left.x = 0;//- Global.SCREEN_WIDTH / 2;
		left.y = Global.SCREEN_HEIGHT;//0;
		left.width = Global.SCREEN_WIDTH;//Global.SCREEN_WIDTH / 2;
		left.height = Global.SCREEN_HEIGHT / 2;//Global.SCREEN_HEIGHT;
		
		left.action(MoveTo.$(0, Global.SCREEN_HEIGHT / 2, 0.6f));
		addActor(left);
		

		right = new Image(materialManager.getTexture().getRegion(2));
		right.x = 0;//Global.SCREEN_WIDTH;
		right.y = -Global.SCREEN_HEIGHT / 2;//0;
		right.width = Global.SCREEN_WIDTH;//Global.SCREEN_WIDTH / 2;
		right.height = Global.SCREEN_HEIGHT / 2;//Global.SCREEN_HEIGHT;
		right.action(MoveTo.$(0, 0, 0.6f).setCompletionListener(new OnActionCompleted(){

			@Override
			public void completed(Action action) {
				onScreenClosed();				
			}
			
		}));
		addActor(right);
		
//		label = new Label("Stage Over!", materialManager.getBitmapFont().getStyle(1));
		//label.scaleX = 3.0f;
		//label.scaleY = 3.0f;
//		label.x = Global.SCREEN_WIDTH / 2 - label.getText().length() * 32 / 2;
//		label.y = Global.SCREEN_HEIGHT / 2;
		
//		addActor(label);		
		
	}

	protected void onScreenClosed() {
		label = new Label("Stage Over!", materialManager.getBitmapFont().getStyle(1));
		//label.scaleX = 3.0f;
		//label.scaleY = 3.0f;
		label.x = Global.SCREEN_WIDTH / 2 - label.getText().length() * 32 / 2;
		label.y = Global.SCREEN_HEIGHT / 2;
		
		addActor(label);
		
	}
	
}
