/**
 * file   : Test.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 13, 2011 5:00:11 PM
 */
package jie.java.android.savingkeeper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public final class Test {
	public static void startActivity(Activity act) {
		Intent intent = new Intent(act, DisplayActivity.class);
		act.startActivity(intent);
//		act.finish();
	}
	
	public static void insertData() {
		DBAccess.TestData data = new DBAccess.TestData();
		data.str = "mystring";
		data.value = 113;
		GLOBAL.DBACCESS.insert(data);
	}
	
	public static void queryData() {
		Cursor cursor = GLOBAL.DBACCESS.query();
		//cursor.moveToFirst();
		int i = cursor.getCount();
		while(cursor.moveToNext()) {
			Log.d(GLOBAL.APP_TAG, cursor.getString(0));
			Log.d(GLOBAL.APP_TAG, ""+cursor.getInt(1));
		};
	}
}
