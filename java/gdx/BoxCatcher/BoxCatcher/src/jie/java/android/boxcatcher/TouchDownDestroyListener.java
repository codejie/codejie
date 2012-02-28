package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;

public class TouchDownDestroyListener implements BoxTouchListener {

	private BoxActor actor = null;
	
	public TouchDownDestroyListener(BoxActor actor) {
		this.actor = actor;
	}
	@Override
	public boolean onTouchDown(float x, float y, int pointer) {
		Gdx.app.log("tag", actor.name + " touchdown.");
		if(actor != null) {
			Gdx.app.log("tag", actor.name + " touchdown.");
			actor.destroy();
		}
		return false;
	}

	@Override
	public boolean onTouchUp(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchDragged(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchMoved(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}
