package jie.java.android.savingkeeper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

public class FileDialog {
	
	public int DIALOG_EXPORT		=	1;
	public int DIALOG_IMPORT		=	2;

	Dialog create(Context context, int type) {
		Dialog dlg = null;
		
		Builder build = new AlertDialog.Builder(context);
		build.setIcon(android.R.drawable.ic_dialog_info);
		String dir = Environment.getExternalStorageDirectory().getPath();
		String file = "Export.xml";
//        LayoutInflater factory = LayoutInflater.from(this);
//        final View textEntryView = factory.inflate(R.layout.addbank, null);
//		build.setView()
		
		return dlg;
	}
}
