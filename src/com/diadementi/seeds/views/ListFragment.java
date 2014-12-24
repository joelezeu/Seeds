/**
 * 
 */
package com.diadementi.seeds.views;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.codecamp14.seeds.R;
import com.codecamp14.seeds.SettingsActivity;
import com.codecamp14.seeds.login.LoginActivity;
import com.diadementi.seeds.helpers.Alert;
import com.diadementi.seeds.helpers.CampaignAdapter;
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.models.Campaign;
import com.google.gson.Gson;

/**
 * @author user pc
 * 
 */
public class ListFragment extends Fragment {
	protected ListView list;
	protected ProgressBar pBar;
	protected ArrayList<Campaign> data;
	protected CampaignAdapter adapter;
	protected String url;
	private Type type;
	protected SwipeRefreshLayout mSwipeRefreshLayout;

	public static enum Type {
		PRI, PUB;
	}

	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences shared;
	String apiKey;

	/**
	 * 
	 */
	ListFragment() {

		this(UrlLink.campaigns);

	}

	public ListFragment(String url) {
		// TODO get adapter at instantiation or onCreate
		data = new ArrayList<Campaign>();
		setHasOptionsMenu(true);
		this.url = url;
		this.type = Type.PUB;
	}

	public ListFragment(String url, Type type) {
		// TODO get adapter at instantiation or onCreate
		data = new ArrayList<Campaign>();
		setHasOptionsMenu(true);
		this.url = url;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO
		shared = getActivity().getSharedPreferences(PREFS_NAME, 0);
		apiKey = shared.getString("api_key", null);
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Bundle b=getArguments();
		// String title=b.containsKey("title")?b.getString("title"):null;
		// if(title!=null){
		// getActivity().setTitle(title);
		// }
		final View rootView = inflater.inflate(R.layout.fragment_main,
				container, false);

		list = (ListView) rootView.findViewById(R.id.campaignList);
		TextView emptyTextView = (TextView) rootView
				.findViewById(android.R.id.empty);
		list.setEmptyView(emptyTextView);
		pBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		pBar.setVisibility(View.INVISIBLE);
		adapter = new CampaignAdapter(getActivity(), R.layout.list_row, data);

		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Campaign c = (Campaign) parent.getItemAtPosition(position);
				String url = UrlLink.getCampaignView(c.getId());
				String json = new Gson().toJson(c);
				if (type == Type.PUB) {
					Intent i = new Intent(getActivity(), DetailActivity.class);
					i.putExtra("json", json);
					i.putExtra("url", url);
					startActivity(i);
				} else {
					Intent i = new Intent(getActivity(),
							UserDetailActivity.class);
					i.putExtra("json", json);
					i.putExtra("url", url);
					startActivity(i);
				}

			}
		});
		makeRequest(url);
		return rootView;

	}

	protected OnRefreshListener mOnRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			makeRequest(url);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		makeRequest(url);
		super.onResume();
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectMan = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectMan.getActiveNetworkInfo();
		boolean isAvailable = false;
		if (networkinfo != null && networkinfo.isConnected()) {
			isAvailable = true;
		}
		return isAvailable;
	}

	/**
	 * @throws NotFoundException
	 */
	public void makeRequest(String url) throws NotFoundException {
		if (isNetworkAvailable()) {
			new CampaignFetch().execute(url);
		} else {
			Alert.showAlert(getActivity(), getString(R.string.noConnection),
					null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			Alert.showAlert(getActivity(), "I got an answer", null);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/* 
	 * 
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		if (this.type.equals(Type.PRI)) {
			inflater.inflate(R.menu.list_pri, menu);
		} else {
			inflater.inflate(R.menu.list, menu);
		}

	}

	/* 
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_new:
			add();
			break;
		case R.id.action_edit:
			edit();
		case R.id.settings:
			openSettings();
		}
		return super.onOptionsItemSelected(item);
	}

	private void edit() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), Add_EditActivity.class);
		intent.putExtra("mode", "edit");
		startActivity(intent);
	}

	private void add() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), Add_EditActivity.class);
		intent.putExtra("mode", "add");
		startActivity(intent);
	}

	public void openSettings() {
		Intent settingsIntent = new Intent(getActivity(),
				SettingsActivity.class);
		startActivity(settingsIntent);
	}

	class CampaignFetch extends AsyncTask<String, Void, String> {

		private RestClient dami;
		private String text;
		private ArrayList<Campaign> dataS;
		private String response = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pBar.setVisibility(View.VISIBLE);
			pBar.setIndeterminate(true);
			dataS = new ArrayList<Campaign>();
		}

		@Override
		protected String doInBackground(String... url) {
			// TODO Auto-generated method stub

			return request(url);

		}

		/**
		 * @param url
		 * @return
		 */
		private String request(String... url) {
			dami = new RestClient(url[0]);
			try {
				dami.AddHeader("Authorization", apiKey);
				dami.setTimeOut(30000);
				dami.Execute(RequestMethod.GET);
				int code = dami.getResponseCode();
				if (code == 400 || code == 401) {
					Intent i = new Intent(getActivity(), LoginActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
				}
				;
				text = dami.getResponse();
				JSONParser(text);
				return response = "successful";
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException ex) {

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		/**
		 * @throws JSONException
		 */
		public void JSONParser(String response) throws JSONException {
			JSONObject mainObject = new JSONObject(response);
			JSONArray dataObject = mainObject.getJSONArray("data");
			for (int i = 0; i < dataObject.length(); i++) {
				dataS.add(Campaign.getInstance((JSONObject) dataObject.get(i)));
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// adapter.addAll(dataS);
			if (!TextUtils.isEmpty(result) && result != null) {
				if (!dataS.isEmpty())
					adapter.refill(dataS);
				else {
					TextView empty = (TextView) list.getEmptyView();
					empty.setText(getString(R.string.no_item));
				}
			} else {
				result = "Unable to reach server";
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
				TextView empty = (TextView) list.getEmptyView();
				empty.setText(getString(R.string.no_item));
			}
			if (mSwipeRefreshLayout.isRefreshing())
				mSwipeRefreshLayout.setRefreshing(false);
			pBar.setVisibility(View.GONE);
			// Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
		}
	}

}
