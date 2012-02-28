package jie.java.android.boxcatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
		param.width = 100;
		param.name = "box";
		param.type = BodyType.DynamicBody;
		param.restitution = 1.0f;// .friction = 0.1f;
		param.friction = 0.1f;
		final BoxActor actor = new RectangleBox(world, param);
		actor.setRegion(new TextureRegion(texture,0, 0, 32, 32));
		
		actor.setContactListener(new BoxContactListener() {

			//final BoxActor a = actor;
			@Override
			public void onAsSourceBeginContact(BoxActor target) {
				// TODO Auto-generated method stub
				Gdx.app.log("tag", actor.name + " begin contact as source - target = " + target.name);
				//if(target.name == "left") {
				//	actor.markToRemove(true);
				//}
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
		
		param.width = 100;
		param.position = new Vector2(120, 130);
		BoxActor other = new RectangleBox(world, param);
		this.addActor(other);
		
		//triangle
		BoxActor.Parameter tp = new BoxActor.Parameter();
		tp.position = new Vector2(200, 120);
		tp.height = 100;
		tp.width = 50;
		tp.name = "triangle";
		tp.type = BodyType.DynamicBody;
		BoxActor triangle = new TriangleBox(world, tp);
		this.addActor(triangle);
		
		
		//ground
		param = new BoxActor.Parameter();
		param.position = new Vector2(10, 10);
		param.height = 10;
		param.width = 460;
		param.name = "ground";
		param.type = BodyType.StaticBody;
		BoxActor ground = new RectangleBox(world, param);
		this.addActor(ground);
		
		BoxActor.Parameter lp = new BoxActor.Parameter();
		lp.position = new Vector2(0, 10);
		lp.height = 500;
		lp.width = 10;
		lp.name = "left";
		lp.type = BodyType.StaticBody;
		BoxActor left = new RectangleBox(world, lp);
		this.addActor(left);

		BoxActor.Parameter rp = new BoxActor.Parameter();
		rp.position = new Vector2(470, 10);
		rp.height = 500;
		rp.width = 10;
		rp.name = "left";
		rp.type = BodyType.StaticBody;
		BoxActor right = new RectangleBox(world, rp);
		this.addActor(right);

	}

	private void initWorld() {
		// TODO Auto-generated method stub
		world = new World(Global.WORLD_GRAVITY, true);
		world.setContactListener(new WorldContactListener());
		
		renderer = new Box2DDebugRenderer();
	}

}
