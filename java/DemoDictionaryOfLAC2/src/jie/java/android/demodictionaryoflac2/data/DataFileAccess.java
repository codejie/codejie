package jie.java.android.demodictionaryoflac2.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import jie.java.android.demodictionaryoflac2.Global;

public class DataFileAccess {

	private RandomAccessFile file = null;
	
	private static DataFileAccess instance = null;
	
	public static DataFileAccess instance() {
		if(instance == null) {
			instance = new DataFileAccess();
			instance.init();
		}
		return instance;
	}

	private int init() {
		try {
			file = new RandomAccessFile(Global.SDCARD_ROOT + Global.DATA_ROOT + Global.LD2_FILE, "r");			
		} catch (FileNotFoundException e) {
			return -1;
		}
		return 0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(file != null) {
			file.close();
		}
		super.finalize();
	}	
	
	public final String getWordXml(int offset, int length, int block1, int block2) {
		
	}
}
