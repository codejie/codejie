package jie.java.android.boxcatcher;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public abstract class WorldScreen extends BCScreen {

	private int screenid = -1;
	private World world = null;
	private Box2DDebugRenderer renderer = null;
	
	public WorldScreen(BCGame game, int screenid) {
		super(game);
		
		this.screenid = screenid;
		
		loadData();
		
		initWorld();
		initBoxes();		
	}
	
	@Override
	public void dispose() {
		
		if(renderer != null) {
			renderer.dispose();
		}
		
		if(world != null) {
			world.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}
	
//
	private void loadData() {
		// TODO Auto-generated method stub
		
	}	
	
	private void initBoxes() {
		// TODO Auto-generated method stub
		
	}

	private void initWorld() {
		//world
		//frame
	}
	
	

	
}
