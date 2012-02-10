package jie.java.android.boxcatcher;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;


public class WorldDebugRenderer {

	protected ImmediateModeRenderer10 renderer;

	private static Vector2[] vertices = new Vector2[100];

	public WorldDebugRenderer () {
		renderer = new ImmediateModeRenderer10();

		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new Vector2();
	}

	public void render (World world) {
		renderBodies(world);
	}

	private final Color SHAPE_NOT_ACTIVE = new Color(0.5f, 0.5f, 0.3f, 1);
	private final Color SHAPE_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
	private final Color SHAPE_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
	private final Color SHAPE_NOT_AWAKE = new Color(0.6f, 0.6f, 0.6f, 1);
	private final Color SHAPE_AWAKE = new Color(0.9f, 0.7f, 0.7f, 1);
	private final Color JOINT_COLOR = new Color(0.5f, 0.8f, 0.8f, 1);

	private void renderBodies (World world) {
		for (Iterator<Body> iter = world.getBodies(); iter.hasNext();) {
			Body body = iter.next();
		
			Transform transform = body.getTransform();
			int len = body.getFixtureList().size();
			List<Fixture> fixtures = body.getFixtureList();
			for (int i = 0; i < len; i++) {
				Fixture fixture = fixtures.get(i);
				if (body.isActive() == false)
					drawShape(fixture, transform, SHAPE_NOT_ACTIVE);
				else if (body.getType() == BodyType.StaticBody)
					drawShape(fixture, transform, SHAPE_STATIC);
				else if (body.getType() == BodyType.KinematicBody)
					drawShape(fixture, transform, SHAPE_KINEMATIC);
				else if (body.isAwake() == false)
					drawShape(fixture, transform, SHAPE_NOT_AWAKE);
				else
					drawShape(fixture, transform, SHAPE_AWAKE);
			}
		
		}

		for (Iterator<Joint> iter = world.getJoints(); iter.hasNext();) {
			Joint joint = iter.next();
			drawJoint(joint);
		}

		int len = world.getContactList().size();
		for (int i = 0; i < len; i++)
			drawContact(world.getContactList().get(i));
		
	}
	
	private static Vector2 t = new Vector2();
	private static Vector2 axis = new Vector2();

	private void drawShape (Fixture fixture, Transform transform, Color color) {
		if (fixture.getType() == Type.Circle) {
			CircleShape circle = (CircleShape)fixture.getShape();
			t.set(circle.getPosition());
			transform.mul(t);
			drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]),color);
		} else {
		
			PolygonShape poly = (PolygonShape)fixture.getShape();
			int vertexCount = poly.getVertexCount();
			for (int i = 0; i < vertexCount; i++) {
				poly.getVertex(i, vertices[i]);
				transform.mul(vertices[i]);
			}
			drawSolidPolygon(vertices, vertexCount, color);
		}
	}

	private final Vector2 v = new Vector2();

	private void drawSolidCircle (Vector2 center, float radius, Vector2 axis, Color color) {
		renderer.begin(GL10.GL_LINE_LOOP);
		float angle = 0;
		float angleInc = 2 * (float)Math.PI / 20;
		for (int i = 0; i < 20; i++, angle += angleInc) {
			v.set((float)Math.cos(angle) * radius + center.x, (float)Math.sin(angle) * radius + center.y);
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(v.x  * Global.WORLD_SCALE, v.y * Global.WORLD_SCALE, 0);
		}
		renderer.end();

		renderer.begin(GL10.GL_LINES);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(center.x * Global.WORLD_SCALE, center.y * Global.WORLD_SCALE, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex((center.x + axis.x * radius) * Global.WORLD_SCALE, (center.y + axis.y * radius) * Global.WORLD_SCALE, 0);
		renderer.end();
	}

	private void drawSolidPolygon (Vector2[] vertices, int vertexCount, Color color) {
		renderer.begin(GL10.GL_LINE_LOOP);
		for (int i = 0; i < vertexCount; i++) {
			Vector2 v = vertices[i];
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(v.x * Global.WORLD_SCALE, v.y * Global.WORLD_SCALE , 0);
		}
		renderer.end();
	}

	private void drawJoint (Joint joint) {
		Body bodyA = joint.getBodyA();
		Body bodyB = joint.getBodyB();
		Transform xf1 = bodyA.getTransform();
		Transform xf2 = bodyB.getTransform();

		Vector2 x1 = xf1.getPosition();
		Vector2 x2 = xf2.getPosition();
		Vector2 p1 = joint.getAnchorA();
		Vector2 p2 = joint.getAnchorB();

		if (joint.getType() == JointType.DistanceJoint) {
			drawSegment(p1, p2, JOINT_COLOR);
		} else if (joint.getType() == JointType.PulleyJoint) {
			PulleyJoint pulley = (PulleyJoint)joint;
			Vector2 s1 = pulley.getGroundAnchorA();
			Vector2 s2 = pulley.getGroundAnchorB();
			drawSegment(s1, p1, JOINT_COLOR);
			drawSegment(s2, p2, JOINT_COLOR);
			drawSegment(s1, s2, JOINT_COLOR);
		} else if (joint.getType() == JointType.MouseJoint) {
			//drawSegment(x1, p1, JOINT_COLOR);
			p1 = ((MouseJoint)joint).getTarget();
			drawSegment(p1, p2, JOINT_COLOR);
			//drawSegment(x2, p2, JOINT_COLOR);	
		} else {
			drawSegment(x1, p1, JOINT_COLOR);
			drawSegment(p1, p2, JOINT_COLOR);
			drawSegment(x2, p2, JOINT_COLOR);
		}
	}

	private void drawSegment (Vector2 x1, Vector2 x2, Color color) {
		renderer.begin(GL10.GL_LINES);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x1.x * Global.WORLD_SCALE, x1.y * Global.WORLD_SCALE, 0);
		renderer.color(color.r, color.g, color.b, color.a);
		renderer.vertex(x2.x * Global.WORLD_SCALE, x2.y * Global.WORLD_SCALE, 0);
		renderer.end();
	}

	private void drawContact (Contact contact) {

	}	
	
	
}
