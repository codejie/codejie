package jie.java.android.boxcatcher;

import jie.java.android.boxcatcher.BoxActor.BoxType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WorldScreen extends BCScreen {

	private World world = null;
	private Box2DDebugRenderer renderer = null;
	
	private Texture texture = null;
	
	public WorldScreen(BCGame game) {
		super(game);
			
		initWorld();
		initBoxes();
	}

	@Override
	protected void finalize() throws Throwable {
		if(world != null) {
			world.dispose();
		}
		super.finalize();
	}



	@Override
	public void render(float delta) {
		super.render(delta);
		
		//GL10 gl = Gdx.graphics.getGL10();// .gl10;
		

//        this.camera.update();//cam.update();
//        this.camera.apply(gl);//cam.apply(gl);
		
		this.act(delta);
		this.draw();
		
		//Matrix4 matrix = this.camera.combined;
		//matrix.scale(Global.WORLD_SCALE,Global.WORLD_SCALE, 1);
		//renderer.render(world, matrix);
		renderer.render(world, this.camera.combined.scale(100, 100, 1));
	}

	private void initBoxes() {
		texture = new Texture(Gdx.files.internal("data/1.png"));
		BoxActor.Parameter param = new BoxActor.Parameter();
		param.position = new Vector2(100, 300);
		param.height = 32;
		param.width = 32;
		param.name = "box";
		param.type = BoxType.BT_DYNAMIC;
		param.restitution = 1.0f;// .friction = 0.1f;
		final BoxActor actor = new RectangleBox(world, param);
		actor.setRegion(new TextureRegion(texture,0, 0, 32, 32));
		
		actor.setContactListener(new BoxContactListener() {

			//final BoxActor a = actor;
			@Override
			public void onAsSourceBeginContact(BoxActor target) {
				// TODO Auto-generated method stub
				Gdx.app.log("tag", actor.name + " begin contact as source - target = " + target.name);
			}

			@Override
			public void onAsTargetBeginContact(BoxActor source) {
				// TODO Auto-generated method stub
				Gdx.app.log("tag", actor.name + " begin contact as targe");
			}

			@Override
			public void onAsSourceEndContact(BoxActor target) {
				// TODO Auto-generated method stub
				Gdx.app.log("tag", "end contact as source");
			}

			@Override
			public void onAsTargetEndContact(BoxActor source) {
				// TODO Auto-generated method stub
				Gdx.app.log("tag", "end contact as target");
			}
			
		});
		this.addActor(actor);
		
		param.position = new Vector2(120, 130);
		BoxActor other = new RectangleBox(world, param);
		this.addActor(other);
		
		param = new BoxActor.Parameter();
		param.position = new Vector2(10, 10);
		param.height = 10;
		param.width = 300;
		param.name = "ground";
		param.type = BoxType.BT_STATIC;
		BoxActor ground = new RectangleBox(world, param);
		this.addActor(ground);
	}

	private void initWorld() {
		// TODO Auto-generated method stub
		world = new World(Global.WORLD_GRAVITY, true);
		world.setContactListener(new WorldContactListener());
		
		renderer = new Box2DDebugRenderer();
	}

}
