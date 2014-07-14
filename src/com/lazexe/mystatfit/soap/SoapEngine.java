package com.lazexe.mystatfit.soap;

import android.os.AsyncTask;

public class SoapEngine {

	private static SoapEngine engine; // Singleton

	private Task task;

	private SoapEngine() {
	}

	public static SoapEngine getInstance() {
		if (engine == null) {
			engine = new SoapEngine();
		}
		return engine;
	}

	public void runCommand(SoapExecutableInterface executable) {
		task = new Task(executable);
		task.execute();
	}

	class Task extends AsyncTask<Void, Void, Void> {

		private SoapExecutableInterface executable;

		public Task(SoapExecutableInterface executable) {
			this.executable = executable;
		}

		@Override
		protected Void doInBackground(Void... params) {
			executable.execute();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			executable.stopShowProgress();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			executable.startShowProgress();
			super.onPreExecute();
		}
		
		

	}
}
