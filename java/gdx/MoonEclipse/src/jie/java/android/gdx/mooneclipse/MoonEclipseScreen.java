package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

/**
 *   File: MoonEcllipseScreen.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 16, 2011
 */

public class MoonEclipseScreen extends Stage implements Screen {

	private Game game = null;
	
	private MoonEclipseGroup moon = new MoonEclipseGroup();
	private ControllerGroup controller = new ControllerGroup();
	
	private boolean p0 = false,p1 = false;
	private int x0 = 0, x1 = 0, y0 = 0, y1 = 0;
	
	public MoonEclipseScreen(Game game, float width, float height, boolean stretch) {
		super(width, height, stretch);
		this.game = game;
	}

	public void dispose() {
		this.moon = null;
		this.controller = null;
		
		super.dispose();
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

		if(GLOBAL.isShift == true)
			return;
		
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		this.act(delta);
		
		this.draw();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
			
		this.setViewport(width, height, true);	
		
		this.moon.setSize(width, height);
		this.controller.setSize(width, height);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub		
		this.moon.setSize(width, height);		
		this.addActor(this.moon);
		
		this.controller.setSize(width, height);
		this.addActor(this.controller);
		
		GLOBAL.isShift = false;
		
		Gdx.input.setInputProcessor(this);				
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		//this.dispose();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		//this.moon.pause();
		GLOBAL.isShift = true;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		//this.moon.resume();
		GLOBAL.isShift = false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		
		//we can process zoom event in global, but not just in moon group
		if(p0 == true && p1 == true) {
			if(pointer == 0) {
				//Gdx.app.log("MoonEclipse", "p0 touchDrag : " + x + "," + y + "," + pointer);
				if(x0 < x1) {
					if(x < x0 && y > y0)
						this.moon.zoomOut();
					else if(x > x0 && y < y0)
						this.moon.zoomIn();
				}
				else if(x0 > x1) {
					if(x < x0 && y > y0)
						this.moon.zoomIn();
					else if(x > x0 && y < y0)
						this.moon.zoomOut();					
				}
			}
			else if(pointer == 1) {
				//Gdx.app.log("MoonEclipse", "p1 touchDrag : " + x + "," + y + "," + pointer);
				if(x0 < x1) {
					if(x < x1 && y > y1)
						this.moon.zoomIn();
					else if(x > x1 && y < y1)
						this.moon.zoomOut();
				}
				else if(x0 > x1) {
					if(x < x1 && y > y1)
						this.moon.zoomOut();
					else if(x > x1 && y < y1)
						this.moon.zoomIn();					
				}
			}
		}
		
		return super.touchDragged(x, y, pointer);
	} 

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		
		//Gdx.app.log("MoonEclipse", "touchDown : " + x + "," + y + "," + pointer + "," + button);
		if(pointer == 0)
		{
			x0 = x;
			y0 = y;
			p0 = true;
		}
		else if(pointer == 1) {
			x1 = x;
			y1 = y;
			p1 = true;
		}
		return super.touchDown(x, y, pointer, button);
	}	
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		//Gdx.app.log("MoonEclipse", "touchUp : " + x + "," + y + "," + pointer + "," + button);
		if(pointer == 0)
		{
			p0 = false;
		}
		else if(pointer == 1) {
			p1 = false;
		}
		return super.touchUp(x, y, pointer, button);
	}		
	
	
}
