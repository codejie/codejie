package jie.java.android.boxcatcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoxActor extends Actor {

	public enum BoxType {
		BT_BOX, BT_CIRCLE, BT_TRIANGLE
	}
	
	public final static class Parameter {
		public String name;
		public BoxType type;
		public Vector2 position;
		public float width, height;
		//
	}
	
	private World world = null;
	private TextureRegion region = null;
	
	public BoxActor(World world, final Parameter param) {
		super(param.name);
		
		this.world = world;
		
		makeBox(param);
	}	
	
	private void makeBox(Parameter param) {
		x = param.position.x;
		y = param.position.y;
		width = param.width;
		height = param.height;
	}
	
	public void setRegion(TextureRegion region) {
		if(this.region == region)
			return;
		
		this.region = region;
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
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

}
