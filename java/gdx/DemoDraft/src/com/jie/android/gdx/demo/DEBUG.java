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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;

public final class DEBUG {
	
	private static ImmediateModeRenderer renderer = null;
	
	public static void initalize() {
		if(GLOBAL.DEBUG == true) {
			renderer = new ImmediateModeRenderer();
		}
	}
	
	public static void renderBody(Body body) {
		if(GLOBAL.DEBUG == false)
			return;
		
		PolygonShape poly = (PolygonShape)body.getFixtureList().get(0).getShape();
		Transform transform = body.getTransform();
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
