package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RectangleBox extends BoxActor {

	public RectangleBox(World world, Parameter param) {
		super(world, param);
	}

	@Override
	protected void makeBox() {		
		BodyDef def = new BodyDef();
		def.type = parameter.type;// BodyType.KinematicBody;// (parameter.type == BoxType.BT_DYNAMIC ? BodyType.DynamicBody : BodyType.StaticBody);
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
		super.update(delta);
		
		//Gdx.app.log("tag", "orgx = " + this.originX);		
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

	private float ox = -1.0f, oy = -1.0f;
	private boolean pressed = false;
	@Override
	public boolean touchDown(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		Gdx.app.log("tag", "touchDown" + " x = " + x + " orgX = " + this.originX);
		
		ox = x;
		oy = y;
		
		if(hit(x, y) == this)
			pressed = true;
		//applyForceToCenter(new Vector2(1, 0));
		
		//body.setLinearVelocity(0.01001f, 0.0f);
		
		//this.body.ap
		
		return super.touchDown(x, y, pointer);
	}	
	
	@Override
	public void touchUp(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		Gdx.app.log("tag", "touchUp");
		pressed = false;
		ox = -1.0f;
		oy = -1.0f;
		super.touchUp(x, y, pointer);
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		// TODO Auto-generated method stub
		//if(ox != -1.0f && oy != -1.0f)
			//applyForceToCenter(new Vector2(x - ox, y - oy));
		Gdx.app.log("tag", "touchDragged");
		super.touchDragged(x, y, pointer);
	}

	@Override
	public boolean touchMoved(float x, float y) {
		// TODO Auto-generated method stub
		//if(ox != -1.0f && oy != -1.0f)
			//applyForceToCenter(new Vector2(x - ox, y - oy));
		Vector2 tmp = new Vector2(x , y);
		tmp = body.getWorldVector(tmp);
		if(hit(x, y) == this) {
			Gdx.app.log("tag", "x = " + x + "  tmp.x= " + tmp.x + " p.x = " + body.getPosition().x + " (x - c.x) = " + (tmp.x / Global.WORLD_SCALE - body.getPosition().x));
			applyForceToCenter((x / Global.WORLD_SCALE - width / (2 * Global.WORLD_SCALE)) * 1, 0);
			
			
			
		//if(pressed == true) {
			//Gdx.app.log("tag", "x = " + x + " tmp.x = " + tmp.x + " this.x = " + this.x);
			//applyForceToCenter(tmp.x - this.x / Global.WORLD_SCALE, 0);
		}
		
		return super.touchMoved(x, y);
	}

}
