package jie.java.test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class MyReader {

	public static final class BlockData {

		public int index = 0;
		public int start = 0;
		public int end = 0;
		
		public final String toString() {
			return "index (" + index + ") = " + start + " : " + end; 
		}
	}	

	private static int offsetCompressedData = 0;
	private static int lengthCompressedData = 0;
	private static int offsetIndex = 0;
	private static int lengthIndex = 0;
	private static int offsetInflatedWords = 0;
	private static int lengthInflatedWords = 0;
	private static int offsetInflatedXml = 0;
	private static int lengthInflatedXml = 0;
//	private static int countDefinitions = 0;
	
	private static ArrayList<Integer> listCompressedDataBlock = new ArrayList<Integer>();
	
	static ArrayList<BlockData> listInflatedBlock = new ArrayList<BlockData>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String ld2file = "./data/3GPP.ld2";//"./data/Vicon English-Chinese(S) Dictionary.ld2";
		//final String ld2file = "./data/Vicon English-Chinese(S) Dictionary.ld2";
		
		try {
			checkFile(ld2file);
			
			inflateFile(ld2file);
			
//			final int countDefinitions = lengthIndex / 4;
//			for(int i = 0; i < lengthIndex / 4; ++ i) {
//				getData(i);
//			}
			//getData(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void checkFile(String ld2file) throws IOException {
		
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(ld2file, "r");
			final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
			file.getChannel().read(buf);
			
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			Output("file = " + ld2file);
			Output("Type = " + new String(buf.array(), 0, 4, "ASCII"));
			Output("Version = " + buf.getShort(0x18) + "." + buf.getShort(0x1A));
			Output("ID = 0x" + Long.toHexString(buf.getLong(0x1C)));
			
			int offset = buf.getInt(0x5C) + 0x60;
			if(buf.limit() > offset) {
				Output("Info Offset = 0x" + Integer.toHexString(offset));
				Output("Info Type = 0x" + Integer.toHexString(buf.getInt(offset)));
				Output("Info Size = 0x" + Integer.toHexString((buf.getInt(offset + 4) + offset + 12)));
				if (buf.getInt(offset) != 3 && buf.limit() > (offset - 0x1C)) {
					offset = (buf.getInt(offset + 4) + offset + 12);
				}
				else if(buf.getInt(offset) != 3) {
					Output("The file is not a LD2 local file.");
					return;
				}
				Output("Dictionary Type = 0x" + Integer.toHexString(buf.getInt(offset)));
				
				lengthCompressedData = buf.getInt(offset + 4) + offset + 8;
//				Output("Data Limit = 0x" + Integer.toHexString(lengthCompressedData) + "(" + lengthCompressedData + ")");
				
				lengthIndex = buf.getInt(offset + 8);// + offsetIndex;
				offsetInflatedWords = buf.getInt(offset + 12);
				lengthInflatedWords = buf.getInt(offset + 16);
				lengthInflatedXml = buf.getInt(offset + 20);
				offsetIndex = offset + 0x1C;
				offsetInflatedXml = offsetInflatedWords + lengthInflatedWords;
				
				final int countDefinitions = (lengthIndex) / 4;	

				buf.position(offsetIndex + lengthIndex + 8 + 4);
				do {
					offset = buf.getInt();
					listCompressedDataBlock.add(offset);
				} while((offset + buf.position()) < lengthCompressedData); 
				
				offsetCompressedData = buf.position();
				
				Output("Index Offset = 0x" + Integer.toHexString(offsetIndex));
				Output("Index Length = 0x" + Integer.toHexString(lengthIndex));
				Output("Compressed Data Offset = 0x" + Integer.toHexString(offsetCompressedData));
				Output("Compressed Data Length = 0x" + Integer.toHexString(lengthCompressedData));
				Output("Inflated Words Offset = 0x" + Integer.toHexString(offsetInflatedWords));
				Output("Inflated Word Length = 0x" + Integer.toHexString(lengthInflatedWords));
				Output("Inflated Xml Offset = 0x" + Integer.toHexString(offsetInflatedXml));
				Output("Inflated Xml Length = 0x" + Integer.toHexString(lengthInflatedXml));
				Output("Quantity of Compressed Data Block = 0x" + Integer.toHexString(listCompressedDataBlock.size()) + "(" + listCompressedDataBlock.size() + ")");			
				Output("Quantity of Definitions = 0x" + Integer.toHexString(countDefinitions) + "(" + countDefinitions + ")");
			}
			else {
				Output("The file is not a LD2 file.");
			}
		}
		finally {
			file.close();
		}
	}

	private static void inflateFile(String ld2file) throws IOException {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(ld2file, "r");
			final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
			file.getChannel().read(buf);
			
//			buf.position(offsetCompressedData);
			int offset = offsetCompressedData;
			int tmp = offsetCompressedData;
			int counter = 0;
						
			int start = 0;
			int size = 0;
			
			for (final Integer block : listCompressedDataBlock) {
				tmp = offsetCompressedData + block.intValue();
				Output(counter + " : Decompress = " + Integer.toHexString(offset) + " length = " + Integer.toHexString((tmp - offset)));
				size = decompress(buf, offset, tmp - offset);
				Output("Done.");
				offset = tmp;
				
				BlockData data = new BlockData();
				data.index = counter ++ ;
				data.start = start;
				data.end = (start += size);
				
				listInflatedBlock.add(data);
			}		
		}
		finally {
			file.close();
		}
		
		for(final BlockData data : listInflatedBlock) {
			Output(data.toString());
		}
	}	
	
	private static int decompress(ByteBuffer buf, int offset, int length) throws IOException {
		final Inflater inflater = new Inflater();
		final InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(buf.array(), offset, length), inflater, 1024 * 8);
		final FileOutputStream out = new FileOutputStream("output.data", true);
		
	    final byte[] buffer = new byte[1024 * 8];
	    int len;
	    int ret = 0;
	    while ((len = in.read(buffer)) > 0) {
	      out.write(buffer, 0, len);
	      ret += len;
	    }		
		
		inflater.end();
		out.close();
		
		return ret;
	}
	
	//wordid -> word -> index -> xml offset and length
	// t1 : wordid -> word -> index
	// t2 : wordid -> xml offset and length -> block 1 -> block 2
	// t3 : block index -> block offset -> block size
	
	private static void getData(final int index) throws IOException {
		RandomAccessFile file = new RandomAccessFile("output.data", "r");
		final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
		file.getChannel().read(buf);
	    buf.order(ByteOrder.LITTLE_ENDIAN);
		int offset = index;
		final int idx[] = new int[6];//		
		getIndex(buf, offset * 10, idx);

		if(idx[5] != idx[1]) {
			Output("self xml = " + getXml(buf, idx[1], idx[5] - idx[1]));
		}
		if(idx[3] == 0) {
			Output("word = " + getWord(buf, idx[0], idx[4] - idx[0]));
		}
		else {
			int ref = idx[3];
			int offsetword = idx[0];
			final int lenword = idx[4] - idx[0];
			
			while(ref -- > 0) {
				offset = buf.getInt(offsetInflatedWords + idx[0]);
				getIndex(buf, offset * 10, idx);
				Output("ref(" + offset + ") xml = " + getXml(buf, idx[1], idx[5] - idx[1]));
				offsetword += 4;
			}
			Output("word = " + getWord(buf, offsetword, lenword));
		}

		file.close();
	}

	private static void getIndex(ByteBuffer buf, int offset, final int[] idx) {
		buf.position(offset);
		idx[0] = buf.getInt();
		idx[1] = buf.getInt();
		idx[2] = buf.get();
		idx[3] = buf.get();
		idx[4] = buf.getInt();
		idx[5] = buf.getInt();
	}
	
	private static final String getWord(ByteBuffer buf, int offset, int len) {
		String word = new String(UTF8Decode(buf, offsetInflatedWords + offset, len));
		return word;
	}
	
	private static final String getXml(ByteBuffer buf, int offset, int len) {
		String xml = new String(UTF8Decode(buf, offsetInflatedWords + lengthInflatedWords + offset, len));
		return xml;
	}

	private static char[] UTF8Decode(final ByteBuffer buf, final int offset, final int len) {
		final Charset cs = Charset.forName("UTF-8");
		final CharsetDecoder cd = cs.newDecoder();
		int size = (int) cd.maxCharsPerByte();
		char[] ret = new char[ len * size ];
		final CharBuffer retbuf = CharBuffer.wrap(ret);
		
		final ByteBuffer in = ByteBuffer.wrap(buf.array(), offset, len);
		CoderResult cr = cd.decode(in, retbuf, true);
		cr = cd.flush(retbuf);
		
		if(ret.length != len) {
			ret = Arrays.copyOf(ret, len);
		}
		
		return ret;
	}
	
	private static void Output(String string) {
		System.out.println(string);
	}

}
