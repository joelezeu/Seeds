//
package com.diadementi.seeds.controllers;

import java.io.File;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.models.Campaign;
import com.diadementi.seeds.util.Request;
import com.diadementi.seeds.util.Request.Params;
import com.diadementi.seeds.util.Request.mCallBack;
import com.diadementi.seeds.util.SeedsException;
import com.google.gson.Gson;

public class AddCampaignFragment extends Fragment {
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

	private enum MODE {
		add, edit
	}

	private MODE mode = MODE.add;
	protected String url = UrlLink.add;
	Campaign c;
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences shared;
	String apiKey;
	int categoryId;

	public AddCampaignFragment() {
		setHasOptionsMenu(true);
	}

	public AddCampaignFragment(String url) {
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
				if (!TextUtils.isEmpty(json)) {
					c = new Gson().fromJson(json, Campaign.class);
					this.mode = MODE.edit;
				} else {
					c = new Campaign();
				}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mode == MODE.add) {
			getActivity().getActionBar().setTitle("Add Campaign");
		} else {
			getActivity().getActionBar().setTitle("Edit Campaign");
		}
		View rootView = inflater.inflate(R.layout.fragment_input, container,
				false);

		spinner = (Spinner) rootView.findViewById(R.id.categorySpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinner_array,
				android.R.layout.simple_spinner_item);

		shared = this.getActivity().getSharedPreferences(PREFS_NAME, 0);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		int catId = 1;
		if (c.getCategory() != null)
			catId = c.getCategory().getId();
		spinner.setSelection(catId - 1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				categoryId = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				categoryId = parent.getSelectedItemPosition() + 1;

			}
		});
		// String json="";
		// json=getActivity().getIntent().getExtras().containsKey("json")?getActivity().getIntent().getStringExtra("json"):null;
		// c=!TextUtils.isEmpty(json)?new Gson().fromJson(json,
		// Campaign.class):new Campaign();
		inputName = (EditText) rootView.findViewById(R.id.inputName);

		inputAmount = (EditText) rootView.findViewById(R.id.inputAmount);
		;
		numOfDays = (EditText) rootView.findViewById(R.id.numOfDays);
		inputArticle = (EditText) rootView.findViewById(R.id.inputArticle);
		selectedPathView = (TextView) rootView.findViewById(R.id.selectedPath);
		imageload = (Button) rootView.findViewById(R.id.imageload);
		imageload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openGallery(SELECT_FILE1);

			}
		});

		// btnStartCampaign = (Button) rootView
		// .findViewById(R.id.btnStartCampaign);
		// btnStartCampaign.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// makeRequest(url);
		// }
		// });

		return rootView;
	}

	/**
	 * @param mode2
	 * @return
	 * @throws NotFoundException
	 */
	public void makeRequest(String url) throws NotFoundException {
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("processing...");

		pDialog.setCancelable(true);
		pDialog.show();
		if (isNetworkAvailable()) {
			// StartCampaign task=new StartCampaign();
			// task.execute(url);
			new Request<Void>(RequestMethod.MultiPartPOST, null,
					new mCallBack<Void>() {

				@Override
				public void done(SeedsException e, Void object) {
					pDialog.dismiss();
					if (e == null) {
						getActivity().finish();
					} else if (e.getCode() > 0) {
						Toast.makeText(getActivity(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					}

				}
			}, new Params() {

				@Override
				public void params(RestClient client) {
					apiKey = shared.getString("api_key", "");
					client.AddHeader("Authorization", apiKey);
					String title = inputName.getText().toString();
					String amount = inputAmount.getText().toString();
					int days;
					try{
						days = Integer.parseInt(numOfDays.getText().toString());
					}catch(NumberFormatException ex){
						days=0;
					}
					String article = inputArticle.getText().toString();
					client.AddParam("title", title);
					client.AddParam("goal", amount);
					client.AddParam("goal_duration",
							Integer.toString(days));
					client.AddParam("category",
							Integer.toString(categoryId));
					client.AddParam("brief", article);
					File image = new File(selectedPath1);
					if (image.isFile()) {
						client.AddFile("photos", image, selectedType);
					}
				}
			}).execute(UrlLink.add);
		} else {
			Toast.makeText(getActivity(), getString(R.string.noConnection),
					Toast.LENGTH_LONG).show();
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
		makeRequest(url);
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
			}

		}
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

}
