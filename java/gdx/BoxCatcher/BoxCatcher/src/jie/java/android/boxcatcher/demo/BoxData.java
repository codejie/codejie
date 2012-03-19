package jie.java.android.boxcatcher.demo;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import jie.java.android.boxcatcher.BoxActor;
import jie.java.android.boxcatcher.Global;
import jie.java.android.boxcatcher.StageData;
import jie.java.android.boxcatcher.StageData.Box;
import jie.java.android.boxcatcher.StageData.BoxRace;

public final class BoxData {

	public static void loadFrames(ArrayList<Box> frames) {
		//ground
		Box gp = new Box();
		gp.x = 10;
		gp.y = 10;
		gp.width = Global.SCREEN_WIDTH - 10;
		gp.height = 10;
		gp.type = BodyType.StaticBody;
		gp.shape = BoxActor.BoxShape.LINE;
		gp.filterBits = 0x0010;
		gp.name = "ground";
		frames.add(gp);
		

		Box lf = new Box();
		lf.x = 10;
		lf.y = 10;
		lf.width = 10;
		lf.height = Global.SCREEN_HEIGHT - 10;
		lf.type = BodyType.StaticBody;
		lf.shape = BoxActor.BoxShape.LINE;
		lf.name = "left";
		lf.filterBits = 0x0001;
		frames.add(lf);

		Box rt = new Box();
		rt.x = Global.SCREEN_WIDTH  - 10;
		rt.y = 10;
		rt.width = Global.SCREEN_WIDTH  - 10;
		rt.height = Global.SCREEN_HEIGHT - 10;
		rt.type = BodyType.StaticBody;
		rt.shape = BoxActor.BoxShape.LINE;
		rt.name = "right";
		rt.filterBits = 0x0001;
		frames.add(rt);
		
		//floor
		Box fr = new Box();
		fr.x = 10;
		fr.y = 74;
		fr.width = Global.SCREEN_WIDTH - 10;
		fr.height = 74;
		fr.type = BodyType.StaticBody;
		fr.shape = BoxActor.BoxShape.LINE;		
		fr.name = "floor";
		fr.filterBits = 0x0010;
		frames.add(fr);

		//dead line
		Box dl = new Box();
		dl.x = 0;
		dl.y = -20;
		dl.width = Global.SCREEN_WIDTH;
		dl.height = -20;
		dl.type = BodyType.StaticBody;
		dl.shape = BoxActor.BoxShape.LINE;
		dl.name = "dead";
		dl.filterBits = 0x0001;
		frames.add(dl);
		
		//dock		
		StageData.Box dk = new StageData.Box();
		dk.width = 300;
		dk.height = 60;
		dk.x = 90;
		dk.y = 12;
		dk.name = "bar";
		dk.friction = 0.0f;
		dk.density = 100.0f;
		dk.type = BodyType.DynamicBody;
		dk.shape = BoxActor.BoxShape.RECTANGLE;
		dk.filterBits = 0x0011;
		dk.race = BoxRace.DOCK;
		frames.add(dk);

	}
	
	public static void loadBoxes(HashMap<Integer, ArrayList<Box>> boxes) {
/*		
		ArrayList<Box> abox = new ArrayList<Box>();
		Box b = new Box();
		b.name = "circle";
		b.x = 350;
		b.y = 350;
		b.height = 128;
		b.width = 128;
		b.texture = 1;
		b.shape = BoxActor.BoxShape.CIRCLE;
		b.type = BodyType.DynamicBody;
		b.angle = 0;//MathUtils.PI * 1.5f;
		b.friction = 1.0f;
		b.filterBits = 0x0001;
		b.race = BoxRace.BOX;
		abox.add(b);
		
		Box d = new Box();
		d.name = "box";
		d.x = 200;
		d.y = 200;
		d.height = 112;
		d.width = 112;
		d.shape = BoxActor.BoxShape.RECTANGLE;
		d.type = BodyType.DynamicBody;
		d.restitution = 0.5f;
		d.texture = -1;//Global.TEXTURE.getRegion(1);
		d.animation = 1;//Global.TEXTURE.getAnimation(1);
		d.angle = MathUtils.PI;// . 1.0f;
		d.filterBits = 0x0001;
		d.race = BoxRace.BOX;
		abox.add(d);
		
		boxes.put(1, abox);
		
		ArrayList<Box> nbox = new ArrayList<Box>();
		Box nb = new Box();
		nb.name = "triangle";
		nb.x = 450;
		nb.y = 450;
		nb.height = 32;
		nb.width = 32;
		nb.texture = 1;
		nb.shape = BoxActor.BoxShape.TRIANGLE;
		nb.type = BodyType.DynamicBody;
		nb.friction = 0.5f;
		nb.filterBits = 0x0001;
		nb.race = BoxRace.BOX;
		nbox.add(nb);
		
		boxes.put(3, nbox);
*/		
	}
	
}
