package jie.java.android.boxcatcher;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import jie.java.android.boxcatcher.StageData.Box;


public final class WorldScreenRegister implements Register {

	public float stateTime = 0.0f;
	private ArrayList<StageData.Box> boxes = new ArrayList<StageData.Box>();
	
	public WorldScreenRegister() {
		Gdx.app.log("tag", "WorldScreenRegister - constructor.");
	}

	@Override
	public void refresh() {
		boxes.clear();
	}
	
	@Override
	public void dispose() {
	}
	
	public void addBox(Box box) {
		boxes.add(box);
	}
	
	public ArrayList<StageData.Box> getBoxes() {
		return boxes;
	}

}
