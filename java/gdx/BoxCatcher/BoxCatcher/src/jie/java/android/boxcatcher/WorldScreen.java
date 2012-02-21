package jie.java.android.boxcatcher;

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
		
//		float x = this.camera.position.x;
//		float y = this.camera.position.y;
//		float z = this.camera.position.z;
//		
//		this.camera.position.set(1, 1, 0.01f);
//		
//		x = this.camera.position.x;
//		y = this.camera.position.y;
//		z = this.camera.position.z;		
		//Matrix4 matrix = new Matrix4();//this.camera.combined;
		//this.camera.combined.set(matrix);		
		initWorld();
		initBoxes();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		GL10 gl = Gdx.graphics.getGL10();// .gl10;
		

//        this.camera.update();//cam.update();
//        this.camera.apply(gl);//cam.apply(gl);
		
		this.act(delta);
		this.draw();
		
		Matrix4 matrix = this.camera.combined;
		matrix.scale(100, 100, 1);
		

		renderer.render(world, matrix);//this.camera.combined);
	}

	private void initBoxes() {
		texture = new Texture(Gdx.files.internal("data/1.png"));
		BoxActor.Parameter param = new BoxActor.Parameter();
		param.position = new Vector2(100, 20);
		param.height = 32;
		param.width = 32;
		param.name = "box";
		BoxActor actor = new BoxActor(world, param);
		actor.setRegion(new TextureRegion(texture,0, 0, 32, 32));
		this.addActor(actor);
	}

	private void initWorld() {
		// TODO Auto-generated method stub
		world = new World(Global.WORLD_GRAVITY, true);
		
		renderer = new Box2DDebugRenderer();
	}

}
