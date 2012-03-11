package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WorldScreen extends BCScreen implements Registerable {

	private StageData stage = null;
	private World world = null;
	//private Box2DDebugRenderer renderer = null;
	private WorldDebugRenderer debugRenderer = new WorldDebugRenderer();
	
	private WorldScreenRegister register = null;
	
	private float stateTime = 0.0f;
	
	public WorldScreen(BCGame game, int stageid) {
		super(game);
		
		Gdx.app.log("tag", "WorldScreen - constructor.");
		
		loadData(stageid);
//		
		initWorld();
//		initFrames();
//		
//		debugRenderer.setDebug(false);
	}
	
	@Override
	public void dispose() {
		
		Gdx.app.log("tag", "WorldScreen - dispose().");
		
		if(world != null) {
			world.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void render(float delta) {
		//Gdx.app.log("tag", "WorldScreen - render()");
		super.render(delta);
		
		world.step(1/60f, 8, 3);
		
		stateTime += delta;
		
		loadBox(stateTime);
		
		act(delta);
		draw();		
		
		debugRenderer.render(world, camera);
	}

	@Override
	public void show() {
		Gdx.app.log("tag", "WorldScreen - show()");

		restore();
		
		super.show();
	}

	@Override
	public void pause() {
		Gdx.app.log("tag", "WorldScreen - pause()");
		
		shot();
		
		super.pause();
	}

	@Override
	public void resume() {
		Gdx.app.log("tag", "WorldScreen - resume()");
		super.resume();
	}
	
//
	private int loadData(int stageid) {
		stage = new StageData(stageid);
		
		return stage.load();
	}	
	
	private void initWorld() {
		if(world != null)
			return;
		
		world = new World(stage.world.gravity, true);
		world.setContactListener(new WorldContactListener());
	}
	
	private void initFrames() {
		ArrayList<StageData.Box> frames = stage.getFrames();
		if(frames != null) {
			Iterator<StageData.Box> box = frames.iterator();
			while(box.hasNext()) {
				this.addActor(new BoxActor(world, box.next()));
			}
		}		
	}

	private void loadBox(float stateTime) {
		
		ArrayList<StageData.Box> abox = stage.getBoxes(stateTime);
		if(abox != null) {
			Iterator<StageData.Box> box = abox.iterator();
			while(box.hasNext()) {
				this.addActor(new BoxActor(world, box.next()));
			}
		}
/*		
		StageData.Box box = stage.getFirstBox(stateTime);
		while(box != null) {
			this.addActor(new BoxActor(world, box));
			
			box = stage.getNextBox();
		}
*/		
	}

	@Override
	public int shot() {
		if(register == null) {
			register = new WorldScreenRegister();
		}
		else {
			register.refresh();
		}
		
		register.stateTime = stateTime;
		
		List<Actor> actors = this.getActors();
		if(actors != null) {
			Iterator<Actor> it = actors.iterator();
			while(it.hasNext()) {
				register.addBox(((BoxActor)it.next()).getBox());
			}
		}
		
		this.clear();
		
		return 0;
	}

	@Override
	public int restore() {
		initFrames();
		
		if(register == null)
			return 0;
		
		
		this.stateTime = register.stateTime;
		
		ArrayList<StageData.Box> abox = register.getBoxes();
		if(abox != null) {
			Iterator<StageData.Box> box = abox.iterator();
			while(box.hasNext()) {
				this.addActor(new BoxActor(world, box.next()));
			}
		}

		
		return 0;
	}

	
}
