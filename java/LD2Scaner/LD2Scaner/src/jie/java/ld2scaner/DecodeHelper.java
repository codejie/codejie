package jie.java.ld2scaner;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
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
		
		public final char[] decode(final ByteBuffer in, final int length) throws CharacterCodingException {
			
			if(decoder == null) 
				return null;
			
			decoder.reset();
			
			char[] ret = new char[length * charsPerByte];
			final CharBuffer out = CharBuffer.wrap(ret);
			
			CoderResult cr = decoder.decode(in, out, true);
			if(!cr.isUnderflow())
				cr.throwException();
			cr = decoder.flush(out);
			if(!cr.isUnderflow())
				cr.throwException();
			
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
	
	public boolean detectWordDecoder(final ByteBuffer in, final int length) {
		for(int i = 0; i < decoderName.length; ++ i) {
			try {
				for(int j = 0; j < 1; ++ j) {
					wordDecoder = new Decoder(decoderName[i]);
					wordDecoder.decode(in, length);
				}
				return true;				
				
			} catch (final CharacterCodingException e) {
				continue;
			} catch (final Throwable e) {
				continue;
			}
		}
		wordDecoder = null;
		return false;
	}

	public boolean detectXmlDecoder(final ByteBuffer in, final int length) {
		for(int i = 0; i < decoderName.length; ++ i) {
			try {
				xmlDecoder = new Decoder(decoderName[i]);
				xmlDecoder.decode(in, length);
				
				return true;
				
			} catch (final CharacterCodingException e) {
				continue;
			} catch (final Throwable e) {
				continue;				
			}
		}
		xmlDecoder = null;
		return false;
	}
	
	public void reset() {
		wordDecoder = null;
		xmlDecoder = null;
	}
	
	public final String DecodeWordData(final ByteBuffer in, final int length) throws CharacterCodingException {
		if(wordDecoder == null) {
			if(!detectWordDecoder(in, length))
				throw new CharacterCodingException();
		}

		return new String(wordDecoder.decode(in, length));
	}
	
	public final String DecodeXmlData(final ByteBuffer in, final int length) throws CharacterCodingException {
		if(xmlDecoder == null) {
			if(!detectXmlDecoder(in, length))
				throw new CharacterCodingException();
		}

		return new String(xmlDecoder.decode(in, length));		
	}
  
}
