package com.lazexe.mystatfit;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.R.string;
import com.lazexe.mystatfit.R.xml;
import com.lazexe.mystatfit.fragments.PrefFragment;
import com.lazexe.mystatfit.utils.PreferencesUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

public class PrefsActivity extends PreferenceActivity {

	private static final String TAG = PrefsActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment()).commit();
	}
}
