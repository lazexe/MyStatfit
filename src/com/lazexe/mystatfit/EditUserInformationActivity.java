package com.lazexe.mystatfit;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.R.id;
import com.lazexe.mystatfit.R.layout;
import com.lazexe.mystatfit.progress.ProgressShowable;
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
import android.widget.ProgressBar;
import android.widget.Spinner;

public class EditUserInformationActivity extends Activity implements
		OnClickListener, OnFocusChangeListener, ProgressShowable {

	private EditText lastNameEditText; // ot4
	private EditText nameEditText; // name
	private EditText secondNameEditText; // parent name
	private EditText newLoginEditText;
	private EditText birthEditText;
	private Spinner genderSpinner;
	private Button acceptButton;
	private Date birthDate;
	private SoapEngine engine;
	
	private ProgressBar progressBar;

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
			if (checkFields()) {
				engine = SoapEngine.getInstance();
				SoapParams params = new SoapParams(Editor.SOAP_ACTION,
						Editor.METHOD_NAME, Editor.NAMESPACE, Editor.URL);
				engine.runCommand(new Editor(params, this, 
						nameEditText.getText().toString(), 
						lastNameEditText.getText().toString(), 
						secondNameEditText.getText().toString(),
						newLoginEditText.getText().toString(), 
						genderSpinner.getSelectedItem().toString(), 
						birthEditText.getText().toString()));
			}
		}
	}

	private boolean checkFields() {
		boolean isCorrect = true;

		// TODO

		return isCorrect;
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
						birthDate = new Date(dayOfMonth, monthOfYear + 1, year);
						birthEditText.setText(birthDate.toString());
					}
				}, 1990, 1, 1);
		dialog.show();
	}

	private void initControls() {
		lastNameEditText = (EditText) findViewById(R.id.edit_surname);
		nameEditText = (EditText) findViewById(R.id.edit_name);
		secondNameEditText = (EditText) findViewById(R.id.edit_parent_name);
		birthEditText = (EditText) findViewById(R.id.edit_birthday);
		birthEditText.setOnFocusChangeListener(this);
		birthEditText.setOnClickListener(this);
		genderSpinner = (Spinner) findViewById(R.id.edit_gender);
		genderSpinner.setSelection(0);
		acceptButton = (Button) findViewById(R.id.accept_edit_button);
		acceptButton.setOnClickListener(this);
		newLoginEditText = (EditText) findViewById(R.id.edit_newlogin);
		progressBar = (ProgressBar) findViewById(R.id.edit_progressbar);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void showProgress() {
		setControlsEnabled(false);
	}

	@Override
	public void hideProgress() {
		setControlsEnabled(true);
	}
	
	private void setControlsEnabled(boolean state) {
		lastNameEditText.setEnabled(state);
		nameEditText.setEnabled(state);
		secondNameEditText.setEnabled(state);
		newLoginEditText.setEnabled(state);
		birthEditText.setEnabled(state);
		genderSpinner.setEnabled(state);
		acceptButton.setEnabled(state);
		if (state)
			progressBar.setVisibility(View.INVISIBLE);
		else
			progressBar.setVisibility(View.VISIBLE);
	}

}
