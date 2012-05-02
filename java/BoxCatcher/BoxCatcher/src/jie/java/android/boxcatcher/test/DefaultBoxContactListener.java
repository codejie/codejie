package jie.java.android.boxcatcher.test;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxContactListener;
import jie.java.android.boxcatcher.Global;

import com.badlogic.gdx.Gdx;

public class DefaultBoxContactListener implements BoxContactListener {
	
	@Override
	public void onBeginContact(BoxActor actor, BoxActor other) {
		Gdx.app.log(Global.APP_TAG, actor.name + " begin contact with " + other.name);
//		if(target.name == "ground") {
	//		actor.markToRemove(true);
		//}
	}

	@Override
	public void onEndContact(BoxActor actor, BoxActor other) {
		Gdx.app.log(Global.APP_TAG, actor.name + " end contact with " + other.name);
//		if(target.name == "ground") {
	//		actor.markToRemove(true);
		//}

	}

}
