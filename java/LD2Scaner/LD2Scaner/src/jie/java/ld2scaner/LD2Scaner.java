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
	
	private final static String ld2File = "./data/3GPP.ld2"; 

	public static void main(String[] args) {
		
		DBHelper db = DBHelper.create("./data/db.db");
		
//		if(FileScan.scan(ld2File, db) != 0) {
//			System.out.println("FAILED.");
//		}
		
		if(verifyData(db, ld2File, 100) != 0) {
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
				size += block.length;
				num = 2;
			}
			
			final ByteBuffer in = ByteBuffer.allocate(size);
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(ld2file, "r");
				file.getChannel().position(block.start);
				
				if(file.getChannel().read(in, size) != size)
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
			String xml = null;
			if(getWordXml(out, word, xml) != 0)
				return -1;
			
			System.out.println("Xml = " + xml);
		}	
		
		return 0;
	}

	private static int getWordXml(byte[] out, WordIndex data, String xml) {
		
		final Charset cs = Charset.forName("UTF-8");
		final CharsetDecoder cd = cs.newDecoder();
		int size = (int) cd.maxCharsPerByte();
		
		char[] ret = new char[data.length * size];
		final CharBuffer retbuf = CharBuffer.wrap(ret);
		
		int offset = 31556 + x_offset - 16384;//offsetInflatedXml + x_offset - 
		
		final ByteBuffer inbuf = ByteBuffer.wrap(out, offset, x_length);
		CoderResult cr = cd.decode(inbuf, retbuf, true);
		cr = cd.flush(retbuf);
		
		if(ret.length != x_length) {
			ret = Arrays.copyOf(ret, x_length);
		}
		
		String xml = new String(ret);
		
		try {
			//RandomAccessFile file = new RandomAccessFile("./data/3GPP.ld2", "r");
			RandomAccessFile file = new RandomAccessFile(ld2file, "r");
			
			final ByteBuffer bb = ByteBuffer.allocate(b_length);
			file.getChannel().position(b_offset);
			int s = file.getChannel().read(bb, b_offset);
			if(s != b_length) {
				return;
			}			

			file.close();
			
			//decompress
			final Inflater inflater = new Inflater();
			final InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(bb.array(), 0, b_length), inflater, b_length);
			
			final byte[] out = new byte[16 * 1024];
			while(in.read(out) > 0);
			
			inflater.end();
			//decode
			final Charset cs = Charset.forName("UTF-8");
			final CharsetDecoder cd = cs.newDecoder();
			int size = (int) cd.maxCharsPerByte();
			
			char[] ret = new char[x_length * size];
			final CharBuffer retbuf = CharBuffer.wrap(ret);
			
			int offset = 31556 + x_offset - 16384;//offsetInflatedXml + x_offset - 
			
			final ByteBuffer inbuf = ByteBuffer.wrap(out, offset, x_length);
			CoderResult cr = cd.decode(inbuf, retbuf, true);
			cr = cd.flush(retbuf);
			
			if(ret.length != x_length) {
				ret = Arrays.copyOf(ret, x_length);
			}
			
			String xml = new String(ret);
			
			Output("xml = " + xml);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			}

	private static int decompressBlock(ByteBuffer in, int size, final byte[] out) {
		final Inflater inflater = new Inflater();
		final InflaterInputStream stream = new InflaterInputStream(new ByteArrayInputStream(in.array(), 0, size), inflater, size);
		
		try {
			while(stream.read(out) > 0);
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}

}
