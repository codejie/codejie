package jie.java.android.demodictionaryoflac2.data;

import java.util.ArrayList;

public class Word {

	public static final class XmlData {
		private int dictid = -1;
//		private int flag = -1;
		private final String xml;
		
		public XmlData(int dictid, final String xml) {
			this.dictid = dictid;
			this.xml = xml;
		}

		public int getDictid() {
			return dictid;
		}

		public String getXml() {
			return xml;
		}
	}
	
	private final int index;
	private final String text;
	private final int flag;
	
	private ArrayList<XmlData> xmlData = new ArrayList<XmlData>();
	
	public Word(int index, final String text, int flag) {
		this.index = index;
		this.text = text;
		this.flag = flag;
	}
	
	public void addXmlData(int dictid, final String xml) {
		xmlData.add(new XmlData(dictid, xml));
	}

	public int getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public int getFlag() {
		return flag;
	}

	public ArrayList<XmlData> getXmlData() {
		return xmlData;
	}	
	
}
