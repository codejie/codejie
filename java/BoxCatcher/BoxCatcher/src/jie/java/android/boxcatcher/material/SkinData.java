package jie.java.android.boxcatcher.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class SkinData {

	private Skin skin = null;
	
	public SkinData() {
		
	}
	
	public void dispose() {
		if(skin != null) {
			skin.dispose();
		}
	}
	
	public int load() {
		skin = new Skin(Gdx.files.internal("data/skin/styles.json"), Gdx.files.internal("data/texture/map.png"));
		
		return 0;
	}
	
	public TextButtonStyle getTextButtonStyle(String name) {
		return skin.getStyle(name, TextButtonStyle.class);
	}
}
