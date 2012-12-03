package jie.java.test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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

	
	private static int limitData = 0;
	private static int offsetIndex = 0;
	private static int offsetCompressedDataHeader = 0;
	private static int lenInflatedWordsIndex = 0;
	private static int lenInflatedWords = 0;
	private static int lenInflatedXml = 0;
	private static int countDefinitions = 0;
	private static int offsetCompressedData = 0;
	
	private static ArrayList<Integer> listDataBlock = new ArrayList<Integer>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String ld2file = "./data/Vicon English-Chinese(S) Dictionary.ld2";
		
		try {
			checkFile(ld2file);
			
			inflateFile(ld2file);
			
			getData(0);
			
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
				
				limitData = buf.getInt(offset + 4) + offset + 8;
				Output("Data Limit = 0x" + Integer.toHexString(limitData));
				
				offsetIndex = offset + 0x1C;
				offsetCompressedDataHeader = buf.getInt(offset + 8) + offsetIndex;
				lenInflatedWordsIndex = buf.getInt(offset + 12);
				lenInflatedWords = buf.getInt(offset + 16);
				lenInflatedXml = buf.getInt(offset + 20);
				countDefinitions = (offsetCompressedDataHeader - offsetIndex) / 4;	

				buf.position(offsetCompressedDataHeader + 8 + 4);
				do {
					offset = buf.getInt();
					listDataBlock.add(offset);
				} while((offset + buf.position()) < limitData); 
				
				offsetCompressedData = buf.position();
				
				Output("Index Offset = 0x" + Integer.toHexString(offsetIndex));
				Output("Compressed Data Header Offset = 0x" + Integer.toHexString(offsetCompressedDataHeader));
				Output("Compressed Data Offset = 0x" + Integer.toHexString(offsetCompressedData));
				Output("Quantity of Compressed Data = 0x" + Integer.toHexString(listDataBlock.size()) + "(" + listDataBlock.size() + ")");
				Output("Length of Inflated Words Index = 0x" + Integer.toHexString(lenInflatedWordsIndex));
				Output("Length of Inflated Word = 0x" + Integer.toHexString(lenInflatedWords));
				Output("Length of Inflated Xml = 0x" + Integer.toHexString(lenInflatedXml));
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
			for (final Integer block : listDataBlock) {
				tmp = offsetCompressedData + block.intValue();
				//Output("Decompress = " + offset + " length = " + (tmp - offset));
				decompress(buf, offset, tmp - offset);
				//Output("Done.");
				offset = tmp;
			}		
		}
		finally {
			file.close();
		}
	}	
	
	private static void decompress(ByteBuffer buf, int offset, int length) throws IOException {
		final Inflater inflater = new Inflater();
		final InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(buf.array(), offset, length), inflater, 1024 * 8);
		final FileOutputStream out = new FileOutputStream("output.data", true);
		
	    final byte[] buffer = new byte[1024 * 8];
	    int len;
	    while ((len = in.read(buffer)) > 0) {
	      out.write(buffer, 0, len);
	    }		
		
		inflater.end();
		out.close();
	}
	
	private static void getData(final int index) throws IOException {
		RandomAccessFile file = new RandomAccessFile("output.data", "r");
		final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
		file.getChannel().read(buf);
	    buf.order(ByteOrder.LITTLE_ENDIAN);
		int offset = 29;
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
				offset = buf.getInt(lenInflatedWordsIndex + idx[0]);
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
		String word = new String(UTF8Decode(buf, lenInflatedWordsIndex + offset, len));
		return word;
	}
	
	private static final String getXml(ByteBuffer buf, int offset, int len) {
		String xml = new String(UTF8Decode(buf, lenInflatedWordsIndex + lenInflatedWords + offset, len));
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
