/**
 * project:		DemoDraft
 * file:		UIStage.java
 * author:		codejie(codejie@gmail.com)
 */
package com.jie.android.gdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;

public class UIStage extends Stage {

	public UIStage() {
		super(GLOBAL.SCREEN_WIDTH, GLOBAL.SCREEN_HEIGHT, true);
		
		initButtons();
	}
	
	private void initButtons() {
		final UIStage stage = this;
		
		Button btn1 = new Button("btn1", new TextureRegion(RESOURCE.uiTexture, 0, 64, 64, 32), new TextureRegion(RESOURCE.uiTexture, 64, 0, 64, 32)) {
			protected boolean touchDown(float x, float y, int pointer) {
				if (this.hit(x, y) == this) {
					stage.OnButton("btn1");
				}
				return super.touchDown(x, y, pointer);
			}
		};
		btn1.x = 32;
		btn1.y = 32;
		this.addActor(btn1);
		
		Button btn2 = new Button("btn2", new TextureRegion(RESOURCE.uiTexture, 0, 64, 64, 32), new TextureRegion(RESOURCE.uiTexture, 64, 0, 64, 32)) {
			protected boolean touchDown(float x, float y, int pointer) {
				if (this.hit(x, y) == this) {
					stage.OnButton("btn2");
				}
				return super.touchDown(x, y, pointer);
			}
		};
		btn2.x = 160;
		btn2.y = 32;
		this.addActor(btn2);
		
		Button btn3 = new Button("btn3", new TextureRegion(RESOURCE.uiTexture, 0, 64, 64, 32), new TextureRegion(RESOURCE.uiTexture, 64, 0, 64, 32)) {
			protected boolean touchDown(float x, float y, int pointer) {
				if (this.hit(x, y) == this) {
					stage.OnButton("btn3");
				}
				return super.touchDown(x, y, pointer);
			}
		};
		btn3.x = 288;
		btn3.y = 32;
		this.addActor(btn3);
	}
	
	private void OnButton(final String name) {
		if (name == "btn1") {
			GLOBAL.DEBUG = !GLOBAL.DEBUG;
		}
		else if (name == "btn2") {
			GLOBAL.COLLISION = !GLOBAL.COLLISION;
		}
		else if (name == "btn3") {
			GLOBAL.ADDACTOR = !GLOBAL.ADDACTOR;
		}
	}
	
	public void step(float delta) {
		this.act(delta);
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		return super.touchDown(x, y, pointer, button);
	}
	
}
