package com.lazexe.mystatfit.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.database.TrainingDatabase;
import com.lazexe.mystatfit.step.StepCounterService;
import com.lazexe.mystatfit.utils.Constants;
import com.lazexe.mystatfit.utils.PreferencesUtils;

public class WalkingFragment extends Fragment implements OnClickListener {

	private static final String TAG = WalkingFragment.class.getName();
	private static float ACTIVITY_COEFFICIENT = 1.752F;

	private long startDateInMills;
	private long endDateInMills;

	TextView stepsTextView;
	TextView caloriesTextView;
	TextView speedTextView;
	TextView timeTextView;
	ImageButton startButton;
	ImageButton lockButton;
	ImageButton stopButton;
	Button startTrainingButton;

	private static int steps;
	private static int calories;
	private float speed;
	private float distance;
	private float stepLength;
	private int weight;
	private int height;
	private int age;
	private int gender;

	private Timer timer;
	private int durationHours;
	private int durationMinutes;
	private int durationSeconds;

	private Handler updateDisplay;

	private boolean paused;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_run, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		stepLength = Integer.parseInt(PreferencesUtils
				.getStepLength(getActivity().getApplicationContext()));
		weight = Integer.parseInt(PreferencesUtils.getUserWeight(getActivity()
				.getApplicationContext()));
		height = Integer.parseInt(PreferencesUtils.getUserHeight(getActivity()
				.getApplicationContext()));
		gender = PreferencesUtils.getUserGender(getActivity()
				.getApplicationContext());
		age = PreferencesUtils
				.getUserAge(getActivity().getApplicationContext());
		startButton = (ImageButton) view
				.findViewById(R.id.start_count_steps_button);
		lockButton = (ImageButton) view
				.findViewById(R.id.lock_count_steps_button);
		stopButton = (ImageButton) view
				.findViewById(R.id.stop_count_steps_button);
		startTrainingButton = (Button) view
				.findViewById(R.id.start_walking_training_button);
		stepsTextView = (TextView) view.findViewById(R.id.steps_textview);
		caloriesTextView = (TextView) view.findViewById(R.id.calories_textview);
		timeTextView = (TextView) view.findViewById(R.id.time_textview);
		speedTextView = (TextView) view.findViewById(R.id.speed_textview);
		setControlsButtonsVisibility(View.INVISIBLE);
		startButton.setOnClickListener(this);
		lockButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		startTrainingButton.setOnClickListener(this);
		stepsTextView.setText("0");
		caloriesTextView.setText("0");
		timeTextView.setText("00:00:00");
		speedTextView.setText("0.0");
		paused = true;
		updateDisplay = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				steps = msg.arg1;
				int bigSteps = msg.arg2;
				if (steps > 0) {
					stepsTextView.setText(String.valueOf(steps));
				}
			}
		};
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		ImageButton clickedButton = null;
		if (view instanceof ImageButton) {
			clickedButton = (ImageButton) view;
		}
		switch (id) {
		case R.id.start_walking_training_button:
			startTrainingButton.setVisibility(View.INVISIBLE);
			setControlsButtonsVisibility(View.VISIBLE);
			start(clickedButton);
			break;
		case R.id.start_count_steps_button:
			start(clickedButton);
			break;
		case R.id.lock_count_steps_button:
			lockScreenFromTouch();
			break;
		case R.id.stop_count_steps_button:
			stopCountSteps();
			break;
		default:
			Log.d(TAG, "onClick DEFAULT");
			break;
		}
	}

	private void start(ImageButton clickedButton) {
		String contentdescription = (String) clickedButton
				.getContentDescription();
		if (contentdescription.equals(getActivity().getString(R.string.play))) {
			paused = false;
			clickedButton.setImageDrawable(getActivity().getResources()
					.getDrawable(android.R.drawable.ic_media_pause));
			clickedButton.setContentDescription(getActivity().getString(
					R.string.pause));
			lockScreenFromTouch();
			startCountSteps();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			prefs.edit().putBoolean(Constants.PREF_IS_TRAINING_RUN_KEY, true)
					.commit();
		} else {
			paused = true;
			clickedButton.setImageDrawable(getActivity().getResources()
					.getDrawable(android.R.drawable.ic_media_play));
			clickedButton.setContentDescription(getActivity().getString(
					R.string.play));
		}
	}

	private void setControlsButtonsVisibility(int visibility) {
		startButton.setVisibility(visibility);
		lockButton.setVisibility(visibility);
		stopButton.setVisibility(visibility);
	}

	private void startCountSteps() {
		steps = 0;
		distance = 0;
		Intent startIntent = new Intent(getActivity(), StepCounterService.class);
		getActivity().startService(startIntent);
		getActivity().bindService(startIntent, stepServiceConnection,
				Context.BIND_AUTO_CREATE);
		timer = new Timer();
		task = createTimerTask();
		timer.schedule(task, 1000, 1000);
		Calendar calendar = Calendar.getInstance();
		startDateInMills = calendar.getTimeInMillis();
	}

	private void lockScreenFromTouch() {
		startButton.setEnabled(!startButton.isEnabled());
		stopButton.setEnabled(!stopButton.isEnabled());
	}

	private void stopCountSteps() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.edit().putBoolean(Constants.PREF_IS_TRAINING_RUN_KEY, false)
				.commit();
		Intent stopIntent = new Intent(getActivity(), StepCounterService.class);
		getActivity().stopService(stopIntent);
		timer.cancel();
		Bundle bundle = new Bundle();
		bundle.putLong(PostTrainingFragment.START_DATE_KEY, startDateInMills);
		bundle.putLong(PostTrainingFragment.END_DATE_KEY, endDateInMills);
		bundle.putInt(PostTrainingFragment.DURATION_HOURS_KEY, durationHours);
		bundle.putInt(PostTrainingFragment.DURATION_MINUTES_KEY,
				durationMinutes);
		bundle.putInt(PostTrainingFragment.DURATION_SECONDS_KEY,
				durationSeconds);
		bundle.putInt(PostTrainingFragment.STEPS_KEY, steps);
		bundle.putInt(PostTrainingFragment.CALORIES_KEY, calories);
		bundle.putFloat(PostTrainingFragment.DISTANCE_KEY, distance);
		PostTrainingFragment postFragment = new PostTrainingFragment();
		postFragment.setArguments(bundle);
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, postFragment).commit();
		Calendar calendar = Calendar.getInstance();
		endDateInMills = calendar.getTimeInMillis();
	}

	private TimerTask createTimerTask() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {

					float elapsedTimeInSeconds = 0;

					@Override
					public void run() {
						durationSeconds++;
						if (durationSeconds == 60) {
							durationSeconds = 0;
							durationMinutes++;
						}
						if (durationMinutes == 60) {
							durationMinutes = 0;
							durationHours++;
						}
						if (steps > 0) {
							distance = steps * stepLength / 100;
							elapsedTimeInSeconds = durationHours * 3600
									+ durationMinutes * 60 + durationSeconds;
							speed = distance / elapsedTimeInSeconds;
							// speed = (int) ((((hours / 3600) + (minutes / 60)
							// +
							// seconds) / steps) * 60);
						}
						// Women
						if (gender == 0) {
							calories += (int) (((((655 + (9.6 * weight)
									+ (1.8 * height) - (4.7 * age)) * ACTIVITY_COEFFICIENT) / 24) / 3600) * (durationSeconds));
						}/* men */else {
							calories += (int) (((((66 + (13.7 * weight)
									+ (5 * height) - (6.8 * age)) * ACTIVITY_COEFFICIENT) / 24) / 3600) * (durationSeconds));
						}

						stepsTextView.setText(String.valueOf(steps));
						caloriesTextView.setText(String.valueOf(calories));
						speedTextView.setText(String.valueOf(speed));
						timeTextView.setText(String.valueOf(durationHours)
								+ " : " + String.valueOf(durationMinutes)
								+ " : " + String.valueOf(durationSeconds));
					}
				});
			}
		};
		return task;
	}

	private TimerTask task = createTimerTask();

	@Override
	public void onStop() {
		super.onStop();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.edit().putBoolean(Constants.PREF_IS_TRAINING_RUN_KEY, false)
				.commit();
		try {
			getActivity().unbindService(stepServiceConnection);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		Intent stepCounterServiceIntent = new Intent(getActivity(),
				StepCounterService.class);
		try {
			getActivity().stopService(stepCounterServiceIntent);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		task.cancel();
		steps = 0;
		calories = 0;
	}

	private final ServiceConnection stepServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			((StepCounterService.LocalBinder) service)
					.gimmeHandler(updateDisplay);
			Log.d(TAG, "onServiceConnected!");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};
}
