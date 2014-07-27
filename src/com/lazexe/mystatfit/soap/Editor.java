package com.lazexe.mystatfit.soap;

import java.util.Calendar;

import org.ksoap2.SoapFault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.lazexe.mystatfit.EditUserInformationActivity;
import com.lazexe.mystatfit.MainActivity;
import com.lazexe.mystatfit.utils.Constants;
import com.lazexe.mystatfit.utils.PreferencesUtils;

public class Editor extends AbstractCommandObject implements
		SoapExecutableInterface {
	
	private static final String TAG = Editor.class.getName();

	private final String USER_PROPERTY_KEY = "user";
	private final String PASSWD_PROPERTY_KEY = "pass";
	private final String NAME_PROPERTY_KEY = "name";
	private final String LASTNAME_PROPERTY_KEY = "lastname";
	private final String SECONDNAME_PROPERTY_KEY = "secondname";
	private final String NEWLOGIN_PROPERTY_KEY = "newlogin";
	private final String GENDER_PROPERTY_KEY = "pol";
	private final String DAY_PROPERTY_KEY = "day";
	private final String MONTH_PROPERTY_KEY = "month";
	private final String YEAR_PROPERTY_KEY = "year";

	public static final String SOAP_ACTION = "http://mystatfit.com/EditProfile";
	public static final String METHOD_NAME = "EditProfile";
	public static final String NAMESPACE = "http://mystatfit.com/";
	public static final String URL = "http://mystatfit.com/aut/";

	private String user;
	private String password;
	private String name;
	private String lastName;
	private String secondName;
	private String newLogin;
	private String gender;
	private String day;
	private String month;
	private String year;
	
	private EditUserInformationActivity activity;

	public Editor(SoapParams params,EditUserInformationActivity activity, String name, String lastName,
			String secondName, String newLogin, String gender, String birth) {
		super(params);

		this.activity = activity;
		
		this.user = PreferencesUtils.getUserLogin(activity);
		this.password = PreferencesUtils.getUserPassword(activity);
		this.name = name;
		this.lastName = lastName;
		this.secondName = secondName;
		this.newLogin = newLogin;
		this.gender = String.valueOf(gender.charAt(0));
		String[] temp = birth.split("\\.");
		Log.d(TAG, birth);
		Log.d(TAG, String.valueOf(temp.length));
		this.day = temp[0];
		this.month = temp[1];
		this.year = temp[2];
	}

	@Override
	public void execute() {
		this.getRequest().addProperty(USER_PROPERTY_KEY, user);
		this.getRequest().addProperty(PASSWD_PROPERTY_KEY, password);
		this.getRequest().addProperty(NAME_PROPERTY_KEY, name);
		this.getRequest().addProperty(LASTNAME_PROPERTY_KEY, lastName);
		this.getRequest().addProperty(SECONDNAME_PROPERTY_KEY, secondName);
		this.getRequest().addProperty(NEWLOGIN_PROPERTY_KEY, newLogin);
		this.getRequest().addProperty(GENDER_PROPERTY_KEY, gender);
		this.getRequest().addProperty(DAY_PROPERTY_KEY, day);
		this.getRequest().addProperty(MONTH_PROPERTY_KEY, month);
		this.getRequest().addProperty(YEAR_PROPERTY_KEY, year);
		try {
			this.getAndroidHttpTransport().call(SOAP_ACTION, this.getEnvelope());
			if (this.getEnvelope().bodyIn instanceof SoapFault) {
				final SoapFault soapFault = (SoapFault) this.getEnvelope().bodyIn;
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(activity, soapFault.faultstring, Toast.LENGTH_LONG).show();
						// set accept button enabled
					}
				});
			} else {
				activity.finish();
				SharedPreferences prefs = activity.getSharedPreferences(Constants.PREFS_USER_DATA_KEY, Context.MODE_PRIVATE);
				prefs.edit().putString(Constants.PREFS_LOGIN_KEY, newLogin).commit();
				prefs.edit().putString(Constants.PREFS_GENDER_KEY, gender).commit();
				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int age = currentYear - Integer.valueOf(year);
				prefs.edit().putInt(Constants.PREFS_AGE_KEY, age).commit();
				Log.d(TAG, "Current year: " + String.valueOf(currentYear));
				Log.d(TAG, "Birthday year: " + year);
				Log.d(TAG, "Age: " + String.valueOf(age));
				Log.d(TAG, "Edit OK");
				Intent mainActivityIntent = new Intent(activity, MainActivity.class);
				activity.startActivity(mainActivityIntent);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public Context getContext() {
		return activity.getApplicationContext();
	}

	@Override
	public void startShowProgress() {
		activity.showProgress();
	}

	@Override
	public void stopShowProgress() {
		activity.hideProgress();
	}

}
