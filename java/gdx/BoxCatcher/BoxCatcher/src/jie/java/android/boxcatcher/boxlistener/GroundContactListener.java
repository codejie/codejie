package jie.java.android.boxcatcher.boxlistener;

import com.badlogic.gdx.Gdx;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxContactListener;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.WorldScreen;

public class GroundContactListener extends BaseContactListener {

	public GroundContactListener(WorldScreen screen, BoxListenerManager manager) {
		super(screen, manager);
	}
	
	@Override
	public void onBeginContact(BoxActor actor, BoxActor other) {
		
		Gdx.app.log("tag", actor.name + " begin contact with " + other.name);
		
		//other.markToRemove(true);
	}

	@Override
	public void onEndContact(BoxActor actor, BoxActor other) {
		Gdx.app.log("tag", actor.name + " end contact with " + other.name);
	}

}
