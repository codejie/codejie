package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.HashMap;

import jie.java.android.boxcatcher.BoxActor.BoxShape;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StageData {

	public final static class Screen {
		public int width	=	0;
		public int height	=	0;
	}
	
	public final static class World {
		//public float scale		=	1.0f;
		public Vector2 gravity	=	new Vector2(0, 0);
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
		public TextureRegion texture = null;
		public Animation animation = null;
		
		//Sound
	}
	
	public Screen screen = null;
	public World world = null;
	public HashMap<Float, ArrayList<Box>> boxes = new HashMap<Float, ArrayList<Box>>();
	
	private int stageID = -1;
	private float deltaStep = 0.0f;
	
	public StageData(int id) {
		stageID = id;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
	public int load() {
		return -1;
	}
	
	public Box getNextBox(float delta) {
		return null;
	}
	
	public TextureRegion getNextRegion(float delta) {
		return null;
	}
	
}
