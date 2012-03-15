package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;

public class TestTouchListener implements BoxTouchListener {

	private boolean pressed = false;
	private float px, py;
	
	@Override
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
		pressed = true;
		px = x;
		py = y;
		return true;
	}

	@Override
	public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
		pressed = false;
	}

	@Override
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
		if(pressed) {
			actor.applyVelocity(((x - px) / Global.WORLD_SCALE), 0);// (x / Global.WORLD_SCALE - actor.width / (2 * Global.WORLD_SCALE)) , 0);
		}
	}

	@Override
	public boolean onTouchMoved(BoxActor actor, float x, float y) {
		Gdx.app.log("tag", actor.name + " touchMoved");
		//actor.applyVelocity((x / Global.WORLD_SCALE - actor.width / (2 * Global.WORLD_SCALE)) , 0);
		return true;
	}

}
