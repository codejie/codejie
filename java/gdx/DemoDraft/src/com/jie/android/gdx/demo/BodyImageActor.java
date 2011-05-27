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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class BodyImageActor extends Image {

	private Body body;
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, PolygonShape shape, float density) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(shape, density);
	}
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, FixtureDef fixturedef) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(fixturedef);	
	}
	
	public void step(float delta) {
		

		//Vector2 vct = new Vector2();

		
		//((PolygonShape)body.getFixtureList().get(0).getShape()).getVertex(0, vct);
		//Transform transform = body.getTransform();
		//transform.mul(vct);
		
		
		//vct = body.getPosition();
		//body.getWorldVector(vct);
		//vct = body.getWorldPoint(vct);
		
		this.rotation = MathUtils.radiansToDegrees * body.getAngle();
		
		//this.x = vct.x * GLOBAL.WORLD_SCALE;// - Math.abs((Math.cos(body.getAngle()) * this.width)));
		//this.y = vct.y * GLOBAL.WORLD_SCALE;// - this.height / 2;

		//this.toLocalCoordinates(vct)
		
		//this.x -= Math.sin(body.getAngle());
		//this.y += Math.sin(body.getAngle());
		
		//Transform transform = body.getTransform();
		//transform.mul(vct);
		
		this.x = body.getPosition().x * GLOBAL.WORLD_SCALE - this.width / 2;
		this.y = body.getPosition().y * GLOBAL.WORLD_SCALE - this.height / 2;
	
	}
	
	protected void draw(SpriteBatch batch, float parentAlpha) {
/*		
		this.x = body.getPosition().x * 100 - this.width / 2;
		this.y = body.getPosition().y * 100 - this.height / 2;
		this.rotation = MathUtils.radiansToDegrees * body.getAngle();	
*/		
		if(GLOBAL.DEBUG == false) {
			super.draw(batch, parentAlpha);
		}
		else {
			DEBUG.renderBody(body);
			super.draw(batch, parentAlpha);
		}
	}
}
