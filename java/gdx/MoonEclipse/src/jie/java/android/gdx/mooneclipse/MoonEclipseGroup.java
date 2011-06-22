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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.RotateTo;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
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
	
	private Group starGroup = new Group();
	private Texture textureStar = null;
	private Texture textureBall = null;
	
	private boolean p0 = false, p1 = false;
	private boolean t0 = false, t1 = false;
	private float x0, y0, x1, y1;
	private float clickDelta = -1.0f;
	private float clickInterval = 0.4f;
	
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
		
		for(GLOBAL.StarData it:GLOBAL.starList) {
			makeStar(it);
		}
		this.addActor(starGroup);		
		
		moon = new Image("moon", new TextureRegion(textureMoon, 0, 0, frameWidth, frameHeight));//getCurrentFrame());
		moon.scaleX = GLOBAL.scale;
		moon.scaleY = GLOBAL.scale;
		this.addActor(moon);
	
		getCurrentFrame();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		if(textureStar != null)
			textureStar.dispose();
		if(textureBall != null)
			textureBall.dispose();
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
		if(clickDelta >= 0.0f) {
			clickDelta += delta;
			if(clickDelta >= clickInterval)
				clickDelta = -1.0f;
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
	
	protected void makeStar(float x, float y) {		
		
		//makeStar(MathUtils.randomBoolean(), MathUtils.random(0, 480), MathUtils.random(0, 64), MathUtils.random(10, 64));	
		
		GLOBAL.StarData data = new GLOBAL.StarData();
				
		if(MathUtils.randomBoolean() == true) {		
			data.isStar = true;
			data.x = x - 32;
			data.y = y - 32;
			data.scale = MathUtils.random(0.3f, 0.8f);
			data.rotate = MathUtils.random(360.0f);
		}
		else  {
			data.isStar = false;
			data.x = x - 16;
			data.y = y - 16;
			data.scale = MathUtils.random(0.6f, 0.8f);
			data.rotate = 0.0f;
		}
		
		data.needFall = (MathUtils.random(100) < 20);
		
		makeStar(data);
		
		if(data.needFall == false)
			GLOBAL.starList.add(data);
	}
	
	protected void makeStar(final GLOBAL.StarData data) {		
		Image star = null;
		
		if(data.isStar == true) {
			if(textureStar == null) {
				textureStar = new Texture(Gdx.files.internal("data/star.png"));			
			}
			star = new Image("star", textureStar);
		}
		else  {
			if(textureBall == null) {
				textureBall = new Texture(Gdx.files.internal("data/ball.png"));			
			}
			star = new Image("ball", textureBall);
		}
		star.x = data.x;
		star.y = data.y;
		star.scaleX = data.scale;
		star.scaleY = data.scale;
		star.rotation = data.rotate;
	
		starGroup.addActor(star);
		
		if(data.needFall == true) {
			final Image a = star;
			
			int rotate = MathUtils.random(360);		
			float scale = MathUtils.random(0.2f, 1.0f);
			float fade = MathUtils.random(1.0f);
			float duration = star.y / 1000.0f + MathUtils.random(0.2f);
			Action action = Parallel.$(
					MoveTo.$(star.x, 0, duration),
					ScaleTo.$(scale, scale, duration),
					RotateTo.$(rotate, duration),
					FadeTo.$(fade, duration)
					).setCompletionListener(
							new OnActionCompleted() {
								@Override
								public void completed(Action action) {
									starGroup.removeActor(a);
								}
							});			
			
			star.action(action);
		}
	}
	
	protected void removeStars() {
		starGroup.clear();
		GLOBAL.starList.clear();
	}
	
	protected boolean touchDown(float x, float y, int pointer) {
		boolean touch = super.touchDown(x, y, pointer);
		if(touch == true) {
			//Gdx.app.log("moon touchDown : ", "Actor - x : " + x + " y : " + y + " hit : " + hit(x, y).name);
			if(hit(x, y) == sky) {
				if(clickDelta == -1.0f) {
					clickDelta = 0.0f;
				}
				else if(clickDelta > 0.1f) {
					clickDelta = -1.0f;
					makeStar(x, y);
				}
			} 
			if(pointer == 0) {
				x0 = x;
				p0 = true;
			}
			else if(pointer == 1) {
				x1 = x;
				p1 = true;
			}
		}
		return touch;
	}	
	
	protected boolean touchUp(float x, float y, int pointer) {
		if(pointer == 0) {
			p0 = false;
			t0 = false;
		}
		else if(pointer == 1) {
			p1 = false;
			t1 = false;
		}
		
		return super.touchUp(x, y, pointer);
	}
	
	protected boolean touchDragged(float x, float y, int pointer) {
		boolean touch = super.touchDragged(x, y, pointer);
		
		//if(touch == true) {
			Gdx.app.log("moon touchDragged : " + pointer, "Actor - x : " + x + " y : " + y + " hit : " + hit(x, y).name);
			if(p0 && p1 && (Math.abs(x0 - x1) < 128)) {
				if(t0 && t1) {
					removeStars();
				}
				if(pointer == 0) {
					if((x - x0) > 32)
						t0 = true;
				}
				else if(pointer == 1) {
					if((x - x1) > 32)
						t1 = true;
				}
			}
		//}
		
		return touch;
	}
	
}
