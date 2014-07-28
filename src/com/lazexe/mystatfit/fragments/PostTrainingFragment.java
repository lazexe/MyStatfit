package com.lazexe.mystatfit.fragments;

import android.app.Fragment;
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

public class PostTrainingFragment extends Fragment implements OnClickListener {

	private RatingBar sentimentRating;
	private EditText pulseEditText;
	private Spinner weatherSpinner;
	
	private Button acceptButton;
	private Button cancelButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post_training, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		sentimentRating = (RatingBar) view.findViewById(R.id.sentiment_ratingbar);
		pulseEditText = (EditText) view.findViewById(R.id.pulse_edittext);
		weatherSpinner = (Spinner) view.findViewById(R.id.weather_spinner);
		acceptButton = (Button) view.findViewById(R.id.accept_post_training_button);
		cancelButton = (Button) view.findViewById(R.id.cancel_post_training_button);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.accept_post_training_button:
			// TODO
			break;
		case R.id.cancel_post_training_button:
			// TODO
			break;
		default:
			Toast.makeText(getActivity(), "Default", Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	
}
