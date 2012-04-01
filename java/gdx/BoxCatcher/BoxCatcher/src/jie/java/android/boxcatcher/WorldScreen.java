package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.Iterator;

import jie.java.android.boxcatcher.StageData.BoxRace;
import jie.java.android.boxcatcher.stagelayer.ScoreStageLayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;



public class WorldScreen extends BCScreen {

	private StageData stage = null;
	private World world = null;

	private WorldDebugRenderer debugRenderer = new WorldDebugRenderer();
	private BoxListenerManager listenerManager = null;
	private MaterialManager materialManager = null;
	private DBAccess dbAccess = null;
	
	private ScoreStageLayer scoreStageLayer = null;
	
	private float stateTime = 0.0f;
		
	public WorldScreen(BCGame game, int stageid) {
		super(game);
		
		dbAccess = this.game.getDBAccess();
		materialManager = this.game.getMaterialManager();
		
		Gdx.app.log("tag", "WorldScreen - constructor.");
		
		loadData(stageid);
//		
		initStageLayers();
		
		initWorld();
		initFrames();
//		
		debugRenderer.setDebug(true);//false);//true);
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
		
		world.step(1/60f, 6, 2);
		
//		stateTime += delta;
//		
//		loadBox(stateTime);
		checkStateTime(delta);
		
		act(delta);
		draw();		
		
		debugRenderer.render(world, camera);
	}

	private void checkStateTime(float delta) {
		stateTime += delta;
		
		if(stateTime < stage.getSetting().maxStateTime) {
			loadBox(stateTime);
		}
		else {
			finishStage();
		}		
	}

	private void finishStage() {
		Gdx.app.log("tag", "GAME Over!");
	}

	@Override
	public void show() {
		Gdx.app.log("tag", "WorldScreen - show()");

		super.show();
	}

	@Override
	public void pause() {
		Gdx.app.log("tag", "WorldScreen - pause()");
			
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
		
		return stage.load(dbAccess);
	}	
	
	private void initStageLayers() {
		scoreStageLayer = new ScoreStageLayer(this, materialManager);
		
		this.addActor(scoreStageLayer);
	}
	
	private void initWorld() {
		if(world != null)
			return;
		
		world = new World(stage.getSetting().gravity, true);
		world.setContactListener(new WorldContactListener());
	
		listenerManager = new BoxListenerManager(this);
	}
	
	private void initFrames() {
		ArrayList<StageData.Box> frames = stage.getFrames();
		if(frames != null) {
			Iterator<StageData.Box> box = frames.iterator();
			while(box.hasNext()) {
				BoxActor actor = new BoxActor(world, box.next());
				makeBoxMaterial(actor);
				makeBoxListener(actor);
				//actor.setContactListener(listenerManager.getGroundContactListener());
				this.addActor(actor);
			}
		}		
	}

	private void loadBox(float stateTime) {
		
		ArrayList<StageData.Box> abox = stage.getBoxes(stateTime);
		if(abox != null) {
			Iterator<StageData.Box> box = abox.iterator();
			while(box.hasNext()) {
				BoxActor actor = new BoxActor(world, box.next());
				makeBoxMaterial(actor);
				makeBoxListener(actor);
				
				//actor.setContactListener(listenerManager.getEachContactListener());
				//actor.SetTouchListener(listenerManager.getDockTouchListener());
				this.addActor(actor);
			}
		}
	}
	
	private void makeBoxMaterial(BoxActor actor) {
		if(actor.getBox().texture != -1) {
			actor.setTexture(materialManager.getTexture().getRegion(actor.getBox().texture));
		}
		else if(actor.getBox().animation != -1) {
			actor.setAnimation(materialManager.getTexture().getAnimation(actor.getBox().animation));
		}		
	}
	
	private void makeBoxListener(BoxActor actor) {
		if(actor.getBox().race == BoxRace.BOX) {
			actor.setContactListener(listenerManager.getEachContactListener());
		}
		else if(actor.getBox().race == BoxRace.DOCK) {
			actor.SetTouchListener(listenerManager.getDockTouchListener());
			actor.setContactListener(listenerManager.getGroundContactListener());			
		}
		else if(actor.getBox().race == BoxRace.DEADLINE) {
			actor.setContactListener(listenerManager.getDeadlineContactorListener());
		}
	}

	//
	public void updateScore(int offset) {
		scoreStageLayer.setScore(stage.getRuntime().score += offset);
	}
	
}
