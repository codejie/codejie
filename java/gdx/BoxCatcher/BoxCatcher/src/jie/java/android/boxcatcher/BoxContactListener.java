package jie.java.android.boxcatcher;

public interface BoxContactListener {

	public void onBeginContact(BoxActor actor, BoxActor other);
	public void onEndContact(BoxActor actor, BoxActor other);
}
