package jie.java.android.boxcatcher;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TriangleBox extends BoxActor {

	public TriangleBox(World world, Parameter param) {
		super(world, param);
	}

	@Override
	protected void makeBox() {
		BodyDef def = new BodyDef();
		def.type = parameter.type;
		def.position.set(getCenter());

		PolygonShape shape = new PolygonShape();
				
		shape.set(new Vector2[] { new Vector2(0.0f, 0.0f)
								, new Vector2(parameter.width / Global.WORLD_SCALE, 0.0f)
								, new Vector2((parameter.width / 2) / Global.WORLD_SCALE, parameter.height / Global.WORLD_SCALE) } );
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = parameter.restitution;
		fd.density = parameter.density;
		fd.friction = parameter.friction;		
		
		body = world.createBody(def);
		body.createFixture(fd);
		
		//shape.dispose();
	}

	private Vector2 getCenter() {
		return new Vector2((parameter.position.x + parameter.width / 2) / Global.WORLD_SCALE
				, (parameter.position.y + parameter.height / 2) / Global.WORLD_SCALE);
	}

}
