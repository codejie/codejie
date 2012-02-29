package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;

public class DefaultBoxContactListener implements BoxContactListener {

	private BoxActor actor = null;
	
	public DefaultBoxContactListener(BoxActor actor) {
		this.actor = actor;
	}
	
	@Override
	public void onBeginContactAsSource(BoxActor target) {
		Gdx.app.log("tag", actor.name + " begin contact as source with " + target.name);
	}

	@Override
	public void onBeginContactAsTarget(BoxActor source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndContactAsSource(BoxActor target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndContactAsTarget(BoxActor source) {
		// TODO Auto-generated method stub

	}

}
