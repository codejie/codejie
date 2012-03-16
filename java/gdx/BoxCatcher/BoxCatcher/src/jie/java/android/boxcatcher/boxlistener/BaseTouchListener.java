package jie.java.android.boxcatcher.boxlistener;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.BoxTouchListener;
import jie.java.android.boxcatcher.WorldScreen;

public abstract class BaseTouchListener implements BoxTouchListener {

	private WorldScreen screen = null;
	private BoxListenerManager manager = null;
	
	public BaseTouchListener(WorldScreen screen, BoxListenerManager manager) {
		this.screen = screen;
		this.manager = manager;		
	}		
	
	@Override
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
		return false;
	}

	@Override
	public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
	}

	@Override
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
	}

	@Override
	public boolean onTouchMoved(BoxActor actor, float x, float y) {
		return false;
	}

}
