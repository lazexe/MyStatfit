package com.lazexe.mystatfit.utils;


public class MathUtils {

	public static final String TAG = MathUtils.class.getName();
	
	public static float round(float value, int digitsAfterComa) {
		int multipleValue = (int) Math.pow(10, digitsAfterComa);
		int semiResult = (int) (value * multipleValue);
		float result = (float)semiResult / (float)multipleValue;
		return result;
	}
}
