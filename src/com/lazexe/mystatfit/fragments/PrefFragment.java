package com.lazexe.mystatfit.fragments;

import com.lazexe.mystatfit.EditUserInformationActivity;
import com.lazexe.mystatfit.LoginActivity;
import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.utils.PreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;

public class PrefFragment extends PreferenceFragment implements
		OnPreferenceClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Preference logoutPreference = findPreference(getString(R.string.logout));
		StringBuilder sBuilder = new StringBuilder(getString(R.string.logout));
		sBuilder.append(" (");
		sBuilder.append(PreferencesUtils.getUserLogin(getActivity()));
		sBuilder.append(")");
		logoutPreference.setTitle(sBuilder.toString());
		logoutPreference.setOnPreferenceClickListener(this);

		sBuilder = new StringBuilder(getString(R.string.edit));
		sBuilder.append(" (");
		sBuilder.append(PreferencesUtils.getUserLogin(getActivity()));
		sBuilder.append(")");
		Preference editPreference = findPreference(getString(R.string.edit));
		editPreference.setTitle(sBuilder.toString());
		editPreference.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		
		String key = preference.getKey();

		if (key.equals(getString(R.string.logout))) {
			SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
			prefs.edit().clear().commit();
			getActivity().setResult(Activity.RESULT_OK);
			getActivity().finish();
			Intent loginActivityIntent = new Intent(getActivity(), LoginActivity.class);
			startActivity(loginActivityIntent);
		}

		if (key.equals(getString(R.string.edit))) {
			Intent editActivityIntent = new Intent(getActivity(),
					EditUserInformationActivity.class);
			startActivity(editActivityIntent);
		}
		
		return false;
	}

}