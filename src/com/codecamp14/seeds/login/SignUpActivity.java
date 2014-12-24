package com.codecamp14.seeds.login;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.codecamp14.seeds.R;
import com.diadementi.seeds.helpers.Alert;
import com.diadementi.seeds.helpers.UrlLink;

public class SignUpActivity extends Activity {

	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText email;
	protected Button signUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		mUsername = (EditText) findViewById(R.id.SignUpusernameField);
		mPassword = (EditText) findViewById(R.id.SignUPpasswordField);
		email = (EditText) findViewById(R.id.SignUPemailField);
		signUpButton = (Button) findViewById(R.id.SignUPbutton);

		signUpButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String signUPemail = email.getText().toString();

				name = name.trim();
				password = password.trim();
				signUPemail = signUPemail.trim();

				if (name.isEmpty() || password.isEmpty()
						|| signUPemail.isEmpty()) {
					AlertDialog.Builder aDialog = new AlertDialog.Builder(
							SignUpActivity.this);
					aDialog.setTitle("Error!!!");
					aDialog.setMessage("Make sure you input your Username, Password and email");
					aDialog.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = aDialog.create();
					dialog.show();
				} else {
					// ParseUser newUser = new ParseUser();
					// newUser.setUsername(username);
					// newUser.setPassword(password);
					// newUser.setEmail(signUPemail);
					// newUser.signUpInBackground(new SignUpCallback() {
					//
					// @Override
					// public void done(ParseException e) {
					// // TODO Auto-generated method stub
					// if(e == null){
					// //Success
					// Intent i = new Intent(SignUpActivity.this,
					// MainActivity.class);
					// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					// startActivity(i);
					// }else{
					// AlertDialog.Builder aDialog = new
					// AlertDialog.Builder(SignUpActivity.this);
					// aDialog.setTitle("Error!!!");
					// aDialog.setMessage(e.getMessage());
					// aDialog.setPositiveButton(android.R.string.ok, null);
					// AlertDialog dialog = aDialog.create();
					// dialog.show();
					// }
					// }
					// });
					new SignUpRequest().execute(name, signUPemail, password);

				}
			}
		});

	}

	private void onSuccess(JSONObject result) throws JSONException {
		// TODO 
		Alert.showAlert(this, result.getString("message")+"\n Please login to continue", null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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

	class SignUpRequest extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO 
			//Log.e("params",Arrays.toString(params));
			RestClient client = new RestClient(UrlLink.register);
			client.AddParam("name", params[0]);
			client.AddParam("email", params[1]);
			client.AddParam("password", params[2]);
			try {
				client.Execute(RequestMethod.POST);
				int code = client.getResponseCode();
				if (code == HttpStatus.SC_CREATED||code==HttpStatus.SC_BAD_REQUEST) {
					String res = client.getResponse();
					JSONObject jsonreponse = new JSONObject(res);
					return jsonreponse;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * 
		 * 
		 * 
		 */
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO handling response better for the ui thread
			int responseCode = 0;
			//if (result!=null) {
				try {
					responseCode = result.getInt("response");
					if (responseCode == 1) {
						onSuccess(result);
					} else {
						AlertDialog.Builder aDialog = new AlertDialog.Builder(
								SignUpActivity.this);
						aDialog.setTitle("Error!!!");
						aDialog.setMessage(result.getString("message"));
						aDialog.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = aDialog.create();
						dialog.show();
					}
				} catch (JSONException e) {
					// TODO work on error handling better
					e.printStackTrace();
					AlertDialog.Builder aDialog = new AlertDialog.Builder(
							SignUpActivity.this);
					aDialog.setTitle("Sorry!!!");
					aDialog.setMessage("Please try again");
					aDialog.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = aDialog.create();
					dialog.show();
				}
			//} else {
				catch(Exception e){
				Toast.makeText(getApplicationContext(), "Unable to connect, Please try again", Toast.LENGTH_LONG).show();
			}

		}

	}

}
