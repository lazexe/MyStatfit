package com.lazexe.mystatfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static int ID_PREFERENCES = 1010;
	private static int ID_EXIT = 1011;

	private Activity activity;

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;

	private String[] drawerItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;
		drawerItems = getResources().getStringArray(
				R.array.navigation_drawer_items);
		// TODO NavigationDrawer

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.exit));
		builder.setMessage(getString(R.string.sure_exit));
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						activity.finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, ID_PREFERENCES, Menu.NONE,
				getString(R.string.preferences));
		menu.add(Menu.NONE, ID_EXIT, Menu.NONE,
				getString(R.string.exit));
		

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == ID_PREFERENCES) {
			Intent preferencesActivityIntent = new Intent(this, PrefsActivity.class);
			startActivity(preferencesActivityIntent);
		}
		
		if (item.getItemId() == ID_EXIT) {
			this.finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	

}
