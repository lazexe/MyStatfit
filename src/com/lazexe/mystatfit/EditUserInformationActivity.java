package com.lazexe.mystatfit;

import com.lazexe.mystatfit.soap.Editor;
import com.lazexe.mystatfit.soap.SoapEngine;
import com.lazexe.mystatfit.soap.SoapParams;
import com.lazexe.mystatfit.utils.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class EditUserInformationActivity extends Activity implements
		OnClickListener, OnFocusChangeListener {

	private EditText surnameEditText;
	private EditText nameEditText;
	private EditText parentNameEditText;
	private EditText birthEditText;
	private Spinner genderSpinner;
	private Button acceptButton;
	private Date birthDate;
	private SoapEngine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user);
		initControls();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.edit_birthday) {
			handleBirthEditText();
		}
		if (view.getId() == R.id.accept_edit_button) {
			// TODO handle click
			engine = SoapEngine.getInstance();
			SoapParams params = new SoapParams(Editor.SOAP_ACTION,
					Editor.METHOD_NAME, Editor.NAMESPACE, Editor.URL);
			engine.runCommand(new Editor(params));
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (hasFocus) {
			if (view.getId() == R.id.edit_birthday) {
				handleBirthEditText();
			}
		}
	}

	private void handleBirthEditText() {
		DatePickerDialog dialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int year,
							int monthOfYear, int dayOfMonth) {
						birthDate = new Date(dayOfMonth, monthOfYear + 1,
								year);
						birthEditText.setText(birthDate.toString());
					}
				}, 1990, 1, 1);
		dialog.show();
	}

	private void initControls() {
		surnameEditText = (EditText) findViewById(R.id.edit_surname);
		nameEditText = (EditText) findViewById(R.id.edit_name);
		parentNameEditText = (EditText) findViewById(R.id.edit_parent_name);
		birthEditText = (EditText) findViewById(R.id.edit_birthday);
		birthEditText.setOnFocusChangeListener(this);
		birthEditText.setOnClickListener(this);
		genderSpinner = (Spinner) findViewById(R.id.edit_gender);
		genderSpinner.setSelection(0);
		acceptButton = (Button) findViewById(R.id.accept_edit_button);
		acceptButton.setOnClickListener(this);
	}

}
