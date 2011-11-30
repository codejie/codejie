package jie.java.android.lingoshook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class FingerDrawView extends View {

	private Bitmap bitmap;
	private Canvas canvas;
	private Path path;
	private Paint paint;
	
	private Paint mPaint;
	
	private float mX, mY;
	
	public FingerDrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1 },0.4f, 6, 3.5f));
		
		path = new Path();
		paint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(0xFF000000);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		canvas.drawPath(path, mPaint);
		
		canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), mPaint);

		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchStart(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touchUp();
			invalidate();
			break;
		default:
			break;
		}
		
		return true;//super.onTouchEvent(event);
	}
	
	private void touchStart(float x, float y) {
		path.reset();
		path.moveTo(x, y);
		mX = x;
		mY = y;
	}
	
	private void touchMove(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if(dx >= 4 || dy >= 4) {
			path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
		}
	}
	
	private void touchUp() {
		path.lineTo(mX, mY);
		canvas.drawPath(path, mPaint);
	}

}
