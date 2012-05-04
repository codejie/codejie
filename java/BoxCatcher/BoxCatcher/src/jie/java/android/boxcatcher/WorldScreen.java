package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import jie.java.android.boxcatcher.StageData.BoxRace;
import jie.java.android.boxcatcher.stagelayer.EndStageLayer;
import jie.java.android.boxcatcher.stagelayer.ScoreStageLayer;
import jie.java.android.boxcatcher.database.DBAccess;


public class WorldScreen extends BCScreen {

	private StageData stage = null;
	private World world = null;

	private WorldDebugRenderer debugRenderer = new WorldDebugRenderer();
	private BoxListenerManager listenerManager = null;
	private MaterialManager materialManager = null;
	private DBAccess dbAccess = null;
	
	private ScoreStageLayer scoreStageLayer = null;
	
	private float fps = 0.33f;
	private float stateTime = 0.0f;
	private boolean isOver = false;
		
	public WorldScreen(BCGame game, int stageid) {
		super(game);
		
		dbAccess = this.game.getDBAccess();
		materialManager = this.game.getMaterialManager();
		
		Gdx.app.log(Global.APP_TAG, "WorldScreen - constructor.");
		
		fps = Global.sysSetting.fps;
		
		loadData(stageid);
//		
		initStageLayers();
		
		initWorld();
		initFrames();
		
		showLevel();
//		
		debugRenderer.setDebug(true);//false);//true);
	}
	
	@Override
	public void dispose() {
		
		Gdx.app.log(Global.APP_TAG, "WorldScreen - dispose().");
		
		if(debugRenderer != null) {
			debugRenderer.dispose();
			debugRenderer = null;
		}
		
		if(world != null) {
			world.dispose();
			world = null;
		}
		
		super.dispose();
	}

	@Override
	public void render(float delta) {
		//Gdx.app.log(Global.APP_TAG, "WorldScreen - render()");
		super.render(delta);
		
		if(world != null) {
			world.step(fps, 6, 2);
		}
		
		act(delta);
		draw();		
		
		if(debugRenderer != null) {
			debugRenderer.render(world, camera);
		}
		
		checkStateTime(delta);
		
	}

	private void checkStateTime(float delta) {
		stateTime += delta;
		
		if(stateTime < stage.getSetting().maxStateTime) {
			loadBox(stateTime);
		}
		else if(isOver == false) {
			finishStage();
			isOver  = true;
		}		
	}

	private void finishStage() {
		//Gdx.app.log(Global.APP_TAG, "GAME Over!");
/*			
		if(debugRenderer != null) {
			debugRenderer.dispose();
			debugRenderer = null;
		}
		
		if(world != null) {
			world.dispose();
			world = null;
		}
*/		
		
		addActor(new EndStageLayer(this, game.getMaterialManager()));
		
		//scoreStageLayer.showResult();
		//game.nextScreen();
	}

	@Override
	public void show() {
		Gdx.app.log(Global.APP_TAG, "WorldScreen - show()");

		super.show();
	}

	@Override
	public void pause() {
		Gdx.app.log(Global.APP_TAG, "WorldScreen - pause()");
			
		super.pause();
	}

	@Override
	public void resume() {
		Gdx.app.log(Global.APP_TAG, "WorldScreen - resume()");
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
	
	public void showLevel() {
		scoreStageLayer.setLevel(stage.getSetting().id);
	}
}
