package com.lazexe.mystatfit;

import com.lazexe.mystatfit.utils.PreferencesUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class PrefsActivity extends PreferenceActivity implements OnPreferenceClickListener {

	private static final String TAG = PrefsActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference logoutPreference = findPreference(getString(R.string.logout));
		StringBuilder sBuilder = new StringBuilder(getString(R.string.logout));
		sBuilder.append(" (");
		sBuilder.append(PreferencesUtils.getUserLogin(this));
		sBuilder.append(")");
		logoutPreference.setTitle(sBuilder.toString());
		logoutPreference.setOnPreferenceClickListener(this);
		
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		
		String key = preference.getKey();
		
		if (key.equals(getString(R.string.logout))) {
			SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
			prefs.edit().clear().commit();
			setResult(RESULT_OK);
			this.finish();
			Intent loginActivityIntent = new Intent(this, LoginActivity.class);
			startActivity(loginActivityIntent);
		}
		
		return false;
	}
	
	
	
	

}
