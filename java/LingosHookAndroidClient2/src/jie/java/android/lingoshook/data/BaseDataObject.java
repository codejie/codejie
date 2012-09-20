package jie.java.android.lingoshook.data;

import android.os.Bundle;

public abstract class BaseDataObject {
	public abstract int init(Bundle savedInstanceState);
	protected abstract int release();
}
