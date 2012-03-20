package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jie.java.android.boxcatcher.BoxActor.BoxShape;
import jie.java.android.boxcatcher.StageData.Box;
import jie.java.android.boxcatcher.StageData.BoxRace;
import jie.java.android.boxcatcher.demo.BoxData;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StageData {

//	public final static class Screen {
//		public int width	=	0;
//		public int height	=	0;
//		
//		public Screen(int width, int height) {
//			this.width = width;
//			this.height = height;
//		}
//	}
	
	public enum BoxRace { 
		UNKNOWN, BOX, DOCK, BORDER, DEADLINE 
	}
	
	public final static class World {
		//public float scale		=	1.0f;
		public Vector2 gravity	=	null;
		
		public World(Vector2 gravity) {
			this.gravity = gravity;
		}
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

	//public Screen screen = null;
	public World world = null;
	private ArrayList<Box> frames = new ArrayList<Box>();
	private HashMap<Integer, ArrayList<Box>> boxes = new HashMap<Integer, ArrayList<Box>>();
	
	private int stageID = -1;
	private Integer currentKey = -1;
	private Iterator<Box> boxIterator = null;
	
	public StageData(int id) {
		stageID = id;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
	public int load() {
		//screen = new Screen(800, 480);
		world = new World(Global.WORLD_GRAVITY);// new Vector2(0, -9.8f));
		
		BoxData.loadFrames(frames);
		BoxData.loadBoxes(boxes);
	
		return 0;
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
