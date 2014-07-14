package com.lazexe.mystatfit.soap;

import android.content.Context;

public interface SoapExecutableInterface {
	public void execute();
	public Context getContext();
	public void startShowProgress();
	public void stopShowProgress();
}
