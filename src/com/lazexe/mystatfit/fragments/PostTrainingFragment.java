package com.lazexe.mystatfit.fragments;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.database.TrainingDatabase;
import com.lazexe.mystatfit.utils.NetworkUtils;
import com.lazexe.mystatfit.utils.PreferencesUtils;

public class PostTrainingFragment extends Fragment implements OnClickListener  {

	private static final String TAG = PostTrainingFragment.class.getName();
	
	public static String START_DATE_KEY = "start";
	public static String END_DATE_KEY = "end";
	public static String DURATION_HOURS_KEY = "durationHours";
	public static String DURATION_MINUTES_KEY = "durationMinutes";
	public static String DURATION_SECONDS_KEY = "durationSeconds";
	public static String STEPS_KEY = "steps";
	public static String DISTANCE_KEY = "distance";
	public static String CALORIES_KEY = "calories";
	
	private long startDateInMills;
	private long endDateInMills;
	private int durationHours;
	private int durationMinutes;
	private int durationSeconds;
	private int steps;
	private float distance;
	private int calories;
	
	private RatingBar sentimentRating;
	private EditText pulseEditText;
	private Spinner weatherSpinner;
	private EditText temperatureEditText;
	private EditText coverEditText;
	
	private Button acceptButton;
	private Button cancelButton;
	
	private PostTrainingFragment thisFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post_training, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		thisFragment = this;
		sentimentRating = (RatingBar) view.findViewById(R.id.sentiment_ratingbar);
		pulseEditText = (EditText) view.findViewById(R.id.pulse_edittext);
		weatherSpinner = (Spinner) view.findViewById(R.id.weather_spinner);
		acceptButton = (Button) view.findViewById(R.id.accept_post_training_button);
		cancelButton = (Button) view.findViewById(R.id.cancel_post_training_button);
		coverEditText = (EditText) view.findViewById(R.id.cover_edittext);
		temperatureEditText = (EditText) view.findViewById(R.id.temperature_edittext);
		
		acceptButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		Bundle bundle = this.getArguments();
		startDateInMills = bundle.getLong(START_DATE_KEY);
		endDateInMills = bundle.getLong(END_DATE_KEY);
		durationHours = bundle.getInt(DURATION_HOURS_KEY);
		durationMinutes = bundle.getInt(DURATION_MINUTES_KEY);
		durationSeconds = bundle.getInt(DURATION_SECONDS_KEY);
		steps = bundle.getInt(STEPS_KEY);
		calories = bundle.getInt(CALORIES_KEY);
		distance = bundle.getFloat(DISTANCE_KEY);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.accept_post_training_button:
			accept();
			
			break;
		case R.id.cancel_post_training_button:
			showConfirmDialog();
			break;
		default:
			break;
		}
	}
	
	private void accept() {
		if (checkFragmentFields()) {
			if (NetworkUtils.isOnline(getActivity()))
				saveToRemoteBase();
			else
				saveToLocalBase();
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			WelcomeFragment welcomeFragment = new WelcomeFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, welcomeFragment).commit();
		}
			
	}
		
	private void saveToRemoteBase() {
		Toast.makeText(getActivity(), "Not implemented!", Toast.LENGTH_LONG).show();
	}
	
	private void saveToLocalBase() {
			Toast.makeText(getActivity(), "Saved to local database", Toast.LENGTH_LONG).show();
			TrainingDatabase trainingDatabase = new TrainingDatabase(getActivity());
			SQLiteDatabase sqLiteDatabase = trainingDatabase.getWritableDatabase();
			ContentValues values = new ContentValues();
			java.util.Date startDate = new Date(startDateInMills);
			java.util.Date endDate = new Date(endDateInMills);
			values.put(TrainingDatabase.runTableFields[1], PreferencesUtils.getUserLogin(getActivity()));
			values.put(TrainingDatabase.runTableFields[2], startDate.getDate());
			values.put(TrainingDatabase.runTableFields[3], startDate.getMonth());
			values.put(TrainingDatabase.runTableFields[4], startDate.getYear());
			values.put(TrainingDatabase.runTableFields[5], startDate.getHours());
			values.put(TrainingDatabase.runTableFields[6], startDate.getMinutes());
			values.put(TrainingDatabase.runTableFields[7], startDate.getSeconds());
			values.put(TrainingDatabase.runTableFields[8], endDate.getDate());
			values.put(TrainingDatabase.runTableFields[9], endDate.getMonth());
			values.put(TrainingDatabase.runTableFields[10], endDate.getYear());
			values.put(TrainingDatabase.runTableFields[11], endDate.getHours());
			values.put(TrainingDatabase.runTableFields[12], endDate.getMinutes());
			values.put(TrainingDatabase.runTableFields[13], endDate.getSeconds());
			values.put(TrainingDatabase.runTableFields[14], durationHours);
			values.put(TrainingDatabase.runTableFields[15], durationMinutes);
			values.put(TrainingDatabase.runTableFields[16], durationSeconds);
			values.put(TrainingDatabase.runTableFields[17], steps);
			values.put(TrainingDatabase.runTableFields[18], distance);
			values.put(TrainingDatabase.runTableFields[19], pulseEditText.getText().toString());
			values.put(TrainingDatabase.runTableFields[20], calories);
			values.put(TrainingDatabase.runTableFields[21], coverEditText.getText().toString());
			values.put(TrainingDatabase.runTableFields[22], sentimentRating.getRating());
			values.put(TrainingDatabase.runTableFields[23], temperatureEditText.getText().toString());
			values.put(TrainingDatabase.runTableFields[24], weatherSpinner.getSelectedItem().toString());
			sqLiteDatabase.insert(TrainingDatabase.RUN_TABLE_NAME, null, values);
			sqLiteDatabase.close();
			trainingDatabase.close();
	}
	
	private void showConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getActivity().getString(R.string.confirm));
		builder.setTitle(getActivity().getString(R.string.confirm_cancel_save_training));
		builder.setPositiveButton(getActivity().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				WelcomeFragment welcomeFragment = new WelcomeFragment();
				fragmentManager.beginTransaction().replace(R.id.content_frame, welcomeFragment).commit();
			}
		});
		builder.setNegativeButton(getActivity().getString(android.R.string.no), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private boolean checkFragmentFields() {
		
		if (isFieldEmpty(pulseEditText))
			return false;
		if (isFieldEmpty(coverEditText))
			return false;
		if (isFieldEmpty(temperatureEditText))
			return false;
		
		return true;
	}
	
	private boolean isFieldEmpty(EditText field) {
		if (field.getText().toString().isEmpty()) {
			field.setError(getActivity().getString(R.string.error_empty_field));
			field.requestFocus();
			return true;
		}
		return false;
	}
}
