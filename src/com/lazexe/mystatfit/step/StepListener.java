package com.lazexe.mystatfit.step;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepListener implements SensorEventListener {

	private final static String TAG = StepListener.class.getName();
	private SensorManager sensorManager;
	private Context context;

	private long lastTime;
	public static int steps;
	public static int bigSteps;
	private int tempCount;

	public StepListener(Context context) {
		this.context = context;
	}

	public void start() {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		Log.d(TAG, "Started!");
	}

	public void stop() {
		sensorManager.unregisterListener(this);
		Log.d(TAG, "Stopped!");
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// No need
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float accelationSquareRoot = +((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)) - 1.0f;
		long actualTime = System.currentTimeMillis();
		if (actualTime - lastTime > 300) {
			if (accelationSquareRoot < -0.45f) {
				steps++;
				tempCount++;
				if (accelationSquareRoot < -1.0f) {
					bigSteps++;
					Log.d(TAG, String.valueOf(bigSteps));
				}
				if (steps % 50 == 0) {
					steps++;
					Log.d(TAG, String.valueOf(steps));
				}
				if (tempCount == 4) {
					steps = 0;
				}
				lastTime = actualTime;
			}
		}
	}

	public static int getSteps() {
		return steps;
	}

	public static int getBigSteps() {
		return bigSteps;
	}
	
	

}
