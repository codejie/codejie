package jie.java.android.lingoshook;

import java.io.IOException;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HttpdActivity extends Activity {

	private final static int MSG_WORD		=	0x0F01;
	private final static int MSG_DONE		=	0x0F00;
	private final static int MSG_EXCEPTION	=	0x0F02;
	private final static int MSG_DB_FAILED	=	0x0F03;	
	private final static int MSG_XML_FAILED	=	0x0F04;
	
	public final static int MSG_IMPORT_FILE	=	0;
	
	private final int HTTPD_PORT			=	8102;
	
	private HttpdServer server = null;
	private Handler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.httpd);
		
		initHandler();
		
		TextView tv = (TextView) this.findViewById(R.id.textView2);
		
		if(getLocalAddress(tv) == 0) {
			if(startHttdp() != 0) {
				Toast.makeText(this, "Httpd start failed.", Toast.LENGTH_SHORT).show();
			}			
		}
	}

	@Override
	protected void onDestroy() {
		if(server != null) {
			server.stop();
		}
		super.onDestroy();
	}

	private int getLocalAddress(TextView tv) {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		if(wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
			Toast.makeText(this, "Wifi is NOT available.", Toast.LENGTH_SHORT).show();
			return -1;
		}
		   
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		if(ip == -1) {
			Toast.makeText(this, "Get IP address failed.", Toast.LENGTH_SHORT).show();
			return -1;
		}
		
		tv.setText(String.format("http://%d.%d.%d.%d:%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff), HTTPD_PORT));
		
		return 0;		
	}
	
	private void initHandler() {
		
		//final LinearLayout ll = (LinearLayout) this.findViewById(R.id.linearLayout2);
		final TextView comment = (TextView)this.findViewById(R.id.textView3);
		final TextView word = (TextView)this.findViewById(R.id.textView4);
		
		handler = new Handler() {
			private String src = "";
			private String file = "";
			private boolean overwrite = false;			
			@Override
			public void handleMessage(Message msg) {

				switch(msg.what) {
					case MSG_IMPORT_FILE: {
						src = msg.getData().getString("file");
						file = msg.getData().getString("local");
						overwrite = msg.getData().getBoolean("overwrite");						
						
						comment.setText(R.string.str_httpd_importing);
//						ll.setVisibility(View.VISIBLE);
						
						importDB(file, overwrite);
						break;
					}
					case MSG_WORD: {
						word.setText((String)msg.obj);
						break;
					}
					case MSG_DONE: {
						comment.setText(String.format(getString(R.string.str_httpd_importdone), src));
						word.setText("DONE");
						break;
					}
					case MSG_DB_FAILED: {			
						importXml(file, overwrite);
						break;
					}
					case MSG_XML_FAILED: {
//						ll.setVisibility(View.GONE);
						Toast.makeText(HttpdActivity.this, "Import data failed.", Toast.LENGTH_LONG).show();
						break;
					}
					case MSG_EXCEPTION: {
//						ll.setVisibility(View.GONE);
						Toast.makeText(HttpdActivity.this, "Import data failed - exception", Toast.LENGTH_LONG).show();
						break;
					}					
					default: {
						break;
					}
				}
				
				super.handleMessage(msg);
			}
			
		};
	}

	private int startHttdp() {
		try {
			server = new HttpdServer(this, handler, HTTPD_PORT);
		} catch (IOException e) {
			return -1;
		}
		return 0;	
	}
	
	private int importDB(final String file, final boolean overwrite) {
		try {
			new Thread() {
				@Override
				public void run() {
					if(DBAccess.importData(handler, MSG_WORD, file, (overwrite ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) == 0) {
						handler.sendMessage(Message.obtain(handler, MSG_DONE));
					}
					else {
						handler.sendMessage(Message.obtain(handler, MSG_DB_FAILED));
					}
				}
				
			}.start();
		}
		catch (Exception e) {
			handler.sendMessage(Message.obtain(handler, MSG_EXCEPTION));
		}
		return 0;
	}
	
	private int importXml(final String file, final boolean overwrite) {
		try {
			new Thread() {
				@Override
				public void run() {
					if(DBAccess.importXml(handler, MSG_WORD, file, (overwrite ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) == 0) {
						handler.sendMessage(Message.obtain(handler, MSG_DONE));
					}
					else {
						handler.sendMessage(Message.obtain(handler, MSG_XML_FAILED));
					}
				}
				
			}.start();
		}
		catch (Exception e) {
			handler.sendMessage(Message.obtain(handler, MSG_EXCEPTION));
		}
		return 0;
	}	
}
