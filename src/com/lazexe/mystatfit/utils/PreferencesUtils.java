package com.lazexe.mystatfit.utils;

import com.lazexe.mystatfit.R;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

	public static String getUserLogin(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
		return prefs.getString(context.getString(R.string.login), null);
	}
}
