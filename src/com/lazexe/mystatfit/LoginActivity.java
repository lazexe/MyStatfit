package com.lazexe.mystatfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lazexe.mystatfit.soap.Loginner;
import com.lazexe.mystatfit.soap.SoapEngine;
import com.lazexe.mystatfit.soap.SoapParams;
import com.lazexe.mystatfit.utils.Constants;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getName();

	private SoapEngine engine;

	private EditText loginEditText;
	private EditText passEditText;
	public Button loginButton;
	private Button newRegisterButton;
	private LoginActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initComponents();
	}

	private void initComponents() {
		engine = SoapEngine.getInstance();
		activity = this;
		ActivityButtonHandler handler = new ActivityButtonHandler(this);

		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(handler);
		loginEditText = (EditText) findViewById(R.id.login);
		passEditText = (EditText) findViewById(R.id.pass);
		newRegisterButton = (Button) findViewById(R.id.new_register_button);
		newRegisterButton.setOnClickListener(handler);

		SharedPreferences prefs = getSharedPreferences(
				Constants.PREFS_USER_DATA_KEY, MODE_PRIVATE);
		String login = prefs.getString(getString(R.string.login), null);
		String password = prefs.getString(getString(R.string.password), null);

		if (login != null && password != null) {
			SoapParams params = new SoapParams(Loginner.SOAP_ACTION,
					Loginner.METHOD_NAME, Loginner.NAMESPACE, Loginner.URL);
			engine.runCommand(new Loginner(params, activity, login, password));
		}
	}

	class ActivityButtonHandler implements View.OnClickListener {

		private Context context;

		public ActivityButtonHandler(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.login_button) {
				SoapParams params = new SoapParams(Loginner.SOAP_ACTION,
						Loginner.METHOD_NAME, Loginner.NAMESPACE, Loginner.URL);
				engine.runCommand(new Loginner(params, activity, loginEditText
						.getText().toString(), passEditText.getText()
						.toString()));
			}
			if (v.getId() == R.id.new_register_button) {
				Intent licenceActivityIntent = new Intent(context,
						LicenceAgreementActivity.class);
				startActivity(licenceActivityIntent);
			}
		}

	}

	public Button getLoginButton() {
		return loginButton;
	}

}
