package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

/**
 *   File: MoonEcllipseScreen.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */

public class MoonEclipseScreen extends Stage implements Screen {

	private Game game = null;
	
	private Texture textureMoon = null;
	private int currentTexture = 0;
	
	private int frameWidth = 112;
	private int frameHeight = 112;
	private int currentFrame = 0;
	
	private Image sky = null;
	private Image moon = null;
	
	private float delta = 0.0f;
	private float speed = 0.1f;
	
	public MoonEclipseScreen(Game game, float width, float height, boolean stretch) {
		super(width, height, stretch);
		this.game = game;
		
		create();
	}
	
	private void getCurrentFrame() {
		currentFrame ++;
		if(currentFrame > (13 * 9 - 1))
			currentFrame = 0;
		if(currentTexture != (currentFrame / 9))
		{
			if(textureMoon != null)
				textureMoon.dispose();
			currentTexture = currentFrame / 9;
			textureMoon = new Texture(Gdx.files.internal("data/m" + currentTexture + ".jpg"));
		}
		
		sky.region = new TextureRegion(textureMoon, (currentFrame - (currentFrame / 9) * 9)  * frameWidth, 0, 12, 12);
		moon.region = new TextureRegion(textureMoon, (currentFrame - (currentFrame / 9) * 9)  * frameWidth, 0, frameWidth, frameHeight);
	}
	
	private void create() {
		textureMoon = new Texture(Gdx.files.internal("data/m0.png"));
		
		sky = new Image("sky", new TextureRegion(textureMoon, 0, 0, 12, 12));
		sky.height = 800;
		sky.width = 480;
		
		this.addActor(sky);
		
		moon = new Image("moon", new TextureRegion(textureMoon, 0, 0, frameWidth, frameHeight));//getCurrentFrame());
		moon.x = this.centerX() - frameWidth / 2;
		moon.y = this.centerY() - frameHeight /2;
		moon.scaleX = 1.5f;
		moon.scaleY = 1.5f;
				
		this.addActor(moon);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		if((this.delta += delta) > speed)
		{
			getCurrentFrame();
			this.delta = 0.0f;
		}
		
		this.draw();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
