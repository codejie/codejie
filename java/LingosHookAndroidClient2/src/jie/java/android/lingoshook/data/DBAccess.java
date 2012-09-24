package jie.java.android.lingoshook.data;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DBAccess extends BaseDataObject {
	
	private static final String TABLE_TEST		=	"Test";
	
	private static final String COLUMN_ID		=	"id";
	private static final String COLUMN_VALUE	=	"value";
	
	
	private static SQLiteDatabase db = null;


	@Override
	public int init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int release() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
