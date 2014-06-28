package com.lazexe.mystatfit;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lazexe.mystatfit.utils.SoapUtils;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getName();

	private static final String SOAP_ACTION = "http://mystatfit.com/CheckAuthorization"; // 100%
	private static final String METHOD_NAME = "CheckAuthorization"; // 100%
	private static final String NAMESPACE = "http://mystatfit.com/"; // 100%
	private static final String URL = "http://mystatfit.com/aut/"; // 100%

	private EditText loginEditText;
	private EditText passEditText;
	private Button loginButton;
	private Button newRegisterButton;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initComponents();
	}

	private void initComponents() {
		activity = this;
		ActivityButtonHandler handler = new ActivityButtonHandler(this);

		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(handler);
		loginEditText = (EditText) findViewById(R.id.login);
		passEditText = (EditText) findViewById(R.id.pass);
		newRegisterButton = (Button) findViewById(R.id.new_register_button);
		newRegisterButton.setOnClickListener(handler);
	}

	class ActivityButtonHandler implements View.OnClickListener {

		private Context context;

		public ActivityButtonHandler(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.login_button) {
				LoginTask loginTask = new LoginTask();
				loginTask.execute();
			}
			if (v.getId() == R.id.new_register_button) {
				Intent registerActivityIntent = new Intent(context,
						RegistrationActivity.class);
				startActivity(registerActivityIntent);
			}
		}

	}

	class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loginButton.setEnabled(false);
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
			final String login = loginEditText.getText().toString();
			final String password = passEditText.getText().toString();
			request.addProperty("user", login);
			request.addProperty("password", password);
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
							loginButton.setEnabled(true);
						}
					});
				} else {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loginButton.setEnabled(true);
							activity.finish();
							SharedPreferences prefs = getPreferences(MODE_PRIVATE);
							Editor editor = prefs.edit();
							editor.putString(getString(R.string.login), login);
							editor.putString(getString(R.string.password),
									password);
							editor.commit();
						}
					});
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapUtils.ScanSoapObject(resultsRequestSOAP);
					Intent intent = new Intent(activity, MainActivity.class);
					startActivity(intent);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return null;
		}

	}

}
