package jie.java.android.demodictionaryoflac2.data;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;


public class DataFileAccess {

	private String ld2file = null;
	
	private RandomAccessFile file = null;	
	private static DataFileAccess instance = null;
	
	private int blockIndex = -1; 
	private int blockStart = -1;
	private final byte[] blockCache = new byte[2 * 16 * 1024];
	
	public static DataFileAccess instance() {
		return instance;
	}

	public static int init(final String ld2file) {
		if(instance != null)
			return 0;
		instance = new DataFileAccess();
		instance.ld2file = ld2file;
		return instance.init();
	}
	
	private int init() {
		try {
			file = new RandomAccessFile(ld2file, "r");//Global.SDCARD_ROOT + Global.DATA_ROOT + Global.LD2_FILE, "r");			
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
	
	public int setBlockCache(int index, int start, int offset, int size) {
		if(index == blockIndex)
			return 0;
		
		blockStart = start;
		
		if(setBlockCache(offset, size) == 0) {
			blockIndex = index;
			return 0;
		} else {
			blockIndex = -1;
			return -1;
		}		
	}

	private int setBlockCache(int offset, int size) {
		final ByteBuffer in = ByteBuffer.allocate(size);
		
		try {
			if(file.getChannel().read(in, offset) != size)
				return -1;			
		} catch (IOException e) {
			return -1;
		}
		
		if(decompressBlock(in, size, blockCache) != 0)
			return -1;
		
		return 0;
		}

	private static int decompressBlock(ByteBuffer in, int size, final byte[] out) {
		final Inflater inflater = new Inflater();
		final InflaterInputStream stream = new InflaterInputStream(new ByteArrayInputStream(in.array(), 0, size), inflater, size);
		
		try {
			while(stream.read(out) > 0);
		} catch (IOException e) {
			return -1;
		}
		inflater.end();
		return 0;
	}	
	
	private final Charset cs = Charset.forName("UTF-8");
	private final CharsetDecoder cd = cs.newDecoder();
	private final int cdSize = (int)cd.maxCharsPerByte();
	
	public final String getWordXml(int offset, int length) {
		cd.reset();
		final char[] ret = new char[cdSize * length];
		final CharBuffer retbuf = CharBuffer.wrap(ret);
		final ByteBuffer cachebuf = ByteBuffer.wrap(blockCache, offset - blockStart, length);
		cd.decode(cachebuf, retbuf, true);
		cd.flush(retbuf);
		
		return new String(ret, 0, length).trim();
	}
}
