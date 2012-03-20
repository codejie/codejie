package jie.java.android.boxcatcher.boxlistener;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxContactListener;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.WorldScreen;

public abstract class BaseContactListener implements BoxContactListener {

	protected WorldScreen screen = null;
	protected BoxListenerManager manager = null;
	
	public BaseContactListener(WorldScreen screen, BoxListenerManager manager) {
		this.screen = screen;
		this.manager = manager;		
	}	
	
	@Override
	public abstract void onBeginContact(BoxActor actor, BoxActor other);

	@Override
	public abstract void onEndContact(BoxActor actor, BoxActor other);
}
