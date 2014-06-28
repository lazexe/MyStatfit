package com.lazexe.mystatfit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.lazexe.mystatfit.utils.SoapUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity implements OnClickListener {
	
	private static final String SOAP_ACTION = "http://mystatfit.com/Registration"; // 100%
	private static final String METHOD_NAME = "Registration"; // 100%
	private static final String NAMESPACE = "http://mystatfit.com/"; // 100%
	private static final String URL = "http://mystatfit.com/aut/"; // 100%

	private TextView emailEditText;
	private TextView passwordEditText;
	private TextView confirmPasswordEditText;
	private Button registrationButton;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		activity = this;
		emailEditText = (TextView) findViewById(R.id.reg_email);
		passwordEditText = (TextView) findViewById(R.id.reg_pass);
		confirmPasswordEditText = (TextView) findViewById(R.id.reg_pass_confirm);
		registrationButton = (Button) findViewById(R.id.button_register);
		registrationButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_register) {
			if (!isEmailCorrect())
				return;

			if (!isPasswordCorrect())
				return;

			RegisterTask registerTask = new RegisterTask();
			registerTask.execute();
		}
	}

	private boolean isEmailCorrect() {
		// a@t.ru
		boolean isCorrect = true;
		Pattern pattern = Pattern
				.compile("[a-zA-Z]{1}[a-zA-Z\\d\\u002E\\u005F]+@([a-zA-Z]+\\u002E){1,2}((net)|(com)|(org)|(ru))");
		String email = emailEditText.getText().toString();

		if (!email.contains("@")) {
			isCorrect = false;
			emailEditText.setError(getString(R.string.error_dog));
			emailEditText.requestFocus();
			return isCorrect;
		}

		if (email.length() < 6) {
			isCorrect = false;
			emailEditText.setError(getString(R.string.error_length));
			emailEditText.requestFocus();
			return isCorrect;
		}

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			isCorrect = false;
			emailEditText.setError(getString(R.string.error_email));
			emailEditText.requestFocus();
			return isCorrect;
		}

		return isCorrect;
	}

	private boolean isPasswordCorrect() {
		boolean isCorrect = true;

		String password = passwordEditText.getText().toString();
		String confirmedPassword = confirmPasswordEditText.getText().toString();

		if (password.length() < 4) {
			isCorrect = false;
			passwordEditText
					.setError(getString(R.string.error_password_length));
			passwordEditText.requestFocus();
			return isCorrect;
		}

		if (!password.equals(confirmedPassword)) {
			isCorrect = true;
			confirmPasswordEditText
					.setError(getString(R.string.error_password_mismatch));
			confirmPasswordEditText.requestFocus();
			return isCorrect;
		}

		return isCorrect;
	}

	class RegisterTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					registrationButton.setEnabled(false);
				}
			});
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = false;
			envelope.xsd = SoapSerializationEnvelope.XSD;
			envelope.enc = SoapSerializationEnvelope.ENC;
			envelope.setAddAdornments(false);
			envelope.encodingStyle = SoapSerializationEnvelope.ENC;
			envelope.env = SoapSerializationEnvelope.ENV;
			envelope.implicitTypes = true;
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			final String login = emailEditText.getText().toString();
			final String password = passwordEditText.getText().toString();
			final String confirmPasswd = confirmPasswordEditText.getText().toString();
			request.addProperty("user", login);
			request.addProperty("pass", password);
			request.addProperty("pass2", confirmPasswd);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.debug = true;
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				if (envelope.bodyIn instanceof SoapFault) {
					final SoapFault soapFault = (SoapFault) envelope.bodyIn;
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(activity, soapFault.faultstring,
									Toast.LENGTH_LONG).show();
							registrationButton.setEnabled(true);
						}
					});
				} else {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							registrationButton.setEnabled(true);
							activity.finish();
							SharedPreferences prefs = getPreferences(MODE_PRIVATE);
							Editor editor = prefs.edit();
							editor.putString(getString(R.string.login), login);
							editor.putString(getString(R.string.password),
									password);
							editor.commit();
							Toast.makeText(activity, "Register ok", Toast.LENGTH_LONG).show();
						}
					});
//					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
//					SoapUtils.ScanSoapObject(resultsRequestSOAP);
//					Intent intent = new Intent(activity, MainActivity.class);
//					startActivity(intent);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return null;
		}

	}

}
