/**
 * project:		DemoDraft
 * file:		ActorStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jie.android.gdx.demo.BodyImageActorElement.ActorShape;
import com.jie.android.gdx.demo.BodyImageActorElement.ActorType;

public class ActorStage extends Stage {
	
	private static int NUMBER_ACTOR		=	5;
	
	private World world;
	
	//private BodyImageActor[] actorArray = new BodyImageActor[NUMBER_ACTOR];
	
	private Group frameGroup = new Group("frame");
	private Group actorGroup = new Group("actor");
	
	private Vector<BodyImageActorElement> actorVector = new Vector<BodyImageActorElement>();
	
	public ActorStage() {
		super(GLOBAL.SCREEN_WIDTH, GLOBAL.SCREEN_HEIGHT, true);
		
		initWorld();
		initActors();
	}
	
	private void initWorld() {
		world = new World(GLOBAL.WORLD_GRAVITY, true);
	
		//create frame
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		
		PolygonShape shape = new PolygonShape();
				
		//bottom
		float width = GLOBAL.SCREEN_WIDTH - GLOBAL.FRAME_BASE * 2;
		float height = GLOBAL.FRAME_BASE;
		float x = GLOBAL.FRAME_BASE;
		float y = GLOBAL.GROUND_Y;
		
		shape.setAsBox(TOOLKIT.Screen2World(width / 2), TOOLKIT.Screen2World(height / 2));	
		def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y), TOOLKIT.Screen2World(width, height)));
	
		BodyImageActor bottom = new BodyImageActor("bottom", new TextureRegion(RESOURCE.colorTexture, 0, 0, 64, 64), world, def, shape, 1.0f);
		bottom.width = width;
		bottom.height = height;
		
		frameGroup.addActor(bottom);
		
		//top
		y = GLOBAL.SKY_Y;
		def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y), TOOLKIT.Screen2World(width, height)));
		
		BodyImageActor top = new BodyImageActor("top", new TextureRegion(RESOURCE.colorTexture, 0, 64 * 3, 64, 64), world, def, shape, 1.0f);
		top.width = width;
		top.height = height;
		
		frameGroup.addActor(top);
		
		//left
		width = GLOBAL.FRAME_BASE;
		height = GLOBAL.SKY_Y -  GLOBAL.GROUND_Y + GLOBAL.FRAME_BASE;
		x = 0;
		y = GLOBAL.GROUND_Y;
		
		shape.setAsBox(TOOLKIT.Screen2World(width / 2), TOOLKIT.Screen2World(height / 2));	
		def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y), TOOLKIT.Screen2World(width, height)));
	
		BodyImageActor left = new BodyImageActor("left", new TextureRegion(RESOURCE.colorTexture, 0, 64, 64, 64), world, def, shape, 1.0f);
		left.width = width;
		left.height = height;
		
		frameGroup.addActor(left);
		
		//right
		width = GLOBAL.FRAME_BASE;
		height = GLOBAL.SKY_Y -  GLOBAL.GROUND_Y + GLOBAL.FRAME_BASE;
		x = GLOBAL.SCREEN_WIDTH - GLOBAL.FRAME_BASE;
		y = GLOBAL.GROUND_Y;
		
		shape.setAsBox(TOOLKIT.Screen2World(width / 2), TOOLKIT.Screen2World(height / 2));	
		def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y), TOOLKIT.Screen2World(width, height)));
	
		BodyImageActor right = new BodyImageActor("right", new TextureRegion(RESOURCE.colorTexture, 0, 64 * 2, 64, 64), world, def, shape, 1.0f);
		right.width = width;
		right.height = height;
		
		frameGroup.addActor(right);

		shape.dispose();	
		
		this.addActor(frameGroup);

	}
	
	private void initActors() {
		
		for(int i = 0; i < NUMBER_ACTOR; ++ i) {
			BodyImageActorElement ele = new BodyImageActorElement();
			
			ele.name = "actor" + i;
			ele.width = 32;
			ele.height = 32;
			ele.x = MathUtils.random(ele.width, GLOBAL.SCREEN_WIDTH - ele.width);
			ele.y = 200 + GLOBAL.GROUND_Y + i * 64;
			ele.type = ActorType.Dynamic;
			ele.shape = ActorShape.Box;
			ele.texture = RESOURCE.colorTexture;
			ele.tx = 0;
			ele.ty = 0;
			
			actorVector.add(ele);
		}
		
		//this.addActor(actorGroup);
	}
	
	private float count = 0.0f;
	private boolean done = false;
	
	public void step(float delta) {
		world.step(delta, 3, 3);
/*	
		int index = 0;
		while(index < root.getGroups().size()) {
			if(root.getGroups().get(index).name != "store") {
				for(int i = 0; i < root.getGroups().get(index).getActors().size(); ++ i) {
					//Gdx.app.log("ActorStage:", "actor name : " + root.getGroups().get(index).getActors().get(i).name);
					((BodyImageActor)root.getGroups().get(index).getActors().get(i)).step(delta);
				}
			}
			++ index;
		}
*/		
		if(count > 1.0f && done == false) {
			
			addBodyImageActor(actorVector.get(1));
			addBodyImageActor(actorVector.get(2));
			
			count = 0.0f;
			done = true;
		}
		else {
			count += delta;
		}
	}
	
	public void addBodyImageActor(BodyImageActorElement ele) {
		
		this.addActor(ele.makeActor(world));
	}
	
	public void removeBodyImageActor(String name) {
		BodyImageActor actor = (BodyImageActor)this.findActor(name);
		if(actor != null) {
			actor.finalize();
			this.removeActor(actor);
		}
	}
	
}
