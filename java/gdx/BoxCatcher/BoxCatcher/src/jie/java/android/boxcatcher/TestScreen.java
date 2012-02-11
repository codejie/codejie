package jie.java.android.boxcatcher;

public class TestScreen extends BCScreen {

	TestStage stage = null;
	
	public TestScreen(BCGame game) {
		super(game);
		
		stage = new TestStage();
	}

	@Override
	public void show() {
		this.addActor(stage);
		super.show();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return super.touchDragged(x, y, pointer);
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return super.touchMoved(x, y);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		
		this.draw();
		//stage.draw();
	}



}
