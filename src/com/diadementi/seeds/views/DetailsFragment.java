package com.diadementi.seeds.views;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.codecamp14.seeds.R;
import com.diadementi.seeds.helpers.Alert;
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.models.Campaign;
import com.google.gson.Gson;

public class DetailsFragment extends Fragment {
	WebView webView;
	ProgressBar pBar;
	protected String url;
	protected MODE mode;
	public static enum MODE{
		PRI,PUB
	}
	Campaign c;
	Intent prevI;

	boolean respondcheck = false;

	public DetailsFragment(String url) {
		this.url = url;
		setHasOptionsMenu(true);
		this.mode=MODE.PUB;
	}
	public DetailsFragment(String url,MODE mode) {
		this.url = url;
		this.mode=mode;
		setHasOptionsMenu(true);
	}

	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences shared;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		prevI=getActivity().getIntent();
		String json = "";
		json = prevI.getExtras().containsKey("json") ?prevI.getStringExtra("json") : null;
				if(!TextUtils.isEmpty(json))
				c=new Gson().fromJson(json, Campaign.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setHasOptionsMenu(boolean hasMenu) {
		super.setHasOptionsMenu(hasMenu);
	}
	

	/* *
	 * 
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		if(this.mode.equals(MODE.PRI)){
		inflater.inflate(R.menu.detail_priv, menu);		
		}
		else{
			inflater.inflate(R.menu.detail_pub, menu);
		}

	}

	/* 
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId=item.getItemId();
		switch(itemId){
		case R.id.action_share:
			sharePost();
			break;
		case R.id.action_edit:
			editPost();
			break;
		case R.id.action_discard:
			discard();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void discard() {
		// TODO Add more process to discard campaign

		makeRequest(UrlLink.delete(c.getId()));
		Log.e("response check in discard function",""+respondcheck);
		if(respondcheck){

		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
		}
		
	}
	private void editPost() {
		// TODO Auto-generated method stub
		Intent i=new Intent(getActivity(),getActivity().getClass());
		i.putExtras(prevI);
		i.putExtra("mode", "edit");
		startActivity(i);
		
	}
	private void sharePost() {
		// TODO Auto-generated method stub
		Intent shareIntent=new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, url);
		startActivity(shareIntent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_view, container, false);
		webView = (WebView) rootView.findViewById(R.id.itemView);
		pBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		// pBar.setVisibility(View.GONE);
		pBar.setMax(100);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		
		settings.setJavaScriptEnabled(true);
		webView.setWebChromeClient(new MyWebChrome());
		webView.setWebViewClient(new MyWebClient());
		webView.loadUrl(url);
		String title = webView.getTitle();
		getActivity().setTitle(title);
		return rootView;
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		webView.reload();
		super.onResume();
		
	}
	/**
	 * 
	 * @param progress
	 */

	public void setValue(int progress) {
		pBar.setProgress(progress);
	}

	private class MyWebChrome extends WebChromeClient {

		/**
		 * Start of webChromeClient
		 */
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if (!(newProgress == 100))
				setValue(newProgress);
			else
				pBar.setVisibility(View.GONE);

			super.onProgressChanged(view, newProgress);
		}

	}

	private class MyWebClient extends WebViewClient {

		/**
		 * functions for webclient were commented
		 */

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Handling page not found error
			if (errorCode == ERROR_CONNECT) {
				Toast.makeText(getActivity(),
						"Unable to Connect Check Connection,Retry later",
						Toast.LENGTH_LONG).show();
			} else if (errorCode == ERROR_TIMEOUT) {
				Alert.showAlert(getActivity(),
						getString(R.string.noConnection),null);

			}

			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			pBar.setIndeterminate(true);
			pBar.setVisibility(View.VISIBLE);
			pBar.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			pBar.setVisibility(View.GONE);
			pBar.setProgress(100);
			super.onPageFinished(view, url);
		}
	}
	public boolean isNetworkAvailable() {
		ConnectivityManager connectMan=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo=connectMan.getActiveNetworkInfo();
		boolean isAvailable=false;
		if(networkinfo != null&&networkinfo.isConnected()){
			isAvailable=true;
		}
		return isAvailable;
	}
	/**
	 * @param mode2 
	 * @return 
	 * @throws NotFoundException
	 */
	public boolean makeRequest(String url) throws NotFoundException {
		
		boolean requestResult=false;
		if (isNetworkAvailable()) {
			Request task=new Request();
			task.execute(url);

			requestResult=respondcheck;
			Log.i("boolean value of response check",""+requestResult);

		} else {
			Alert.showAlert(getActivity(), getString(R.string.noConnection),
					null);
		}
		return requestResult;
	}
	public class Request extends AsyncTask<String, String, String> {
		private static final String TAG_MESSAGE = "message";
		private static final String TAG_SUCCESS = "response";


		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("processing...");

			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			RestClient client = new RestClient(args[0]);

			shared=getActivity().getSharedPreferences(PREFS_NAME, 0);
			client.AddHeader("Authorization", shared.getString("api_key", null));

			return requestExecute(client,RequestMethod.DELETE);

		}

		/**
		 * @param client
		 * @return
		 */
		public String requestExecute(RestClient client,RequestMethod method) {
			String response;
			try {

				client.Execute(method);
				int responseCode = client.getResponseCode();
				Log.e("response code",""+responseCode);
				if (responseCode == 200 || responseCode == 400
						|| responseCode == 201) {
					response = client.getResponse();
					Log.v("response from server", response);
					return response;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("exception", "this", ex);

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			respondcheck=responseTest(result);
			Log.v("response check in onpost execute",""+respondcheck);


		}

		/**
		 * This function checks the JSON's response key
		 *  and give an Alert dialog message of the response message
		 * 
		 * @param result  JSON string from HttpResponse
		 * @return boolean true if 1 and false if 0 
		 */
		public boolean responseTest(String result) {
			boolean res=false;
			String show = "";
			String title = "";
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject json = new JSONObject(result);
					Log.i("json response", json.toString());
					int responseTest = json.getInt(TAG_SUCCESS);
					switch (responseTest) {
					case 0:
						show = json.getString(TAG_MESSAGE);
						Alert.showAlert(getActivity(), show, title);
						break;
					case 1:
						show = json.getString(TAG_MESSAGE);
						res=true;
						break;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				show = "There was an error, Please try again";
				Alert.showAlert(getActivity(), show, title);
			}
			
			return res;
		}


	}



}
