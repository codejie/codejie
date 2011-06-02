/**
 * project:		DemoDraft
 * file:		ActorStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.jie.android.gdx.demo.BodyImageActorElement.ActorShape;
import com.jie.android.gdx.demo.BodyImageActorElement.ActorType;

public class ActorStage extends Stage {
	
	private int actorID = 0;
	
	private static int NUMBER_ACTOR		=	1;//5;
	
	private World world;
	private WorldDebugRenderer worldRenderer = null;
	
	private ActorContactListener actorContactListener;
	
	private ActorEventManager actorEventManager;
	
	private Group frameGroup = new Group("frame");
	private Group actorGroup = new Group("actor");
	
	private Vector<BodyImageActorElement> actorVector = new Vector<BodyImageActorElement>();
	private BodyImageActor hitActor = null;
	
	public ActorStage() {
		super(GLOBAL.SCREEN_WIDTH, GLOBAL.SCREEN_HEIGHT, true);

		//if (GLOBAL.DEBUG == true)
			worldRenderer = new WorldDebugRenderer();
		
		actorEventManager = new ActorEventManager();
		
		initWorld();
		initActors();
	}
	
	private void initWorld() {
		world = new World(GLOBAL.WORLD_GRAVITY, true);
	
		actorContactListener = new ActorContactListener();
		actorContactListener.setStage(this);
		
		world.setContactListener(actorContactListener);
		
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
			
			ele.name = "box" + i;
			ele.width = 64;
			ele.height = 64;
			ele.x = MathUtils.random(ele.width, GLOBAL.SCREEN_WIDTH - ele.width);
			ele.y = GLOBAL.GROUND_Y + MathUtils.random(0, 128);
			ele.type = ActorType.Dynamic;
			ele.shape = ActorShape.Box;
			ele.texture = RESOURCE.boxTexture;
			ele.tx = 0;
			ele.ty = 0;
			
			actorVector.add(ele);
		}
		
		for (int i = 0; i < NUMBER_ACTOR; ++ i) {
			BodyImageActorElement ele = new BodyImageActorElement();
			
			ele.name = "circle" + i;
			ele.width = 64;
			ele.height = 64;
			ele.x = MathUtils.random(ele.width, GLOBAL.SCREEN_WIDTH - ele.width);
			ele.y = GLOBAL.GROUND_Y + MathUtils.random(0, 128);
			ele.type = ActorType.Dynamic;
			ele.shape = ActorShape.Circle;
			ele.texture = RESOURCE.ballTexture;
			ele.tx = 0;
			ele.ty = 0;
			
			actorVector.add(ele);
		}
		
		this.addActor(actorGroup);
		
		addBodyImageActor(actorVector.get(1));
		addBodyImageActor(actorVector.get(0));
	}
	
	private float count = 0.0f;
	private boolean done = false, done2 = false;
	
	public void step(float delta) {
		
		actorEventManager.step(delta);
		
		world.step(delta, 3, 3);
		//worldRenderer.render(world);
		this.act(delta);
		
		if (GLOBAL.ADDACTOR == true) {
			
			int index = ((int)(delta * 1000)) % (NUMBER_ACTOR * 2); 
			addBodyImageActor(actorVector.get(index));
			
			GLOBAL.ADDACTOR = false;
		}
		
/*	
		if(count > 1.0f && done == false) {
			
			addBodyImageActor(actorVector.get(1));
			addBodyImageActor(actorVector.get(6));

			
			count = 0.0f;
			
			if(done2 == false) {
				if(actorID > 30) {
					BodyImageActor actor = (BodyImageActor)actorGroup.findActor("actor10");
					if(actor != null) {
						applyForce(actor, new Vector2(0, 1f * actor.getBodyMass()), new Vector2(0, 0));
						done2 = true;
					}
				}
			}
			
			done = true;
		
		}
		
		if (count > 2.0f && done2 == false) {
			BodyImageActor actor = (BodyImageActor)actorGroup.findActor("actor0");
			//applyForce(actor, new Vector2(0, 1f * actor.getBodyMass()), new Vector2(0, 0));
			//done2 = true;
			//actor.applyForce(new Vector2(0, 2f * actor.getBodyMass()), new Vector2(0, 0));
			if(actor != null) {
				MoveTo action = MoveTo.$(0.0f, 0.0f,1.0f);
				actorEventManager.applyAction(actor, action);
			}
			done2 = true;
		}
		{
			count += delta;
		}
*/		
	}
	
	public void draw() {
		super.draw();

		if (GLOBAL.DEBUG == true)
			worldRenderer.render(world);
	}
	
	public void addBodyImageActor(BodyImageActorElement ele) {
		//ele.name = "actor" + actorID ++;
		actorGroup.addActor(ele.makeActor(world));
		//this.addActor(ele.makeActor(world));
	}
	
	public void applyForce(BodyImageActor actor, Vector2 force, Vector2 point) {
		actorEventManager.applyForce(actor, force, point);
	}
	
	public void makeMouseJoint(BodyImageActor actor, Vector2 vct) {
		actor.makeMouseJoint((BodyImageActor)frameGroup.findActor("bottom"), vct);	
	}
	
	public void onBeginActorContact(BodyImageActor a, BodyImageActor b) {
		//Gdx.app.log("ActorStage: BEGIN - ", ("Contact: a - " + a.name + " b - " + b.name));
		
		//if ()
		
/*		
		if(a.name.startsWith("actor1") && b.name.startsWith("top")) {
			Gdx.app.log("ActorStage: ", ("Contact: a - " + a.name + " b - " + b.name));
			actorEventManager.clearForces(a);
		}
		else if(a.name.startsWith("top") && b.name.startsWith("actor1")) {
			Gdx.app.log("ActorStage: ", ("Contact: a - " + a.name + " b - " + b.name));
			actorEventManager.clearForces(b);
		}
*/
		//GLOBAL.DEBUG = !GLOBAL.DEBUG;
	}
	
	public void onEndActorContact(BodyImageActor a, BodyImageActor b) {
		//Gdx.app.log("ActorStage: END - ", ("Contact: a - " + a.name + " b - " + b.name));
		
		if (hitActor != null) {
			if (a.name == "top" || a.name == "bottom" || a.name == "left" || a.name == "right")
				return;
			if (b.name == "top" || b.name == "bottom" || b.name == "left" || b.name == "right")
				return;

			if ((a == hitActor || b == hitActor)) {
				if (GLOBAL.COLLISION == false) {
					final Stage stage = this;
					final Actor actor = hitActor;
					actorEventManager.addAction(hitActor, 
							MoveTo.$(GLOBAL.SCREEN_WIDTH - hitActor.width, GLOBAL.SCREEN_HEIGHT - hitActor.height, 2).setCompletionListener(new OnActionCompleted() {
								public void completed(Action action) {
									stage.removeActor(actor);
								}
							}
						)
					);
					hitActor = null;
				}
			}
		}
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		boolean touch = super.touchDown(x, y, pointer, button);
		if (touch) {
			if (hitActor != null) {
				hitActor.clearMouseJoint();
			}
			hitActor = (BodyImageActor)this.getLastTouchedChild();
			if(hitActor != null){				
				hitActor.makeMouseJoint((BodyImageActor)frameGroup.findActor("bottom"), TOOLKIT.Screen2World(TOOLKIT.Frame2Screen(x, y)));
			}
		}
/*		
		BodyImageActor actor = (BodyImageActor)this.getLastTouchedChild();
		if (actor != null) {
			Gdx.app.log("ActorStage: - Down - ", "x : " + x + " - y : " + y);
			
			Gdx.app.log("ActorStage: - Actor - ", "x : " + actor.x + " - y : " + actor.y);
			Vector2 local = actor.getBody().getPosition();
			Gdx.app.log("ActorStage: - Body local - ", "x : " + local.x + " - y : " + local.y);
			Vector2 wo = actor.getBody().getWorldPoint(local);
			Gdx.app.log("ActorStage: - Body world - ", "x : " + wo.x + " - y : " + wo.y);
			
			if (mj == null) {
			Gdx.app.log("ActorStage: ", "x : " + x + " - y : " + y);
			
			MouseJointDef def = new MouseJointDef();
			def.bodyB = actor.getBody();//((BodyImageActor)actorGroup.findActor("actor1")).getBody();
			def.bodyA = ((BodyImageActor)frameGroup.findActor("bottom")).getBody();
			
			def.target.set(x / GLOBAL.WORLD_SCALE, (GLOBAL.SCREEN_HEIGHT - y) / GLOBAL.WORLD_SCALE);
			def.maxForce = 1.8f * def.bodyB.getMass();
			def.dampingRatio = 1.0f;
			//def.frequencyHz = 5;
			//def.bodyA.setAwake(true);
			mj = (MouseJoint)world.createJoint(def);
			//((BodyImageActor)actorGroup.findActor("actor1")).getBody().setAwake(true);
			}
			
			
			//actorEventManager.markToRemove(actor);
			//MoveTo action = MoveTo.$(0.0f, 400.0f, 3.0f);
			//actorEventManager.applyAction(actor, action);
			
			//actor.setBodyActive(!actor.isBodyActive());
			//makeMouseJoint(actor, new Vector2(x / GLOBAL.WORLD_SCALE, y / GLOBAL.WORLD_SCALE));
		}
		}
*/		
		return touch;		
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		boolean touch = super.touchUp(x, y, pointer, button);
		if (hitActor != null) {
			hitActor.clearMouseJoint();
			hitActor = null;
		}
/*		
		if (mj != null) {
			world.destroyJoint(mj);
			mj = null;
		}
*/		
		return touch;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		if (hitActor != null) {
			hitActor.refreshMouseJoint(TOOLKIT.Screen2World(TOOLKIT.Frame2Screen(x, y)));
		}
/*		
		if (mj != null) {
			Gdx.app.log("ActorStage: ", "x : " + x + " - y : " + y);
			Gdx.app.log("ActorStage mj - ", "x : " + mj.getTarget().x + " - y : " + mj.getTarget().y); 
			mj.setTarget(new Vector2(x / GLOBAL.WORLD_SCALE, (GLOBAL.SCREEN_HEIGHT - y) / GLOBAL.WORLD_SCALE));
		}
*/		
		return super.touchDragged(x, y, pointer);
	}
	
}
