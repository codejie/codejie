/**
 * project:		DemoDraft
 * file:		ActorElement.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 27, 2011
 */


package com.jie.android.gdx.demo;

import com.badlogic.gdx.graphics.Texture;

public final class BodyImageActorElement {
	
	public enum ActorType {
		Static, Dynamic
	}
	public enum ActorShape {
		Box, Circle
	}
	
	public String name;
	
	public float x, y;
	public float width, height;
	public float angle;
	
	public ActorType type;
	public ActorShape shape;
	
	public float density = 1.0f;
	public float restitution = 0.0f;
	public float friction = 0.0f;
	
	public Texture texture;
	public float tx, ty;

}
