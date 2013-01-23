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

	private static final String DEF_PART_1 = "<DIV style=\"PADDING-BOTTOM: 0px; LINE-HEIGHT: 1.2em; PADDING-LEFT: 10px; WIDTH: 100%; PADDING-RIGHT: 10px; FONT-FAMILY: 'Tahoma'; FONT-SIZE: 10.5pt; PADDING-TOP: 10px\"><TABLE border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><TBODY><TR><TD style=\"BORDER-BOTTOM: #92b0dd 1px solid; BORDER-LEFT: #92b0dd 1px solid; LINE-HEIGHT: 1em; BACKGROUND: #cfddf0; COLOR: #000080; FONT-SIZE: 9pt; BORDER-TOP: #92b0dd 1px solid; BORDER-RIGHT: #92b0dd 1px solid\" nowrap><SPAN style=\"PADDING-BOTTOM: 0px; PADDING-LEFT: 2px; PADDING-RIGHT: 4px; PADDING-TOP: 0px\">Vicon English-Chinese(S) Dictionary</SPAN></TD><TD style=\"BORDER-BOTTOM: #92b0dd 1px solid\"></TD><TD style=\"BORDER-BOTTOM: #92b0dd 1px solid\" width=\"100%\" align=\"right\"><DIV style=\"WIDTH: 11px; HEIGHT: 11px; MARGIN-RIGHT: 10px\"></DIV></TD><TD style=\"BORDER-BOTTOM: #92b0dd 1px solid\"><DIV style=\"WIDTH: 11px; HEIGHT: 11px; \"></DIV></TD></TR></TBODY></TABLE><DIV ><DIV style=\"MARGIN: 5px 0px\"><DIV style=\"WIDTH: 100%\"><DIV style=\"LINE-HEIGHT: normal; FLOAT: left\">&nbsp;</DIV><DIV style=\"OVERFLOW-X: hidden; WIDTH: 100%\"><DIV style=\"LINE-HEIGHT: normal; MARGIN: 0px 0px 5px; COLOR: #808080\"><SPAN style=\"LINE-HEIGHT: normal; COLOR: #000000; FONT-SIZE: 10.5pt\"><B>";
	private static final String DEF_PART_2 = "</B></SPAN> &nbsp;<SPAN style=\"LINE-HEIGHT: normal;FONT-SIZE: 10.5pt\">[<FONT color=\"#009900\">";
	private static final String DEF_PART_3 = "</FONT>]</SPAN></DIV><DIV style=\"MARGIN: 0px 0px 5px\">";
	private static final String DEF_PART_4 = "</DIV></DIV></DIV></DIV></DIV></DIV>";
	
	public static class XmlHandler extends DefaultHandler {

		//<C><F><H><M>e?;?</M></H><I><N>×ÖÄ¸A</N></I></F></C>
		private enum Tags {
			UNKNOWN, C, F, H, L, N, I, E, U, M
		}
		
		private HashMap<String, Tags> tagMap = null;
		
		private String word = null;
		
		private String ret = null;
		private Tags tag = Tags.UNKNOWN;
		
		private Stack<Tags> tagStack = new Stack<Tags>();
		
		private String extension = null;
		
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
//			Log.d("======", "characters() - ch:" + (new String(ch)).trim() + " start = " + start + " length:" + length);// uri + " localName :" + localName + " qName:" + qName);
			String str = (new String(ch, start, length)).trim();
			Log.d("=====", "characters = " + str);
			switch(tag) {
			case N:
				ret += str;
				break;
			case E:
				extension = str;
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
//				ret += "</DIV>";
				if(extension != null) {
					ret += "<DIV>Extension : [" + extension + "]</DIV>";
					extension = null;
				}
				break;
			case N:
//				ret += "</DIV></DIV>";
				break;
			case M:
//				ret += "</FONT>]</SPAN>";
				break;
			case U:
				ret += "</FONT>";
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
				ret = DEF_PART_1 + word;
				break;
			case N:
				ret += DEF_PART_3;
				break;
			case M:
				ret += DEF_PART_2;
				break;
			case U:
//				ret += "<P>";
				ret += "<FONT COLOR='#990000'>";
				break;
				
			}
			
			tagStack.push(tag);
			
		}
		
	}
	
	private static XmlHandler handler = new XmlHandler();
	
	public static final String translate(final String word, final String xml) {
		String ret = "<HTML><HEAD><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></HEAD><BODY>";
		
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
