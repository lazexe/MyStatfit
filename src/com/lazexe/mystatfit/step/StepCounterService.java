package com.lazexe.mystatfit.step;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class StepCounterService extends Service {

	public class LocalBinder extends Binder {
		public void gimmeHandler(Handler handler) {
			clientHandler = handler;
		}
	}
	
	private static final String TAG = StepCounterService.class.getName();
	private StepListener stepListener;
	private Handler clientHandler;
	private final IBinder binder = new LocalBinder();
	private final Handler serviceHandler= new Handler();
	private final Runnable updateSteps = new Runnable() {
		
		@Override
		public void run() {
			Log.i(TAG, "Update steps is running");
			int step = StepListener.getSteps();
			int bigSteps = StepListener.getBigSteps();
			if (clientHandler != null) {
				Log.i(TAG, "Update steps.");
				Message message = Message.obtain();
				message.arg1 = step;
				message.arg2 = bigSteps;
				clientHandler.sendMessage(message);
			}
			serviceHandler.postDelayed(this, 1000);
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		clientHandler = null;
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		stepListener = new StepListener(this);
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "onStart");
		start();
		return START_STICKY;
	}
	
	private void start() {
		stepListener.start();
		serviceHandler.post(updateSteps);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestoy");
		super.onDestroy();
		serviceHandler.removeCallbacks(updateSteps);
		stepListener.stop();
	}

	
	
	
	
}
