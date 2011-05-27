/**
 * project:		DemoDraft
 * file:		ActorElement.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 27, 2011
 */


package com.jie.android.gdx.demo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyImageActorElement {
	
	public enum ActorType {
		Static, Dynamic
	}
	public enum ActorShape {
		Box, Circle
	}
	
	public String name;// = new String();
	
	public float x, y;
	public float width, height;
	public float angle = 0.0f;
	
	public ActorType type;
	public ActorShape shape;
	
	public float density = 1.0f;
	public float restitution = 0.0f;
	public float friction = 0.0f;
	
	public Texture texture;
	public float tx, ty;
	
	public int data = 0;

	public BodyDef makeBodyDef() {
		BodyDef def = new BodyDef();
		if(type == ActorType.Dynamic) {
			def.type = BodyType.DynamicBody;
		}
		else {
			def.type = BodyType.StaticBody;
		}
		def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y),TOOLKIT.Screen2World(width, height)));
		def.angle = angle;
		
		return def;
	}
	
	public PolygonShape makePolygonShape() {
		PolygonShape pshape = new PolygonShape();
		if(shape == ActorShape.Box) {
			pshape.setAsBox(TOOLKIT.Screen2World(width) / 2, TOOLKIT.Screen2World(height) / 2);
		}
		else {
			pshape.setRadius(TOOLKIT.Screen2World(width) / 2);
		}
		
		return pshape;
	}
	
	public FixtureDef makeFixtureDef(PolygonShape shape) {
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = restitution;
		fd.density = density;
		fd.friction = friction;
		
		return fd;
	}
	
	public BodyImageActor makeActor(World world) {
		
		BodyDef def = makeBodyDef();
		PolygonShape pshape = makePolygonShape();
		FixtureDef fd = makeFixtureDef(pshape);
		
		BodyImageActor actor = new BodyImageActor(name, new TextureRegion(texture, tx, ty, width, height), world, def, fd);
		actor.x = x;
		actor.y = y;
		actor.width = width;
		actor.height = height;
		
		pshape.dispose();
		
		return actor;
	}
}
