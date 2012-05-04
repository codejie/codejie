package jie.java.android.boxcatcher;

import com.badlogic.gdx.math.Vector2;


public final class Global {
	
	public enum PlatformType { DESKTOP, ANDROID, HTML5 };
	
	public static class SystemSetting {
		public PlatformType type;
		public float fps;
	}
	
	public static final int SCREEN_WIDTH		=	800;//480;//Gdx.graphics.getWidth();
	public static final int SCREEN_HEIGHT		=	480;//800;//Gdx.graphics.getHeight();
	
	public static final float WORLD_SCALE		=	50.0f;
	public static final Vector2 WORLD_GRAVITY 	= 	new Vector2(0.0f, -9.8f);
	
	public static final String APP_TITLE		=	"BoxCatcher";
	public static final String APP_VERSION		=	"dev";
	public static final String APP_TAG			=	"bx";

	public static SystemSetting sysSetting = new SystemSetting();
	
}
