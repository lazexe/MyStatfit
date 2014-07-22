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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lazexe.mystatfit.R;
import com.lazexe.mystatfit.R.array;
import com.lazexe.mystatfit.R.drawable;
import com.lazexe.mystatfit.R.id;
import com.lazexe.mystatfit.R.layout;
import com.lazexe.mystatfit.R.menu;
import com.lazexe.mystatfit.R.string;
import com.lazexe.mystatfit.adapters.DrawerItem;
import com.lazexe.mystatfit.adapters.NavigationDrawerAdapter;
import com.lazexe.mystatfit.fragments.RunFragment;
import com.lazexe.mystatfit.fragments.WelcomeFragment;
import com.lazexe.mystatfit.spinnernavigation.SpinnerNavigationItem;
import com.lazexe.mystatfit.spinnernavigation.TitleNavigationAdapter;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getName();

	private MainActivity activity;
	private FrameLayout contentFrame;
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
		fragmentTransaction = getFragmentManager().beginTransaction();
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		fragmentTransaction.replace(R.id.content_frame, welcomeFragment);
		fragmentTransaction.commit();
	}

	private void initControls() {
		activity = this;
		contentFrame = (FrameLayout) findViewById(R.id.content_frame);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		drawerLayout.setDrawerShadow(
				getResources().getDrawable(R.drawable.drawer_shadow),
				GravityCompat.START);
		dataList = new ArrayList<DrawerItem>();
		dataList.add(new DrawerItem(getString(R.string.trainings)));
		dataList.add(new DrawerItem(getString(R.string.run),
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
			if (dataList.get(position).getTitle() == null) {
				selectItem(position);
			}
		}
	}

	private void selectItem(int position) {
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
		getActionBar().setTitle(dataList.get(position).getItemName());
		String item = dataList.get(position).getItemName();

		if (item.equals(getString(R.string.run))) {
			fragmentTransaction = getFragmentManager().beginTransaction();
			RunFragment runFragment = new RunFragment();
			fragmentTransaction.replace(R.id.content_frame, runFragment);
			fragmentTransaction.commit();
		} else if (item.equals(getString(R.string.settings))) {
			Intent preferencesActivityIntent = new Intent(this,
					PrefsActivity.class);
			startActivityForResult(preferencesActivityIntent, 11);
		} else if (item.equals(getString(R.string.quit))) {
			this.finish();
		} else {
			fragmentTransaction = getFragmentManager().beginTransaction();
			WelcomeFragment welcomeFragment = new WelcomeFragment();
			fragmentTransaction.replace(R.id.content_frame, welcomeFragment);
			fragmentTransaction.commit();

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
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.main_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item))
			return true;
		
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.finish();
		}
	}

}
