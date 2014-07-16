package com.lazexe.mystatfit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

	public static boolean isOnline(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			if (networkInfo.isAvailable() && networkInfo.isConnected()) {
				return true;
			}
		}
		return false;
	}
}
