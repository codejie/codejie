package jie.java.android.lingoshook;

import java.io.IOException;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class HttpdActivity extends Activity {

	private final static int MSG_WORD		=	0x0F01;
	private final static int MSG_DONE		=	0x0F00;
	private final static int MSG_EXCEPTION	=	0x0F02;
	private final static int MSG_FAILED		=	0x0F03;	
	
	public final static int MSG_IMPORT_FILE	=	0;
	
	private HttpdServer server = null;
	private Handler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.httpd);
		
		initHandler();
		
		TextView tv = (TextView) this.findViewById(R.id.textView1);
		
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
		
		tv.setText(String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff)));
		
		return 0;		
	}
	
	
//	private String getLocalAddress() {
//	   WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//	   if(wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
//		   return "Wifi is NOT available.";
//	   }
//	   
//	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//	   int ip = wifiInfo.getIpAddress();
//	   if(ip == -1) {
//		   return "Get IP address failed.";
//	   }
//
//	   return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
//	}
//	
	private void initHandler() {
		
		final TextView tv = (TextView)this.findViewById(R.id.textView1);
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch(msg.what) {
					case MSG_IMPORT_FILE: {
						importFile(msg);
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
			server = new HttpdServer(this, handler, 8080);
		} catch (IOException e) {
			return -1;
		}
		return 0;
		
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					server = new HttpdServer(8080);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}.start();			
	}
	
	private void importFile(Message msg) {
		String file = msg.getData().getString("local");
		
		
	}
	
	private int importDB(String file, boolean overwrite) {
		try {
			new Thread() {
				@Override
				public void run() {
					if(DBAccess.importData(handler, MSG_WORD, file, (c.isChecked() ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) == 0) {
						handler.sendMessage(Message.obtain(handler, MSG_DONE));
					}
					else {
						handler.sendMessage(Message.obtain(handler, MSG_FAILED));
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
