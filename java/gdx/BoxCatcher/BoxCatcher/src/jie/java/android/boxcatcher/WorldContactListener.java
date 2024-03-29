package jie.java.android.boxcatcher;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		BoxActor a = (BoxActor)(contact.getFixtureA().getBody().getUserData());
		BoxActor b = (BoxActor)(contact.getFixtureB().getBody().getUserData());
		
		if(a.contactListener != null) {
			a.contactListener.onBeginContact(a, b);
		}
		if(b.contactListener != null) {
			b.contactListener.onBeginContact(b, a);
		}
	}

	@Override
	public void endContact(Contact contact) {
		BoxActor a = (BoxActor)(contact.getFixtureA().getBody().getUserData());
		BoxActor b = (BoxActor)(contact.getFixtureB().getBody().getUserData());
		
		if(a.contactListener != null) {
			a.contactListener.onEndContact(a, b);
		}
		if(b.contactListener != null) {
			b.contactListener.onEndContact(b, a);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
