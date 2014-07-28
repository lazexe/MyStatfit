package com.lazexe.mystatfit.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import com.lazexe.mystatfit.utils.NetworkUtils;

public class PostTrainingFragment extends Fragment implements OnClickListener  {

	private static final String TAG = PostTrainingFragment.class.getName();
	
	public static String START_DATE_KEY = "start";
	public static String END_DATE_KEY = "end";
	public static String DURATION_HOURS_KEY = "durationHours";
	public static String DURATION_MINUTES_KEY = "durationMinutes";
	public static String DURATION_SECONDS_KEY = "durationSeconds";
	public static String STEPS_KEY = "steps";
	public static String CALORIES_KEY = "calories";
	
	private long startDateInMills;
	private long endDateInMills;
	private int durationHours;
	private int durationMinutes;
	private int durationSeconds;
	private int steps;
	private int calories;
	
	private RatingBar sentimentRating;
	private EditText pulseEditText;
	private Spinner weatherSpinner;
	
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
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.accept_post_training_button:
			save();
			break;
		case R.id.cancel_post_training_button:
			showConfirmDialog();
			break;
		default:
			break;
		}
	}
	
	private void save() {
		if (NetworkUtils.isOnline(getActivity())) {
			Toast.makeText(getActivity(), "Not implemented!", Toast.LENGTH_LONG).show();
		} else  {
			Toast.makeText(getActivity(), "Saved to local database", Toast.LENGTH_LONG).show();
		}
		FragmentManager fragmentManager = getActivity().getFragmentManager();
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		fragmentManager.beginTransaction().replace(R.id.content_frame, welcomeFragment).commit();
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
}
