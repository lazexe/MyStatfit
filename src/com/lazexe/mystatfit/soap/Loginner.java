package com.lazexe.mystatfit.soap;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.lazexe.mystatfit.LoginActivity;
import com.lazexe.mystatfit.MainActivity;
import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.utils.Constants;
import com.lazexe.mystatfit.utils.SoapUtils;

public class Loginner extends AbstractCommandObject implements SoapExecutableInterface {

	private static final String TAG = Loginner.class.getName();

	private final String USER_PROPERTY_KEY = "user";
	private final String PASSWD_PROPERTY_KEY = "password";

	public static final String SOAP_ACTION = "http://mystatfit.com/CheckAuthorization";
	public static final String METHOD_NAME = "CheckAuthorization";
	public static final String NAMESPACE = "http://mystatfit.com/";
	public static final String URL = "http://mystatfit.com/aut/";

	private String login;
	private String password;

	private LoginActivity loginActivity;

	public Loginner(SoapParams params, LoginActivity loginActivity, String login, String password) {
		super(params);
		this.loginActivity = loginActivity;
		this.login = login;
		this.password = password;
	}

	@Override
	public void execute() {
		this.getRequest().addProperty(USER_PROPERTY_KEY, login);
		this.getRequest().addProperty(PASSWD_PROPERTY_KEY, password);
		try {
			this.getAndroidHttpTransport().call(params.getSoapAction(),
					this.getEnvelope());
			if (this.getEnvelope().bodyIn instanceof SoapFault) {
				final SoapFault soapFault = (SoapFault) this.getEnvelope().bodyIn;
				loginActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(loginActivity, soapFault.faultstring,
								Toast.LENGTH_LONG).show();
						loginActivity.loginButton.setEnabled(true);

					}
				});
			} else {
				loginActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginActivity.loginButton.setEnabled(true);
						loginActivity.finish();
						SharedPreferences prefs = loginActivity
								.getSharedPreferences(
										Constants.PREFS_USER_DATA_KEY,
										Context.MODE_PRIVATE);
						Editor editor = prefs.edit();
						editor.putString(
								loginActivity.getString(R.string.login), login);
						editor.putString(
								loginActivity.getString(R.string.password),
								password);
						editor.commit();
					}
				});
				SoapObject resultsRequestSOAP = (SoapObject) this.getEnvelope().bodyIn;
				SoapUtils.ScanSoapObject(resultsRequestSOAP);
				Intent intent = new Intent(loginActivity, MainActivity.class);
				loginActivity.startActivity(intent);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
