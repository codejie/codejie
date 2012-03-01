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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WorldScreen extends BCScreen {

	private World world = null;
	private Box2DDebugRenderer renderer = null;
	
	private Texture texture = null;
	
	public WorldScreen(BCGame game) {
		super(game);
			
		initWorld();
		initBoxes();
		
//		Gdx.input.setInputProcessor(this);
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
		renderer.render(world, this.camera.combined.scale(Global.WORLD_SCALE, Global.WORLD_SCALE, 1));
	}

	private void initBoxes() {
		
		initFrame();
		initActors();
	}

	private void initActors() {
	
		texture = new Texture(Gdx.files.internal("data/1.png"));
/*		
		Image image = new Image();
		image.height = 32;
		image.width = 32;
		image.x = 100;
		image.y = 200;
		image.touchable = true;
		image.setRegion(new TextureRegion(texture,0, 0, 32, 32));
		this.addActor(image);
*/

		BoxActor.Parameter param = new BoxActor.Parameter();
		param.x = 100;
		param.y = 500;
		param.height = 32;
		param.width = 32;
		param.name = "box";
		param.type = BodyType.DynamicBody;
		param.restitution = 0.1f;// .friction = 0.1f;
		param.friction = 0.1f;
		param.shape = BoxActor.BoxShape.BS_RECTANGLE;
		final BoxActor actor = new BoxActor(world, param);
		actor.setRegion(new TextureRegion(texture,0, 0, 32, 32));
		
		actor.setContactListener(new DefaultBoxContactListener(actor));
		
		actor.SetTouchListener(new TouchDownDestroyListener(actor));
		this.addActor(actor);
		
////		
//		param.width = 100;
//		param.x = 120;
//		param.y = 130;
//		BoxActor other = new BoxActor(world, param);
//		other.SetTouchListener(new TouchDownDestroyListener(other));
//		this.addActor(other);
		
		//triangle
		BoxActor.Parameter tp = new BoxActor.Parameter();
		tp.x = 200;
		tp.y = 420;
		tp.height = 64;
		tp.width = 64;
		tp.name = "triangle";
		tp.type = BodyType.DynamicBody;
		tp.shape = BoxActor.BoxShape.BS_TRIANGLE;
		tp.restitution = 0.9f;
		BoxActor triangle = new BoxActor(world, tp);
		this.addActor(triangle);
		
		//circle
		BoxActor.Parameter cp = new BoxActor.Parameter();
		cp.width = 64;
		cp.x = 200;
		cp.y = 500;
		cp.name = "circle";
		cp.shape = BoxActor.BoxShape.BS_CIRCLE;
		cp.type = BodyType.DynamicBody;
		cp.angle = 20.0f;
		cp.restitution = 0.9f;
		cp.friction = 0.9f;
		cp.density = 1.0f;
		BoxActor circle = new BoxActor(world, cp);
		this.addActor(circle);	
		
		//bar
		
		BoxActor.Parameter bp = new BoxActor.Parameter();
		bp.width = 300;
		bp.height = 64;
		bp.x = 90;
		bp.y = 40;
		bp.name = "bar";
		bp.friction = 0.5f;
		bp.density = 0.0f;
		bp.type = BodyType.DynamicBody;
		bp.shape = BoxActor.BoxShape.BS_RECTANGLE;
		BoxActor bar = new BoxActor(world, bp);
		bar.touchable = true;
		
		bar.SetTouchListener(new TestTouchListener(bar));
		
		this.addActor(bar);
		
	}

	private void initFrame() {
		//ground
		BoxActor.Parameter gp = new BoxActor.Parameter();
		gp.x = 10;
		gp.y = 10;
		gp.width = Global.SCREEN_WIDTH - 10;
		gp.height = 10;
		gp.type = BodyType.StaticBody;
		gp.shape = BoxActor.BoxShape.BS_LINE;
		BoxActor ground = new BoxActor(world, gp);
		this.addActor(ground);
		
		gp.width = 10;
		gp.height = Global.SCREEN_HEIGHT - 10;
		BoxActor left = new BoxActor(world, gp);
		this.addActor(left);
		
		gp.x = Global.SCREEN_WIDTH  - 10;
		gp.y = 10;
		gp.width = Global.SCREEN_WIDTH  - 10;
		gp.height = Global.SCREEN_HEIGHT - 10;
		BoxActor right = new BoxActor(world, gp);

	}

	private void initWorld() {
		// TODO Auto-generated method stub
		world = new World(Global.WORLD_GRAVITY, true);
		world.setContactListener(new WorldContactListener());
		
		renderer = new Box2DDebugRenderer();
	}

}
