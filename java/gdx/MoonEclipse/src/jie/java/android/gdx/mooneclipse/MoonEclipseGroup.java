/**
 *   File: MoonEclipseGroup.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 17, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.badlogic.gdx.utils.Disposable;

public class MoonEclipseGroup extends Group implements Disposable{
	
	private Texture textureMoon = null;
	private int currentTexture = 0;
	
	private int frameWidth = 112;
	private int frameHeight = 112;
	private int currentFrame = 0;
	
	private Image sky = null;
	private Image moon = null;
	
	private float delta = 0.0f;
	private float speed = 0.1f;
	
	private Label log = null;//new Label("log", new BitmapFont());
	
	private boolean isPause = false;
	
	public static MoonEclipseGroup MOON = null;
	
	public MoonEclipseGroup() {
		create();
	}
	
	public void create() {
		textureMoon = new Texture(Gdx.files.internal("data/m0.jpg"));
		
		sky = new Image("sky", new TextureRegion(textureMoon, 0, 0, 12, 12));
		sky.x = 0;
		sky.y = 0;
		this.addActor(sky);
		
		moon = new Image("moon", new TextureRegion(textureMoon, 0, 0, frameWidth, frameHeight));//getCurrentFrame());
		this.addActor(moon);
		
		log = new Label("log", new BitmapFont());
		this.addActor(log);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		if(textureMoon != null)
			textureMoon.dispose();
	}

	public void setSize(float width, float height) {
		sky.width = width;
		sky.height = height;
		
		moon.x = width  / 2 - moon.width / 2;
		moon.y = height / 2 - moon.height / 2;
		
		log.x = 30;
		log.y = 30;
	}
	
	public void delta(float delta) {
		if((this.delta += delta) > speed)
		{
			getCurrentFrame();
			this.delta = 0.0f;
		}
		
		log.setText(" delta : " + delta + " - width : " + this.width + " height : " + this.height);
	}
	
	private void getCurrentFrame() {
		if(isPause == true)
			return;
		
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
	
	public void pause() {
		this.isPause = true;
	}
	
	public void resume() {
		this.isPause = false;
	}
	
	public static void initResource() {
		if(MoonEclipseGroup.MOON == null)
			MoonEclipseGroup.MOON = new MoonEclipseGroup();
	}
}
