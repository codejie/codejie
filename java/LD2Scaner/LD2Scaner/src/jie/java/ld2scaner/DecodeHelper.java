package jie.java.ld2scaner;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public static class DecodeHelper {
	
	private final class Decoder {
		public Charset charset = null;
		public CharsetDecoder decoder = null;
		public int sizeCharsPerByte = -1;
		
		public Decoder(final String name) {
			charset = Charset.forName(name);
			decoder = charset.newDecoder();
			sizeCharsPerByte = decoder.maxCharsPerByte();
		}
		
		public final String decode();
	}
	public static final String DecodeWordData();
	public static final String DecodeXmlData();


  private static final ArrayHelper.SensitiveStringDecoder[] AVAIL_ENCODINGS = { new ArrayHelper.SensitiveStringDecoder(Charset.forName("UTF-8")),
  new ArrayHelper.SensitiveStringDecoder(Charset.forName("UTF-16LE")), new ArrayHelper.SensitiveStringDecoder(Charset.forName("UTF-16BE")),
  new ArrayHelper.SensitiveStringDecoder(Charset.forName("EUC-JP"))    };	

	final static Charset charset = Charset.forName("UTF-8");
	final static CharsetDecoder charsetDecodeer = charset.newDecoder();
	final static int sizeCharsPerByte = (int) charsetDecodeer.maxCharsPerByte();	

	
	
  private static  
  
  
  
}
