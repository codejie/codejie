/**
 * project:		DemoDraft
 * file:		BackgroundStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.RotateTo;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

public class BackgroundStage extends Stage {
	
	private Label output = null;
	
	private int idBubble = 0;
	//private Action bubbleAction = null;
	
	public BackgroundStage() {
		super(GLOBAL.SCREEN_WIDTH, GLOBAL.SCREEN_HEIGHT, true);
		
		if (GLOBAL.DEBUG == false) {
			Image bgImage = new Image("bg", RESOURCE.bgTexture);
			bgImage.x = 0;
			bgImage.y = 0;
			bgImage.width = GLOBAL.SCREEN_WIDTH;
			bgImage.height = GLOBAL.SCREEN_HEIGHT;
			//this.addActor(bgImage);
			
			makeBubble();
		}
		else {
			output = new Label("output", new BitmapFont());
			output.x = 30;
			output.y = GLOBAL.SCREEN_HEIGHT - 30;
			
			this.addActor(output);
		}
	}
	
	private void makeBubbleAction(final Actor actor) {
		
		final Stage stage = this;
		
		int duration = MathUtils.random(3, 7);
		MoveTo moveto = MoveTo.$(actor.x, GLOBAL.SKY_Y - 64, duration);
		moveto.setCompletionListener(new OnActionCompleted() {
			public void completed(Action action) {
				stage.removeActor(actor);
			}
		}
		);
		
		int rotate = MathUtils.random(360);		
		float scale = MathUtils.random(1.0f, 1.7f);
		float fade = MathUtils.random(1.0f);
		Action action = Parallel.$(
				moveto,
				ScaleTo.$(scale, scale, duration),
				RotateTo.$(rotate, duration),
				FadeTo.$(fade, duration)
				);
		
		actor.action(action);
	}
	
	private void makeBubble() {
		
		Image img = null;
		
		int x = MathUtils.random(0, (int)GLOBAL.SCREEN_WIDTH);
		int width = MathUtils.random(10, 60);
		
		if (x % 2 == 0) {
			img = new Image("bubble" + idBubble, RESOURCE.bubbleTexture1);
		}
		else {
			img = new Image("bubble" + idBubble, RESOURCE.bubbleTexture2);
		}
		img.x = x;
		img.y = GLOBAL.GROUND_Y;
		img.width = width;
		img.height = width;
		
		makeBubbleAction(img);
		
		this.addActor(img);
	}
	
	private void createBubbles(float delta) {
		int step = (int)(delta * 10000);
		
		if (step % 5 == 0 || step % 8 == 0 || step % 10 == 0 || step % 13 == 0 || step % 15 == 0) {
			makeBubble();
		}	
	}
	
	public void step(float delta) {
		
		this.act(delta);
		
		createBubbles(delta);
		
		if (GLOBAL.DEBUG == true) {
			output.setText("fps: " + Gdx.graphics.getFramesPerSecond() + "fps" + " -- deltatime: " + delta);
		}
	}
	
}
