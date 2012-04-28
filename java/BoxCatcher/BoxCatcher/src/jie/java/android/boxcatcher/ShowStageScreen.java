package jie.java.android.boxcatcher;

import jie.java.android.boxcatcher.stagelayer.BaseStageLayer;
import jie.java.android.boxcatcher.stagelayer.EndGameLayer;
import jie.java.android.boxcatcher.stagelayer.EndStageLayer;

public class ShowStageScreen extends BCScreen {

	private BaseStageLayer layer = null;
	
	public ShowStageScreen(BCGame game) {
		super(game);

		initLayer();
	}

	void initLayer() {
		layer = new EndGameLayer(this, this.game.getMaterialManager());
		
		addActor(layer);
	}

	@Override
	public void render(float delta) {

		super.render(delta);

		//act(delta);
		draw();
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	
}
