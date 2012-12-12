package jie.java.ld2scaner;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import jie.java.ld2scaner.FileScan.BlockData;

public class LD2Scaner {

	public static final class WordIndex {
		public int offset = 0;
		public int length = 0;
		public int block1 = 0;
		public int block2 = 0;		
	}
	
	public static final class WordData {
		public int index = 0;
		public String word = null;
		public ArrayList<WordIndex> block = new ArrayList<WordIndex>();
	}
	
	private final static String ld2File = "./data/Vicon English-Chinese(S) Dictionary.ld2";// "./data/3GPP.ld2"; 

	public static void main(String[] args) {
		
		DBHelper db = DBHelper.create("./data/db.db");
		
//		if(FileScan.scan(ld2File, db) != 0) {
//			System.out.println("FAILED.");
//		}
		
		if(verifyData(db, ld2File, 1134) != 0) {
			System.out.println("VerifyData() failed.");
		}
		
		db.close();		
	}

	private static int verifyData(DBHelper db, String ld2file, int index) {
		
		final WordData data = new WordData();
		data.index = index;
		
		if(db.getWordData(data) != 0)
			return -1;
		
		System.out.println("Word = " + data.word);
		
		for(final WordIndex word : data.block) {
			
			final BlockData block = new BlockData();
			block.index = word.block1;
			if(db.getBlockDat(block) != 0)
				return -1;
			
			int size = block.length;
			int num = 1;
			
			if(word.block2 != -1) {
				final BlockData block2 = new BlockData();
				block2.index = word.block2;
				if(db.getBlockDat(block2) != 0)
					return -1;
				size += block2.length;
				num = 2;
			}
			
			final ByteBuffer in = ByteBuffer.allocate(size);
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(ld2file, "r");
//				file.getChannel().position(block.offset);				
				if(file.getChannel().read(in, block.offset) != size)
					return -1;
				file.close();
			} catch (FileNotFoundException e) {
				return -1;
			} catch (IOException e) {
				return -1;
			}

			final byte[] out = new byte[num * 16 * 1024];		
			//decompress
			if(decompressBlock(in, size, out) != 0)
				return -1;
			//getXml
			String xml = getWordXml(out, num * 16 * 1024, block, word);
			
			System.out.println("Xml = " + xml);
		}	
		
		return 0;
	}

	private static final String getWordXml(final byte[] in, int insize, final BlockData block, final WordIndex data) {
		
		final Charset cs = Charset.forName("UTF-8");
		final CharsetDecoder cd = cs.newDecoder();
		int size = (int) cd.maxCharsPerByte();
		
		char[] ret = new char[data.length * size];
		final CharBuffer retbuf = CharBuffer.wrap(ret);
		
		int offset = data.offset - block.start; 
		
		final ByteBuffer inbuf = ByteBuffer.wrap(in, offset, data.length);
		CoderResult cr = cd.decode(inbuf, retbuf, true);
		cr = cd.flush(retbuf);
		
		if(ret.length != data.length) {
			ret = Arrays.copyOf(ret, data.length);
		}
		
		return new String(ret);
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

}
