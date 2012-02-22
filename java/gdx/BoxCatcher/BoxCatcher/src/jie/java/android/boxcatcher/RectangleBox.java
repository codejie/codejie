package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class RectangleBox extends BoxActor {

	public RectangleBox(World world, Parameter param) {
		super(world, param);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void makeBox() {
		x = parameter.position.x;
		y = parameter.position.y;
		width = parameter.width;
		height = parameter.height;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set((x + width/ 2) / Global.WORLD_SCALE, (y + height /2 ) / Global.WORLD_SCALE);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / Global.WORLD_SCALE / 2, height / Global.WORLD_SCALE  / 2);
		
		body = world.createBody(def);
		body.createFixture(shape, 1.0f);
	}

	@Override
	protected void update(float delta) {	
		Gdx.app.log("tag", "x = " + body.getPosition().x + " y = " + body.getPosition().y);
		x = body.getPosition().x * Global.WORLD_SCALE - width /2;
		y = body.getPosition().y * Global.WORLD_SCALE - height /2;
	}

}
