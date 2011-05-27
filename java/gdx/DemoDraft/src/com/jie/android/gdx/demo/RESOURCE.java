package com.jie.android.gdx.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * project:		DemoDraft
 * file:		RESOURCE.java
 * author:		codejie(codejie@gmail.com)
 * update:		May 26, 2011
 */

public final class RESOURCE {

	public static Texture bgTexture;
	public static Texture uiTexture;
	public static Texture actorTexture;
	public static Texture colorTexture;
	
	public static Music bgMusic;
	public static Sound actorSound1;
	
	public static void initalize() {
		bgTexture = new Texture(Gdx.files.internal("data/stones.jpg"));
		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));
		colorTexture = new Texture(Gdx.files.internal("data/Textures.png"));
	}
	
	protected void finalize() {
		bgTexture.dispose();
		uiTexture.dispose();
		colorTexture.dispose();
	}
}
