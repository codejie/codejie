/**
 * project:		DemoDraft
 * file:		ActorStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

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

public class ActorStage extends Stage {
	
	private static int NUMBER_ACTOR		=	5;
	
	private World world;
	
	//private BodyImageActor[] actorArray = new BodyImageActor[NUMBER_ACTOR];
	
	private Group frameGroup = new Group("frame");
	private Group actorGroup = new Group("actor");
	private Group storeGroup = new Group("stroe");
	
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
		float width = 32;
		float height = 32;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(TOOLKIT.Screen2World(width) / 2, TOOLKIT.Screen2World(height) / 2);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.restitution = 0.1f;
		fd.density = 1.0f;
	
		for(int i = 0; i < NUMBER_ACTOR; ++ i) {
			float x = MathUtils.random(width, width + 32);//GLOBAL.SCREEN_WIDTH - width);
			float y = 300 + GLOBAL.GROUND_Y + i * 64;
			def.position.set(TOOLKIT.getWorldBoxCenter(TOOLKIT.Screen2World(x, y), TOOLKIT.Screen2World(width, height)));
			def.angle = i * 0.5f;
			
			BodyImageActor actor = new BodyImageActor("actor" + i, new TextureRegion(RESOURCE.colorTexture, 0, 64 * (i % 4), 32, 32), world, def, fd);

			storeGroup.addActor(actor);
			//actorArray[i].width = width;
			//actorArray[i].height = height;
			
			//this.addActor(actorArray[i]);
		}
		
		shape.dispose();
		
		
		actorGroup.addActor(storeGroup.getActors().get(0));
		actorGroup.addActor(storeGroup.getActors().get(1));
		
		this.addActor(actorGroup);
	}
	
	private float count = 0.0f;
	private boolean done = false;
	public void step(float delta) {
		world.step(delta, 3, 3);
		
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
		
		if(count > 5.0f && done == false) {
			actorGroup.addActor(storeGroup.getActors().get(2));
			actorGroup.addActor(storeGroup.getActors().get(3));
			actorGroup.removeActor(storeGroup.getActors().get(0));
			count = 0.0f;
			done = true;
		}
		else {
			count += delta;
		}
	}
	
	protected BodyImageActor createBodyImageActor(BodyImageActorElement elm) {
		
	}
	
	public void addBodyImageActor(BodyImageActor actor) {
		
	}
	
	public void removeBodyImageActor(String name) {
		
	}
	
}
