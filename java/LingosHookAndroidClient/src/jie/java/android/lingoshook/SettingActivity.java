package jie.java.android.lingoshook;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

public class SettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	private CheckBoxPreference _checkbox = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.setting);
		
		//_checkbox = (CheckBoxPreference)this.getPreferenceScreen().findPreference("r_checkbox");
		//_checkbox.setOnPreferenceChangeListener(this);
		//Setting.isResultDisplay = _checkbox.isChecked();
		//this.getPreferenceScreen().setOnPreferenceChangeListener(this);
		//this.getPreferenceScreen().setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		//Setting.isResultDisplay = _checkbox.isChecked();
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		// TODO Auto-generated method stub
		//Setting.isResultDisplay = _checkbox.isChecked();
		return true;
	}

}
