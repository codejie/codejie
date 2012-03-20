package jie.java.android.boxcatcher;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoxActor extends Actor {

	public enum BoxShape {
		RECTANGLE, CIRCLE, TRIANGLE, LINE
	}
		
	protected World world = null;
	protected Body body = null;	
	protected StageData.Box box = null;
	
	protected TextureRegion texture = null;
	protected Animation animation = null;
	protected float textureWidth = 0.0f;
	protected float textureHeight = 0.0f;
		
	protected BoxContactListener contactListener = null;
	protected BoxDestroyListener destroyListener = null;
	protected BoxTouchListener touchListener = null;
	
	public BoxActor(World world, StageData.Box box) {
		super(box.name);
		
		this.world = world;
		this.box = box;
		
		init();
		makeBox();
	}	
	
	protected void init() {
		x = box.x;
		y = box.y;
		width = box.width;
		height = box.height;
		rotation = MathUtils.radiansToDegrees * box.angle;

		scaleX = 1.0f;
		scaleY = 1.0f;
		
		switch(box.shape) {
		case RECTANGLE: {
			originX = box.width / 2.0f;
			originY = box.height / 2.0f;
			
			textureWidth = box.width;
			textureHeight = box.height;

			break;
		}
		case CIRCLE: {
			originX = box.width / 2.0f;
			originY = box.width / 2.0f;
			
			textureWidth = box.width;
			textureHeight = box.width;

			break;
		}
		case TRIANGLE: {
			originX = 0;//-box.width / 2.0f;
			originY = 0;//-box.height;// / 2.0f;// / 2.0f;

			textureWidth = box.width;
			textureHeight = box.height;

			break;
		}
		case LINE: {
			originX = box.width;
			originY = box.height;// / 2.0f;			
			
			textureWidth = 0.0f;
			textureHeight = 0.0f;
			
			break;
		}
		default: {
			originX = box.width;
			originY = box.height;// / 2.0f;			
			
			textureWidth = 0.0f;
			textureHeight = 0.0f;
			
			break;
		}
		}
	}
	
	//
	protected void makeBox() {
		BodyDef def = new BodyDef();
		def.type = box.type;
		def.angle = box.angle;
		def.position.set(getCenter());
		//def.angularDamping = 1.0f;
		def.linearDamping = 0.0f;
		
		Shape shape = makeShape();

		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = box.restitution;
		fd.density = box.density;
		fd.friction = box.friction;
		fd.filter.categoryBits = box.filterBits;
		fd.filter.maskBits = box.filterBits;
		
		body = world.createBody(def);
		body.createFixture(fd);

		shape.dispose();
		Gdx.app.log("tag", this.name + " body mass = " + body.getMass());
		
		body.setUserData(this);
	}
 
	private Shape makeShape() {
		
		switch(box.shape) {
		case RECTANGLE: {
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(box.width / Global.WORLD_SCALE / 2, box.height / Global.WORLD_SCALE / 2);
			
			return shape;
		}
		case TRIANGLE: {
			PolygonShape shape = new PolygonShape();
			
			shape.set(new Vector2[] { new Vector2((box.width / 2) / Global.WORLD_SCALE, box.height / Global.WORLD_SCALE)
									, new Vector2(0.0f, 0.0f)
									, new Vector2(box.width / Global.WORLD_SCALE, 0.0f) } );
			
			return shape;
		}
		case CIRCLE: {
			CircleShape shape = new CircleShape();
			shape.setRadius(box.width / 2 / Global.WORLD_SCALE);
			return shape;
		}
		case LINE: {
			EdgeShape shape = new EdgeShape();
			shape.set(box.x / Global.WORLD_SCALE, box.y / Global.WORLD_SCALE
					, box.width / Global.WORLD_SCALE, box.height / Global.WORLD_SCALE); 
			return shape;
		}
		default:
			return null;
		}
	}

	protected Vector2 getCenter() {
		switch(box.shape) {
		case RECTANGLE: {
			return new Vector2((box.x + box.width / 2) / Global.WORLD_SCALE, (box.y + box.height / 2) / Global.WORLD_SCALE);
		}
		case CIRCLE: {
			return new Vector2((box.x + box.width / 2) / Global.WORLD_SCALE, (box.y + box.width / 2) / Global.WORLD_SCALE);
		}
		case TRIANGLE: {
			return new Vector2((box.x) / Global.WORLD_SCALE, (box.y) / Global.WORLD_SCALE);
		}
		case LINE: {
			return new Vector2(0, 0);
		}
		default: {
			return new Vector2(0, 0);
		}
		}
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

		//world.step(1/30f, 3, 3);
		
		box.stateTime += delta;
		
		update(delta);
		
		super.act(delta);
	}

	protected void update(float delta) {
		if(body == null)
			return;

		if(animation != null) {
			setTexture(animation.getKeyFrame(box.stateTime, true));
		}
		
		//if(texture != null) {
			rotation = MathUtils.radiansToDegrees * body.getAngle();
			
			switch(box.shape) {
			case RECTANGLE: {
				x = body.getPosition().x * Global.WORLD_SCALE - box.width / 2;// * MathUtils.cos(rotation);
				y = body.getPosition().y * Global.WORLD_SCALE - box.height / 2;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);
				break;
			}
			case CIRCLE: {
				x = body.getPosition().x * Global.WORLD_SCALE - box.width / 2;// * 2;// * MathUtils.cos(rotation);
				y = body.getPosition().y * Global.WORLD_SCALE - box.width / 2;// * 2;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);
				break;
			}
			case TRIANGLE: {
				x = body.getPosition().x * Global.WORLD_SCALE;// - box.width / 2;// * MathUtils.cos(rotation);
				y = body.getPosition().y * Global.WORLD_SCALE;// - box.height / 2 ;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);
				break;
			}
			case LINE: {
				x = body.getPosition().x * Global.WORLD_SCALE;// - box.width / 2;// * MathUtils.cos(rotation);
				y = body.getPosition().y * Global.WORLD_SCALE;// - box.height / 2 ;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);
				break;
			}
			default: {
				x = body.getPosition().x * Global.WORLD_SCALE;// - box.width / 2;// * MathUtils.cos(rotation);
				y = body.getPosition().y * Global.WORLD_SCALE;// - box.height / 2 ;// * MathUtils.sin(rotation);// / MathUtils.sin(rotation);				
			}
			}

		//}
		
		//Gdx.app.log("tag", "body x = " + body.getPosition().x + " y = " + body.getPosition().y);
	}	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if(texture != null) {
			if(scaleX == 1.0f && scaleY == 1.0f && rotation == 0.0f) {
				batch.draw(texture, x, y, textureWidth, textureHeight);
			}
			else {
				batch.draw(texture, x, y, originX, originY, textureWidth, textureHeight, scaleX, scaleY, rotation);
			}
		}
	}

	@Override
	public Actor hit(float x, float y) {
		
		//Vector2 tmp = new Vector2(x, y);
		//this.toLocalCoordinates(tmp);
		
		return (x > 0 && x < width && y > 0 && y < height) ? this : null;
	}	

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		if(touchListener != null) {
			return touchListener.onTouchDown(this, x, y, pointer);
		}
		else {
			return super.touchDown(x, y, pointer);
		}
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		if(touchListener != null) {
			touchListener.onTouchUp(this, x, y, pointer);
		}
		else {
			super.touchUp(x, y, pointer);
		}
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		if(touchListener != null) {
			touchListener.onTouchDragged(this, x, y, pointer);
		}
		else {
			super.touchDragged(x, y, pointer);
		}
	}

	@Override
	public boolean touchMoved(float x, float y) {
		if(touchListener != null) {
			if(hit(x, y) != null) {
				return touchListener.onTouchMoved(this, x, y);
			}
		}

		return super.touchMoved(x, y);
	}	
	
	public void setTexture(TextureRegion texture) {
		if(this.texture == texture)
			return;
		
		this.texture = texture;
	}
	
	public void setAnimation(Animation animation) {
		if(this.animation == animation)
			return;
		this.animation = animation;
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
	
	protected void destroy() {
		if(body != null) {
			world.destroyBody(body);
			body = null;
		}
		super.remove();
		//super.markToRemove(true);
	}
	
	public StageData.Box getBox() {
		box.x = body.getPosition().x;
		box.y = body.getPosition().y;
		box.angle = body.getAngle();
		
		return box;
	}
	
	public int getId() {
		return box.id;
	}
}
