package jie.java.android.lingoshook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import jie.java.android.lingoshook.NanoHTTPD.Response;

public class HttpdServer extends NanoHTTPD {

	Context context = null;
	Handler handler = null;
	
	public HttpdServer(Context context, Handler handler, int port) throws IOException {
		super(port, null);
		this.context = context;
	}

	@Override
	public Response serve(String uri, String method, Properties header,	Properties parms, Properties files) {

		displayDebugInfo(uri, method, header, parms, files);	
		
		try {
			if(method.equals("GET")) {
				if(uri.equals("/") || uri.equals("/index.html")) {
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
		} catch (IOException ex) {
			return badRequest();
		}		
		return unsupportedRequest(uri, method, header, parms, files);
		
//		String msg = "<html><body><h1>Hello</h1><p><form action='' method='POST'><p><p>Your name: <input type='text' name='username'></p></form></body></html>";
//		
//		Response resp = new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, msg);//"<HTML><BODY>HELLO World!</BODY></HTML>");
//		
//		
//		return resp;
		//int resourceId = context.getResources().getIdentifier("jie.java.android.lingoshook:raw/index.html", null, null);
//		File f = null;
//			f = new File("android_asset");
//		try {
//			InputStream stream = context.getAssets().open("index.html");
//			return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, stream);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		
		//File f = new File(context.getResources().openRawResource(resourceId));
		
//		return serveFile( uri, header, f, true );
	}

	private void displayDebugInfo(String uri, String method, Properties header,	Properties parms, Properties files) {
		myOut.println( method + " '" + uri + "' " );
		
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
	}

	private Response unsupportedRequest(String uri, String method, Properties header, Properties parms, Properties files) {
		
		return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT,	"Error 404, file not found.");
		
	}

	private Response requestInputDataDone(String uri, String method, Properties header, Properties parms, Properties files) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response requestInputData(String uri, String method, Properties header, Properties parms, Properties files) {
		// TODO Auto-generated method stub
		return null;
	}

	private Response requestImportFileDone(String uri, String method, Properties header, Properties parms, Properties files) throws IOException {

		String ifile = parms.getProperty("import");
		if(ifile == null || ifile.equals("")) {
			return new Response(HTTP_BADREQUEST, MIME_PLAINTEXT, HTTP_BADREQUEST);//
		}
		boolean oflag = !(parms.getProperty("overwrite") == null || parms.getProperty("overwrite").equals(""));
		
		String lfile = files.getProperty("import");
		if(lfile == null || lfile.equals("")) {
			return new Response(HTTP_BADREQUEST, MIME_PLAINTEXT, HTTP_BADREQUEST);//
		}
		
		Message msg = Message.obtain(handler, HttpdActivity.MSG_IMPORT_FILE);
		msg.getData().putString("file", ifile);
		msg.getData().putString("local", lfile);
		msg.getData().putBoolean("overwrite", oflag);		
		handler.sendMessage(Message.obtain(handler, HttpdActivity.MSG_IMPORT_FILE));
		
		InputStream stream = context.getAssets().open("import_file_done.html");
		return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, stream);
	}

	private Response requestImportFile(String uri, String method, Properties header, Properties parms, Properties files) throws IOException {
		InputStream stream = context.getAssets().open("import_file.html");
		return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, stream);
	}

	private Response requestRoot(String uri, String method, Properties header, Properties parms, Properties files) throws IOException {
		InputStream stream = context.getAssets().open("index.html");
		return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, stream);
	}

	private Response badRequest() {
		return new Response(HTTP_INTERNALERROR, MIME_PLAINTEXT,	"500 Internal Server Error");	
	}
}
