package com.lazexe.mystatfit.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.step.StepCounterService;
import com.lazexe.mystatfit.utils.PreferencesUtils;

public class RunFragment extends Fragment implements OnClickListener {

	private static final String TAG = RunFragment.class.getName();
	private static float ACTIVITY_COEFFICIENT = 1.752F;

	TextView stepsTextView;
	TextView caloriesTextView;
	TextView speedTextView;
	TextView timeTextView;
	ImageButton startButton;
	ImageButton lockButton;
	ImageButton stopButton;

	private static int steps;
	private static int calories;
	private float speed;
	private float stepLength;
	private int weight;
	private int height;
	private int age;
	private int gender;

	private Timer timer;
	private int hours;
	private int minutes;
	private int seconds;

	private Handler updateDisplay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_run, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		stepLength = Integer.parseInt(PreferencesUtils.getStepLength(getActivity().getApplicationContext()));
		weight = Integer.parseInt(PreferencesUtils.getUserWeight(getActivity().getApplicationContext()));
		height = Integer.parseInt(PreferencesUtils.getUserHeight(getActivity().getApplicationContext()));
		gender = PreferencesUtils.getUserGender(getActivity().getApplicationContext());
		age = PreferencesUtils.getUserAge(getActivity().getApplicationContext());
		startButton = (ImageButton) view
				.findViewById(R.id.start_count_steps_button);
		lockButton = (ImageButton) view
				.findViewById(R.id.lock_count_steps_button);
		stopButton = (ImageButton) view
				.findViewById(R.id.stop_count_steps_button);
		stepsTextView = (TextView) view.findViewById(R.id.steps_textview);
		caloriesTextView = (TextView) view.findViewById(R.id.calories_textview);
		timeTextView = (TextView) view.findViewById(R.id.time_textview);
		speedTextView = (TextView) view.findViewById(R.id.speed_textview);
		startButton.setOnClickListener(this);
		lockButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
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
		switch (id) {
		case R.id.start_count_steps_button:
			startCountSteps();
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

	private void startCountSteps() {
		Intent startIntent = new Intent(getActivity(),
				StepCounterService.class);
		getActivity().startService(startIntent);
		getActivity().bindService(startIntent, new StepServiceConnection(),
				Context.BIND_AUTO_CREATE);
		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}
	
	private void lockScreenFromTouch() {
		startButton.setEnabled(!startButton.isEnabled());
		stopButton.setEnabled(!stopButton.isEnabled());
		if (getActivity().getActionBar().isShowing()) {
			getActivity().getActionBar().hide();
		} else {
			getActivity().getActionBar().show();
		}
	}
	
	private void stopCountSteps() {
		Intent stopIntent = new Intent(getActivity(),
				StepCounterService.class);
		getActivity().stopService(stopIntent);
		timer.cancel();
	}

	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {

				float distance = 0;
				float elapsedTimeInSeconds = 0;
				
				@Override
				public void run() {
					seconds++;
					if (seconds == 60) {
						seconds = 0;
						minutes++;
					}
					if (minutes == 60) {
						minutes = 0;
						hours++;
					}
					
					if (steps > 0) {
						distance = steps * stepLength / 100;
						elapsedTimeInSeconds = hours * 3600 + minutes * 60 + seconds;
						speed = distance / elapsedTimeInSeconds;
						Log.d(TAG, "Steps" + String.valueOf(steps));
						Log.d(TAG, "Step length " +  String.valueOf(stepLength));
						Log.d(TAG, "Distance " + String.valueOf(distance));
						Log.d(TAG, "Elapsed time " + String.valueOf(elapsedTimeInSeconds));
						Log.d(TAG, "Speed " + String.valueOf(speed));
						//speed = (int) ((((hours / 3600) + (minutes / 60) + seconds) / steps) * 60);
					}

					
					Log.d(TAG, "Weight " + String.valueOf(weight));
					Log.d(TAG, "Height " + String.valueOf(height));
					Log.d(TAG, "Age " + String.valueOf(age));
					Log.d(TAG, "Coef" + String.valueOf(ACTIVITY_COEFFICIENT));
					 //Women
					if(gender == 0){
						calories +=
						(int)(((((655+(9.6*weight)+(1.8*height)-(4.7*age))*ACTIVITY_COEFFICIENT)/24)/3600)
						*(seconds));
					}/*men*/ else{
						calories +=
						(int)(((((66+(13.7*weight)+(5*height)-(6.8*age))*ACTIVITY_COEFFICIENT)/24)/3600)
						*(seconds));
					}
					Log.d(TAG, "Calories: " + calories);
					caloriesTextView.setText(String.valueOf(calories));
					speedTextView.setText(String.valueOf(speed));
					timeTextView.setText(String.valueOf(hours) + " : "
							+ String.valueOf(minutes) + " : "
							+ String.valueOf(seconds));
				}
			});
		}
	};
	
	

	@Override
	public void onStop() {
		super.onStop();
		//TODO
	}



	class StepServiceConnection implements ServiceConnection {

		private final String TAG;

		public StepServiceConnection() {
			TAG = StepServiceConnection.class.getName();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			((StepCounterService.LocalBinder) service)
					.gimmeHandler(updateDisplay);
			Log.d(TAG, "onServiceConnected!");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}
}
