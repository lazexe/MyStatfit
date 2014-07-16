package com.lazexe.mystatfit;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.lazexe.mystatfit.fragments.RunFragment;
import com.lazexe.mystatfit.fragments.WelcomeFragment;
import com.lazexe.mystatfit.spinnernavigation.SpinnerNavigationItem;
import com.lazexe.mystatfit.spinnernavigation.TitleNavigationAdapter;

public class MainActivity extends Activity implements OnNavigationListener {

	private static final String TAG = MainActivity.class.getName();

	private static int ID_PREFERENCES = 1010;
	private static int ID_EXIT = 1011;

	private MainActivity activity;
	private FrameLayout contentFrame;
	private FragmentTransaction fragmentTransaction;

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private String[] drawerStringItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initControls();
		fragmentTransaction = getFragmentManager().beginTransaction(); 
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		fragmentTransaction.replace(R.id.content_frame, welcomeFragment);
		fragmentTransaction.commit();
	}

	private void initControls() {
		activity = this;
		contentFrame = (FrameLayout) findViewById(R.id.content_frame);
		drawerStringItems = getResources().getStringArray(
				R.array.navigation_drawer_items);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerLayout.setDrawerShadow(
				getResources().getDrawable(R.drawable.drawer_shadow),
				GravityCompat.START);
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawet_list_item, drawerStringItems));
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
				getActionBar().setTitle("");
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
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			drawerList.setItemChecked(position, true);
			drawerLayout.closeDrawer(drawerList);
			String[] titles = getResources().getStringArray(
					R.array.navigation_drawer_items);
			String newTitle = titles[position];
			getActionBar().setTitle(newTitle);

			switch (position) {
			case 0: // Trainings
				getActionBar().setDisplayShowTitleEnabled(false);
				getActionBar()
						.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				ArrayList<SpinnerNavigationItem> items = new ArrayList<SpinnerNavigationItem>();
				String[] trainingTypes = getResources().getStringArray(R.array.training_type);
				for (int i = 0; i < trainingTypes.length; i++) {
					items.add(new SpinnerNavigationItem(trainingTypes[i], R.drawable.ic_launcher));
				}
				
				TitleNavigationAdapter adapter = new TitleNavigationAdapter(
						activity, items);
				getActionBar().setListNavigationCallbacks(adapter, activity);
				break;

			default:
				getActionBar().setNavigationMode(
						ActionBar.NAVIGATION_MODE_STANDARD);
				break;
			}
		}
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
		menu.add(Menu.NONE, ID_EXIT, Menu.NONE, getString(R.string.exit));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item))
			return true;

		if (item.getItemId() == ID_PREFERENCES) {
			Intent preferencesActivityIntent = new Intent(this,
					PrefsActivity.class);
			startActivityForResult(preferencesActivityIntent, 11);
		}

		if (item.getItemId() == ID_EXIT) {
			this.finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.finish();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Log.d(TAG, String.valueOf(itemPosition));
		fragmentTransaction = getFragmentManager().beginTransaction();
		if (itemPosition == 0) {
			RunFragment runFragment = new RunFragment();
			fragmentTransaction.replace(R.id.content_frame, runFragment);
			fragmentTransaction.commit();
		}
		return false;
	}

}
