package jie.java.android.lingoshook;

import com.wooboo.adlib_android.AdListener;
import com.wooboo.adlib_android.ImpressionAdView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/*
public class AdPanelView extends TextView {

	public AdPanelView(Context context,AttributeSet paramAttributeSet) {
		  super(context,paramAttributeSet);
		  this.setText("AD");
	}
	
	public AdPanelView(Context context) {
		super(context);
		this.setText("AD");
		// TODO Auto-generated constructor stub
	}

}
*/

public class AdPanelView extends View implements AdListener {

	private static String APP_AD_ID		=	"123456789012345678901234567890AB";
	private static boolean IS_TESTING	=	true;
	private static int[] AD_TYPE = null;
	
	public AdPanelView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		// TODO Auto-generated constructor stub
		
		initWoobooAdView();
	}

	private void initWoobooAdView() {
		ImpressionAdView ad = new ImpressionAdView(this.getContext(), APP_AD_ID, this, this.getTop(), this.getLeft(), 0xFFFFFF, IS_TESTING, AD_TYPE);
		ad.show(40);
		ad.setAdListener(this);		
	}
	
	private static void freeWoobooAdView() {
		ImpressionAdView.close();
	}
	
	@Override
	public void onFailedToReceiveAd(Object arg0) {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "AD load failed.");
	}

	@Override
	public void onPlayFinish() {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "AD play over.");
	}

	@Override
	public void onReceiveAd(Object arg0) {
		// TODO Auto-generated method stub
		Log.d(Global.APP_TITLE, "AD received.");
	}
	
	public static void release() {
		freeWoobooAdView();
	}
	
}
