package jie.java.android.lingoshook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ImportDBActivity extends Activity implements OnClickListener {

	private final int DIALOG_USING = 0;
	
	private final int MSG_WORD		=	0x0F01;
	private final int MSG_DONE		=	0x0F00;
	private final int MSG_EXCEPTION	=	0x0F02;
	private final int MSG_FAILED		=	0x0F03;
	
	private Handler handler = null;
	
	private boolean isDone = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.importdb);
		final TextView v = (TextView)this.findViewById(R.id.textView3);
		final Button btn = (Button)this.findViewById(R.id.button1);
		
		((EditText) this.findViewById(R.id.editText1)).setText(Environment.getExternalStorageDirectory().getPath());
        ((EditText) this.findViewById(R.id.editText2)).setText("LH_Export.db3");
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
							
				switch(msg.what) {
					case MSG_WORD: {
						v.setText((String)msg.obj);
						break;
					}
					case MSG_DONE: {
						
						ImportDBActivity.this.findViewById(R.id.linearLayout1).setVisibility(View.GONE);
						
						isDone = true;
						btn.setText(R.string.title_done);
						
//						Score.init();

						break;
					}
					case MSG_FAILED: {
						ImportDBActivity.this.findViewById(R.id.linearLayout1).setVisibility(View.GONE);

						Toast.makeText(ImportDBActivity.this, "Import data failed.", Toast.LENGTH_LONG).show();
						break;
					}
					case MSG_EXCEPTION: {
						ImportDBActivity.this.findViewById(R.id.linearLayout1).setVisibility(View.GONE);

						Toast.makeText(ImportDBActivity.this, "Import data failed - exception", Toast.LENGTH_LONG).show();
						break;
					}
					default: {
						break;
					}
				}
				
				super.handleMessage(msg);
			}
			
		};
		
		this.findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view == this.findViewById(R.id.button1)) {					
			if(!isDone) {
				final CheckBox c = (CheckBox)this.findViewById(R.id.checkBox1);
				if(c.isChecked() && DBAccess.isUsing()) {
					this.showDialog(DIALOG_USING);
				}
				else {
					importDB();
				}
			}
			else {
				ImportDBActivity.this.finish();
			}
		}
	}
	
	private int importDB() {
		final String file = ((EditText) this.findViewById(R.id.editText1)).getText().toString() + "/" + ((EditText) this.findViewById(R.id.editText2)).getText().toString();
		final CheckBox c = (CheckBox)this.findViewById(R.id.checkBox1);
		
		this.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
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
