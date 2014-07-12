package com.lazexe.mystatfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static int ID_PREFERENCES = 1010;
	private static int ID_EXIT = 1011;

	private Activity activity;

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private String[] drawerStringItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;
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
			// TODO
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
	
	

}
