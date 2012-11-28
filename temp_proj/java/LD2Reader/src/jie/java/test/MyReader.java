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
		final String ld2file = "./data/3GPP.ld2";
		
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
			
			final int offset = buf.getInt(0x5C) + 0x60;
			if(buf.limit() > offset) {
				
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
