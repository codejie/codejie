package jie.java.android.boxcatcher;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WorldDebugRenderer {

	private Box2DDebugRenderer renderer = null;	
	
	private boolean debug = true;
	
	public WorldDebugRenderer() {
		
		renderer = new Box2DDebugRenderer();
	}
	
	public void dispose() {
		renderer.dispose();
	}

	public void render(World world, Camera camera) {
		if(debug == false)
			return;
		
		renderer.render(world, camera.combined.scale(Global.WORLD_SCALE, Global.WORLD_SCALE, 1));
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
