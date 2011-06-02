package com.jie.android.gdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;

public class TouchScreen extends Screen implements InputProcessor {

	private BackgroundStage bgStage;
	//private UIStage uiStage;
	private ActorStage actStage;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		RESOURCE.initalize();
		DEBUG.initalize();
		
		bgStage = new BackgroundStage();
		actStage = new ActorStage();
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		bgStage.dispose();
		actStage.dispose();
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		if(GLOBAL.DEBUG == true)
			Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		//GL10 gl = Gdx.app.getGraphics().getGL10();
		//gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float timestep = Gdx.graphics.getDeltaTime();
		
		bgStage.step(timestep);
		actStage.step(timestep);
		
		bgStage.draw();
		actStage.draw();
	}	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		
		actStage.touchDown(x, y, pointer, button);
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		actStage.touchUp(x, y, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		actStage.touchDragged(x, y, pointer);
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
