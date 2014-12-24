//
package com.diadementi.seeds.controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.codecamp14.seeds.R;
import com.diadementi.seeds.helpers.Alert;
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.models.Campaign;
import com.diadementi.seeds.views.UserDetailActivity;
import com.google.gson.Gson;

public class EditCampaignFragment extends Fragment {
	/*
	 * { "id": "1", "title":
	 * "Space Rites: Interactive Installation & Performance Series", "brief":
	 * "Taylor Lee Shepherd's oscilloscopes will translate footsteps, voices and music into responsive visuals in the St. Maurice church."
	 * , "creator": "New Orleans airlift", "goal": "6000", "created":
	 * "2014-08-26 10:55:00", "goal_duration": "2014-09-04", "total_donations":
	 * "0", "total_donors": "0" },
	 */
	Spinner spinner;
	EditText inputName, inputAmount, numOfDays, inputArticle;
	Button btnStartCampaign, imageload;
	ProgressDialog pDialog;
	private static final int SELECT_FILE1 = 1;
	String selectedPath1 = "NONE";
	String selectedType = "None";
	TextView selectedPathView;
	private enum MODE{
		add,edit
	}
	private MODE mode=MODE.add;
	private String urls[]={UrlLink.add,UrlLink.update};
	protected String url=UrlLink.update;
	Campaign c;
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_SUCCESS = "response";
	private static final String TAG_DATA = "data";
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences shared;
	String apiKey;
	int categoryId;

	public EditCampaignFragment() {
		setHasOptionsMenu(true);
	}

	public EditCampaignFragment(String url) {
		setHasOptionsMenu(true);
		this.url = url;
	}

	/*
	 * 
	 * 
	 *
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String json = "";
		json = getActivity().getIntent().getExtras().containsKey("json") ? getActivity()
				.getIntent().getStringExtra("json") : null;
				if(!TextUtils.isEmpty(json)){
					c=new Gson().fromJson(json, Campaign.class);
					this.mode=MODE.edit;
				}else{ 
					c=new Campaign();
				}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mode==MODE.add){
			getActivity().getActionBar().setTitle("Add Campaign");
		}else{
			getActivity().getActionBar().setTitle("Edit");
		}
		View rootView = inflater.inflate(R.layout.fragment_input, container,
				false);

		spinner = (Spinner) rootView.findViewById(R.id.categorySpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinner_array,
				android.R.layout.simple_spinner_item);

		shared=this.getActivity().getSharedPreferences(PREFS_NAME, 0);


		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		int catId=1;
		if (c.getCategory()!=null)
			catId = c.getCategory().getId();
		spinner.setSelection(catId-1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				categoryId=position+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				categoryId=parent.getSelectedItemPosition()+1;
				
			}
		});
		// String json="";
		// json=getActivity().getIntent().getExtras().containsKey("json")?getActivity().getIntent().getStringExtra("json"):null;
		// c=!TextUtils.isEmpty(json)?new Gson().fromJson(json,
		// Campaign.class):new Campaign();
		inputName = (EditText) rootView.findViewById(R.id.inputName);
		inputName.setText(c.getTitle());
		inputAmount = (EditText) rootView.findViewById(R.id.inputAmount);
		inputAmount.setText(Integer.toString(c.getGoal()));
		numOfDays = (EditText) rootView.findViewById(R.id.numOfDays);
		numOfDays.setText(c.getGoalDuration());
		inputArticle = (EditText) rootView.findViewById(R.id.inputArticle);
		inputArticle.setText(c.getDesc());
		selectedPathView = (TextView) rootView.findViewById(R.id.selectedPath);
		selectedPathView.setVisibility(View.INVISIBLE);
		imageload = (Button) rootView.findViewById(R.id.imageload);
		imageload.setText("Change Image");
		imageload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openGallery(SELECT_FILE1);

			}
		});

//		btnStartCampaign = (Button) rootView
//				.findViewById(R.id.btnStartCampaign);
//		btnStartCampaign.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				makeRequest(url);
//			}
//		});

		return rootView;
	}

	/**
	 * @param mode2 
	 * @return 
	 * @throws NotFoundException
	 */
	public void makeRequest(String url) throws NotFoundException {
		
		if (isNetworkAvailable()) {
			StartCampaign task=new StartCampaign(getActivity());
			task.execute(url);
		} else {
			Toast.makeText(getActivity(), getString(R.string.noConnection),Toast.LENGTH_LONG).show();
		}
	}

