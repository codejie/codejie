package jie.java.android.boxcatcher;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoxActor extends Actor {

	public enum BoxShape {
		BS_RECTANGLE, BS_CIRCLE, BS_TRIANGLE
	}
		
	public final static class Parameter {
		public String name;
		public BodyType type;
		public BoxShape shape;
		public Vector2 position;
		public float width, height;
		public float angle = 0.0f;
		public float density = 1.0f;
		public float restitution = 0.0f;
		public float friction = 0.0f;
	}
	
	protected World world = null;
	protected Parameter parameter = null;
	protected TextureRegion region = null;
	protected Body body = null;
	
	protected BoxContactListener contactListener = null;
	protected BoxDestroyListener destroyListener = null;
	protected BoxTouchListener touchListener = null;
	
	public BoxActor(World world, final Parameter param) {
		super(param.name);
		
		this.world = world;
		this.parameter = param;		
		
		init();
		makeBox();
		if(body != null) {
			body.setUserData(this);
		}
	}
	
	protected void init() {
		x = parameter.position.x;
		y = parameter.position.y;
		width = parameter.width;
		height = parameter.height;
		rotation = MathUtils.radiansToDegrees * parameter.angle;
		originX = width / 2.0f;
		originY = height / 2.0f;
	}
	
	//
	protected void makeBox() {
		BodyDef def = new BodyDef();
		def.type = parameter.type;
		def.angle = parameter.angle;
		def.position.set(getCenter());
		
		Shape shape = makeShape(parameter.shape);

		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = parameter.restitution;
		fd.density = parameter.density;
		fd.friction = parameter.friction;
		
		body = world.createBody(def);
		body.createFixture(fd);
		
		shape.dispose();
		Gdx.app.log("tag", this.name + " body mass = " + body.getMass());		
	}
 
	private Shape makeShape(BoxShape shapetype) {
		
		switch(shapetype) {
		case BS_RECTANGLE: {
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(parameter.width / Global.WORLD_SCALE / 2, parameter.height / Global.WORLD_SCALE / 2);
			
			return shape;
		}
		case BS_TRIANGLE: {
			PolygonShape shape = new PolygonShape();
			
			shape.set(new Vector2[] { new Vector2(0.0f, 0.0f)
									, new Vector2(parameter.width / Global.WORLD_SCALE, 0.0f)
									, new Vector2((parameter.width / 2) / Global.WORLD_SCALE, parameter.height / Global.WORLD_SCALE) } );
			
			return shape;
		}
		case BS_CIRCLE: {
			CircleShape shape = new CircleShape();
			shape.setRadius(parameter.width / Global.WORLD_SCALE);
			return shape;
		}
		default:
			return null;
		}
	}

	protected Vector2 getCenter() {
		return new Vector2((parameter.position.x + parameter.width / 2) / Global.WORLD_SCALE
				, (parameter.position.y + parameter.height / 2) / Global.WORLD_SCALE);
	}	
	
	@Override
	public void act(float delta) {
		if(this.isMarkedToRemove()) {
			if(destroyListener != null && !destroyListener.onDestory()) {
					return;
			}
			this.destroy();
			return;
		}

		world.step(delta, 3, 3);
		update(delta);
		super.act(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if(region != null) {
			if(scaleX == 1.0f && scaleY == 1.0f && rotation == 0.0f) {
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

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		if(touchListener != null) {
			touchListener.onTouchDown(x, y, pointer);
		}
			
		return super.touchDown(x, y, pointer);
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		if(touchListener != null) {
			touchListener.onTouchUp(x, y, pointer);
		}

		super.touchUp(x, y, pointer);
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		if(touchListener != null) {
			touchListener.onTouchDragged(x, y, pointer);
		}

		super.touchDragged(x, y, pointer);
	}

	@Override
	public boolean touchMoved(float x, float y) {
		if(touchListener != null) {
			touchListener.onTouchMoved(x, y);
		}

		return super.touchMoved(x, y);
	}	
	
	public void setRegion(TextureRegion region) {
		if(this.region == region)
			return;
		
		this.region = region;
	}
	
	public void applyForce(Vector2 force, Vector2 point) {
		if(body != null) {
			body.applyForce(force, point);
		}
	}
	
	public void applyForceToCenter(Vector2 force) {
		if(body != null) {
			body.applyForceToCenter(force);
		}
	}
	public void applyForceToCenter(float forceX, float forceY) {
		if(body != null) {
			body.applyForceToCenter(forceX, forceY);
		}
	}
	
	public void applyVelocity(Vector2 vel) {
		if(body != null) {
			body.setLinearVelocity(vel);
		}
	}
	
	public void applyVelocity(float x, float y) {
		if(body != null) {
			body.setLinearVelocity(x, y);
		}
	}
	
	public void setContactListener(BoxContactListener listener) {
		contactListener = listener;
	}
	
	public void setDestroyListener(BoxDestroyListener listener) {
		destroyListener = listener;
	}
	
	public void SetTouchListener(BoxTouchListener listener) {
		touchListener = listener;
	}

	protected void update(float delta) {
		if(body == null)
			return;
		
		rotation = MathUtils.radiansToDegrees * body.getAngle();

		x = body.getPosition().x * Global.WORLD_SCALE - parameter.width / 2;// * MathUtils.cos(rotation);
		y = body.getPosition().y * Global.WORLD_SCALE - parameter.height / 2;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);		
	}

	protected void destroy() {
		if(body != null) {
			world.destroyBody(body);
			body = null;
		}
		super.remove();
		//super.markToRemove(true);
	}
	
}
