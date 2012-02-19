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
	
	public final class Parameter {
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
		
	}
	
	public void setRegion(TextureRegion region) {
		if(this.region == region)
			return;
		this.region = region;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

}
