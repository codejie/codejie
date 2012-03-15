package jie.java.android.boxcatcher;

public interface BoxTouchListener {
	public boolean onTouchDown(BoxActor actor, float x, float y, int pointer);
	public void onTouchUp(BoxActor actor, float x, float y, int pointer);
	public void onTouchDragged(BoxActor actor, float x, float y, int pointer);
	public boolean onTouchMoved(BoxActor actor, float x, float y);
}
