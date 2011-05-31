/**
 * project:		DemoDraft
 * file:		ActorEventManager.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 31, 2011
 */


package com.jie.android.gdx.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ActorEventManager {
	
	private class RemoveElement {
		public boolean hasStageParent = true;
		public Stage stage = null;
		public Group group = null;
		
		public void clean(BodyImageActor actor) {
			
		}
		
		public void exec(BodyImageActor actor) {
			actor.destoryBody();
			if (hasStageParent == true) {
				stage.removeActor(actor);
			}
			else {
				group.removeActor(actor);
			}
		}
	}
	
	private class ActionElement {
		public ArrayList<Action> actionList = null;
		
		public void clean(BodyImageActor actor) {
			actionList.clear();
		}
		
		public void exec(BodyImageActor actor) {
			
		}
	}
	
	private class ForceElement {
		private class Force {
			public Vector2 force = null;
			public Vector2 point = null;
						
			public Force(final Vector2 f, final Vector2 p) {
				force = f;
				point = p;
			}
		}
		public ArrayList<Force> forceList = new ArrayList<Force>();
		
		
		public void addForce(final Vector2 f, final Vector2 p) {
			this.forceList.add(new Force(f, p));
		}
		
		public void clean(BodyImageActor actor) {
			forceList.clear();
		}
		
		public void exec(BodyImageActor actor) {
			for (Force it:this.forceList) {
				actor.applyForce(it.force, it.point);
			}
		}		
	}
	
	protected class EventElement {
		public RemoveElement removeElement = null;
		public ActionElement actionElement = null;
		public ForceElement forceElement = null;
	}
	
	protected HashMap<BodyImageActor, EventElement> eventMap = new HashMap<BodyImageActor, EventElement>();
	
	//
	
	public void markToRemove(BodyImageActor actor, Stage stage) {
		
		EventElement ele = eventMap.get(actor);
		if (ele != null) {
			if(ele.removeElement == null) {
				if (ele.actionElement != null) {
					ele.actionElement.clean(actor);
					ele.actionElement = null;
				}
				if (ele.forceElement != null) {
					ele.forceElement.clean(actor);
					ele.forceElement = null; 
				}
				
				ele.removeElement = new RemoveElement();
				ele.removeElement.hasStageParent = true;
				ele.removeElement.stage = stage;
			}
		}
		else {
			ele = new EventElement();
			ele.removeElement = new RemoveElement();
			ele.removeElement.hasStageParent = true;
			ele.removeElement.stage = stage;
			
			eventMap.put(actor, ele);
		}		
	}
	
	public void markToRemove(BodyImageActor actor, Group group) {
		EventElement ele = eventMap.get(actor);
		if (ele == null) {
			ele = new EventElement();
			ele.removeElement = new RemoveElement();
			ele.removeElement.hasStageParent = false;
			ele.removeElement.group = group;
			
			eventMap.put(actor, ele);
		}
		else {
			if(ele.removeElement == null) {
				if (ele.actionElement != null) {
					ele.actionElement.clean(actor);
					ele.actionElement = null;
				}
				if (ele.forceElement != null) {
					ele.forceElement.clean(actor);
					ele.forceElement = null; 
				}
				
				ele.removeElement = new RemoveElement();
				ele.removeElement.hasStageParent = false;
				ele.removeElement.group = group;
			}
		}
	}
	
	public void applyForce(BodyImageActor actor, Vector2 force, Vector2 point) {
		EventElement ele = eventMap.get(actor);
		if (ele == null) {
			ele = new EventElement();
			ele.forceElement = new ForceElement();
			ele.forceElement.addForce(force, point);
			
			eventMap.put(actor, ele);
		}
		else {
			
		}
	}
	
	public void clearForces(BodyImageActor actor) {
		EventElement ele = eventMap.get(actor);
		if (ele != null) {
			if (ele.forceElement != null) {
				ele.forceElement.clean(actor);
				ele.forceElement = null;
			}
		}
		else {
			
		}		
	}
	
	
	public void step(float delta) {
	
		Iterator<Map.Entry<BodyImageActor, EventElement>> it = eventMap.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry<BodyImageActor, EventElement> ele = (Map.Entry<BodyImageActor, EventElement>)it.next();
			
			if (ele != null) {
				if (ele.getValue().actionElement != null) {
					ele.getValue().actionElement.exec(ele.getKey());
					it.remove();
					//eventMap.remove(ele.getKey());
				}
				else if (ele.getValue().forceElement != null) {
					ele.getValue().forceElement.exec(ele.getKey());
				}
				else if (ele.getValue().removeElement != null) {
					ele.getValue().removeElement.exec(ele.getKey());
					it.remove();
					//eventMap.remove(ele.getKey());
				}
			}
		}	
	}
}
