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

	private World pworld = null;
	private Body body = null;
	
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, PolygonShape shape, float density) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(shape, density);
		
		pworld = world; 
	}
	
	public BodyImageActor(String name, TextureRegion texture, World world, BodyDef def, FixtureDef fixturedef) {
		super(name, texture);
		
		body = world.createBody(def);
		body.createFixture(fixturedef);	
		
		pworld = world;
	}
	
	public void finalize() {
		if(pworld != null && body != null) {
			pworld.destroyBody(body);
			body = null;
		}
	}
	
	public void step(float delta) {
	
		this.rotation = MathUtils.radiansToDegrees * body.getAngle();
		
		this.x = body.getPosition().x * GLOBAL.WORLD_SCALE - this.width / 2;
		this.y = body.getPosition().y * GLOBAL.WORLD_SCALE - this.height / 2;
	
	}
	
	protected void draw(SpriteBatch batch, float parentAlpha) {
		step(0.0f);
		
		if(GLOBAL.DEBUG == false) {
			super.draw(batch, parentAlpha);
		}
		else {
			DEBUG.renderBody(body);
			super.draw(batch, parentAlpha);
		}
	}
}
