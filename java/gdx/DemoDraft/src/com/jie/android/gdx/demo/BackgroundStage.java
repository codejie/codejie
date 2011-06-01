/**
 * project:		DemoDraft
 * file:		BackgroundStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

public class BackgroundStage extends Stage {
	private Label output = null;
	
	public BackgroundStage() {
		super(GLOBAL.SCREEN_WIDTH, GLOBAL.SCREEN_HEIGHT, true);
		
		Image bgImage = new Image("bg", RESOURCE.bgTexture);
		bgImage.x = 0;
		bgImage.y = 0;
		bgImage.width = GLOBAL.SCREEN_WIDTH;
		bgImage.height = GLOBAL.SCREEN_HEIGHT;
		if (GLOBAL.DEBUG == false)
			this.addActor(bgImage);
		
		output = new Label("output", new BitmapFont());
		output.x = 30;
		output.y = GLOBAL.SCREEN_HEIGHT - 30;
		
		this.addActor(output);
	}
	
	public void step(float delta) {
		output.setText("fps: " + Gdx.graphics.getFramesPerSecond() + "fps" + " -- deltatime: " + delta);
	}
	
}
