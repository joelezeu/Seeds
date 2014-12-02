package com.diadementi.seeds.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codecamp14.seeds.R;
import com.diadementi.seeds.controllers.AddCampaignFragment;
import com.diadementi.seeds.controllers.EditCampaignFragment;
import com.diadementi.seeds.views.ListFragment.Type;

public class Add_EditActivity extends ActionBarActivity {
	Type t = Type.PRI;
	String mode = "add";
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add__edit);
		if (savedInstanceState == null) {
			i = getIntent();
			String type = i.getExtras().containsKey("type") ? i
					.getStringExtra("type") : "PUB";
			t = Type.valueOf(type);
			mode = i.getExtras().containsKey("mode") ? i.getStringExtra("mode")
					: mode;
			if (savedInstanceState == null) {
				mode = i.getExtras().containsKey("mode") ? i
						.getStringExtra("mode") : mode;
				switch (mode) {
				case "add":
					getSupportFragmentManager().beginTransaction()
							.add(R.id.container, new AddCampaignFragment())
							.commit();

					break;
				case "edit":
					getSupportFragmentManager().beginTransaction()
							.add(R.id.container, new EditCampaignFragment())
							.commit();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add__edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add__edit,
					container, false);
			return rootView;
		}
	}
}
