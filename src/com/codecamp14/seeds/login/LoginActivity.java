package com.codecamp14.seeds.login;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;
import com.codecamp14.seeds.MainActivity;
import com.codecamp14.seeds.R;
import com.diadementi.seeds.helpers.UrlLink;

public class LoginActivity extends Activity {
	
	protected EditText mEmail;
	protected EditText mPassword;
	protected Button loginButton;
	protected String success="0";
	protected TextView mSignUpTextView;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		mSignUpTextView = (TextView)findViewById(R.id.signUP);
		mSignUpTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
				startActivity(i);
			}
		});
		mEmail = (EditText)findViewById(R.id.usernameField);
		mPassword = (EditText)findViewById(R.id.passwordField);
		loginButton = (Button)findViewById(R.id.button1);
		
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = mEmail.getText().toString();
				String password = mPassword.getText().toString();
				
				email = email.trim();
				password = password.trim();
				
				if(email.isEmpty() || password.isEmpty()){
					AlertDialog.Builder aDialog = new AlertDialog.Builder(LoginActivity.this);
					aDialog.setTitle("Error!!!");
					aDialog.setMessage("Please make sure you input your Username and Password.");
					aDialog.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = aDialog.create();
					dialog.show();
				}
				else {
					//Login
//					ParseUser.logInInBackground(username, password, new LogInCallback(){
//
//						@Override
//						public void done(ParseUser user, ParseException e) {
//							// TODO Auto-generated method stub
//							if(e == null){
//							//Success
//								Intent i = new Intent(LoginActivity.this, MainActivity.class);
//								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//								startActivity(i);
//							}else{
//								AlertDialog.Builder aDialog = new AlertDialog.Builder(LoginActivity.this);
//								aDialog.setTitle("Error!!!");
//								aDialog.setMessage(e.getMessage());
//								aDialog.setPositiveButton(android.R.string.ok, null);
//								AlertDialog dialog = aDialog.create();
//								dialog.show();
//							}
//						}
//						
//					});
					new LoginRequest().execute(email,password);
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	private void onSuccess(JSONObject result) throws JSONException{
		String name=result.getString("name");
		String email=result.getString("email");
		String apiKey=result.getString("apiKey");
		String joined=result.getString("joined");
		//TODO find how to save a session of users credentials
		SharedPreferences shared=getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor=shared.edit();
		editor.putString("name", name);
		editor.putString("email", email);
		editor.putString("api_key", apiKey);
		editor.putString("joined", joined);
		editor.commit();
		
		Intent i = new Intent(LoginActivity.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
	class LoginRequest extends AsyncTask<String, Void, JSONObject>{
		

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			RestClient client =new RestClient(UrlLink.login);
			client.AddParam("email", params[0]);
			client.AddParam("password", params[1]);
			try{
				client.Execute(RequestMethod.POST);
			int code=client.getResponseCode();
			Log.v("Httpcode",""+code);
			if(code==HttpStatus.SC_OK){
				String res=client.getResponse();
				Log.i("response",res);
				JSONObject jsonreponse=new JSONObject(res);
				return jsonreponse;
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO handling response better for the ui thread
			super.onPostExecute(result);
			int responseCode=0;
			try {
				responseCode = result.getInt("response");
				Log.v("responsecode",""+responseCode);
				if(responseCode==1){
					onSuccess(result);
				}else{
					AlertDialog.Builder aDialog = new AlertDialog.Builder(LoginActivity.this);
					aDialog.setTitle("Error!!!");
					aDialog.setMessage(result.getString("message"));
					aDialog.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = aDialog.create();
					dialog.show();
				}
			} catch (JSONException e) {
				// TODO work on error handling better
				e.printStackTrace();
				AlertDialog.Builder aDialog = new AlertDialog.Builder(LoginActivity.this);
				aDialog.setTitle("Sorry!!!");
				aDialog.setMessage("Please try again");
				aDialog.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = aDialog.create();
				dialog.show();
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Unable to connect, Please try again", Toast.LENGTH_LONG).show();
			}

		}
		
		
	}
}
