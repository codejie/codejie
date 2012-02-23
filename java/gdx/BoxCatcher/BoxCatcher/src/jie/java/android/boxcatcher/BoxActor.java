package jie.java.android.boxcatcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BoxActor extends Actor {

	public enum BoxType {
		BT_STATIC, BT_DYNAMIC
	}
	
	public enum BoxShape {
		BS_BOX, BS_CIRCLE, BS_TRIANGLE
	}
	
	public static Vector2 toWorld(final Vector2 vct) {
		return new Vector2(vct.x / Global.WORLD_SCALE, vct.y / Global.WORLD_SCALE);
	}
	
	public static float toWorld(final float v) {
		return v /Global.WORLD_SCALE;
	}
	
	public final static class Parameter {
		public String name;
		public BoxType type;
		public Vector2 position;
		public float width, height;
		public float angle = 0.0f;
		public float density = 1.0f;
		public float restitution = 0.0f;
		public float friction = 0.5f;
		//
	}
	
	protected World world = null;
	protected Parameter parameter = null;
	protected TextureRegion region = null;
	protected Body body = null;
	
	public BoxActor(World world, final Parameter param) {
		super(param.name);
		
		this.world = world;
		this.parameter = param;		
		
		init();
		makeBox();
	}
	
	protected void init() {
		x = parameter.position.x;
		y = parameter.position.y;
		width = parameter.width;
		height = parameter.height;
		rotation = MathUtils.radiansToDegrees * parameter.angle;	
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		world.step(delta, 3, 3);
		update(delta);
		super.act(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if(region != null) {
			if(scaleX == 1 && scaleY ==1 && rotation == 0) {
				batch.draw(region, x, y, width, height);
			}
			else {
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
			}
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return x > 0 && x < width && y > 0 && y < height ? this : null;
	}	

	public void setRegion(TextureRegion region) {
		if(this.region == region)
			return;
		
		this.region = region;
	}	
	
	abstract protected void makeBox();
	abstract protected void update(float delta);

}
