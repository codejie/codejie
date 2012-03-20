package jie.java.android.boxcatcher.stagelayer;

import jie.java.android.boxcatcher.MaterialManager;
import jie.java.android.boxcatcher.WorldScreen;

import com.badlogic.gdx.scenes.scene2d.Group;

public class BaseStageLayer extends Group {

	protected WorldScreen screen = null;
	protected MaterialManager materialManager = null;
	
	public BaseStageLayer(WorldScreen screen, MaterialManager materialManager) {
		this.screen = screen;
		this.materialManager = materialManager;
	}
}
