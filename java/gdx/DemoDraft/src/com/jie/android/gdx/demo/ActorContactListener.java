

package com.jie.android.gdx.demo;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ActorContactListener implements ContactListener {

	private ActorStage actorStage = null;
	
	public void setStage(ActorStage stage) {
		actorStage = stage;
	}
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(actorStage != null) {
			actorStage.onActorContact((BodyImageActor)fa.getBody().getUserData(), (BodyImageActor)fb.getBody().getUserData());
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(actorStage != null) {
			actorStage.onActorContact((BodyImageActor)fa.getBody().getUserData(), (BodyImageActor)fb.getBody().getUserData());
		}
	}

}
