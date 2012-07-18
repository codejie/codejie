package jie.java.android.lingoshook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class ImportDBActivity extends Activity implements OnClickListener {

	private Handler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.importdb);
		final TextView v = (TextView)this.findViewById(R.id.textView3);
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x0F01) {
					String str = msg.getData().getString("word");
					v.setText(str);
					v.invalidate();
				}
				else if(msg.what == 0)
				{
					ImportDBActivity.this.finish();
				}
				super.handleMessage(msg);
			}
			
		};
		
		this.findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view == this.findViewById(R.id.button1)) {
			this.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
			new Thread() {

				@Override
				public void run() {
					//handler.post(ImportDBActivity.this);
					DBAccess.importData(handler, "/mnt/sdcard/s46.db3", 0);
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}
				
			}.start();
		}
	}

}
