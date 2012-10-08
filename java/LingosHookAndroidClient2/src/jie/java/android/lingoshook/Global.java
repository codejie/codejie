package jie.java.android.lingoshook;

import android.os.Bundle;
import jie.java.android.lingoshook.data.DBAccess;

public class Global {

	public static final String APP_TITLE	=	"lac";
	
	public static int init(Bundle savedInstanceState) {
		DBAccess.instance.init(savedInstanceState);
		
		return 0;
	}
}
