package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class FontScreen extends Stage implements Screen {

	private MyGame game = null;
	
	private BitmapFont bf = null;
	private TextureAtlas ta = null;
	private Label label = null;
	
	public FontScreen(MyGame myGame) {
		super(800, 480, true);
		
		game = myGame;
		
		ta = new TextureAtlas("data/font-image/Comic1.png");
		bf = new BitmapFont(Gdx.files.internal("data/comic.fnt"), ta.findRegion("Comic"), false);
		
		label = new Label("test", new LabelStyle(bf, new Color()));

		label.x = 100;
		label.y = 100;
		
		this.addActor(label);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


}
