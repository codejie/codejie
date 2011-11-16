package jie.java.android.savingkeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

import android.database.Cursor;
import android.util.Xml;

/*
<SavingKeeper version="11.11.08">
<Saving>
<Item id="1">
<Title></Title>
<Amount></Amount>
<Currency></Currency>
<Type></Type>
<Bank></Bank>
<Note></Note>
</Item>
</Saving>
</SavingKeeper>
 * 
 */

public class BackupManager {

	private static final String TAG_SAVINGKEEPER		=	"SavingKeeper";
	private static final String TAG_SAVING				=	"Saving";
	private static final String TAG_ITEM				=	"Item";
	private static final String TAG_TITLE				=	"Title";
	private static final String TAG_AMOUNT				=	"Amount";
	private static final String TAG_CURRENCY			=	"Currency";
	private static final String TAG_CHECKIN				=	"Checkin";
	private static final String TAG_TYPE				=	"Type";
	private static final String TAG_BANK				=	"Bank";
	private static final String TAG_NOTE				=	"Note";
	
	private static final String ATTR_VERSION			=	"version";
	private static final String ATTR_ID					=	"id";
	
	private static final String VALUE_VERSION			=	"1";
	
	public static int exportSavingList(final String filename) {
		
		File file = new File(filename);
		try {
			file.createNewFile();
		}
		catch (IOException e) {
			return -1;
		}
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
		}
		catch (FileNotFoundException e) {
			return -1;
		}
		
		Cursor cursor = GLOBAL.DBACCESS.querySaving();
		
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(os, "UTF-8");
			serializer.startDocument(null, false);
						
			serializer.startTag(null, TAG_SAVINGKEEPER);
			serializer.attribute(null, ATTR_VERSION, VALUE_VERSION);
			
			serializer.startTag(null, TAG_SAVING);
			
			while(cursor.moveToNext()) {
				serializer.startTag(null, TAG_ITEM);
				serializer.attribute(null, ATTR_ID, cursor.getString(0));
				
				serializer.startTag(null, TAG_TITLE);
				serializer.text(cursor.getString(1));
				serializer.endTag(null, TAG_TITLE);
				
				serializer.startTag(null, TAG_AMOUNT);
				serializer.text(cursor.getString(2));
				serializer.endTag(null, TAG_AMOUNT);
				
				serializer.startTag(null, TAG_CURRENCY);
				serializer.text(cursor.getString(3));
				serializer.endTag(null, TAG_CURRENCY);
				
				serializer.startTag(null, TAG_CHECKIN);
				serializer.text(cursor.getString(4));
				serializer.endTag(null, TAG_CHECKIN);
				
				serializer.startTag(null, TAG_TYPE);
				serializer.text(cursor.getString(5));
				serializer.endTag(null, TAG_TYPE);
				
				serializer.startTag(null, TAG_BANK);
				serializer.text(cursor.getString(6));
				serializer.endTag(null, TAG_BANK);
				
				serializer.startTag(null, TAG_NOTE);
				serializer.text(cursor.getString(7));
				serializer.endTag(null, TAG_NOTE);			
				
				serializer.endTag(null, TAG_ITEM);				
			}			
			
			serializer.endTag(null, TAG_SAVING);
			serializer.endTag(null, TAG_SAVINGKEEPER);			
			
			serializer.endDocument();
			serializer.flush();
			
			os.close();
		}
		catch (Exception e) {
			return -1;
		}
		
		cursor.close();
		
		return 0;
	}
	
	public int importSavingList(final String filename) {
		return 0;
	}
	
}
