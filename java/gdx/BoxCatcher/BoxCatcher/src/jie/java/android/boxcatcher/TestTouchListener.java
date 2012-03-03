package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;

public class TestTouchListener implements BoxTouchListener {

	private BoxActor actor = null;
	private boolean pressed = false;
	private float px, py;
	
	public TestTouchListener(BoxActor actor) {
		this.actor = actor;
	}
	
	@Override
	public boolean onTouchDown(float x, float y, int pointer) {
		pressed = true;
		px = x;
		py = y;
		return true;
	}

	@Override
	public boolean onTouchUp(float x, float y, int pointer) {
		pressed = false;
		return true;
	}

	@Override
	public boolean onTouchDragged(float x, float y, int pointer) {
		if(pressed) {
			actor.applyVelocity(((x - px) / Global.WORLD_SCALE), 0);// (x / Global.WORLD_SCALE - actor.width / (2 * Global.WORLD_SCALE)) , 0);
		}
		return true;
	}

	@Override
	public boolean onTouchMoved(float x, float y) {
//		Gdx.app.log("tag", actor.name + " touchMoved");
		//actor.applyVelocity((x / Global.WORLD_SCALE - actor.width / (2 * Global.WORLD_SCALE)) , 0);
		return false;
	}

}
