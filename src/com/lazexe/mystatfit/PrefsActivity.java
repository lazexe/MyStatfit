package com.lazexe.mystatfit;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.lazexe.mystatfit.fragments.PrefFragment;

public class PrefsActivity extends PreferenceActivity {

	private static final String TAG = PrefsActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment()).commit();
	}
}
