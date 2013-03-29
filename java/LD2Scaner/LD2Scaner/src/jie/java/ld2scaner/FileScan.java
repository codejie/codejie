package jie.java.ld2scaner;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import jie.java.ld2scaner.FileScan.WordFlag;

public class FileScan {

	public enum WordFlag {
		None(0), Normal(1), Reference(2), Normal_Reference(4);
		
		private final int value;
		
		private WordFlag(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
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
	
	protected interface OnDataListener {
		public int OnWordData(WordFlag flag, int index, final ByteBuffer buf, int offset, int length);
		public void OnXmlData(int wordid, int index, final ByteBuffer buf, int offset, int length);
		public void OnReferenceData(int wordid, int index, int refindex);		
	}

	private int offsetIndex = 0;
	private int lengthIndex = 0;
	private int offsetCompressedData = 0;
	private int lengthCompressedData = 0;
	private int offsetInflatedWords = 0;
	private int lengthInflatedWords = 0;
	private int offsetInflatedXml = 0;
	private int lengthInflatedXml = 0;
	
	private ArrayList<BlockData> listBlockData = new ArrayList<BlockData>();
	
//	final static Charset charset = Charset.forName("UTF-8");
//	final static CharsetDecoder charsetDecodeer = charset.newDecoder();
//	final static int sizeCharsPerByte = (int) charsetDecodeer.maxCharsPerByte();	
		
	private Decoder wordDecoder = null;
	private Decoder xmlDecoder = null;
	
	private int dictid = -1;
	private String ld2file = null;
	private DBHelper db = null;
	
	public FileScan(int dictid, final String ld2file, final DBHelper db) {
		this.dictid = dictid;
		this.ld2file = ld2file;
		this.db = db;
	}
	
	public boolean init() {
		return true;
	}
	
	public int scan() {
		
		outputLog("file = " + ld2file);			
		
		if(scanInfo(ld2file, db) != 0) {
			return -1;
		}
		
		try {
			if(detectDecoder(ld2file + ".inflated") != 0)
				return -1;
		} catch (final ArrayIndexOutOfBoundsException e) {
			return -1;
		}

		
		if(scanData(dictid, ld2file + ".inflated", db) != 0) {
			return -1;
		}
		
		outputLog("Scan " + ld2file + " done.");
		
		return 0;
	}
	
	private int scanInfo(String ld2file, DBHelper db) {
		try {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(ld2file, "r");
				final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
				file.getChannel().read(buf);
				
				buf.order(ByteOrder.LITTLE_ENDIAN);
				
//				checkHeader(buf);

				buf.position(0);
				if(checkIndex(buf) != 0) {
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
		
		if(outputBaseInfo(dictid, ld2file, db) != 0) {
			return -1;
		}			
		return 0;
	}

	private int scanData(final int dictid, final String inflatedfile, final DBHelper db) {

		OnDataListener dataListener = new OnDataListener() {			
				
			@Override
			public int OnWordData(WordFlag flag, int index, final ByteBuffer buf, int offset, int length) {
				String word = getWord(buf, offset, length);
				outputLog("word = " + word);
				return outputWord(db, index, word, flag.getValue());
			}

			@Override
			public void OnXmlData(int wordid, int index, final ByteBuffer buf, int offset, int length) {
				outputLog("self(" + offset + ") xml = " + getXml(buf, offset, length));
				outputWordIndex(db, wordid, dictid, index, offset, length);					
			}
			
			@Override
			public void OnReferenceData(int wordid, int index, int refindex) {
				outputReference(db, wordid, dictid, index, refindex);
			}
			
		};
		
		try {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(inflatedfile, "r");
				final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
				file.getChannel().read(buf);
			    buf.order(ByteOrder.LITTLE_ENDIAN);		
				
				final int countDefinitions = offsetInflatedWords / 10 - 1;//326290;//lengthIndex / 4;
				for(int i = 0; i < countDefinitions; ++ i) {
					outputLog("i = " + i);
					//getData(buf, i, dataListener);					
					//getData_without_index(buf, i, dataListener);
					getData_with_index(buf, i, dataListener);
//					if(checkData(buf, i, db) != 0) {
//						return -1;
//					}
				}
			} finally {
				file.close();
			}
		} catch (CharacterCodingException e) {
			// CharacterCoding is not supported.
			e.printStackTrace();			
			return -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}	

	private int checkIndex(final ByteBuffer buf) {
		
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
				lengthIndex = buf.getInt(offset + 8) + offsetIndex;
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
	
	private int inflate(final ByteBuffer buf, final String inflatedfile) {
		
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
	
	private int decompress(final FileOutputStream output, ByteBuffer buf, int offset, int length) throws IOException {
		
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
	
	private boolean flagDetectDecoder = true;

	private int detectDecoder(final String inflatedfile) throws ArrayIndexOutOfBoundsException {
				
		OnDataListener dataListener = new OnDataListener() {	
			
			final int maxTry = 50;
			int trying = 0;
			@Override
			public int OnWordData(WordFlag flag, int index, final ByteBuffer buf, int offset, int length) {
				if(wordDecoder == null) {
					wordDecoder = new Decoder();
				}
				try {
					final ByteBuffer in = ByteBuffer.wrap(buf.array(), offsetInflatedWords + offset, length);
					wordDecoder.detect(in, length);
				
					if(++ trying > maxTry) {
						flagDetectDecoder = false;
					}
				} catch (final CharacterCodingException e) {
					try {
						wordDecoder.next();
						trying = 0;
					} catch (final ArrayIndexOutOfBoundsException e1) {
						throw e1;
					}
				}
				return 0;
			}

			@Override
			public void OnXmlData(int wordid, int index, final ByteBuffer buf, int offset, int length) {
				if(xmlDecoder == null) {
					xmlDecoder = new Decoder();
				}
				try {
					final ByteBuffer in = ByteBuffer.wrap(buf.array(), offsetInflatedXml + offset, length);
					xmlDecoder.detect(in, length);
					if(++ trying > maxTry) {
						flagDetectDecoder = false;
					}
				} catch (final CharacterCodingException e) {
					try {
						xmlDecoder.next();
						trying = 0;
					} catch (final ArrayIndexOutOfBoundsException e1) {
						throw e1;
					}
				}				
			}
			
			@Override
			public void OnReferenceData(int wordid, int index, int refindex) {}			
		};
		
		try {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(inflatedfile, "r");
				final ByteBuffer buf = ByteBuffer.allocate((int) file.getChannel().size());
				file.getChannel().read(buf);
			    buf.order(ByteOrder.LITTLE_ENDIAN);		
				
				final int countDefinitions = offsetInflatedWords / 10 - 1;//326290;//lengthIndex / 4;
				for(int i = 0; i < countDefinitions; ++ i) {
					if(!flagDetectDecoder) {
						break;
					}
					outputLog("i = " + i);
					//getData(buf, i, dataListener);
					getData_without_index(buf, i, dataListener);
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
	
//	private void getData(ByteBuffer buf, final int index, OnDataListener dataListener) {
//	
//		int offset = index;
//		final int idx[] = new int[6];		
//		getIndex(buf, offset * 10, idx);
//
//		int wordid = -1;
//			
//		if(idx[5] != idx[1]) {
//			dataListener.OnXmlData(wordid, index, buf, idx[1], idx[5] - idx[1]);
//		} else {
//			return;//used to filter the 'extension' word, because I need not make the index for them now.
//		}
//		
//		if(idx[3] == 0) {
//			dataListener.OnWordData(null, index, buf, idx[0], idx[4] - idx[0]);
//		}
//		else {
//			int ref = idx[3];
//			int offsetword = idx[0];
//			int lenword = idx[4] - idx[0];
//			
//			while(ref -- > 0) {
//				offset = buf.getInt(offsetInflatedWords + offsetword);
//				getIndex(buf, offset * 10, idx);
////				dataListener.OnWordData(index, buf, idx[0], idx[4] - idx[0]);
//				dataListener.OnXmlData(wordid, index, buf, idx[1], idx[5] - idx[1]);
//				offsetword += 4;
//				lenword -= 4;
//			}
//			wordid = dataListener.OnWordData(null, index, buf, offsetword, lenword);
//		}
//	}

	private void getData_without_index(ByteBuffer buf, final int index, OnDataListener dataListener) {
		
		int offset = index;
		final int idx[] = new int[6];		
		getIndex(buf, offset * 10, idx);

		if((idx[5] - idx[1]) > 0) {
			//word
			int wordid = dataListener.OnWordData(WordFlag.Normal, index, buf, idx[0] + idx[3] * 4, (idx[4] - idx[0]) - idx[3] * 4);
			//xml data
			dataListener.OnXmlData(wordid, index, buf, idx[1], idx[5] - idx[1]);
		}
	}

	private void getData_with_index(ByteBuffer buf, final int index, OnDataListener dataListener) {
		
		int offset = index;
		final int idx[] = new int[6];		
		getIndex(buf, offset * 10, idx);
		
		int wordid = -1;
		
		if ((idx[3] == 0) && ((idx[5] - idx[1]) > 0)) {
			//word
			wordid = dataListener.OnWordData(WordFlag.Normal, index, buf, idx[0], idx[4] - idx[0]);
			dataListener.OnXmlData(wordid, index, buf, idx[1], idx[5] - idx[1]);
		} else if ((idx[3] == 0) && ((idx[5] - idx[1]) == 0)) {
			//impossible
		} else if ((idx[3] > 0) && ((idx[5] - idx[1]) > 0)) {
			//word + index
			wordid = dataListener.OnWordData(WordFlag.Normal_Reference, index, buf, idx[0] + idx[3] * 4, (idx[4] - idx[0]) - idx[3] * 4);
			dataListener.OnXmlData(wordid, index, buf, idx[1], idx[5] - idx[1]);
			getReferenceData(wordid, index, buf, idx[3], idx[0], dataListener);
		} else if ((idx[3] > 0) && ((idx[5] - idx[1]) == 0)) {
			//only index
			wordid = dataListener.OnWordData(WordFlag.Reference, index, buf, idx[0] + idx[3] * 4, (idx[4] - idx[0]) - idx[3] * 4);
			getReferenceData(wordid, index, buf, idx[3], idx[0], dataListener);
		}
	}
	
	private void getReferenceData(int wordid, int index, ByteBuffer buf, int ref, int offset, OnDataListener dataListener) {
		while(ref -- > 0) {
			dataListener.OnReferenceData(wordid, index, buf.getInt(offsetInflatedWords + offset));
			offset += 4;
		}
	}

	private void getIndex(ByteBuffer buf, int offset, final int[] idx) {
		buf.position(offset);
		idx[0] = buf.getInt();
		idx[1] = buf.getInt();
		idx[2] = buf.get();
		idx[3] = buf.get();
		idx[4] = buf.getInt();
		idx[5] = buf.getInt();
	}
	
	private final String getWord(ByteBuffer buf, int offset, int len) {
		final ByteBuffer in = ByteBuffer.wrap(buf.array(), offsetInflatedWords + offset, len);
		return new String(wordDecoder.decode(in, len));
	}
	
	private final String getXml(ByteBuffer buf, int offset, int len) {
		final ByteBuffer in = ByteBuffer.wrap(buf.array(), offsetInflatedXml + offset, len);
		return new String(xmlDecoder.decode(in, len));
	}
		
	private void outputLog(String string) {
		System.out.println(string);
	}

//////////////////////////////////////////////////////////
	
	private int outputBaseInfo(int dictid, final String ld2file, final DBHelper db) {
		String regex = "[^(/|\\|\\\\|//)][\\w\\s()-]+(\\.ld2)";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(ld2file);
		boolean f = matcher.find();
		if(!f) {
			return -1;
		}
		int pos = matcher.start();
		String file = ld2file.substring(pos);
		regex = "(\\.ld2)";
		p = Pattern.compile(regex);
		matcher = p.matcher(file);
		f = matcher.find();
		if(!f) {
			return -1;
		}
		
		String title = file.substring(0, matcher.start()); 
		
		db.insertBaseInfo(dictid, title, file, offsetInflatedXml);
		db.createDictionaryTables(dictid);
		
		for(final BlockData data : listBlockData) {
			db.insertBlockInfo(dictid, data);
		}
		return 0;
	}
	
	private int outputWord(final DBHelper db, int index, final String word, int flag) {
		return db.insertWordInfo(index, word, flag);
//		return 0;
	}	
	
	private int outputWordIndex(final DBHelper db, int wordid, int dictid, final int index, final int offset, final int length) {
		for(final BlockData data : listBlockData) {
			if((offsetInflatedXml + offset) <= data.end) {
				if((offsetInflatedXml + offset + length) <= data.end) {
					//db.insertWordIndex(index, offsetInflatedXml + offset, length, data.index, -1);
					db.insertWordIndex(wordid, dictid, index, offset, length, data.index, -1);
				}
				else {
					//db.insertWordIndex(index, offsetInflatedXml + offset, length, data.index, data.index + 1);
					db.insertWordIndex(wordid, dictid, index, offset, length, data.index, data.index + 1);
				}
				return 0;
			}
		}
		return -1;
	}

	protected void outputReference(DBHelper db, int wordid, int dictid, int index, int refindex) {
		db.insertReferenceIndex(wordid, dictid, index, refindex);
	}	
}
