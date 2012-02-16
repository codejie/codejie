package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageScreen extends BCScreen {

	Image image = null;
	Texture texture = null;
	Animation animation = null;
	TextureRegion[] list = null;
	
	float i = 0;
	
	public ImageScreen(BCGame game) {
		super(game);
		
		init();
	}

	private void init() {
		texture = new Texture(Gdx.files.internal("data/1.png"));
		
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight() / 5);
		list = new TextureRegion[6 * 5];
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 6; j ++) {
				list[i * 6 + j] = tmp[i][j];
			}
		}
		
		//animation = new Animation(0.1f, new TextureRegion(texture, 0, 0, 112, 112), new TextureRegion(texture, 56, 0, 112, 112));
		animation = new Animation(1.0f, list);
		image = new Image();
		image.x = 0;
		image.y = 0;
		image.height = 164;//texture.getHeight() / 5;
		image.width = 164;//texture.getWidth() / 6;
		
		this.addActor(image);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		float delta = Gdx.graphics.getDeltaTime();
		i+= delta;
		image.setRegion(animation.getKeyFrame(i, true));
		//this.getSpriteBatch().draw(animation.getKeyFrame(i, true), 0, 0);
		
		Gdx.app.log("tag", "delta = " + i);
		
		super.draw();
	}
	
	
	

}
