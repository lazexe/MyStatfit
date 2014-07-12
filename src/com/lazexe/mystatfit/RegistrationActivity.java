package com.lazexe.mystatfit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lazexe.mystatfit.soap.Registrator;
import com.lazexe.mystatfit.soap.SoapEngine;
import com.lazexe.mystatfit.soap.SoapParams;

public class RegistrationActivity extends Activity implements OnClickListener {

	private SoapEngine engine;
	private TextView emailEditText;
	private TextView passwordEditText;
	private TextView confirmPasswordEditText;
	public Button registrationButton;
	private RegistrationActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		initComponents();
	}

	public void initComponents() {
		engine = SoapEngine.getInstance();
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

			SoapParams params = new SoapParams(Registrator.SOAP_ACTION,
					Registrator.METHOD_NAME, Registrator.NAMESPACE,
					Registrator.URL);
			engine.runCommand(new Registrator(params, activity, emailEditText
					.getText().toString(), passwordEditText.getText()
					.toString(), confirmPasswordEditText.getText().toString()));
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
		
		if (email.contains(" ")) {
			isCorrect = false;
			emailEditText.setError(getString(R.string.error_whitespace));
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

	@Override
	public void onBackPressed() {
		this.finish();
		Intent licenteActivityIntent = new Intent(this,
				LicenceAgreementActivity.class);
		startActivity(licenteActivityIntent);
	}

}
