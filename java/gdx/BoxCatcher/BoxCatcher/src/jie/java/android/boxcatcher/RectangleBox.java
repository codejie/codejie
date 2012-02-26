package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class RectangleBox extends BoxActor {

	public RectangleBox(World world, Parameter param) {
		super(world, param);
	}

	@Override
	protected void makeBox() {		
		BodyDef def = new BodyDef();
		def.type = (parameter.type == BoxType.BT_DYNAMIC ? BodyType.DynamicBody : BodyType.StaticBody);
		def.position.set(getCenter());
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth(), getHeight());
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = parameter.restitution;
		fd.density = parameter.density;
		fd.friction = parameter.friction;		
		
		body = world.createBody(def);
		body.createFixture(fd);
		
		shape.dispose();
	}

	@Override
	protected void update(float delta) {
		rotation = MathUtils.radiansToDegrees * body.getAngle();

		x = body.getPosition().x * Global.WORLD_SCALE - parameter.width / 2;// * MathUtils.cos(rotation);
		y = body.getPosition().y * Global.WORLD_SCALE - parameter.height / 2;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		Gdx.app.log("tag", "touchDown");
		
		//this.body.ap
		
		return super.touchDown(x, y, pointer);
	}
	
	
	private Vector2 getCenter() {
		return new Vector2((parameter.position.x + parameter.width / 2) / Global.WORLD_SCALE
				, (parameter.position.y + parameter.height / 2) / Global.WORLD_SCALE);
	}
	
	private float getWidth() {
		return parameter.width / Global.WORLD_SCALE / 2;
	}
	
	private float getHeight() {
		return parameter.height / Global.WORLD_SCALE / 2;
	}
}
