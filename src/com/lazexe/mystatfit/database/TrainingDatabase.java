package com.lazexe.mystatfit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrainingDatabase extends SQLiteOpenHelper {

	private static final String TAG = TrainingDatabase.class.getName();
	
	private static final String DB_NAME = "training_database.db";
	private static final int DB_VERSION = 1;
	
	private static final String RUN_TABLE_NAME = "run_table";
	
	private String[] runTableFields = 
		{
			"_id", 
			"user_id",
			"start_day",
			"start_month",
			"start_year",
			"start_hour",
			"start_minute",
			"start_second",
			"end_day",
			"end_month",
			"end_year",
			"end_hour",
			"end_minute",
			"end_second",
			"duration_hours",
			"duration_minutes",
			"duration_seconds",
			"steps",
			"pulse",
			"calories",
			"cover",
			"emotion",
			"temperature",
			"weather"
		};
	
 	
	public TrainingDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	private String createRunTableQuery() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE ");
		stringBuilder.append(RUN_TABLE_NAME);
		stringBuilder.append(" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		for (int i = 1; i < runTableFields.length; i++) {
			stringBuilder.append(runTableFields[i]);
			stringBuilder.append(" TEXT");
			if (i != runTableFields.length - 1)
				stringBuilder.append(", ");
		}
		stringBuilder.append(");");
		Log.d(TAG, stringBuilder.toString());
		return stringBuilder.toString();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = createRunTableQuery();
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO
	}

}
