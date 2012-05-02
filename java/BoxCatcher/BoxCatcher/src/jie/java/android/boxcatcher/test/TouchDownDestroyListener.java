package jie.java.android.boxcatcher.test;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.BoxTouchListener;
import jie.java.android.boxcatcher.Global;

import com.badlogic.gdx.Gdx;

public class TouchDownDestroyListener implements BoxTouchListener {

	@Override
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
//		Gdx.app.log(Global.APP_TAG, actor.name + " touchdown.");
		if(actor != null) {
			Gdx.app.log(Global.APP_TAG, actor.name + " touchdown.");
			//actor.destroy();
		}
		return true;
	}

	@Override
	public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
		if(actor != null) {
			Gdx.app.log(Global.APP_TAG, actor.name + " touchup.");
		}
	}

	@Override
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
		if(actor != null) {
			Gdx.app.log(Global.APP_TAG, actor.name + " touchdragged.");
		}
	}

	@Override
	public boolean onTouchMoved(BoxActor actor, float x, float y) {
		if(actor != null) {
			Gdx.app.log(Global.APP_TAG, actor.name + " touchMoved.");
		}
		return true;
	}

}
