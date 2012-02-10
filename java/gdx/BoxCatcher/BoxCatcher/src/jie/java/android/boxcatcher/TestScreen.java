package jie.java.android.boxcatcher;

public class TestScreen extends BCScreen {

	private TestStage stage = null;
	
	public TestScreen(BCGame game) {
		super(game);
		
		stage = new TestStage();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		stage.draw();
	}



}
