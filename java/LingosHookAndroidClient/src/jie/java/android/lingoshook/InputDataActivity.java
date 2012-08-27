package jie.java.android.lingoshook;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputDataActivity extends ImportBaseActivity implements OnClickListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.inputdata);
		
		((Button) this.findViewById(R.id.button1)).setOnClickListener(this);	
	}

	@Override
	public void onClick(View view) {
		if(view == this.findViewById(R.id.button1)) {
			if(inputData() == 0) {
				Toast.makeText(this, this.getString(R.string.str_httpd_inputdone), Toast.LENGTH_SHORT).show();
				this.finish();
			}
			else {
				Toast.makeText(this, this.getString(R.string.str_httpd_inputfail), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private int inputData() {
		
		Message msg = Message.obtain(this.getHandler(), HttpdActivity.MSG_INPUT_DATA);
		
		String str = ((EditText) this.findViewById(R.id.editText1)).getText().toString();
		if(str == null || str.equals("")) {
			msg.getData().putString("dict", "Default Dictionary");	
		}
		else {
			msg.getData().putString("dict", str);
		}	
	
		str = ((EditText) this.findViewById(R.id.editText2)).getText().toString();
		if(str == null || str.equals("")) {
			return -1;
		}
		msg.getData().putString("word", str);
		
		str = ((EditText) this.findViewById(R.id.editText3)).getText().toString();
		if(str == null || str.equals("")) {
			msg.getData().putString("symbol", "");	
		}
		else {
			msg.getData().putString("symbol", str);
		}

		str = ((EditText) this.findViewById(R.id.editText4)).getText().toString();
		if(str == null || str.equals("")) {
			msg.getData().putString("category1", "");	
		}
		else {
			msg.getData().putString("category1", str);
		}
		
		str = ((EditText) this.findViewById(R.id.editText5)).getText().toString();
		if(str == null || str.equals("")) {
			return -1;
		}
		msg.getData().putString("meaning1", str);
		
		this.getHandler().sendMessage(msg);
		
		return 0;
	}

}
