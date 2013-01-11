package jie.java.android.demodictionaryoflac2.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XmlTranslator {

	public static class XmlHandler extends DefaultHandler {

		private HashMap<String, Integer> tagMap = null;
		
		private String word = null;
		
		private String ret = null;
		private Integer tag = -1;
		
		public XmlHandler() {
			initMap();
		}
		
		private void initMap() {
			tagMap = new HashMap<String, Integer>();
			tagMap.put("C", 1);
			tagMap.put("F", 2);
			tagMap.put("H", 3);
			tagMap.put("L", 4);
			tagMap.put("N", 5);
			tagMap.put("I", 6);
			tagMap.put("E", 7);
			tagMap.put("U", 8);
			tagMap.put("M", 9);
		}
		
		public void setWord(final String word) {
			this.word = word;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			Log.d("======", "characters() - ch:" + (new String(ch)).trim() + " length:" + length);// uri + " localName :" + localName + " qName:" + qName);
			String str = (new String(ch)).trim();
			switch(tag) {
			case 1:
				ret = "<DIV>" + word;
			}
		}
		
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Log.d("======", "endElement() - uri:" + uri + " localName :" + localName + " qName:" + qName);
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			Log.d("======", "startElement() - uri:" + uri + " localName :" + localName + " qName:" + qName);
			
			tag = tagMap.get(qName);
			if(tag == null) {
				Log.e("========", "Unknown tag - " + qName);
			}
		}
		
	}
	
	private XmlHandler handler = new XmlHandler();
	
	public static final String translate(final String word, final String xml) {
		String ret = "<HTML><BODY>";
		
//		for(final String x : xml) {
			try {
				procXml(xml);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		
		ret += "</BODY></HTML>";
		
		return ret;
	}
	
	private static final String procXml(final String xml) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		
		final String ret = null;
	
		parser.parse(is, handler);
		
		return ret;
	}
	
}
