package jie.java.android.boxcatcher.boxlistener;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.WorldScreen;

public class DockTouchListener extends BaseTouchListener {

	private float px = 0.0f;
	
	public DockTouchListener(WorldScreen screen, BoxListenerManager manager) {
		super(screen, manager);

	}

	@Override
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
		px = x;
		return true;
	}

	@Override
	public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
	}

	@Override
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
		actor.applyVelocity(((x - px) / Global.WORLD_SCALE), 0);
	}

	
	
}
