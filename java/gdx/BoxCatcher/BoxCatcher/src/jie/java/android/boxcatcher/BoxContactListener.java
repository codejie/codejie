package jie.java.android.boxcatcher;

public interface BoxContactListener {

	public void onBeginContactAsSource(BoxActor target);
	public void onBeginContactAsTarget(BoxActor source);
	public void onEndContactAsSource(BoxActor target);
	public void onEndContactAsTarget(BoxActor source);
}
