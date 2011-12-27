package jie.java.android.lingoshook;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

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
