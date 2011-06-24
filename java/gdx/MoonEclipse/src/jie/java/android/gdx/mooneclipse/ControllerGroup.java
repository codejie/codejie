/**
 *   File: ControllerGroup.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 20, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.utils.Disposable;

public class ControllerGroup extends Group implements Disposable {

	private Button play = null;
	private Button help = null;
	private Button close = null;
	private Image track = null;
	private Image line = null;
	private Image dockbar = null;
	private Texture texture = null;
	//private Image about = null;
	
	private float miniCount = 0.0f;
	private float miniTimeout = 1.5f;
	private int readyMini = 0;//0: none; 1: ready; 2: go
	private float miniY = 0.0f;
	
	private float trackPos = 0.0f;
	private int trackFrame = GLOBAL.currentFrame;
	
	public ControllerGroup() {
		super("controller");
		create();
	}
	
	private void create() {
		texture = new Texture(Gdx.files.internal("data/pause_play2.png"));
		
		play = new Button("play", new TextureRegion(texture, 1, 63, 63, 63), new TextureRegion(texture, 1, 63, 63, 63)) {
			protected boolean touchUp(float x, float y, int pointer) {
				if(this.hit(x, y) == this)
					((ControllerGroup)(this.parent)).onPlayButton();
				return super.touchUp(x, y, pointer);
			}
		};
//		pause = new Image("pause", new TextureRegion(texture, 1, 64, 63, 63));
		
		line = new Image("line", new TextureRegion(texture, 2, 2, 1, 1));
		line.height = 6;
		
		track = new Image("track", new TextureRegion(texture, 20, 20, 1, 1));/* {
			protected boolean touchDragged(float x, float y, int pointer) {
				if(this.hit(x, y) == this) {
					((ControllerGroup)(this.parent)).dragTrack(x, y);
				}
				return super.touchDragged(x, y, pointer);
			}
		};
		*/
		track.height = 14;
		track.width = 48;
		
		help = new Button("help", new Texture(Gdx.files.internal("data/help.png"))) {
			protected boolean touchUp(float x, float y, int pointer) {
				if(this.hit(x, y) == this)
					((ControllerGroup)(this.parent)).onHelpButton();
				return super.touchUp(x, y, pointer);
			}
		};
		help.y = 16;
		
		close = new Button("close", new Texture(Gdx.files.internal("data/close.png"))) {
			protected boolean touchUp(float x, float y, int pointer) {
				if(this.hit(x, y) == this)
					((ControllerGroup)(this.parent)).onCloseButton();
				return super.touchUp(x, y, pointer);
			}
		};
		close.y = 16;
		close.x = 16;
		
		dockbar = new Image("dockbar", new TextureRegion(texture, 2, 2, 1, 1)) {
			protected boolean touchDown(float x, float y, int pointer) {
				if(this.hit(x, y) == this) {
					((ControllerGroup)(this.parent)).maxDockbar();
				}
				return super.touchDown(x, y, pointer);
			}
		};
		
		this.addActor(play);
		this.addActor(help);
		this.addActor(close);
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		texture.dispose();
	}
	
	public void delta(float delta) {
		if(GLOBAL.isPause == true) {
			getCurrentFrame();
		}
		else if(readyMini == 1) {
			miniCount += delta;
			if(miniCount > miniTimeout) {
				drawDockbar();
			
				//Gdx.app.log("controller touchDragged : ", "Actor - x : " + x + " y : " + y + " ready : " + readyMini);		
			}
		}
	}
		
	private void getCurrentFrame() {
		track.x = (line.width - track.width) * GLOBAL.currentFrame / (13 * 9) + line.x;
	}
	
	protected void draw(SpriteBatch batch, float parentAlpha) {
		delta(Gdx.graphics.getDeltaTime());
		super.draw(batch, parentAlpha);
	}
	
	public void setSize(float width, float height) {
		play.x = (width - 64) / 2;
		play.y = 24;
		
		line.x = 32;
		line.y = 32 + 64 + 16;
		line.width = width - 64;

		track.x = 64;
		track.y = 32 + 64 + 16 - 4;
		
		help.x = width - 16 - 32;
		
		dockbar.width = width;
		
		this.width = width;
		this.height = height;
		
		this.removeActor(line);
		this.removeActor(track);		
		
		drawActors();
	}
	
	protected void drawActors() {
		if(GLOBAL.isPause == true) {
			this.addActor(line);
			this.addActor(track);			
			
			play.unpressedRegion = new TextureRegion(texture, 1, 0, 63, 63);
			play.pressedRegion = new TextureRegion(texture, 1, 0, 63, 63);
		}
		else {			
			this.removeActor(line);
			this.removeActor(track);
			
			play.unpressedRegion = new TextureRegion(texture, 1, 63, 63, 63);
			play.pressedRegion = new TextureRegion(texture, 1, 63, 63, 63);
		}		
	}
	
	
	protected void drawDockbar() {
		
		readyMini = 2;
		
		dockbar.height = 132.0f;
		this.addActor(dockbar);
		
		dockbar.action(FadeIn.$(1));
	}
	
	protected void removeDockbar() {
		
		readyMini = 0;
		miniCount = 0.0f;
			
		this.removeActor(dockbar);
	}
	
	protected void miniDockbar() {
		readyMini = 0;
		miniCount = 0.0f;
		
		this.action(MoveTo.$(0, -104, 1));
/*		
		this.action(MoveTo.$(0, -82, 1).setCompletionListener(
				new OnActionCompleted() {
					public void completed(Action action) {
						
					}
				}
			));
*/			
	}
	
	protected void maxDockbar() {
		this.action(MoveTo.$(0, 0, 1));
		
		final ControllerGroup g = this;
		dockbar.action(FadeOut.$(1).setCompletionListener(				
				new OnActionCompleted() {
					public void completed(Action action) {
						g.removeActor(dockbar);
					}
				}
			));
	}	
	
	protected boolean touchDown(float x, float y, int pointer) {
		
		boolean touch = super.touchDown(x, y, pointer);
		if(touch == true) {
			Actor hitActor = this.hit(x, y);
			if(hitActor != null) {
				if(hitActor == track) {
					trackPos = x;
					trackFrame = GLOBAL.currentFrame;
				}
			}			
			
			//Gdx.app.log("controller touchDown : ", "Actor - x : " + x + " y : " + y + " hit : " + hit(x, y).name);
		}
		else {
			//Gdx.app.log("controller touchDown : ", "Actor - x : " + x + " y : " + y + " hit : NULL");
			if(GLOBAL.isPause == false && y < 110.0f && readyMini == 0) {
				readyMini = 1;
				miniY = y;
			}
		}
		return touch;
	}
	
	protected boolean touchUp(float x, float y, int pointer) {
		boolean touch = super.touchUp(x, y, pointer);
		
		//if(touch == true) {
			//Actor hitActor = this.hit(x, y);
			//if(hitActor != null && hitActor == track) {
			if(trackPos != 0)
				trackPos = 0;
			//}			
		//}
		//else {
			if(readyMini != 0) {
				removeDockbar();
			}
		//}

		return touch;
	}
	
	protected boolean touchDragged(float x, float y, int pointer) {
		
		boolean touch = super.touchDragged(x, y, pointer);
		//if(touch == true) {
			//Actor hitActor = this.hit(x, y);
			//if(hitActor != null && hitActor == track) {
			if(trackPos != 0) {
				int step = (int)((x - trackPos) * 13 * 9 / (line.width));
				//Gdx.app.log("controller touchDragged : ", "trackPos : " + trackPos + " x : " + x + "width : " + track.width + " Step : " + step);
				GLOBAL.currentFrame = (trackFrame + step);
				
				if(GLOBAL.currentFrame < 0) {
					GLOBAL.currentFrame = 0;
				}
				else if(GLOBAL.currentFrame > (13 * 9 - 1)) {
					GLOBAL.currentFrame = (13 * 9 - 1);
				}
			}
		//}
		//else {
			if(readyMini == 2) {
				if((miniY - y) > 32.0f) {
					miniDockbar();
					//Gdx.app.log("controller touchDragged : ", "Actor - x : " + x + " y : " + y + " MINI!");				
				}
			}
	//	}
		
		return touch;
	}
	
	protected void onPlayButton() {
		GLOBAL.isPause = !GLOBAL.isPause;
		
		drawActors();
	}
	
	protected void onHelpButton() {
		final ControllerGroup g = this;

		Image about = new Image("about", new Texture(Gdx.files.internal("data/about.jpg"))) {
			protected boolean touchDown(float x, float y, int pointer) {
				if(this.hit(x, y) == this) {
					final Image a = this;
					Action act = FadeTo.$(0.0f, 1.0f).setCompletionListener( new OnActionCompleted() {
						@Override
						public void completed(Action action) {
							// TODO Auto-generated method stub
							g.removeActor(a);
						}});
					this.action(act);
				}
				return super.touchDown(x, y, pointer);
			}
		};
		about.x = (this.width - about.width) / 2;
		about.y = (this.height - about.height) / 2;
		
		this.addActor(about);
	}
	
	protected void onCloseButton() {
		//Gdx.app.exit();
		System.exit(0);
	}
	
	protected void dragTrack(float x, float y) {
		//Gdx.app.log("controller dragTrack : ", "x : " + x + " y : " + y);
	}
}
