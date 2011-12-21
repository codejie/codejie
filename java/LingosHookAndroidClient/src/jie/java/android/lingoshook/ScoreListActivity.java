package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;

public class ScoreListActivity extends Activity {

	private class ScoreCursorApdater extends CursorAdapter {

		public ScoreCursorApdater(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.score_list);
		
		loadTotalData();		
	}
	
	private void loadTotalData() {
		
	}
	
	private void loadScoreList() {
		
		
		
	}

}
