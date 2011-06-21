/**
 *   File: MoonEclipseGroup.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 17, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private Image sky = null;
	private Image moon = null;
	
	public MoonEclipseGroup() {
		super("moon");
		create();
	}
	
	public void create() {
		textureMoon = new Texture(Gdx.files.internal("data/m0.jpg"));
	
		sky = new Image("sky", new TextureRegion(textureMoon, 0, 0, 12, 12));
		sky.x = 0;
		sky.y = 0;
		this.addActor(sky);
		
		moon = new Image("moon", new TextureRegion(textureMoon, 0, 0, frameWidth, frameHeight));//getCurrentFrame());
		moon.scaleX = GLOBAL.scale;
		moon.scaleY = GLOBAL.scale;
		this.addActor(moon);
	
		getCurrentFrame();
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
		
		//controller.setSize(width, height);
	}
	
	public void delta(float delta) {
		if((GLOBAL.delta += delta) > GLOBAL.speed)
		{
			getCurrentFrame();
			GLOBAL.delta = 0.0f;
		}
	}
	
	protected void draw(SpriteBatch batch, float parentAlpha) {
		this.delta(Gdx.graphics.getDeltaTime());
		super.draw(batch, parentAlpha);
	}
	
	private void getCurrentFrame() {
		if(GLOBAL.isPause == false) {
			GLOBAL.currentFrame ++;
		}
		if(GLOBAL.currentFrame > (13 * 9 - 1))
			GLOBAL.currentFrame = 0;
		if(currentTexture != (GLOBAL.currentFrame / 9))
		{
			if(textureMoon != null)
				textureMoon.dispose();
			currentTexture = GLOBAL.currentFrame / 9;
			textureMoon = new Texture(Gdx.files.internal("data/m" + currentTexture + ".jpg"));
		}
		
		sky.region = new TextureRegion(textureMoon, (GLOBAL.currentFrame - (GLOBAL.currentFrame / 9) * 9)  * frameWidth, 0, 12, 12);
		moon.region = new TextureRegion(textureMoon, (GLOBAL.currentFrame - (GLOBAL.currentFrame / 9) * 9)  * frameWidth, 0, frameWidth, frameHeight);
	}
	
	public void pause() {
		GLOBAL.isPause = true;
	}
	
	public void resume() {
		GLOBAL.isPause = false;
	}
	
	public void zoomOut() {
		if(GLOBAL.scale < 3.0f) {
			GLOBAL.scale += 0.2f;
			moon.scaleX = GLOBAL.scale;
			moon.scaleY = GLOBAL.scale;				
		}
	}
	
	public void zoomIn() {
		if(GLOBAL.scale > 0.6f) {
			GLOBAL.scale -= 0.2f;
			
			moon.scaleX = GLOBAL.scale;
			moon.scaleY = GLOBAL.scale;					
		}
	}
	
	protected boolean touchDown(float x, float y, int pointer) {
		boolean touch = super.touchDown(x, y, pointer);
		if(touch == true) {
			Gdx.app.log("moon touchDown : ", "Actor - x : " + x + " y : " + y + " hit : " + hit(x, y).name);
		}
		return touch;
	}	
	
	protected boolean touchUp(float x, float y, int pointer) {
		return super.touchUp(x, y, pointer);
	}
	
	protected boolean touchDragged(float x, float y, int pointer) {
		return super.touchDragged(x, y, pointer);
	}
	
}
