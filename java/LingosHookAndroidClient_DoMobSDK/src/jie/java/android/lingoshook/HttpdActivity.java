package jie.java.android.lingoshook;

import java.io.IOException;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

public class HttpdActivity extends Activity {

	private HttpdServer server = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.httpd);
		
		TextView tv = (TextView) this.findViewById(R.id.textView1);
		tv.setText(getLocalAddress());
		
		startHttdp();
	}

	@Override
	protected void onDestroy() {
		if(server != null) {
			server.stop();
		}
		super.onDestroy();
	}

	private String getLocalAddress() {
	   WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
	   if(wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
		   return "Wifi is NOT available.";
	   }
	   
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	   int ip = wifiInfo.getIpAddress();
	   if(ip == -1) {
		   return "Get IP address failed.";
	   }

	   return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
	}
	
	private void initHandler() {
		
	}
	
	private void startHttdp() {
		try {
			server = new HttpdServer(this, 8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
}
