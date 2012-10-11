package jie.java.android.lingoshook.data;

import android.os.Bundle;

public abstract class BaseDataObject {
	
	public abstract int init(Bundle savedInstanceState);
	
	public int dump(Bundle bundle) {
		return 0;
	}
	
	protected void release() {
		
	}
}
