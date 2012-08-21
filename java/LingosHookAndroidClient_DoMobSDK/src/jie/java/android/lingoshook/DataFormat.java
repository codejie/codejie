package jie.java.android.lingoshook;

import java.util.Iterator;
import java.util.Vector;



public class DataFormat {

	public static final int FORMAT_HTML		=	1;
	public static final int FORMAT_TEXT		=	2;
	
	public static final int TYPE_DICT		=	1;
	public static final int TYPE_MEMORY		=	2;
	
	private static final String STR_1	= "<HTML><HEAD><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/><TITLE></TITLE></HEAD><BODY><DIV style=\"PADDING-BOTTOM:0px;LINE-HEIGHT:1.2em;PADDING-LEFT:10px;WIDTH:100%;PADDING-RIGHT:10px;FONT-FAMILY:'Tahoma';FONT-SIZE:14pt;PADDING-TOP:10px\"><TABLE border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><TBODY><TR><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid;BORDER-LEFT:#92b0dd 1px solid;LINE-HEIGHT:1em;BACKGROUND:#cfddf0;COLOR:#000080;FONT-SIZE:10pt;BORDER-TOP:#92b0dd 1px solid;BORDER-RIGHT:#92b0dd 1px solid\" nowrap><DIV style=\"MARGIN:0px 3px 1px 0px;\"><SPAN style=\"PADDING-BOTTOM:0px;PADDING-LEFT:2px;PADDING-RIGHT:4px;PADDING-TOP:0px\">";	
	private static final String STR_2	= "</SPAN></DIV></TD><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid\" width=\"100%\" align=\"right\"><DIV style=\"WIDTH:11px;HEIGHT:11px;MARGIN-RIGHT:10px\"></DIV></TD></TR></TBODY></TABLE><DIV style=\"MARGIN:5px 0px;WIDTH:100%\"><DIV style=\"LINE-HEIGHT:normal;MARGIN:0px 0px 5px;COLOR:#808080\"><SPAN style=\"LINE-HEIGHT:normal;COLOR:#000000;FONT-SIZE:13pt\"><B>";	
	private static final String STR_3	= "</B></SPAN><SPAN style=\"LINE-HEIGHT:normal;FONT-SIZE:12pt\">&nbsp;[<FONT color=\"#009900\">";	
	private static final String STR_4	= "</FONT>]</SPAN></DIV><DIV style=\"MARGIN:0px 0px 5px\">";	
	private static final String STR_5	= "<DIV style=\"MARGIN:4px 0px\"><FONT color=\"#C04040\">";
	private static final String STR_6	= "</FONT>&nbsp;";
	private static final String STR_7	= "</DIV>";
	private static final String STR_8	= "</DIV></DIV></DIV></DIV></BODY></HTML>";	
	
	public static class Data {
		public String dict = null;
		public String word = null;
		public String symbol = null;
		public Vector<String> category = new Vector<String>();
		public Vector<String> meaning = new Vector<String>();
		
		public Data() {
		}
	}
	
	public static void data2html(final Data data, StringBuffer html) {
	
		html.append(STR_1 + data.dict);
		html.append(STR_2);
		html.append(STR_3);
		
		html.append(data.symbol);
		html.append(STR_4);
		
		for(int i = 0; i < data.category.size(); ++ i) {
			html.append(STR_5);
			html.append(data.category.elementAt(i));
			html.append(STR_6);
			html.append(data.meaning.elementAt(i));
			html.append(STR_7);
		}
		
		html.append(STR_8);
	}
}
