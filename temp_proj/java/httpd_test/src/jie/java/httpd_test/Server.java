package jie.java.httpd_test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import jie.java.httpd_test.NanoHTTPD.Response;

//http://elonen.iki.fi/code/nanohttpd/

public class Server extends NanoHTTPD {

	public Server() throws IOException
	{
		super(8080, new File("."));
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
		
		if(method.equals("GET")) {
			if(uri.equals("/")) {
				return requestRoot(uri, method, header, parms, files);
			}
		}
		else if(method.equals("POST")) {
			if(uri.equals("/import_file.html")) {
				return requestImportFile(uri, method, header, parms, files);
			}
			else if(uri.equals("/import_file_done.html")) {
				return requestImportFileDone(uri, method, header, parms, files);
			}				
			else if(uri.equals("/input_data.html")) {
				return requestInputData(uri, method, header, parms, files);
			}
			else if(uri.equals("/input_data_done.html")) {
				return requestInputDataDone(uri, method, header, parms, files);
			}			
		}
		else {
			return unsupportedRequest(uri, method, header, parms, files);
		}
		return null;		
		
//		if(method.equals("POST")) {
//			if(uri.equals("/import_file_done.html")) {
//				return importfile(uri, method, header, parms, files);
//			}
//			else if(uri.equals("/input_data.html")) {
//				String msg = "<html><body><h1>Hello server</h1>\n";
//				if ( parms.getProperty("username") == null )
//					msg +=
//						"<form action='?' method='get'>\n" +
//						"  <p>Your name: <input type='text' name='username'></p>\n" +
//						"</form>\n";
//				else
//					msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";
//		
//				msg += "</body></html>\n";
//				return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, msg );		
//			}
// 
//		}
//		
//		
//		return super.serve(uri, method, header, parms, files);// NanoHTTPD.serve(uri, header, new File("."), true );
//		if(uri.isEmpty()) {
//			
//		}
//		
//		System.out.println( method + " '" + uri + "' " );
//		String msg = "<html><body><h1>Hello server</h1>\n";
//		if ( parms.getProperty("username") == null )
//			msg +=
//				"<form action='?' method='get'>\n" +
//				"  <p>Your name: <input type='text' name='username'></p>\n" +
//				"</form>\n";
//		else
//			msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";
//
//		msg += "</body></html>\n";
//		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, msg );
	}


	private Response requestImportFileDone(String uri, String method,
			Properties header, Properties parms, Properties files) {

		Enumeration e = header.propertyNames();
		while ( e.hasMoreElements())
		{
			String value = (String)e.nextElement();
			myOut.println( "  HDR: '" + value + "' = '" +
								header.getProperty( value ) + "'" );
		}
		e = parms.propertyNames();
		while ( e.hasMoreElements())
		{
			String value = (String)e.nextElement();
			myOut.println( "  PRM: '" + value + "' = '" +
								parms.getProperty( value ) + "'" );
		}
		e = files.propertyNames();
		while ( e.hasMoreElements())
		{
			String value = (String)e.nextElement();
			myOut.println( "  FLE: '" + value + "' = '" +
								files.getProperty( value ) + "'" );
		}
		
		try {
			FileInputStream  is = new FileInputStream(new File("import_file_done.html"));
			
			return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, is);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	private Response unsupportedRequest(String uri, String method,
			Properties header, Properties parms, Properties files) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response requestInputDataDone(String uri, String method,
			Properties header, Properties parms, Properties files) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response requestInputData(String uri, String method,
			Properties header, Properties parms, Properties files) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response requestImportFile(String uri, String method,
			Properties header, Properties parms, Properties files) {
		try {
			FileInputStream  is = new FileInputStream(new File("import_file.html"));
			
			return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, is);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Response requestRoot(String uri, String method, Properties header,
			Properties parms, Properties files) {
		try {
			FileInputStream  is = new FileInputStream(new File("index.html"));
			
			return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, is);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Response importfile(String uri, String method, Properties header, Properties parms, Properties files) {

		String ifile = parms.getProperty("import");
		if(ifile == null || ifile.isEmpty()) {
			return new Response(HTTP_BADREQUEST, MIME_PLAINTEXT, HTTP_BADREQUEST);//
		}
		boolean oflag = !(parms.getProperty("overwrite") == null || parms.getProperty("overwrite").isEmpty());
		
		String lfile = files.getProperty("import");
		if(lfile == null || lfile.isEmpty()) {
			return new Response(HTTP_BADREQUEST, MIME_PLAINTEXT, HTTP_BADREQUEST);//
		}
		
		

		return new Response(HTTP_OK, MIME_PLAINTEXT, HTTP_OK + " - " + lfile);
		
		//return super.serve(uri, method, header, parms, files);
	}

	public static void main( String[] args )
	{
		try
		{
			new Server();
		}
		catch( IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		System.out.println( "Listening on port 8080. Hit Enter to stop.\n" );
		try { System.in.read(); } catch( Throwable t ) {};
	}
	
}
