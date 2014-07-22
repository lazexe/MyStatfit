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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.step.StepCounterService;

public class RunFragment extends Fragment implements OnClickListener {

	private static final String TAG = RunFragment.class.getName();

	TextView stepsTextView;
	TextView caloriesTextView;
	TextView speedTextView;
	TextView timeTextView;
	ImageButton startButton;
	ImageButton lockButton;
	ImageButton stopButton;

	private static int steps;
	private static int calories;
	private int speed;

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
			Intent startIntent = new Intent(getActivity(),
					StepCounterService.class);
			getActivity().startService(startIntent);
			getActivity().bindService(startIntent, new StepServiceConnection(),
					Context.BIND_AUTO_CREATE);
			startCountSteps();
			break;
		case R.id.lock_count_steps_button:
			startButton.setEnabled(!startButton.isEnabled());
			stopButton.setEnabled(!stopButton.isEnabled());
			if (getActivity().getActionBar().isShowing()) {
				getActivity().getActionBar().hide();
			} else {
				getActivity().getActionBar().show();
			}
			break;
		case R.id.stop_count_steps_button:
			Intent stopIntent = new Intent(getActivity(),
					StepCounterService.class);
			getActivity().stopService(stopIntent);
			break;
		default:
			Log.d(TAG, "onClick DEFAULT");
			break;
		}
	}

	private void startCountSteps() {
		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {

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
						speed = (int) ((((hours / 3600) + (minutes / 60) + seconds) / steps) * 60);
					}

					// //Women
					// if(gender == 0){
					// calories +=
					// (int)(((((655+(9.6*weight)+(1.8*height)-(4.7*age))*ACTIVITY_COEFFICIENT)/24)/3600)
					// *(second));
					// }//men
					// else{
					// calories +=
					// (int)(((((66+(13.7*weight)+(5*height)-(6.8*age))*ACTIVITY_COEFFICIENT)/24)/3600)
					// *(second));
					// }

					speedTextView.setText(String.valueOf(speed));
					timeTextView.setText(String.valueOf(hours) + " : "
							+ String.valueOf(minutes) + " : "
							+ String.valueOf(seconds));
				}
			});
		}
	};

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
