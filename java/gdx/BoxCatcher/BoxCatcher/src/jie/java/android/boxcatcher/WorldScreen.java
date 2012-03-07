package jie.java.android.boxcatcher;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WorldScreen extends BCScreen {

	private StageData stage = null;
	private World world = null;
	//private Box2DDebugRenderer renderer = null;
	private WorldDebugRenderer debugRenderer = new WorldDebugRenderer();
	
	private float stateTime = 0.0f;
	
	public WorldScreen(BCGame game, int stageid) {
		super(game);
		
		loadData(stageid);
		
		initWorld();
		initBoxes();
		
		debugRenderer.setDebug(false);
	}
	
	@Override
	public void dispose() {
		
		if(world != null) {
			world.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		world.step(1/60f, 8, 3);
		
		stateTime += delta;
		
		initBox(stateTime);
		
		act(delta);
		draw();		
		
		debugRenderer.render(world, camera);
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
	private int loadData(int stageid) {
		stage = new StageData(stageid);
		
		return stage.load();
	}	
	
	private void initWorld() {
		world = new World(stage.world.gravity, true);
		world.setContactListener(new WorldContactListener());
	}
	
	private void initBoxes() {
		initBox(0.0f);
	}

	private void initBox(float stateTime) {
		StageData.Box box = stage.getFirstBox(stateTime);
		while(box != null) {
			this.addActor(new BoxActor(world, box));
			
			box = stage.getNextBox();
		}
	}
	
}
