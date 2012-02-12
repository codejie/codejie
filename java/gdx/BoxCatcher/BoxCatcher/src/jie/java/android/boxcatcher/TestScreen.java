package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TestScreen extends BCScreen implements ContactListener {

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		renderer.render(world);
		super.draw();
	}

	//TestStage stage = null;
	private World world = null;
	private WorldDebugRenderer renderer = null;	
	
	public TestScreen(BCGame game) {
		super(game);
		
		initWorld();
		initBoxes();
//		stage = new TestStage();
	}
	
	private void initBoxes() {
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.angle = 1/360f;
		
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
		//d.angle = 120.0f;
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

	@Override
	public void render(float delta) {
		super.render(delta);
		world.step(delta, 3, 3);
		//renderer.render(world);
	}

	private void initWorld() {
		world = new World(Global.WORLD_GRAVITY, true);
		world.setContactListener(this);
		renderer = new WorldDebugRenderer();
		
	}	

	@Override
	public void show() {
		//this.addActor(stage);
		super.show();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub

		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
	 	Gdx.app.log("test", "touchDragged - x = " + x + ", y = " + y);
		return super.touchDragged(x, y, pointer);
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
	 	Gdx.app.log("test", "touchMoved - x = " + x + ", y = " + y);
		return super.touchMoved(x, y);
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}



}
