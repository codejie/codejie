package jie.java.android.boxcatcher.boxlistener;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.WorldScreen;

public class DeadlineContactListener extends BaseContactListener {

	public DeadlineContactListener(WorldScreen screen,
			BoxListenerManager manager) {
		super(screen, manager);
	}

	@Override
	public void onBeginContact(BoxActor actor, BoxActor other) {
		other.markToRemove(true);
		this.screen.updateScore(-10);
	}

	@Override
	public void onEndContact(BoxActor actor, BoxActor other) {
		// TODO Auto-generated method stub

	}

}
