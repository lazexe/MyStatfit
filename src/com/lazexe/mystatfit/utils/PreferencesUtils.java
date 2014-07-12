package com.lazexe.mystatfit.utils;

import com.lazexe.mystatfit.R;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

	public static String getUserLogin(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getString("Login", null);
	}
	
	public static String getUserPassword(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getString("Password", null);
	}
}
