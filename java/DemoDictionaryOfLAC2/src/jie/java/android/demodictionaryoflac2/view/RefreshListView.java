package jie.java.android.demodictionaryoflac2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {

	private class FooterView extends LinearLayout {
		private TextView title = null;
		private ProgressBar progress = null;
		
		public FooterView(Context context) {
			super(context);
			
			initViews();
		}
		
		public FooterView(Context context, AttributeSet attrs) {
			super(context, attrs);
			
			initViews();
		}
		
		private void initViews() {
/*			
			LinearLayout layout = new LinearLayout(this.getContext());
			layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			
			layout.setOrientation(HORIZONTAL);
*/			
			//this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
			progress = new ProgressBar(this.getContext());
			progress.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			progress.setPadding(80, 10, 20, 10);
			title = new TextView(this.getContext());
			title.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));// CONTENT));
			title.setGravity(Gravity.CENTER_VERTICAL);
			title.setText("Release to load...");
//			progress.setMinimumHeight(40);
			
			this.addView(progress);
			this.addView(title);
			
//			this.addView(layout);
		}
		
		public void setVisible(boolean isVisible) {
			progress.setVisibility(isVisible ? View.VISIBLE : View.GONE);
			title.setVisibility(isVisible ? View.VISIBLE : View.GONE);
			//this.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		}
			
		public void setText(String text) {
			title.setText(text);
		}
	}
	
	public interface OnRefreshListener {
		public void onRefresh(int firstVisibleItem, int visibleItemCount, int totalItemCount);

		public void onBeginRefresh();

		public void onEndRefresh();

		public void onStopRefresh();
	}	
	
	private FooterView footer = null;
	
	private boolean isBottom = false;
	private int footerHeight = 0;	
	private int mLastMotionY = -1;

	private int firstVisibleItem = -1;
	private int visibleItemCount = -1;
	private int totalItemCount = -1;
	private boolean hasNotify = false;
	
	private boolean isEnabled = true;
	
	private OnRefreshListener refreshListener = null;	
	
	public RefreshListView(Context context) {
		super(context);
		
		initFooter();
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFooter();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFooter();
	}

	private void initFooter() {
		footer = new FooterView(this.getContext());
		
		footer.setVisible(false);
		this.addFooterView(footer);
		
		measureView(footer);
		
		footerHeight = footer.getMeasuredHeight();
		
		this.setOnScrollListener(this);
	}
	
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }	
	
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	
		if(totalItemCount > 0 && (totalItemCount > visibleItemCount) && ((firstVisibleItem + visibleItemCount) == totalItemCount)) {
			isBottom = true;
			
			this.firstVisibleItem = firstVisibleItem;
			this.visibleItemCount = visibleItemCount;
			this.totalItemCount = totalItemCount;		
		}
		else {
			isBottom = false;
		}	
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isEnabled) {
			if(isBottom) {
				
				if(mLastMotionY == -1) {
					mLastMotionY = (int) ev.getRawY();
				}			
				
				if(ev.getAction() == MotionEvent.ACTION_MOVE) {
	
					footer.setVisible(isBottom);
					int pad = (int) (((mLastMotionY - ev.getRawY()) - footerHeight ) / 1.75);
					footer.setPadding(0, 0, 0, pad);
					
					if(!hasNotify && refreshListener != null) {
						refreshListener.onBeginRefresh();
						hasNotify = true;
					}				
		        }
				else if(ev.getAction() == MotionEvent.ACTION_DOWN) {
					mLastMotionY = (int) ev.getRawY();// ev.getY();
				}
				else if(ev.getAction() == MotionEvent.ACTION_UP) {
					
					if(refreshListener != null) {
						refreshListener.onRefresh(this.firstVisibleItem, this.visibleItemCount, this.totalItemCount);
					}
					
					footer.setPadding(0, 0, 0, 0);
					footer.setVisible(false);
					
					if(refreshListener != null) {
						refreshListener.onEndRefresh();
					}		
					hasNotify = false;
				}
			}
			else {
				if(ev.getAction() == MotionEvent.ACTION_UP) {
					if(refreshListener != null) {
						refreshListener.onStopRefresh();
					}
					hasNotify = false;
				}
			}
		}
		
		return super.onTouchEvent(ev);
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		refreshListener = listener;
	}
	
	public void setText(String text) {
		footer.setText(text);
	}
	
	public void setRefreshEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
