package com.lazexe.mystatfit.utils;

import com.lazexe.mystatfit.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

	public static String getUserLogin(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getString(Constants.PREFS_LOGIN_KEY, null);
	}
	
	public static String getUserPassword(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getString(Constants.PREFS_PASSWORD_KEY, null);
	}
	
	public static String getStepLength(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(Constants.PREFS_STEP_LENGTH_KEY, "-1");
	}
	
	public static String getUserWeight(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(Constants.PREFS_WEIGHT_KEY, "-1");
	}
	
	public static String getUserHeight(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(Constants.PREFS_HEIGHT_KEY, "-1");
	}
	
	public static int getUserAge(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getInt(Constants.PREFS_AGE_KEY, -1);
	}
	
	public static int getUserGender(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		String temp = prefs.getString(Constants.PREFS_GENDER_KEY, "empty");
		String[] genders = context.getResources().getStringArray(R.array.gender_type);
		if (temp.equalsIgnoreCase(genders[0])) {
			return 1;
		} else {
			return 0;
		}
	}
}
