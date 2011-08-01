package com.seekika.android.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.listeners.SignupListener;
import com.seekika.android.app.tasks.SignUpTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity implements SignupListener {
	private static final String TAG = "SignupActivity";
	private boolean mError=false;
	private String mErrorMessage="";
	
	private static final int PROGRESS_DIALOG = 1;
	private static final int ALERT_DIALOG = 2;
	private static final int LOGIN=1;
	
	private String _username;
	private String _name;
	private String _email;
	private String _password;
	//components
	private EditText mUsername;
	private EditText mName;
	private EditText mEmail;
	private EditText mPassword;
	private Button mBtnSignUp;
	private Button mBtnSignIn;
	private ProgressDialog mProgressDialog;
	private AlertDialog mAlertDialog;
	
	private SignUpTask mSignUpTask;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle(getString(R.string.app_name));
        
        //load components
        initComponents();
    }
    
    private void initComponents(){
    	mName=(EditText)findViewById(R.id.name);
    	mUsername=(EditText)findViewById(R.id.username);
    	mEmail=(EditText)findViewById(R.id.email);
    	mPassword=(EditText)findViewById(R.id.password);
    	mBtnSignUp=(Button)findViewById(R.id.btn_create_profile);
    	mBtnSignIn=(Button)findViewById(R.id.btn_sign_in);
    	
    	mBtnSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"Sign up user");
				mError=false;
				//check that name is not empty
				if(TextUtils.isEmpty(mName.getText())){
					mErrorMessage=getString(R.string.empty_profile_name) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mUsername.getText())){
					mErrorMessage+=getString(R.string.empty_profile_username) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mEmail.getText())){
					mErrorMessage+=getString(R.string.empty_profile_email) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mPassword.getText())){
					mErrorMessage+=getString(R.string.empty_profile_password) + "\n";
					mError=true;
				}
				
				if(!mError){
					//save to Seekika Web App
					showDialog(PROGRESS_DIALOG);
					mSignUpTask=new SignUpTask();
					mSignUpTask.applicationContext=Signup.this;
					
					_username=mUsername.getText().toString().trim();
					_name=mName.getText().toString().trim();
					_email=mEmail.getText().toString().trim();
					//password needs to be encrypted
					Encryption enc=new Encryption();
					_password=mPassword.getText().toString().trim();
					
					mSignUpTask.setmSignUpListener(Signup.this);
					String androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
					mSignUpTask.execute(_name,_email,_username,_password,androidId);
					
				}else{
					final Toast t = Toast.makeText(Signup.this, "Error!\n\n" + mErrorMessage,
                            Toast.LENGTH_LONG);
                    t.show();
                    mErrorMessage = "";
				}

				
			}
		});
    	
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"Return user to home screen");
				Intent intent=new Intent(Signup.this,Login.class);
				startActivityForResult(intent,LOGIN);
				setResult(RESULT_OK);
				
			}
		});
      	
    }

    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id){
		case PROGRESS_DIALOG:
			mProgressDialog=new ProgressDialog(this);
			DialogInterface.OnClickListener loadingButtonListener=new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					mSignUpTask.setmSignUpListener(null);
					
				}
			};
			mProgressDialog.setTitle("Signup");
			mProgressDialog.setMessage("Please wait");
			mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
           //mProgressDialog.setButton("Cancel",loadingButtonListener);
			return mProgressDialog;
			
    	}
    	return null;
    }
    
	@Override
	public void signUpComplete(String result) {
		String dialogMessage = null;
        String dialogTitle = null;
        dismissDialog(PROGRESS_DIALOG);
        if(result==null){
        	new AlertDialog.Builder(Signup.this).setTitle("Connection Error").setMessage(getString(R.string.connection_error)).setNeutralButton("Close", null).show();
        }else{
        	try {
        		Log.i(TAG,result);
				JSONObject jsonObj = new JSONObject();
				JSONArray aryJSONStrings = new JSONArray(result);
				int i=0;
				String result_message=aryJSONStrings.getJSONObject(i).getString("message");
				String userKey=aryJSONStrings.getJSONObject(i).getString("key");
				
				Log.i(TAG,"user_key " + userKey);
				Log.i(TAG,"message " + result_message);
				
				if(result_message.equalsIgnoreCase("success")){
					Intent intent=new Intent(Signup.this,Home.class);
	        		intent.putExtra("_username", _username);
	        		intent.putExtra("userKey", userKey);
	        		SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
	        		SharedPreferences.Editor editor = settings.edit();
	        		editor.putString("userKey", userKey);
	        		editor.putString("auth_username",_username);
	        		editor.commit();
	        		
	        		startActivity(intent);
	        		finish();
				}else{
					new AlertDialog.Builder(Signup.this).setTitle("Signup Error").setMessage(getString(R.string.signup_failed)).setNeutralButton("Close", null).show();
				}
        	
        	} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  
        }
		
	}

}
