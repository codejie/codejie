package jie.java.android.boxcatcher;

public interface BoxContactListener {

	public void onAsSourceBeginContact(BoxActor target);
	public void onAsTargetBeginContact(BoxActor source);
	public void onAsSourceEndContact(BoxActor target);
	public void onAsTargetEndContact(BoxActor source);
}
