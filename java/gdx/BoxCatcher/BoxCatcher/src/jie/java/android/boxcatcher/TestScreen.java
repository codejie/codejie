package jie.java.android.boxcatcher;

public class TestScreen extends BCScreen {

	// TestStage stage = null;
	
	public TestScreen(BCGame game) {
		super(game);
		
		//stage = new TestStage();
	}

	@Override
	public void show() {
		this.addActor(new TestStage());
		super.show();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		this.draw();
		//stage.draw();
	}



}
