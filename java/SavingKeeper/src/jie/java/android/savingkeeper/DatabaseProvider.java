/**
 * file   : DatabaseProvider.java
 * author : codejie (codejie@gmail.com)
 * date   : Jul 12, 2011 4:20:30 PM
 */
package jie.java.android.savingkeeper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

	private class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME			= "SavingKeeper.db";
		private static final int DATABASE_VERSION			= 1;
		
		public static final String TABLE_NAME_BANKBOOK		= "BankBook";
		public static final String TABLE_COLUMN_NO			= "No";
		public static final String TABLE_COLUMN_TITLE		= "Title";
		public static final String TABLE_COLUMN_AMOUNT		= "Amount";
		public static final String TABLE_COLUMN_CHECKIN		= "CheckIn";
		public static final String TABLE_COLUMN_TYPE		= "Type";
		public static final String TABLE_COLUMN_RATE		= "Rate";
		public static final String TABLE_COLUMN_BANK		= "Bank";
		public static final String TABLE_COLUMN_NOTE		= "Note";
		
		public DatabaseHelper(Context context, String name,	CursorFactory factory, int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + TABLE_NAME_BANKBOOK + " ("
						+ TABLE_COLUMN_NO + " INTEGER PRIMARY KEY,"
						+ TABLE_COLUMN_TITLE + " TEXT,"
						+ TABLE_COLUMN_AMOUNT + " REAL,"
						+ TABLE_COLUMN_CHECKIN + " INTEGER,"
						+ TABLE_COLUMN_TYPE + " INTEGER,"
						+ TABLE_COLUMN_RATE + " REAL,"
						+ TABLE_COLUMN_BANK + " INTEGER,"
						+ TABLE_COLUMN_NOTE + " TEXT"
						+ ");";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
