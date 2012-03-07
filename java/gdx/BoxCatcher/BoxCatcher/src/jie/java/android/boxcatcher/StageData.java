package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jie.java.android.boxcatcher.BoxActor.BoxShape;

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
	
	public final static class World {
		//public float scale		=	1.0f;
		public Vector2 gravity	=	null;
		
		public World(Vector2 gravity) {
			this.gravity = gravity;
		}
	}
	
	public final static class Box {
		public String name;
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
	}
	
	//public Screen screen = null;
	public World world = null;
	public HashMap<Integer, ArrayList<Box>> boxes = new HashMap<Integer, ArrayList<Box>>();
	
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
		
		ArrayList<Box> abox = new ArrayList<Box>();
//		
//		Box b = new Box();
//		b.name = "b";
//		b.x = 300;
//		b.y = 350;
//		b.height = 32;
//		b.width = 32;
//		b.shape = BoxActor.BoxShape.CIRCLE;
//		b.type = BodyType.DynamicBody;
//		abox.add(b);
//		
//		
		Box c = new Box();
		c.name = "c";
		c.x = 10;
		c.y = 10;
		c.height = 10;
		c.width = 790;
		c.shape = BoxActor.BoxShape.LINE;
		c.type = BodyType.StaticBody;		
		abox.add(c);
//		
		Box d = new Box();
		d.name = "d";
		d.x = 400;
		d.y = 350;
		d.height = 112;
		d.width = 112;
		d.shape = BoxActor.BoxShape.RECTANGLE;
		d.type = BodyType.DynamicBody;
		d.restitution = 0.5f;
		d.texture = 1;//Global.TEXTURE.getRegion(1);
		d.animation = 1;//Global.TEXTURE.getAnimation(1);
		abox.add(d);
		
		boxes.put(0, abox);
		
		
//		ArrayList<Box> nbox = new ArrayList<Box>();
//		Box nb = new Box();
//		nb.name = "nb";
//		nb.x = 100;
//		nb.y = 350;
//		nb.height = 32;
//		nb.width = 32;
//		nb.shape = BoxActor.BoxShape.TRIANGLE;
//		nb.type = BodyType.DynamicBody;
//	
//		nbox.add(nb);
//		
//		boxes.put(2, nbox);
		
		
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

}
