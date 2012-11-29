package jie.java.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MyReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String ld2file = "./data/3GPP.ld2";//"./data/Vicon English-Chinese(S) Dictionary.ld2";
		
		try {
			checkLD2File(ld2file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void checkLD2File(String ld2file) throws IOException {
		
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
				
				final int limitData = buf.getInt(offset + 4) + offset + 8;
				Output("Data Limit = 0x" + Integer.toHexString(limitData));
				
				final int offsetIndex = offset + 0x1C;
				final int offsetCompressedDataHeader = buf.getInt(offset + 8) + offsetIndex;
				final int lenInflatedWordsIndex = buf.getInt(offset + 12);
				final int lenInflatedWords = buf.getInt(offset + 16);
				final int lenInflatedXml = buf.getInt(offset + 20);
				final int countDefinitions = (offsetCompressedDataHeader - offsetIndex) / 4;	

				buf.position(offsetCompressedDataHeader + 8);
				int countCompressedBlock = -1;
				do {
					offset = buf.getInt();
					++ countCompressedBlock;
				} while((offset + buf.position()) < limitData); 
				
				final int offsetCompressedData = buf.position();
				
				Output("Index Offset = 0x" + Integer.toHexString(offsetIndex));
				Output("Compressed Data Header Offset = 0x" + Integer.toHexString(offsetCompressedDataHeader));
				Output("Compressed Data Offset = 0x" + Integer.toHexString(offsetCompressedData));
				Output("Quantity of Compressed Data = 0x" + Integer.toHexString(countCompressedBlock) + "(" + countCompressedBlock + ")");
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

	private static void Output(String string) {
		System.out.println(string);
	}

}
