/**
 * file:   StarGame.java
 * author: codejie (codejie@gmail.com)
 * date:   Jun 2, 2011 11:32:00 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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

public class StarGame extends Game implements InputProcessor {

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	
	private Stage stage = null;
	private Texture ballTexture = null;
	private Texture starTexture = null;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		stage = new Stage(480, 800, true);
		
		ballTexture = new Texture(Gdx.files.internal("data/ball.png"));
		starTexture = new Texture(Gdx.files.internal("data/star.png"));
		
		Gdx.input.setInputProcessor(this);
	}

	private void makeStar(boolean star, int x, int y, int size) {
		Image img = null;
		
		if (star == true) {
			img = new Image("star", starTexture);
		}
		else {
			img = new Image("ball", ballTexture);
			if(size > 24)
				size -= 24;
		}
		img.x = x;
		img.y = y;
		img.width = size;
		img.height = size;
		
		this.addAction(img);
		
		stage.addActor(img);		
	}
	
	private void addAction(final Image img) {
		
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
	
	public void render() {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float delta = Gdx.graphics.getDeltaTime();
		
		int roll = (int)(delta * 1000000);
		if (roll % 15 == 0) {
			makeStar(MathUtils.randomBoolean(), MathUtils.random(0, 480), MathUtils.random(0, 64), MathUtils.random(10, 64));
		}
		
		stage.act(delta);
		stage.draw();
	}
	
	public void dispose() {
		stage.dispose();
		ballTexture.dispose();
		starTexture.dispose();
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
	 */
	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
		makeStar(MathUtils.randomBoolean(), arg0, 800 - arg1, MathUtils.random(10, 64));
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchMoved(int, int)
	 */
	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
