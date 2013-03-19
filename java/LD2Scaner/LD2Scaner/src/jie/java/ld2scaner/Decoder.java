package jie.java.ld2scaner;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

public class Decoder {

	public static final String[] decoderName = new String[] { "UTF-8", "UTF-16LE", "UTF-16BE", "EUC-JP" };
	
	private Charset charset = null;
	private CharsetDecoder decoder = null;
	private int charsPerByte = -1;
	
	private int index = 0;
	
	public Decoder(final String name) {
		charset = Charset.forName(name);
		decoder = charset.newDecoder();
		charsPerByte = (int) decoder.maxCharsPerByte();
	}
	
	public Decoder() {
		this(0);
		
		index = 0;
	}
	
	public Decoder(int index) {
		this(decoderName[index]);
	}
		
	public void next() throws ArrayIndexOutOfBoundsException {
		if(index > decoderName.length - 1)
			throw new ArrayIndexOutOfBoundsException();
	
		index ++;
		
		charset = Charset.forName(decoderName[index]);
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
	
}
