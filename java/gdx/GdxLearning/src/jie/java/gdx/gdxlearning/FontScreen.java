package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
		
		//ta = new TextureAtlas("data/font-image/Comic1.png");
		bf = new BitmapFont(Gdx.files.internal("data/t.fnt"), Gdx.files.internal("data/t.png"), false);
		
		label = new Label("test", new LabelStyle(bf, new Color(1f, 1f, 0f, 0.5f)));

		label.x = 100;
		label.y = 100;
				
		this.addActor(label);
		
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		if(bf != null) {
			bf.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		label.setText(String.format("%.4f - %d", delta, Gdx.graphics.getFramesPerSecond()));
		label.y += (delta * 30);
		
		this.draw();
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

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 v = new Vector2(x, y);
		this.toStageCoordinates(x, y, v);
		if(this.hit(v.x, v.y) == label) {
			label.setText("WOOO...");
			game.setScreen(new ImageScreen(game));
		}
		return super.touchDown(x, y, pointer, button);
	}


}
