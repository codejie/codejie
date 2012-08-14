package jie.java.android.lingoshook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources;

import jie.java.android.lingoshook.NanoHTTPD.Response;

public class HttpdServer extends NanoHTTPD {

	Context context = null;
	
	public HttpdServer(Context context, int port) throws IOException {
		super(port, new File("."));
		this.context = context;
	}

	@Override
	public Response serve(String uri, String method, Properties header,	Properties parms, Properties files) {
		
//		String msg = "<html><body><h1>Hello</h1><p><form action='' method='POST'><p><p>Your name: <input type='text' name='username'></p></form></body></html>";
//		
//		Response resp = new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, msg);//"<HTML><BODY>HELLO World!</BODY></HTML>");
//		
//		
//		return resp;
		//int resourceId = context.getResources().getIdentifier("jie.java.android.lingoshook:raw/index.html", null, null);
//		File f = null;
//			f = new File("android_asset");
		try {
			InputStream stream = context.getAssets().open("index.html");
			return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		//File f = new File(context.getResources().openRawResource(resourceId));
		
//		return serveFile( uri, header, f, true );
	}

}
