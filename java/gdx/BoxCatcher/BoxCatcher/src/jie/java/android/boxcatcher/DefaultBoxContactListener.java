package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;

public class DefaultBoxContactListener implements BoxContactListener {

	private BoxActor actor = null;
	
	public DefaultBoxContactListener(BoxActor actor) {
		this.actor = actor;
	}
	
	@Override
	public void onBeginContact(BoxActor other) {
		Gdx.app.log("tag", actor.name + " begin contact with " + other.name);
//		if(target.name == "ground") {
	//		actor.markToRemove(true);
		//}
	}

	@Override
	public void onEndContact(BoxActor other) {
		Gdx.app.log("tag", actor.name + " end contact with " + other.name);
//		if(target.name == "ground") {
	//		actor.markToRemove(true);
		//}

	}

}
