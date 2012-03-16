package jie.java.android.boxcatcher;

import jie.java.android.boxcatcher.material.TextureData;

public class MaterialManager {

	private TextureData texture = null;
	
	public MaterialManager() {
		texture = new TextureData();
	}
	
	public void dispose() {
		if(texture != null) {
			texture.dispose();
		}
	}
	
	public int load() {
		texture.load();
		return -1;
	}
	
	public TextureData getTexture() {
		return texture;
	}
	
}
