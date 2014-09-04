package com.codecamp14.seeds.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codecamp14.seeds.MainActivity;
import com.codecamp14.seeds.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	
	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText email;
	protected Button signUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		mUsername = (EditText)findViewById(R.id.SignUpusernameField);
		mPassword = (EditText)findViewById(R.id.SignUPpasswordField);
		email = (EditText)findViewById(R.id.SignUPemailField);
		signUpButton = (Button)findViewById(R.id.SignUPbutton);

		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String signUPemail = email.getText().toString();
				
				username = username.trim();
				password = password.trim();
				signUPemail = signUPemail.trim();
				
				if(username.isEmpty() || password.isEmpty() || signUPemail.isEmpty()){
					AlertDialog.Builder aDialog = new AlertDialog.Builder(SignUpActivity.this);
					aDialog.setTitle("Error!!!");
					aDialog.setMessage("Make sure you input your Username, Password and email");
					aDialog.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = aDialog.create();
					dialog.show();
				}
				else {
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(signUPemail);
					newUser.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e == null){
								//Success
								Intent i = new Intent(SignUpActivity.this, MainActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
							}else{
								AlertDialog.Builder aDialog = new AlertDialog.Builder(SignUpActivity.this);
								aDialog.setTitle("Error!!!");
								aDialog.setMessage(e.getMessage());
								aDialog.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = aDialog.create();
								dialog.show();
							}
						}
					});
				}
			}
		});
		
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
}
