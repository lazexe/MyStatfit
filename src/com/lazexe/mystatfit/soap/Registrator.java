package com.lazexe.mystatfit.soap;

import org.ksoap2.SoapFault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.lazexe.mystatfit.EditUserInformationActivity;
import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.RegistrationActivity;
import com.lazexe.mystatfit.utils.Constants;

public class Registrator extends AbstractCommandObject implements
		SoapExecutableInterface {

	private final String USER_PROPERTY_KEY = "user";
	private final String PASSWD_PROPERTY_KEY = "pass";
	private final String CONF_PASSWD_PROPERTY_KEY = "pass2";

	public static final String SOAP_ACTION = "http://mystatfit.com/Registration";
	public static final String METHOD_NAME = "Registration";
	public static final String NAMESPACE = "http://mystatfit.com/";
	public static final String URL = "http://mystatfit.com/aut/";

	private String login;
	private String password;
	private String confirmedPassword;

	private RegistrationActivity registrationActivity;

	public Registrator(SoapParams params,
			RegistrationActivity registrationActivity, String login,
			String password, String confirmedPassword) {
		super(params);
		this.registrationActivity = registrationActivity;
		this.login = login;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	@Override
	public void execute() {
		this.getRequest().addProperty(USER_PROPERTY_KEY, login);
		this.getRequest().addProperty(PASSWD_PROPERTY_KEY, password);
		this.getRequest().addProperty(CONF_PASSWD_PROPERTY_KEY,
				confirmedPassword);
		try {
			this.getAndroidHttpTransport()
					.call(SOAP_ACTION, this.getEnvelope());
			if (this.getEnvelope().bodyIn instanceof SoapFault) {
				final SoapFault soapFault = (SoapFault) this.getEnvelope().bodyIn;
				registrationActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(registrationActivity,
								soapFault.faultstring, Toast.LENGTH_LONG)
								.show();
						registrationActivity.registrationButton
								.setEnabled(true);
					}
				});
			} else {
				registrationActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						registrationActivity.registrationButton
								.setEnabled(true);
						registrationActivity.finish();
						SharedPreferences prefs = registrationActivity
								.getSharedPreferences(
										Constants.PREFS_USER_DATA_KEY,
										Context.MODE_PRIVATE);
						Editor editor = prefs.edit();
						editor.putString(
								registrationActivity.getString(R.string.login),
								login);
						editor.putString(registrationActivity
								.getString(R.string.password), password);
						editor.commit();
						Toast.makeText(registrationActivity, "Register ok",
								Toast.LENGTH_LONG).show();
						registrationActivity.finish();
						Intent editUserActivityIntent = new Intent(
								registrationActivity,
								EditUserInformationActivity.class);
						registrationActivity
								.startActivity(editUserActivityIntent);
					}
				});
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public Context getContext() {
		return registrationActivity.getApplicationContext();
	}

	@Override
	public void startShowProgress() {
		registrationActivity.showProgress();
	}

	@Override
	public void stopShowProgress() {
		registrationActivity.hideProgress();
	}

}
