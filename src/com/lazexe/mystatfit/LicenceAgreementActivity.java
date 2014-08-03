package com.lazexe.mystatfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LicenceAgreementActivity extends Activity implements
		OnClickListener {

	private Button agreeButton;
	private Button disagreeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_licence);
		initControls();
	}

	private void initControls() {
		agreeButton = (Button) findViewById(R.id.licence_agree_button);
		agreeButton.setOnClickListener(this);
		disagreeButton = (Button) findViewById(R.id.licence_disagree_button);
		disagreeButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.licence_agree_button) {
			this.finish();
			Intent registrationActivityIntent = new Intent(this,
					RegistrationActivity.class);
			startActivity(registrationActivityIntent);
		}
		if (view.getId() == R.id.licence_disagree_button)
			this.finish();
	}

}
