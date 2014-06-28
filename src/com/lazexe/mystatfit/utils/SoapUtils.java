package com.lazexe.mystatfit.utils;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import android.util.Log;

public class SoapUtils {

	private static final String TAG = SoapUtils.class.getName();
	
	public static void ScanSoapObject(SoapObject result) {
		for (int i = 0; i < result.getPropertyCount(); i++) {
			if (result.getProperty(i) instanceof SoapObject) {
				ScanSoapObject((SoapObject) result.getProperty(i));
			} else {
				PropertyInfo pi = new PropertyInfo();
				result.getPropertyInfo(i, pi);
				String name = pi.getName();
				String value = String.valueOf(pi.getValue());
				Log.d(TAG, name + ": " + value);
			}
		}
	}
}
