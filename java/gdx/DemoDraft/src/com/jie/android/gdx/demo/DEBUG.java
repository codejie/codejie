/**
 * project:		DemoDraft
 * file:		DEBUG.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 25, 2011
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public final class DEBUG {
	
	private static ImmediateModeRenderer renderer = null;
	
	public static void initalize() {
		if(GLOBAL.DEBUG == true) {
			renderer = new ImmediateModeRenderer();
		}
	}
	
	private static Vector2 t = new Vector2();
	private static Vector2 axis = new Vector2();
	//private static Vector2[] vertices = new Vector2[100];
	
	private static void drawShape (Fixture fixture, Transform transform, Color color) {
		{
			PolygonShape poly = (PolygonShape)fixture.getShape();
			int vertexCount = poly.getVertexCount();
			
			Vector2[] vertices = new Vector2[10];
			for(int i = 0; i < vertices.length; ++ i) {
				vertices[i] = new Vector2();
			}
			
			for (int i = 0; i < vertexCount; i++) {
				poly.getVertex(i, vertices[i]);
				transform.mul(vertices[i]);
			}
			drawSolidPolygon(vertices, vertexCount, color);
		}
	}	
	
	public static void renderBody(Body body) {
		if(GLOBAL.DEBUG == false)
			return;
		if (body == null)
			return;

		drawShape(body.getFixtureList().get(0), body.getTransform(), new Color(1, 1, 1, 1));
		
		
		
/*		
		Transform transform = body.getTransform();
		PolygonShape poly = (PolygonShape)body.getFixtureList().get(0).getShape();
		//Transform transform = body.getTransform();
		Vector2[] vertices = new Vector2[10];
		for(int i = 0; i < vertices.length; ++ i) {
			vertices[i] = new Vector2();
		}
		int vertexCount = poly.getVertexCount();
		for (int i = 0; i < vertexCount; i++) {
			poly.getVertex(i, vertices[i]);
			transform.mul(vertices[i]);
		}
		drawSolidPolygon(vertices, vertexCount, new Color(1, 1, 1, 1));	
*/
	}
	
	private static void drawSolidPolygon (Vector2[] vertices, int vertexCount, Color color) {
		renderer.begin(GL10.GL_LINE_LOOP);
		for (int i = 0; i < vertexCount; i++) {
			Vector2 v = vertices[i];
			renderer.color(color.r, color.g, color.b, color.a);
			renderer.vertex(v.x * GLOBAL.WORLD_SCALE, v.y * GLOBAL.WORLD_SCALE, 0);
		}
		renderer.end();
	}
}
