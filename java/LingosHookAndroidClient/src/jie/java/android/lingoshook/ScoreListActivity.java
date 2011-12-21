package jie.java.android.lingoshook;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ScoreListActivity extends Activity {

	private class ScoreCursorApdater extends CursorAdapter {

		public ScoreCursorApdater(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView u = (TextView)view.findViewById(R.id.textUpdated);
			TextView a = (TextView)view.findViewById(R.id.textAmount);
			
			u.setText(cursor.getString(0));
			a.setText(cursor.getString(1));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup group) {
			// TODO Auto-generated method stub
            return getLayoutInflater().inflate(R.layout.score_item, group);
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.score_list);
		
		loadTotalData();
		loadScoreList();
	}
	
	private void loadTotalData() {
		
	}
	
	private void loadScoreList() {
		
		GridView grid = (GridView)this.findViewById(R.id.gridScore);
		Cursor cursor = DBAccess.getScoreStatData();
		grid.setAdapter(new ScoreCursorApdater(this, cursor));
		
		
	}

}
