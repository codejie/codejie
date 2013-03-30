package jie.java.android.demodictionaryoflac2.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

public class DictFileAccess {

	public enum DecoderType {
		
		UNKNOWN("unknown"), UTF_8("UTF-8"), UTF_16LE("UTF-16LE"), UTF_16BE("UTF-16BE"), EUC_JP("EUC-JP");
		
		private final String name;

		private DecoderType(final String name) {
			this.name = name;
		}

		public final String getName() {
			return name;
		}
	}
	
	private class Decoder {
	
		private Charset charset = null;
		private CharsetDecoder decoder = null;
		private int charsPerByte = -1;
		
		private void set(final String name) {
			
		}
		
		public final char[] decode(int decoder, final ByteBuffer in, final int length) {
			
			if(decoder == null) 
				return null;
			
			decoder.reset();
			
			char[] ret = new char[length * charsPerByte];
			final CharBuffer out = CharBuffer.wrap(ret);
			
			decoder.decode(in, out, true);
			decoder.flush(out);
			
			int size = out.position();
			
			if(ret.length != size) {
				ret = Arrays.copyOf(ret, size);
			}
			
			return ret;
		}		
	}
	
	public int currentDict	=	-1;
	Decoder
}
