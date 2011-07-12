package jie.java.android.savingkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SavingKeeper extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent intent = new Intent();
        intent.setClassName("jie.java.android.savingkeeper", "MsgBoxActivity");
        startActivity(intent);
        
    }
}