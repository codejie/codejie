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

	private int MSG_WORD	=	0x0F01;
	private int MSG_DONE	=	0x0F00;
	
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
				if(msg.what == MSG_WORD) {
					String str = (String)msg.obj;//msg.getData().getString("word");
					v.setText(str);
//					v.invalidate();
				}
				else if(msg.what == MSG_DONE)
				{
					isDone = true;
					btn.setText(R.string.title_done);
					//ImportDBActivity.this.finish();
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
	    		final String file = ((EditText) this.findViewById(R.id.editText1)).getText().toString() + "/" + ((EditText) this.findViewById(R.id.editText2)).toString();
	    		final CheckBox c = (CheckBox)this.findViewById(R.id.checkBox1);
				
				this.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
				new Thread() {
					@Override
					public void run() {
						//handler.post(ImportDBActivity.this);
						if(DBAccess.importData(handler, MSG_WORD, file, (c.isChecked() ? DBAccess.IMPORTTYPE_OVERWRITE : DBAccess.IMPORTTYPE_APPEND)) == 0) {
							Score.init();
							handler.sendMessage(Message.obtain(handler, MSG_DONE));
						}
						else {
							Toast.makeText(ImportDBActivity.this, "Import data failed.", Toast.LENGTH_LONG).show();
						}
					}
					
				}.start();
			}
			else {
				this.findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
				ImportDBActivity.this.finish();
			}
		}
	}

}
