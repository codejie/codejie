package jie.java.ld2scaner;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

public class DecodeHelper {
	
	private final class Decoder {
		public Charset charset = null;
		public CharsetDecoder decoder = null;
		public int charsPerByte = -1;
		
		public Decoder(final String name) {
			charset = Charset.forName(name);
			decoder = charset.newDecoder();
			charsPerByte = (int) decoder.maxCharsPerByte();
		}
		
		public final char[] decode(final ByteBuffer in, final int length) {
			char[] ret = new char[length * charsPerByte];
			final CharBuffer out = CharBuffer.wrap(ret);
			decoder.decode(in, out, true);
			decoder.flush(out);
			if(ret.length != out.length()) {
				ret = Arrays.copyOf(ret, out.length());
			}
			return ret;
		}
		
		public void reset() {
			if(decoder != null) {
				decoder.reset();
			}
		}
	}
	
	private Decoder wordDecoder = null;
	private Decoder xmlDecoder = null;
	
	private String[] decoderName = new String[] { "UTF-8", "UTF-16LE", "UTF-16BE", "EUC-JP" };
	
	public final String detectWordDecoder(final ByteBuffer in, final int length) {
		for(int i = 0; i < decoderName.length; ++ i) {
			try {
				wordDecoder = new Decoder(decoderName[i]);
				return new String(wordDecoder.decode(in, length));				
				
			} catch (final Throwable e) {
				continue;
			}
		}
		wordDecoder = null;
		return null;
	}

	public final String detectXmlDecoder(final ByteBuffer in, final int length) {
		for(int i = 0; i < decoderName.length; ++ i) {
			try {
				xmlDecoder = new Decoder(decoderName[i]);
				return new String(xmlDecoder.decode(in, length));		
				
			} catch (final Throwable e) {
				continue;
			}
		}
		xmlDecoder = null;
		return null;
	}
	
	public void reset() {
		wordDecoder = null;
		xmlDecoder = null;
	}
	
	public final String DecodeWordData(final ByteBuffer in, final int length) {
		if(wordDecoder == null) {
			return detectWordDecoder(in, length);
		}
		wordDecoder.reset();
		return new String(wordDecoder.decode(in, length));
	}
	
	public final String DecodeXmlData(final ByteBuffer in, final int length) {
		if(xmlDecoder == null) {
			return detectXmlDecoder(in, length);
		}
		xmlDecoder.reset();
		return new String(xmlDecoder.decode(in, length));		
	}
  
}
