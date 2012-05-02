package jie.java.android.boxcatcher.boxlistener;

import com.badlogic.gdx.Gdx;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxContactListener;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.WorldScreen;

public class EachContactListener extends BaseContactListener {

	public EachContactListener(WorldScreen screen, BoxListenerManager manager) {
		super(screen, manager);
	}
	
	@Override
	public void onBeginContact(BoxActor actor, BoxActor other) {
		//Gdx.app.log(Global.APP_TAG, actor.name + " begin contact(each) with " + other.name);
		//actor.setContactListener(null);
	}

	@Override
	public void onEndContact(BoxActor actor, BoxActor other) {
		//Gdx.app.log(Global.APP_TAG, actor.name + " end contact(each) with " + other.name);
		screen.updateScore(5);
	}

}
