package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jie.java.android.boxcatcher.BoxActor.BoxShape;
import jie.java.android.boxcatcher.database.DBAccess;

public class StageData {
	
	public enum BoxRace { 
		UNKNOWN, BOX, DOCK, BORDER, DEADLINE 
	}
		
	public final static class Box {
		public int id = -1;
		public String name;
		public BoxRace race = BoxRace.UNKNOWN;
		public BodyType type;
		public BoxShape shape;
		public float x, y;
		public float width, height;
		public float angle = 0.0f;
		public float density = 1.0f;
		public float restitution = 0.0f;
		public float friction = 0.0f;
		public short filterBits = 0x00FF;
		
		//Texture
		public int texture = -1;
		public int animation = -1;
		
		//Sound
		
		//dynamic data
		public float stateTime = 0.0f;
	}

	public final class Setting {
		public int id = -1;
		public String title;
		public int maxStateTime = -1;
		
		public Vector2 gravity = null;
	}
	
	public final class Runtime {
		public int score = 0;
	}

	private Setting setting = new Setting();
	private Runtime runtime = new Runtime();
	
	private ArrayList<Box> frames = new ArrayList<Box>();
	private HashMap<Integer, ArrayList<Box>> boxes = new HashMap<Integer, ArrayList<Box>>();
	
	private Integer currentKey = -1;
	private Iterator<Box> boxIterator = null;
	
	public StageData(int id) {
		setting.id = id;
	}
	
	public int load(DBAccess db) {
		
		db.loadSetting(setting.id, setting);
		db.loadFrames(setting.id, frames);
		db.loadBoxes(setting.id, boxes);
		
		//setting.gravity = Global.WORLD_GRAVITY;
		
//		BoxData.loadFrames(frames);
//		BoxData.loadBoxes(boxes);
	
		return 0;
	}
	
	public Setting getSetting() {
		return setting;
	}
	
	public Runtime getRuntime() {
		return runtime;
	}
	
	public Box getFirstBox(float stateTime) {
		Integer key = (int)stateTime;
		if(key == currentKey)
			return null;
				
		ArrayList<Box> abox = boxes.get(key);
		if(abox == null)
			return null;
		
		currentKey = key;
		
        boxIterator = abox.iterator();
        
        if(!boxIterator.hasNext())
        	return null;
        
        return boxIterator.next();
	}
	
	public Box getNextBox() {
		if(boxIterator == null)
			return null;
		if(!boxIterator.hasNext())
			return null;
		return boxIterator.next();
	}
	
	public ArrayList<Box> getFrames() {
		return frames;
	}	
	
	public ArrayList<Box> getBoxes(float stateTime) {
		Integer key = (int)stateTime;
		if(key == currentKey)
			return null;
		
		currentKey = key;
		
		return  boxes.get(key);
	}
	
}
