package com.lazexe.mystatfit.fragments;

import com.lazexe.mystatfit.EditUserInformationActivity;
import com.lazexe.mystatfit.LoginActivity;
import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.soap.SoapEngine;
import com.lazexe.mystatfit.soap.SoapParams;
import com.lazexe.mystatfit.soap.UserFeatureEditor;
import com.lazexe.mystatfit.utils.PreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

public class PrefFragment extends PreferenceFragment implements
		OnPreferenceClickListener, OnPreferenceChangeListener {
	
	private static final String TAG = PrefFragment.class.getName();

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
		
		Preference weightPreference = findPreference(getString(R.string.weight));
		weightPreference.setOnPreferenceChangeListener(this);
		Preference heightPreference = findPreference(getString(R.string.height));
		heightPreference.setOnPreferenceChangeListener(this);
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

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		String data = (String) newValue;
		String login = PreferencesUtils.getUserLogin(getActivity());
		String password = PreferencesUtils.getUserPassword(getActivity());
		SoapParams params;
		String url = "http://mystatfit.com/aut/";
		String namespace = "http://mystatfit.com/";
		String soapAction = null;
		String methodName = null;
		if (key.equals(getString(R.string.height))) {
			soapAction = "http://mystatfit.com/Rost";
			methodName = "Rost";
		} else if (key.equals(getString(R.string.weight))) {
			soapAction = "http://mystatfit.com/Weight";
			methodName = "Weight";
		} else if (key.equals(getString(R.string.step_length))) {
			// TODO if need to keep this on remote server
			return false;
		} else {
			return false;
		}
		params = new SoapParams(soapAction, methodName, namespace, url);
		SoapEngine engine = SoapEngine.getInstance();
		engine.runCommand(new UserFeatureEditor(params, getActivity(), login, password, data));
		return true;
	}

}
