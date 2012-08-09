package jie.java.android.lingoshook;


import cn.domob.android.ads.DomobAdListener;
import cn.domob.android.ads.DomobAdView;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


public class AdPanelView implements DomobAdListener {

	public static int BANNER_WIDTH		=	50;
	
	private static boolean IS_AD_SHOW	=	true;//false;//true;
	private static String APP_AD_ID		=	"56OJyM1ouMGoaSnvCK";//"56OJyNcouMGCCFOyTJ";

/*	
	public AdPanelView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		// TODO Auto-generated constructor stub
		
		initWoobooAdView();
	}
*/	
	public AdPanelView(Context context, LinearLayout parent, int x, int y) {
		if(IS_AD_SHOW) {
			
			DomobAdView ad = new DomobAdView(context, APP_AD_ID, DomobAdView.INLINE_SIZE_320X50);
			ad.setRefreshable(true);
			ad.setOnAdListener(this);
			parent.addView(ad, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
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
