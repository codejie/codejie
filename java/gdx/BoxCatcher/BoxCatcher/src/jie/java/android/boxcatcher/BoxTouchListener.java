package jie.java.android.boxcatcher;

public interface BoxTouchListener {
	public boolean onTouchDown(float x, float y, int pointer);
	public boolean onTouchUp(float x, float y, int pointer);
	public boolean onTouchDragged(float x, float y, int pointer);
	public boolean onTouchMoved(float x, float y);
}
