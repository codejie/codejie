package jie.java.android.lingoshook;

public final class Score {

	private static final float rate[][] = {
			{ 1.75f, 0.80f, 0.45f, 0.17f },
			{ 1.50f, 1.25f, 0.55f, 0.20f },
			{ 1.00f, 0.80f, 0.45f, 0.20f },
			{ 0.80f, 0.50f, 0.30f, 0.17f }
	};
	
	private static long deltaUpdated = 0;
	
	public static void setDeltaUpdated(long delta) {
		deltaUpdated = delta;
	}
}
