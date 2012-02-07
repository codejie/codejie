package jie.java.android.lingoshook;

import cn.domob.android.ads.DomobAdListener;
import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobAdView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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

public class AdPanelView implements DomobAdListener {

	private static boolean IS_AD_SHOW	=	true;
	private static String APP_AD_ID		=	"56OJyNcouMGCCFOyTJ";
	private static boolean IS_TESTING	=	true;
	private static int[] AD_TYPE 		=	null;

/*	
	public AdPanelView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		// TODO Auto-generated constructor stub
		
		initWoobooAdView();
	}
*/	
	public AdPanelView(Activity activity, LinearLayout parent, int x, int y) {
		if(IS_AD_SHOW) {
			
			DomobAdView ad = new DomobAdView(activity);
			DomobAdManager.setPublisherId(APP_AD_ID);
			ad.setRequestInterval(20);
			parent.addView(ad, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			DomobAdManager.setIsTestMode(IS_TESTING);
		}
	}

	public static void release() {

	}

	@Override
	public void onFailedToReceiveFreshAd(DomobAdView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLandingPageClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLandingPageOpening() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceivedFreshAd(DomobAdView arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
