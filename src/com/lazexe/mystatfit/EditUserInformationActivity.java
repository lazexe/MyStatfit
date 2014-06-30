package com.lazexe.mystatfit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class EditUserInformationActivity extends Activity implements OnClickListener {

	private EditText surnameEditText;
	private EditText nameEditText;
	private EditText parentNameEditText;
	private EditText birthEditText;
	private Spinner genderSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user);
		
		initControls();
	}


	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.edit_birthday) {
			DatePickerDialog dialog = new DatePickerDialog(this, new  DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3) {
					// TODO save data
					
				}
			}, 1990, 3, 15);
			dialog.show();
		}
	}


	private void initControls() {
		surnameEditText = (EditText) findViewById(R.id.edit_surname);
		nameEditText = (EditText) findViewById(R.id.edit_name);
		parentNameEditText = (EditText) findViewById(R.id.edit_parent_name);
		birthEditText = (EditText) findViewById(R.id.edit_birthday);
		birthEditText.setOnClickListener(this);
		genderSpinner = (Spinner) findViewById(R.id.edit_gender);
		genderSpinner.setSelection(0);
	}
}
