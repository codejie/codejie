/**
 * project:		DemoDraft
 * file:		BodyImageActor.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 25, 2011
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class BodyImageActor extends Image {

	private Body body = null;
	
	private MouseJoint mouseJoint = null;
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, Shape shape, float density) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(shape, density);
		body.setUserData(this);
	}
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, FixtureDef fixturedef) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(fixturedef);	
		body.setUserData(this);
	}
	
	public void destroyBody() {
		if (body != null) {
			World world = body.getWorld();
			world.destroyBody(body);
			body = null;
		}
	}
	
	public Body getBody() {
		return body;
	}
	
	public void remove() {
		this.destroyBody();
		super.remove();
	}
	
	public void applyForce(Vector2 force, Vector2 point) {
		if (body != null) {
			body.applyForce(force, point);//Bugs? should use World coordinate?
		}
	}
	
	public float getBodyMass() {
		if (body != null) {
			return body.getMass();
		}
		else {
			return 0.0f;
		}
	}
	
	public boolean isBodyDestroy() {
		return ((body == null) ? true : false);
	}
	
	public void setBodyActive(boolean active) {
		if (body != null) {
			body.setActive(active);
		}
	}
	
	public boolean isBodyActive() {
		if (body != null) {
			return body.isActive();
		}
		return false;
	}
	
	public void makeMouseJoint(BodyImageActor other, Vector2 target) {
		if (body != null) {
			World world = body.getWorld();

			if (mouseJoint != null) {
				world.destroyJoint(mouseJoint);
			}
			MouseJointDef def = new MouseJointDef();
			def.bodyA = other.getBody();
			def.bodyB = body;
			def.target.set(target);
			def.maxForce = 10.0f * GLOBAL.WORLD_GRAVITY.y;
			
			mouseJoint = (MouseJoint)world.createJoint(def);
			
			body.setAwake(true);			
		}		
	}
	
	public void clearMouseJoint() {
		if (body != null) {
			if (mouseJoint != null) {
				body.getWorld().destroyJoint(mouseJoint);
				mouseJoint = null;
			}
		}
	}
	
	public void refreshMouseJoint(Vector2 target) {
		if (mouseJoint != null) {
			mouseJoint.setTarget(target);
		}
	}
	
	protected void draw(SpriteBatch batch, float parentAlpha) {
		if (body != null) {
			this.x = body.getPosition().x * GLOBAL.WORLD_SCALE - this.width / 2;
			this.y = body.getPosition().y * GLOBAL.WORLD_SCALE - this.height / 2;
		
			this.rotation = MathUtils.radiansToDegrees * body.getAngle();
		}

		if(GLOBAL.DEBUG == false) {
			super.draw(batch, parentAlpha);
		}
	}
	
}
