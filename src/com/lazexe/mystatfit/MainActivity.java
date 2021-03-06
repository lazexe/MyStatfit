package com.lazexe.mystatfit;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lazexe.mystatfit.adapters.DrawerItem;
import com.lazexe.mystatfit.adapters.NavigationDrawerAdapter;
import com.lazexe.mystatfit.fragments.WalkingFragment;
import com.lazexe.mystatfit.fragments.WelcomeFragment;
import com.lazexe.mystatfit.utils.Constants;
import com.lazexe.mystatfit.utils.PreferencesUtils;

public class MainActivity extends Activity {

	private MainActivity activity;
	private FragmentTransaction fragmentTransaction;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private ArrayList<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initControls();
		setContentFragment(new WelcomeFragment());
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.edit().putBoolean(Constants.PREF_IS_TRAINING_RUN_KEY, false).commit();
	}

	private void initControls() {
		activity = this;
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerLayout.setDrawerShadow(
				getResources().getDrawable(R.drawable.drawer_shadow),
				GravityCompat.START);
		dataList = new ArrayList<DrawerItem>();
		dataList.add(new DrawerItem(getString(R.string.trainings)));
		dataList.add(new DrawerItem(getString(R.string.running),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.walking),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.cycle_racing),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.mountain_biking),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.hiking),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.downhill),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.cross_country_skiing),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.snowboarding),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.skating),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.swimming),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.riding_in_a_wheelchair),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.rowing),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.nordic_walking),
				R.drawable.ic_launcher));
		dataList.add(new DrawerItem(getString(R.string.other)));
		dataList.add(new DrawerItem(getString(R.string.settings),
				android.R.drawable.ic_menu_preferences));
		dataList.add(new DrawerItem(getString(R.string.quit),
				android.R.drawable.ic_lock_power_off));
		NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(this,
				R.layout.drawet_list_item, dataList);
		drawerList.setAdapter(adapter);
		drawerList.setOnItemClickListener(new DrawerItemClickListener());

		if (getActionBar() != null) {
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();
			}

		};

		drawerLayout.setDrawerListener(drawerToggle);
	}

	@Override
	public void setTitle(CharSequence title) {
		if (getActionBar() != null)
			getActionBar().setTitle(title);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			if (dataList.get(position).getTitle() == null) {
				
				boolean isTrainingGoes = PreferencesUtils.isUserTrains(activity);
				if (isTrainingGoes) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle(getString(R.string.sure_exit));
					builder.setMessage(getString(R.string.sure_close_training));
					builder.setPositiveButton(getString(android.R.string.yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									selectItem(position);
									dialog.cancel();
								}
							});
					builder.setNegativeButton(getString(android.R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
					builder.setCancelable(false);
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					selectItem(position);
				}
			}
		}
	}

	private void selectItem(int position) {
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
		getActionBar().setTitle(dataList.get(position).getItemName());
		String item = dataList.get(position).getItemName();
		if (item.equals(getString(R.string.walking))) {
			setContentFragment(new WalkingFragment());
		} else if (item.equals(getString(R.string.settings))) {
			Intent preferencesActivityIntent = new Intent(this,
					PrefsActivity.class);
			startActivityForResult(preferencesActivityIntent, 11);
		} else if (item.equals(getString(R.string.quit))) {
			this.finish();
		} else {
			setContentFragment(new WelcomeFragment());
		}
	}
	
	private void setContentFragment(Fragment contentFragment) {
		fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, contentFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onBackPressed() {
		showExitDialog();
	}

	private void showExitDialog() {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) return true;
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) this.finish();
	}

}
