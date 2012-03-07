package jie.java.android.boxcatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureData {

	private final static class RegionData {
		public RegionData(int texture, int x, int y, int width, int height) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public int texture;
		public int x, y;
		public int width, height;
	}
	
	private final static class AnimationData {
		public AnimationData(int texture, int width, int height, float duration) {
			this.texture = texture;
			this.width = width;
			this.height = height;
			this.duration = duration;
		}
		public int texture;
		public int width, height;
		public float duration;
	}
	
	private HashMap<Integer, String> textureIndex = new HashMap<Integer, String>();
	private HashMap<Integer, RegionData> regionIndex = new HashMap<Integer, RegionData>();
	private HashMap<Integer, AnimationData> animationIndex = new HashMap<Integer, AnimationData>();
	
	private HashMap<Integer, Texture> texture = new HashMap<Integer, Texture>();
	private HashMap<Integer, TextureRegion> region = new HashMap<Integer, TextureRegion>();
	private HashMap<Integer, Animation> animation = new HashMap<Integer, Animation>();
	
	public TextureData() {
		
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	public void dispose() {
		
		Gdx.app.log("tag", "TextureData - dispose().");
		
		region.clear();
		animation.clear();
		
		Iterator<Entry<Integer, Texture>> it = texture.entrySet().iterator();
		while(it.hasNext()) {
			it.next().getValue().dispose();
		}
		texture.clear();
		
		textureIndex.clear();
		regionIndex.clear();
		animationIndex.clear();
	}
	
	public int load() {
		textureIndex.put(1, new String("data/1.png"));
		textureIndex.put(2, new String("data/m0.jpg"));
		
		regionIndex.put(1, new RegionData(2, 0, 0, 112, 112));
		
		animationIndex.put(1, new AnimationData(1, 256/6, 256/5, 1/25f));
		
		return 0;
	}
	
	public Texture getTexture(int index) {
		Texture tt = texture.get(index);
		if(tt == null) {
			return loadTexture(index);
		}
		return tt;
	}
	
	public TextureRegion getRegion(int index) {
		TextureRegion tr = region.get(index); 
		if(tr == null) {
			return loadRegion(index);
		}
		return tr;
	}

	public Animation getAnimation(int index) {
		Animation ani = animation.get(index); 
		if(ani == null) {
			return loadAnimation(index);
		}
		return ani;
	}
	
	private Texture loadTexture(int index) {
		String file = textureIndex.get(index);
		if(file == null)
			return null;
		
		Texture tt = new Texture(Gdx.files.internal(file));
		texture.put(index, tt);
		
		return tt;
	}
	
	private TextureRegion loadRegion(int index) {
		RegionData data = regionIndex.get(index);
		if(data == null)
			return null;
		
		Texture tt = getTexture(data.texture);
		if(tt == null)
			return null;
		
		TextureRegion tr = new TextureRegion(tt, data.x, data.y, data.width, data.height);
		region.put(index, tr);
		
		return tr;
	}

	private Animation loadAnimation(int index) {
		AnimationData data = animationIndex.get(index);
		if(data == null)
			return null;
		
		Texture tt = getTexture(data.texture);
		if(tt == null)
			return null;
		
		TextureRegion[][] tmp = TextureRegion.split(tt, data.width, data.height);
		int col = tt.getWidth() / data.width;
		int row = tt.getHeight() / data.height;
		
		TextureRegion[] list = new TextureRegion[col * row];
		for(int i = 0; i < row; ++ i) {
			for(int j = 0; j < col; ++ j) {
				list[i * col + j] = tmp[i][j];
			}
		}
		
		Animation ani = new Animation(data.duration, list);
		animation.put(index, ani);
		
		return ani;
	}
	
	
}
