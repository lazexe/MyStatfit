package com.lazexe.mystatfit.soap;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import com.lazexe.mystatfit.MainActivity;
import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.utils.Constants;
import com.lazexe.mystatfit.utils.SoapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class UserFeatureEditor extends AbstractCommandObject implements SoapExecutableInterface {

	
	private static final String USER_PROPERTY_KEY = "user";
	private static final String PASSWD_PROPERTY_KEY = "password";
	private static String DATA_PROPERTY_KEY = "test";
	
	private Activity activity;
	private String login;
	private String password;
	private String data;
	
	public UserFeatureEditor(SoapParams params, Activity activity, String login, String password, String data) {
		super(params);
		this.activity = activity;
		this.login = login;
		this.password = password;
		this.data = data;
		if (params.getMethodName().equals(activity.getString(R.string.weight))) {
			DATA_PROPERTY_KEY = "weight";
		} else {
			DATA_PROPERTY_KEY = "rost";
		}
	}

	@Override
	public void execute() {
		this.getRequest().addProperty(USER_PROPERTY_KEY, login);
		this.getRequest().addProperty(PASSWD_PROPERTY_KEY, password);
		this.getRequest().addProperty(DATA_PROPERTY_KEY, data);
		try {
			this.getAndroidHttpTransport().call(params.getSoapAction(),
					this.getEnvelope());
			if (this.getEnvelope().bodyIn instanceof SoapFault) {
				final SoapFault soapFault = (SoapFault) this.getEnvelope().bodyIn;
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(activity, soapFault.faultstring,
								Toast.LENGTH_LONG).show();
					}
				});
			} else {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(activity, activity.getString(R.string.success), Toast.LENGTH_LONG).show();
					}
				});
				SoapObject resultsRequestSOAP = (SoapObject) this.getEnvelope().bodyIn;
				SoapUtils.ScanSoapObject(resultsRequestSOAP);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			// TODO
		}
	}

	@Override
	public Context getContext() {
		return activity.getApplicationContext();
	}

	@Override
	public void startShowProgress() {
		// do nothing
	}

	@Override
	public void stopShowProgress() {
		// do nothing
	}

}
