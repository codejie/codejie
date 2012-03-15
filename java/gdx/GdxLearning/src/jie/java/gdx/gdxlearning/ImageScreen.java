package jie.java.gdx.gdxlearning;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class ImageScreen extends Stage implements Screen {

	private MyGame game = null;	
	
	private Image image = null;
	private Texture texture = null;
	private Action action = null;
	
	public ImageScreen(MyGame myGame) {
		super(800, 480, true);
		
		game = myGame;
		
		texture = new Texture(Gdx.files.internal("data/t.png"));
		image = new Image(new TextureRegion(texture, 0, 0, 32, 32));
		
		image.x = 100;
		image.y = 100;
		
		action = MoveTo.$(200, 300, 5.0f);		
		this.addActor(image);
		
		action.setTarget(image);
		
		Gdx.input.setInputProcessor(this);
	}	
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 v = new Vector2(x, y);
		this.toStageCoordinates(x, y, v);
		if(this.hit(v.x, v.y) == image) {
			game.setScreen(new FontScreen(game));
		}
		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		action.act(delta);
		this.act(delta);
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
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
