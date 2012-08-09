package jie.java.android.lingoshook;

import java.io.File;
import java.io.IOException;

public class HttpdServer extends NanoHTTPD {

	public HttpdServer(int port) throws IOException {
		super(port, new File("."));
	}

}
