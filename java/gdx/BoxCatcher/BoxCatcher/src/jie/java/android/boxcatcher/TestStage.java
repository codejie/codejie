package jie.java.android.boxcatcher;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TestStage extends BCStage {

	private World world = null;
	private WorldDebugRenderer renderer = null;
	
	public TestStage() {
		super("test");
		
		initWorld();
		initBoxes();
	}

	@Override
	public void dispose() {
		world.dispose();
		super.dispose();
	}

	@Override
	public void draw() {
		super.draw();
		world.step((1.0f/ 60.0f), 3, 3);
		renderer.render(world);
	}

	private void initBoxes() {
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(200/Global.WORLD_SCALE, 20/Global.WORLD_SCALE);
		def.position.set(240/Global.WORLD_SCALE, 20/Global.WORLD_SCALE);
		
		Body body = world.createBody(def);
		body.createFixture(shape, 0.1f);

		def.type = BodyType.DynamicBody;
		def.position.set(200 / Global.WORLD_SCALE, 600 / Global.WORLD_SCALE);
		shape.setAsBox(32 / Global.WORLD_SCALE, 32 / Global.WORLD_SCALE);
		
		Body box = world.createBody(def);
		box.createFixture(shape, 0.1f);

		BodyDef d = new BodyDef();
		d.type = BodyType.DynamicBody;
		d.angle = 120.0f;
		d.position.set(100/Global.WORLD_SCALE, 300/Global.WORLD_SCALE);
		
		CircleShape s = new CircleShape();
		s.setRadius(32.0f / Global.WORLD_SCALE);
		
		
		Body cc = world.createBody(d);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = s;
		fd.restitution = 0.6f;
		fd.density = 0.1f;
		fd.friction = 0.1f;
//		fd.filter.categoryBits = 2;
//		fd.filter.maskBits = 2;

		cc.createFixture(fd);//s, 0.1f);
		
	}

	private void initWorld() {
		world = new World(Global.WORLD_GRAVITY, true);
		renderer = new WorldDebugRenderer();
		
	}

}
