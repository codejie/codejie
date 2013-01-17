package jie.java.android.demodictionaryoflac2.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XmlTranslator {

	public static class XmlHandler extends DefaultHandler {

		private enum Tags {
			UNKNOWN, C, F, H, L, N, I, E, U, M
		}
		
		private HashMap<String, Tags> tagMap = null;
		
		private String word = null;
		
		private String ret = null;
		private Tags tag = Tags.UNKNOWN;
		
		private Stack<Tags> tagStack = new Stack<Tags>();
		
		public XmlHandler() {
			initMap();
		}
		
		private void initMap() {
			tagMap = new HashMap<String, Tags>();
			
			tagMap.put("C", Tags.C);
			tagMap.put("F", Tags.F);
			tagMap.put("H", Tags.H);
			tagMap.put("L", Tags.L);
			tagMap.put("N", Tags.N);
			tagMap.put("I", Tags.I);
			tagMap.put("E", Tags.E);
			tagMap.put("U", Tags.U);
			tagMap.put("M", Tags.M);
		}
		
		public void reset(final String word) {
			this.word = word;
			this.ret = null;
			tagStack.clear();
		}

		public final String getRet() {
			return this.ret;
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
//			Log.d("======", "characters() - ch:" + (new String(ch)).trim() + " length:" + length);// uri + " localName :" + localName + " qName:" + qName);
			String str = (new String(ch)).trim();
			switch(tag) {
			case N:
				ret += str;
				break;
			case E:
				ret += str;
				break;
			case M:
				ret += str;
				break;
			case U:
				ret += str;
				break;
			}
		}
		
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
//			Log.d("======", "endElement() - uri:" + uri + " localName :" + localName + " qName:" + qName);
			
			tag = tagMap.get(qName);
			
			if(tag == null) {
				Log.e("========", "Unknown tag - " + qName);
			}
			if(tag == null)
				return;
//			Log.d("=====", "end tag = " + tag);
			
			switch(tag) {
			case C:
				ret += "</DIV>";
				break;
			case N:
				ret += "</DIV>";
				break;
			case E:
				ret += "</DIV>";
				break;
			case M:
				ret += "]";
				break;
			}			
			
			tag = tagStack.pop();			
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//			Log.d("======", "startElement() - uri:" + uri + " localName :" + localName + " qName:" + qName);
			
			tag = tagMap.get(qName);
			if(tag == null) {
				Log.e("========", "Unknown tag - " + qName);
			}
			if(tag == null)
				return;
						
//			Log.d("=====", "start tag = " + tag);
			switch(tag) {
			case C:
				ret = "<DIV>" + word;
				break;
			case N:
				ret += "</DIV><DIV>";
				break;
			case E:
				ret += "<DIV>";
				break;
			case M:
				ret += "[";
				break;
			case U:
				ret += "<P>";
				break;
				
			}
			
			tagStack.push(tag);
			
		}
		
	}
	
	private static XmlHandler handler = new XmlHandler();
	
	public static final String translate(final String word, final String xml) {
		String ret = "<HTML><BODY>";
		
//		for(final String x : xml) {
			try {
				handler.reset(word);
				ret += procXml(xml);
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
		
		return handler.getRet();
	}
	
}
