package jie.java.android.boxcatcher.boxlistener;

import com.badlogic.gdx.Gdx;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxListenerManager;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.WorldScreen;

public class DockTouchListener extends BaseTouchListener {

	private float px = 0.0f;
	
	public DockTouchListener(WorldScreen screen, BoxListenerManager manager) {
		super(screen, manager);

	}

	@Override
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
		actor.applyVelocity(0, 0);
		px = x;
		//Gdx.app.log(Global.APP_TAG, actor.name + " down - x : " + x + " - px : " + px);
		return true;
	}

	@Override
	public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
		px = 0;
		actor.applyVelocity(0, 0);
		//Gdx.app.log(Global.APP_TAG, actor.name + " up - x : " + x + " - px : " + px);
	}

	@Override
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
		//Gdx.app.log(Global.APP_TAG, actor.name + " dragged - x : " + x + " - px : " + px);
		actor.applyVelocity(((x - px) / Global.WORLD_SCALE) * 2, 0);
		//actor.applyForceToCenter((x - px) * Global.WORLD_SCALE, 0);// ApplyForce
//		if(x > px) {
//			actor.applyVelocity(((100) / Global.WORLD_SCALE) * 4, 0);
//		}
//		else {
//			actor.applyVelocity(((-100) / Global.WORLD_SCALE) * 4, 0);
//		}
	}

	
	
}
