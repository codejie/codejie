package jie.java.android.lingoshook;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import jie.java.android.lingoshook.NanoHTTPD.Response;

public class HttpdServer extends NanoHTTPD {

	public HttpdServer(int port) throws IOException {
		super(port, new File("."));
	}

	@Override
	public Response serve(String uri, String method, Properties header,	Properties parms, Properties files) {
		
		String msg = "<html><body><h1>Hello</h1><p><form action='' method='POST'><p><p>Your name: <input type='text' name='username'></p></form></body></html>";
		
		Response resp = new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, msg);//"<HTML><BODY>HELLO World!</BODY></HTML>");
		
		
		return resp;
	}

}
