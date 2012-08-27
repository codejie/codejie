package jie.java.android.lingoshook;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ImportDBActivity extends ImportBaseActivity implements OnClickListener {

	private final int DIALOG_USING = 0;
	
	private boolean isDone = false;
	
	private TextView v = null;
	private Button btn = null;	
	private LinearLayout layout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.importdb);

		v = (TextView)this.findViewById(R.id.textView3);
		btn = (Button)this.findViewById(R.id.button1);
		layout = (LinearLayout) ImportDBActivity.this.findViewById(R.id.linearLayout1);
		
		((EditText) this.findViewById(R.id.editText1)).setText(Environment.getExternalStorageDirectory().getPath());
        ((EditText) this.findViewById(R.id.editText2)).setText("LH_Export.db3");
			
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
		
		layout.setVisibility(View.VISIBLE);
		
		Message msg = Message.obtain(this.getHandler(), HttpdActivity.MSG_IMPORT_FILE);
		msg.getData().putString("file", file);
		msg.getData().putString("local", file);
		msg.getData().putBoolean("overwrite", c.isChecked());

		this.getHandler().sendMessage(msg);
		
		return 0;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
		switch(id) {
		case DIALOG_USING: {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.str_wordindb);
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					importDB();
					dialog.dismiss();
				}
			});
			
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			dlg = builder.create();
		}
		break;
		default:;
		}
		return dlg;
	}

	@Override
	protected void onMsg_ImportFile(String file, String local, boolean overwrite) {
		super.onMsg_ImportFile(file, local, overwrite);
	}

	@Override
	protected void onMsg_Word(String word) {
		v.setText(word);
	}

	@Override
	protected void onMsg_ImportFileDone(String file, String local, boolean overwrite) {
		layout.setVisibility(View.GONE);		
		isDone = true;
		btn.setText(R.string.title_done);
	}

	@Override
	protected void onMsg_ImportFileFailed(boolean isDBFile, String file, String local) {
		layout.setVisibility(View.GONE);
		Toast.makeText(ImportDBActivity.this, "Import data failed.", Toast.LENGTH_LONG).show();	
	}

	@Override
	protected void onMsg_Exception() {
		layout.setVisibility(View.GONE);
		Toast.makeText(ImportDBActivity.this, "Import data failed - exception", Toast.LENGTH_LONG).show();
	}

}