	/* 
	 * 
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.input, menu);
	}

	/* 
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_accept:
			accept();
			break;
		case R.id.action_cancel:
			getActivity().finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void accept() {
		// TODO Auto-generated method stub

		Log.e("mode",mode.toString());
		switch(mode){
		case edit:
			makeRequest(urls[1]+c.getId());
			break;
		default:
			makeRequest(urls[0]);
			break;		
		}
		
	}

	public void openGallery(int req_code) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivityForResult(
				Intent.createChooser(intent, "Select image to upload"),
				req_code);
	}

	public String[] getFileInfo(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.MIME_TYPE };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		int index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);

		cursor.moveToFirst();
		String info[] = { cursor.getString(column_index),
				cursor.getString(index) };
		// return cursor.getString(column_index);
		cursor.close();
		return info;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			Uri selectedImageUri = data.getData();
			if (requestCode == SELECT_FILE1) {
				String[] selectedinfo = getFileInfo(selectedImageUri);
				selectedPath1 = selectedinfo[0];
				selectedType = selectedinfo[1];
				selectedPathView.setText(selectedPath1);
				selectedPathView.setVisibility(View.VISIBLE);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectMan = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectMan.getActiveNetworkInfo();
		boolean isAvailable = false;
		if (networkinfo != null && networkinfo.isConnected()) {
			isAvailable = true;
		}
		return isAvailable;
	}

	public class StartCampaign extends AsyncTask<String, String, String> {
		boolean respondcheck = false;
		private Activity context;
		private String data;
		
		public StartCampaign(Activity context) {
			// TODO Auto-generated constructor stub
			this.context=context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("processing...");

			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub

			String title = inputName.getText().toString();
			c.setTitle(title);
			String amount = inputAmount.getText().toString();
			c.setGoal(inputAmount.getInputType());
			int days = numOfDays.getInputType();
			String cdate=c.getTimeCreated();
			String closeDate="";
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date mdateObject=format.parse(cdate);
				long time=mdateObject.getTime();
				long now=new Date().getTime();
				closeDate=DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS).toString();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			
			c.setGoalDuration(closeDate);
			String article = inputArticle.getText().toString();
			c.setDesc(article);
			

			//RequestMethod method=mode.equals(MODE.edit)?RequestMethod.PUT2:RequestMethod.MultiPartPOST;
			RequestMethod method=RequestMethod.MultiPartPOST;
			RestClient client = new RestClient(args[0]);
			Log.i("url", args[0]);
			apiKey=shared.getString("api_key", "");
			client.AddHeader("Authorization", apiKey);
			Log.e("api_key",apiKey);

			client.AddParam("title", title);
			client.AddParam("goal", amount);
			client.AddParam("created", c.getTimeCreated());
			client.AddParam("goal_duration", Integer.toString(days));
			client.AddParam("category", Integer.toString(categoryId));
			Log.v("category",Integer.toString(categoryId));
			client.AddParam("brief", article);
			File image = new File(selectedPath1);
			if (image.isFile()) {
				client.AddFile("photos", image, selectedType);
			}
			return requestExecute(client,method);

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

				Log.e("error message from client",client.getErrorMessage()+ client.getResponse());


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
			this.respondcheck=responseTest(result);
			pDialog.dismiss();
			if(this.respondcheck){
				Intent newIntent=new Intent(context,UserDetailActivity.class);
				Log.e("post edit",data);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				newIntent.putExtra("json",data);
				startActivity(newIntent);
				}

		}

		/**
		 * This function checks the JSON's response key
		 *  and give an Alert dialog message of the response message
		 * 
		 * @param string  JSON string from HttpResponse
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
						Toast.makeText(getActivity(), show, Toast.LENGTH_LONG).show();
						break;
					case 1:
						data = json.getString(TAG_DATA);
						JSONObject j=new JSONObject(data);
						Campaign newCamp=Campaign.getInstance(j);
						data=new Gson().toJson(newCamp);
						res=true;
						break;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				show = "Unable to connect, Please try again";
				Alert.showAlert(context, show, title);
			}
			
			return res;
		}

	}

}
