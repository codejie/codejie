package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

/**
 *   File: MoonEcllipseScreen.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */

public class MoonEclipseScreen extends Stage implements Screen {

	private Game game = null;
	
	private MoonEclipseGroup moon = null;
	
	public MoonEclipseScreen(Game game, float width, float height, boolean stretch) {
		super(width, height, stretch);
		this.game = game;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		this.moon.delta(delta);
		
		this.draw();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		this.setViewport(width, height, true);	
		
		this.moon.setSize(width, height);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		this.moon = MoonEclipseGroup.MOON;
		this.moon.resume();
		
		this.moon.setSize(width, height);		
		this.addActor(moon);
		
		Gdx.input.setInputProcessor(this);				
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		//this.dispose();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		this.moon.pause();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		this.moon.resume();
	}

	
	
}
