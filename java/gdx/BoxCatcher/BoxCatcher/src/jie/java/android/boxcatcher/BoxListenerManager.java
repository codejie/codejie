package jie.java.android.boxcatcher;

import java.util.HashMap;

import jie.java.android.boxcatcher.boxlistener.DeadlineContactListener;
import jie.java.android.boxcatcher.boxlistener.DockTouchListener;
import jie.java.android.boxcatcher.boxlistener.EachContactListener;
import jie.java.android.boxcatcher.boxlistener.GroundContactListener;

public final class BoxListenerManager {

	private final class BoxData {
		public int countContact = 0;
	}
	
	private WorldScreen screen = null;
	
	private HashMap<Integer, BoxData> mapBoxData = null;

	private GroundContactListener groundContactListener = null;
	private EachContactListener eachContactListener = null;
	
	private DockTouchListener dockTouchListener = null;

	private DeadlineContactListener deadlineContactListener = null;
	
	public BoxListenerManager(WorldScreen screen) {
		this.screen = screen;
		
		groundContactListener = new GroundContactListener(screen, this);
		eachContactListener = new EachContactListener(screen , this);
		deadlineContactListener = new DeadlineContactListener(screen, this);
		
		dockTouchListener = new DockTouchListener(screen , this);
	}

	public BoxContactListener getGroundContactListener() {
		// TODO Auto-generated method stub
		return groundContactListener;
	}
	
	public BoxContactListener getEachContactListener() {
		return eachContactListener;
	}

	public BoxTouchListener getDockTouchListener() {
		// TODO Auto-generated method stub
		return dockTouchListener;
	}

	public BoxContactListener getDeadlineContactorListener() {
		// TODO Auto-generated method stub
		return deadlineContactListener ;
	}
	
	
}
