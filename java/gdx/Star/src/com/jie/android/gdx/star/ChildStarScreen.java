/**
 * file   : ChildStarScreen.java
 * author : codejie (codejie@gmail.com)
 * date   : Jun 3, 2011 5:43:30 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ChildStarScreen extends StarScreen {

	public ChildStarScreen(Game game) {
		super(game, StarScreen.ScreenType.Child);
	}

	@Override
	protected void initTexture() {
		ballTexture = new Texture(Gdx.files.internal("data/child-ball.png"));
		starTexture = new Texture(Gdx.files.internal("data/child-star.png"));
	}
	
}
