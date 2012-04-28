package jie.java.android.boxcatcher;

import jie.java.android.boxcatcher.material.BitmapFontData;
import jie.java.android.boxcatcher.material.SkinData;
import jie.java.android.boxcatcher.material.TextureData;

public class MaterialManager {

	private TextureData texture = null;
	private BitmapFontData bitmapFont = null;
	private SkinData skin = null;
	
	public MaterialManager() {
		texture = new TextureData();
		bitmapFont = new BitmapFontData();
		skin = new SkinData();
	}
	
	public void dispose() {
		if(texture != null) {
			texture.dispose();
		}
		if(bitmapFont != null) {
			bitmapFont.dispose();
		}
		if(skin != null) {
			skin.dispose();
		}
	}
	
	public int load() {
		texture.load();
		bitmapFont.load();
		skin.load();
		
		return 0;
	}
	
	public TextureData getTexture() {
		return texture;
	}
	
	public BitmapFontData getBitmapFont() {
		return bitmapFont;
	}
	
	public SkinData getSkin() {
		return skin;
	}
}
