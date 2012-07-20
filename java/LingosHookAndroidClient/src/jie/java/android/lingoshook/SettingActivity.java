package jie.java.android.lingoshook;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.setting);
		
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_word_numloadnew)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_word_numloadold)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_word_loadmistake)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_word_loadresult)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_word_loadspeaker)).setOnPreferenceChangeListener(this);
		
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_finger_refresh)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_finger_interval)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_finger_pencolor)).setOnPreferenceChangeListener(this);
		this.getPreferenceScreen().findPreference(this.getString(R.string.set_key_finger_penwidth)).setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object value) {
		if(preference.getKey().equals(this.getString(R.string.set_key_word_numloadnew))) {
			Setting.numLoadNewWord = Integer.parseInt((String)value);
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_word_numloadold))) {
			Setting.numLoadOldWord = Integer.parseInt((String)value);
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_word_loadmistake))) {
			Setting.loadMistakeWord = (Boolean)value;
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_word_loadresult))) {
			Setting.loadResultDisplay = (Boolean)value;
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_word_loadspeaker))) {
			Setting.loadSpeaker = (Boolean)value;
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_finger_refresh))) {
			Setting.refeshFingerPanel = (Boolean)value;
		}		
		else if(preference.getKey().equals(this.getString(R.string.set_key_finger_interval))) {
			Setting.intervalFingerPanel = Integer.parseInt((String)value);
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_finger_pencolor))) {
			Setting.colorFingerPanelPen = Integer.parseInt((String)value, 16);
		}
		else if(preference.getKey().equals(this.getString(R.string.set_key_finger_penwidth))) {
			Setting.widthFingerPanelPen = Integer.parseInt((String)value);
		}
		else {
			return false;
		}
		
		preference.getEditor().commit();

		return true;
	}

}
