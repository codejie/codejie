/**
 * file   : StarScreen.java
 * author : codejie (codejie@gmail.com)
 * date   : Jun 3, 2011 5:14:11 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.RotateTo;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public abstract class StarScreen extends Stage implements Screen {

	public enum ScreenType {
		Romantic, Child
	}
	
	protected Game game;
	
	protected ScreenType type;
	
	protected Texture ballTexture = null;
	protected Texture starTexture = null;
	
	protected boolean isTouched = false;
	protected float touchedCount = 0.0f;
	
	public StarScreen(Game game, ScreenType type) {
		super(480, 800, true);
		
		this.game = game;
		this.type = type;
		
		initTexture();
		
		Gdx.input.setInputProcessor(this);
	}
	
	public void dispose() {
		super.dispose();
		ballTexture.dispose();
		starTexture.dispose();
	}
	
	protected abstract void initTexture(); 

	private void makeStar(boolean star, int x, int y, int size) {
		Image img = null;
		
		if (star == true) {
			img = new Image("star", starTexture);
			if(this.type == ScreenType.Child && size > 24)
				size -= 12;			
		}
		else {
			img = new Image("ball", ballTexture);
			if(this.type == ScreenType.Romantic && size > 24)
				size -= 24;
		}
		img.x = x;
		img.y = y;
		img.width = size;
		img.height = size;
		
		this.addAction(img);
		
		super.addActor(img);		
	}
	
	private void addAction(final Image img) {
		
		final Stage stage = this;
		
		int duration = MathUtils.random(3, 60);
		MoveTo moveto = MoveTo.$(img.x, 800, duration);
		moveto.setCompletionListener(new OnActionCompleted() {
			public void completed(Action action) {
				stage.removeActor(img);
			}
		});
		
		int rotate = MathUtils.random(360);		
		float scale = MathUtils.random(0.5f, 2.0f);
		float fade = MathUtils.random(1.0f);
		Action action = Parallel.$(
				moveto,
				ScaleTo.$(scale, scale, duration),
				RotateTo.$(rotate, duration),
				FadeTo.$(fade, duration)
				);
		
		img.action(action);		
	}	
	
	protected void shiftScreen() {
		Screen screen = null;
		if(this.type == ScreenType.Romantic) {
			screen = new ChildStarScreen(this.game);
		}
		else {
			screen = new RomanticStarScreen(this.game);
		}
		this.game.setScreen(screen);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(isTouched) {
			touchedCount += delta;
			if(touchedCount > 2.0f) {
				shiftScreen();
			}
		}
		
		//float delta = Gdx.graphics.getDeltaTime();
		
		int roll = (int)(delta * 100000);
		if (roll % 15 == 0) {
			makeStar(MathUtils.randomBoolean(), MathUtils.random(0, 480), MathUtils.random(0, 64), MathUtils.random(10, 64));
		}
		
		this.act(delta);
		this.draw();
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub		
		makeStar(MathUtils.randomBoolean(), arg0, 800 - arg1, MathUtils.random(10, 64));
		
		return false;
	}
	
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		isTouched = true;
		return false;
	}	
	
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		touchedCount = 0.0f;
		isTouched = false;
		return false;
	}	
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
