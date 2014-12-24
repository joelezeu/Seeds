package com.codecamp14.seeds;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.util.Request;
import com.diadementi.seeds.util.Request.Params;
import com.diadementi.seeds.util.Request.mCallBack;
import com.diadementi.seeds.util.SeedsException;

public class SettingsActivity extends ActionBarActivity {
	Button b;
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences shared;
	String apiKey;
	String email;
	TextView indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		shared = getSharedPreferences(PREFS_NAME, 0);
		apiKey = shared.getString("api_Key", null);
		email = shared.getString("email", null);
		b = (Button) findViewById(R.id.ok);
		indicator = (TextView) findViewById(R.id.indicator);
		indicator.setVisibility(View.INVISIBLE);

		// if (savedInstanceState == null) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.container, new PlaceholderFragment()).commit();
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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

	public void withdraw(View view) {
		indicator.setVisibility(View.VISIBLE);
		new Request<Void>(RequestMethod.POST, null, new mCallBack<Void>() {

			@Override
			public void done(SeedsException e, Void object) {
				// TODO Auto-generated method stub
				if (e == null) {
					indicator
							.setText("Your request has been sent and you will recieve an email shortly");
				} else {
					indicator
							.setText("A problem occured while processing your request,Please try again after some time");
				}

			}
		}, new Params() {

			@Override
			public void params(RestClient client) {
				client.AddHeader("Authorization", apiKey);
				client.AddParam("email", email);
			}
		}).execute(UrlLink.withraw);

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
			setHasOptionsMenu(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);

			return rootView;
		}

	}
}
