package jie.java.android.boxcatcher;

import jie.java.android.boxcatcher.material.BitmapFontData;
import jie.java.android.boxcatcher.material.TextureData;

public class MaterialManager {

	private TextureData texture = null;
	private BitmapFontData bitmapFont = null;
	
	public MaterialManager() {
		texture = new TextureData();
		bitmapFont = new BitmapFontData();
	}
	
	public void dispose() {
		if(texture != null) {
			texture.dispose();
		}
		if(bitmapFont != null) {
			bitmapFont.dispose();
		}
	}
	
	public int load() {
		texture.load();
		bitmapFont.load();
		return -1;
	}
	
	public TextureData getTexture() {
		return texture;
	}
	
	public BitmapFontData getBitmapFont() {
		return bitmapFont;
	}
	
}
