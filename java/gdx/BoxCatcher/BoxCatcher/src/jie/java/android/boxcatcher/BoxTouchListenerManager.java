package jie.java.android.boxcatcher;

public class BoxTouchListenerManager {

	public class ShowInfoListener implements BoxTouchListener {

		@Override
		public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTouchDragged(BoxActor actor, float x, float y,
				int pointer) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onTouchMoved(BoxActor actor, float x, float y) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public class HorizontalMoveListener implements BoxTouchListener {

		private float px;
		
		@Override
		public boolean onTouchDown(BoxActor actor, float x, float y, int pointer) {
			px = x;
			return true;
		}

		@Override
		public void onTouchUp(BoxActor actor, float x, float y, int pointer) {
			px = 0;
		}

		@Override
		public void onTouchDragged(BoxActor actor, float x, float y, int pointer) {
			// TODO Auto-generated method stub
				actor.applyVelocity(((x - px) / Global.WORLD_SCALE), 0);// (x / Global.WORLD_SCALE - actor.width / (2 * Global.WORLD_SCALE)) , 0);			
		}

		@Override
		public boolean onTouchMoved(BoxActor actor, float x, float y) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
}
