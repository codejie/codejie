package jie.java.android.boxcatcher.material;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class BitmapFontData {
	
	private final class StyleData {
		int bitmapFont = -1;
		int color = -1;
		
		public StyleData(int bitmapFont, int color) {
			this.bitmapFont = bitmapFont;
			this.color = color;
		}
	}
	
	private HashMap<Integer, String> bitmapFontIndex = new HashMap<Integer, String>();
	private HashMap<Integer, StyleData> styleIndex = new HashMap<Integer, StyleData>();
	
	private HashMap<Integer, Color> color = new HashMap<Integer, Color>();
	private HashMap<Integer, BitmapFont> bitmapFont = new HashMap<Integer, BitmapFont>();
	
	
	public BitmapFontData() {
		
	}
	
	public void dispose() {
		Iterator<Entry<Integer, BitmapFont>> it = bitmapFont.entrySet().iterator();
		while(it.hasNext()) {
			it.next().getValue().dispose();
		}
		
		color.clear();
		bitmapFont.clear();
		
		styleIndex.clear();
		bitmapFontIndex.clear();
	}
	
	public int load() {
		
		bitmapFontIndex.put(1, new String("data/font/t"));		
		color.put(1, new Color(1f, 1f, 0f, 1f));
		color.put(2, new Color(0f, 1f, 1f, 0.5f));
		styleIndex.put(1, new StyleData(1, 1));
		styleIndex.put(2, new StyleData(1, 2));
		
		return 0;
	}
	
	public BitmapFont getBitmapFont(int index) {
		BitmapFont bf = bitmapFont.get(index);
		if(bf == null) {
			return loadBitmap(index);
		}
		return bf;
	}

	public Color getColor(int index) {
		return color.get(index);
	}
	
	public LabelStyle getStyle(int index) {
		StyleData sd = styleIndex.get(index);
		if(sd == null)
			return null;
		BitmapFont bf = getBitmapFont(sd.bitmapFont);
		if(bf == null)
			return null;
		Color c = getColor(sd.color);
		if(c == null)
			return null;
		return new LabelStyle(bf, c);
	}
	
	private BitmapFont loadBitmap(int index) {
		String file = bitmapFontIndex.get(index);
		if(file == null)
			return null;
		
		BitmapFont bf = new BitmapFont(Gdx.files.internal(file + ".fnt"), Gdx.files.internal(file + ".png"), false);
		//bf.setScale(0.1f);
		bitmapFont.put(index, bf);
		
		return bf;
	}
}
