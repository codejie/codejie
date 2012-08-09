package jie.java.android.lingoshook;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class HttpdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.httpd);
		
		TextView tv = (TextView) this.findViewById(R.id.textView1);
		tv.setText(getLocalAddress());
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
	
}
