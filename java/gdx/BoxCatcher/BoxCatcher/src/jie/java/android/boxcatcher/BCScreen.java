package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BCScreen extends Stage implements Screen {

	protected BCGame game = null;
	
	public BCScreen(BCGame game, float width, float height, boolean stretch) {
		super(width, height, stretch);
		this.game = game;
		
		Gdx.input.setInputProcessor(this);
	}
	
	public BCScreen(BCGame game) {
		super(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, true);
		this.game = game;
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
