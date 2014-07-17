package com.lazexe.mystatfit.fragments;

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

public class RunFragment extends Fragment implements OnClickListener {

	private static final String TAG = RunFragment.class.getName();

	TextView stepsTextView;
	ImageButton startButton;
	ImageButton lockButton;
	ImageButton stopButton;
	private static int steps;

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
			break;
		case R.id.lock_count_steps_button:
			startButton.setEnabled(!startButton.isEnabled());
			stopButton.setEnabled(!stopButton.isEnabled());
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
