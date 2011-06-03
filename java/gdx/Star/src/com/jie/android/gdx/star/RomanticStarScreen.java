/**
 * file   : RomanticStarScreen.java
 * author : codejie (codejie@gmail.com)
 * date   : Jun 3, 2011 5:16:24 PM
 */
package com.jie.android.gdx.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class RomanticStarScreen extends StarScreen {

	public RomanticStarScreen(Game game) {
		super(game, StarScreen.ScreenType.Romantic);
	}

	protected void initTexture() {
		ballTexture = new Texture(Gdx.files.internal("data/ball.png"));
		starTexture = new Texture(Gdx.files.internal("data/star.png"));
	}	

}
