package jie.java.ld2scaner;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
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

public class FileScan {

	public static final class BlockData {

		public int index = 0;
		public int offset = 0;
		public int length = 0;
		public int start = 0;
		public int end = 0;
		
		public final String toString() {
			return "index (" + index + ") = [" + offset + "," + length + "] --> [" + start + "," + end + "]"; 
		}
	}	

	private static int offsetIndex = 0;
	private static int lengthIndex = 0;
	private static int offsetCompressedData = 0;
	private static int lengthCompressedData = 0;
	private static int offsetInflatedWords = 0;
	private static int lengthInflatedWords = 0;
	private static int offsetInflatedXml = 0;
	private static int lengthInflatedXml = 0;
	
	private static ArrayList<BlockData> listBlockData = new ArrayList<BlockData>();
	
	final static Charset charset = Charset.forName("UTF-8");
	final static CharsetDecoder charsetDecodeer = charset.newDecoder();
	final static int sizeCharsPerByte = (int) charsetDecodeer.maxCharsPerByte();	
		
	public static int scan(final String ld2file, final DBHelper db) {
		
		outputLog("file = " + ld2file);			
		
		if(scanInfo(ld2file, db) != 0) {
			return -1;
		}			
		
		if(scanData(ld2file + ".inflated", db) != 0) {
			return -1;
		}
		
		outputLog("Scan " + ld2file + " done.");
		
		return 0;
	}
	
