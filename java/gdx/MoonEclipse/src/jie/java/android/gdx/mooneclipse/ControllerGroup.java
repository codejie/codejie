/**
 *   File: ControllerGroup.java
 * Author: codejie (codejie@gmail.com)
 *   Date: Jun 20, 2011
 */
package jie.java.android.gdx.mooneclipse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.utils.Disposable;

public class ControllerGroup extends Group implements Disposable {

	private Image play = null;
//	private Image pause = null;
	private Image help = null;
	private Image track = null;
	private Image line = null;
	private Image dockbar = null;
	private Texture texture = null;
	
	public ControllerGroup() {
		create();
	}
	
	private void create() {
		texture = new Texture(Gdx.files.internal("data/pause_play2.png"));
		
		play = new Image("play", new TextureRegion(texture, 1, 0, 63, 63));
//		pause = new Image("pause", new TextureRegion(texture, 1, 64, 63, 63));
		
		line = new Image("line", new TextureRegion(texture, 2, 2, 1, 1));
		line.height = 6;
		
		track = new Image("track", new TextureRegion(texture, 20, 20, 1, 1));
		track.height = 14;
		track.width = 48;
		
		help = new Image("help", new Texture(Gdx.files.internal("data/help.png")));
		help.y = 16;
		
		this.addActor(play);
		this.addActor(line);
		this.addActor(track);
		this.addActor(help);
		
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		texture.dispose();
	}
	
	private void getCurrentFrame() {
		
	}
	
	public void zoomIn() {
		
	}
	
	public void zoomOut() {
		
	}
	
	public void setSize(float width, float height) {
		play.x = (width - 64) / 2;
		play.y = 32;
		
		line.x = 32;
		line.y = 32 + 64 + 16;
		line.width = width - 64;

		track.x = 64;
		track.y = 32 + 64 + 16 - 4;
		
		help.x = width - 16 - 32;		
	}
}