	private static int scanInfo(String ld2file, DBHelper db) {
		try {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(ld2file, "r");
				final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
				file.getChannel().read(buf);
				
				buf.order(ByteOrder.LITTLE_ENDIAN);
	
				if(check(buf) != 0) {
					return -1;
				}
				
				buf.position(0);
				if(inflate(buf, ld2file + ".inflated") != 0) {
					return -1;
				}
			} finally {
				file.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		
		if(outputBaseInfo(db) != 0) {
			return -1;
		}			
		return 0;
	}

	private static int scanData(final String inflatedfile, final DBHelper db) {

		try {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(inflatedfile, "r");
				final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
				file.getChannel().read(buf);
			    buf.order(ByteOrder.LITTLE_ENDIAN);		
				
				final int countDefinitions = lengthIndex / 4;
				for(int i = 0; i < countDefinitions; ++ i) {
					if(checkData(buf, i, db) != 0) {
						return -1;
					}
				}
			} finally {
				file.close();
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}	

	private static int check(final ByteBuffer buf) {
		
		try {
			outputLog("Type = " + new String(buf.array(), 0, 4, "ASCII"));
			outputLog("Version = " + buf.getShort(0x18) + "." + buf.getShort(0x1A));
			outputLog("ID = 0x" + Long.toHexString(buf.getLong(0x1C)));
			
			int offset = buf.getInt(0x5C) + 0x60;
			if(buf.limit() > offset) {
				
				outputLog("Info Offset = 0x" + Integer.toHexString(offset));
				outputLog("Info Type = 0x" + Integer.toHexString(buf.getInt(offset)));
				outputLog("Info Size = 0x" + Integer.toHexString((buf.getInt(offset + 4) + offset + 12)));
				
				if (buf.getInt(offset) != 3 && buf.limit() > (offset - 0x1C)) {
					offset = (buf.getInt(offset + 4) + offset + 12);
				}
				else if(buf.getInt(offset) != 3) {
					outputLog("The file is not a LD2 local file.");
					return -1;
				}
				
				outputLog("Dictionary Type = 0x" + Integer.toHexString(buf.getInt(offset)));
				
				lengthCompressedData = buf.getInt(offset + 4) + offset + 8;			
				lengthIndex = buf.getInt(offset + 8);// + offsetIndex;
				offsetInflatedWords = buf.getInt(offset + 12);
				lengthInflatedWords = buf.getInt(offset + 16);
				lengthInflatedXml = buf.getInt(offset + 20);
				offsetIndex = offset + 0x1C;
				offsetInflatedXml = offsetInflatedWords + lengthInflatedWords;
				
				final int countDefinitions = (lengthIndex) / 4;	
	
				buf.position(offsetIndex + lengthIndex + 8 + 4);
				int idx = 0;
				int tmp = 0;
				do {
					offset = buf.getInt();
					BlockData data = new BlockData();					
					data.index = idx ++;
					data.length = offset - tmp;					
					listBlockData.add(data);
					tmp = offset;
					
				} while((offset + buf.position()) < lengthCompressedData); 
				
				offsetCompressedData = buf.position();
				
				outputLog("Index Offset = 0x" + Integer.toHexString(offsetIndex));
				outputLog("Index Length = 0x" + Integer.toHexString(lengthIndex));
				outputLog("Compressed Data Offset = 0x" + Integer.toHexString(offsetCompressedData));
				outputLog("Compressed Data Length = 0x" + Integer.toHexString(lengthCompressedData));
				outputLog("Inflated Words Offset = 0x" + Integer.toHexString(offsetInflatedWords));
				outputLog("Inflated Word Length = 0x" + Integer.toHexString(lengthInflatedWords));
				outputLog("Inflated Xml Offset = 0x" + Integer.toHexString(offsetInflatedXml));
				outputLog("Inflated Xml Length = 0x" + Integer.toHexString(lengthInflatedXml));
				outputLog("Quantity of Compressed Data Block = 0x" + Integer.toHexString(listBlockData.size()) + "(" + listBlockData.size() + ")");			
				outputLog("Quantity of Definitions = 0x" + Integer.toHexString(countDefinitions) + "(" + countDefinitions + ")");

			}
			else {
				outputLog("The file is not a LD2 file.");
				return -1;
			}		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}		
		
		for(final BlockData data : listBlockData) {
			outputLog(data.toString());
		}		
		
		return 0;
	}
	
	private static int inflate(final ByteBuffer buf, final String inflatedfile) {
		
		int offset = offsetCompressedData;
		int counter = 0;
					
		int start = 0;
		int size = 0;
		
		try {
			final FileOutputStream output = new FileOutputStream(inflatedfile, false);			
			
			for (BlockData data : listBlockData) {
			
				outputLog(counter + " : Decompress = " + Integer.toHexString(offset) + " length = " + Integer.toHexString(data.length));
				size = decompress(output, buf, offset, data.length);
				outputLog("Done.");
				data.offset = offset;
				data.start = start;
				data.end = (start += size);
				
				offset += data.length;
			}
			
			output.close();
			
		} catch (IOException e) {
			return -1;
		}		
		
		return 0;
	}
	
	private static int decompress(final FileOutputStream output, ByteBuffer buf, int offset, int length) throws IOException {
		
		final Inflater inflater = new Inflater();
		final InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(buf.array(), offset, length), inflater, 1024 * 8);
		
	    final byte[] buffer = new byte[1024 * 8];
	    int len;
	    int ret = 0;
	    while ((len = in.read(buffer)) > 0) {
	      output.write(buffer, 0, len);
	      ret += len;
	    }		

	    inflater.end();
	    
		return ret;
	}
	
	private static int checkData(ByteBuffer buf, final int index, final DBHelper db) {
		
		int offset = index;
		final int idx[] = new int[6];		
		getIndex(buf, offset * 10, idx);

		String word = null;
		
		if(idx[5] != idx[1]) {
			outputLog("self(" + offset + ") xml = " + getXml(buf, idx[1], idx[5] - idx[1]));
			outputBlockIndex(db, index, idx[1], (idx[5] - idx[1]));
		}
		if(idx[3] == 0) {
			outputLog("word = " + getWord(buf, idx[0], idx[4] - idx[0]));
			word = getWord(buf, idx[0], idx[4] - idx[0]);
		}
		else {
			int ref = idx[3];
			int offsetword = idx[0];
			int lenword = idx[4] - idx[0];
			
			while(ref -- > 0) {
				offset = buf.getInt(offsetInflatedWords + offsetword);
				getIndex(buf, offset * 10, idx);
				outputLog("ref(" + offset + ") xml = " + getXml(buf, idx[1], idx[5] - idx[1]));
				outputBlockIndex(db, index, idx[1], (idx[5] - idx[1]));
				offsetword += 4;
				lenword -= 4;
			}
			outputLog("word = " + getWord(buf, offsetword, lenword));
			word = getWord(buf, offsetword, lenword);
		}
		
		if(outputWord(db, index, word) != 0) {
			return -1;
		}
		
		return 0;
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
		String xml = new String(UTF8Decode(buf, offsetInflatedXml + offset, len));
		return xml;
	}
	
	private static char[] UTF8Decode(final ByteBuffer buf, final int offset, final int len) {
		charsetDecodeer.reset();
		char[] ret = new char[ len * sizeCharsPerByte ];
		final CharBuffer retbuf = CharBuffer.wrap(ret);
		
		final ByteBuffer in = ByteBuffer.wrap(buf.array(), offset, len);
		charsetDecodeer.decode(in, retbuf, true);
		charsetDecodeer.flush(retbuf);
		
		if(ret.length != len) {
			ret = Arrays.copyOf(ret, len);
		}
		
		return ret;
	}
	
	private static void outputLog(String string) {
		System.out.println(string);
	}

//////////////////////////////////////////////////////////
	
	private static int outputBaseInfo(final DBHelper db) {
		db.insertBaseInfo();
		for(final BlockData data : listBlockData) {
			db.insertBlockInfo(data);
		}
		return 0;
	}
	
	private static int outputWord(final DBHelper db, int index, final String word) {
		db.insertWordInfo(index, word);
		return 0;
	}	
	
	private static int outputBlockIndex(final DBHelper db, final int index, final int offset, final int length) {
		for(final BlockData data : listBlockData) {
			if((offsetInflatedXml + offset) <= data.end) {
				if((offsetInflatedXml + offset + length) <= data.end) {
					db.insertWordIndex(index, offsetInflatedXml + offset, length, data.index, -1);
				}
				else {
					db.insertWordIndex(index, offsetInflatedXml + offset, length, data.index, data.index + 1);
				}
				return 0;
			}
		}
		return -1;
	}

}