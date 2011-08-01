package com.seekika.android.app;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;

import com.google.android.c2dm.C2DMessaging;
import com.seekika.android.app.constants.SeekikaConstants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Accounts extends Activity {

    private static final String TAG = "AccountsActivity";
    private static final String AUTH_COOKIE_NAME = "SACSID";
    private int mAccountSelectedPosition = 0;
    private boolean mPendingAuth = false;
    private Context mContext = this;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setTitle(getString(R.string.app_name) + " Notifications");
	        SharedPreferences prefs = Prefs.get(mContext);
	        String deviceRegistrationID = prefs.getString(SeekikaConstants.DEVICE_REGISTRATION_ID, null);
	        if(deviceRegistrationID==null){
	        	 setScreenContent(R.layout.connect);
	        }else{
	        	setScreenContent(R.layout.disconnect);
	        }
	        
	    }
	 
	 @Override
	    protected void onResume() {
	        super.onResume();
	        if (mPendingAuth) {
	            mPendingAuth = false;
	            String regId = C2DMessaging.getRegistrationId(mContext);
	            if (regId != null && !"".equals(regId)) {
	                DeviceRegistrar.registerWithServer(mContext, regId);
	            } else {
	                C2DMessaging.register(mContext, SeekikaConstants.SENDER_ID);
	            }
	        }
	    }

	 
	 private void setScreenContent(int screenId) {
	        setContentView(screenId);
	        switch (screenId) {
	            case R.layout.disconnect:
	                setDisconnectScreenContent();
	                break;
	            case R.layout.connect:
	                setConnectScreenContent();
	                break;
	        }
	    }
	 
	 private void setConnectScreenContent() {
		 List<String> accounts = getGoogleAccounts();
		 if (accounts.size() == 0) {
			 AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	            builder.setMessage(R.string.needs_account);
	            builder.setPositiveButton(R.string.add_account, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
	                }
	            });
	            builder.setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	            });
	            builder.setIcon(android.R.drawable.stat_sys_warning);
	            builder.setTitle(R.string.attention);
	            builder.show();
		 }else{
			 	final ListView listView = (ListView) findViewById(R.id.select_account);
	            listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.account, accounts));
	            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	            listView.setItemChecked(mAccountSelectedPosition, true);

	            final Button connectButton = (Button) findViewById(R.id.connect);
	            connectButton.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {
	                    // Register in the background and terminate the activity
	                    mAccountSelectedPosition = listView.getCheckedItemPosition();
	                    TextView account = (TextView) listView.getChildAt(mAccountSelectedPosition);
	                    register((String) account.getText());
	                    finish();
	                }
	            });

	            final Button exitButton = (Button) findViewById(R.id.exit);
	            exitButton.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {
	                    finish();
	                }
	            });
		 }
	 }
	 
	 private void setDisconnectScreenContent() {
	        final SharedPreferences prefs = Prefs.get(mContext);
	        String accountName = prefs.getString(SeekikaConstants.ACCOUNT_NAME, "error");

	        // Format the disconnect message with the currently connected account
	        // name
	        TextView disconnectText = (TextView) findViewById(R.id.disconnect_text);
	        String message = getResources().getString(R.string.disconnect_text);
	        String formatted = String.format(message, accountName);
	        disconnectText.setText(formatted);

	        Button disconnectButton = (Button) findViewById(R.id.disconnect);
	        disconnectButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                // Delete the current account from shared preferences
	                Editor editor = prefs.edit();
	                editor.putString(SeekikaConstants.AUTH_COOKIE, null);
	                editor.putString(SeekikaConstants.DEVICE_REGISTRATION_ID, null);
	                editor.commit();

	                // Unregister in the background and terminate the activity
	                C2DMessaging.unregister(mContext);
	                finish();
	            }
	        });

	        Button exitButton = (Button) findViewById(R.id.exit);
	        exitButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                finish();
	            }
	        });
	    }
	 
	 private void register(final String accountName) {
		// Store the account name in shared preferences
	        final SharedPreferences prefs = Prefs.get(mContext);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(SeekikaConstants.ACCOUNT_NAME, accountName);
	        editor.putString(SeekikaConstants.AUTH_COOKIE, null);
	        editor.commit();

	        // Obtain an auth token and register
	        AccountManager mgr = AccountManager.get(mContext);
	        Account[] accts = mgr.getAccountsByType("com.google");
	        for (Account acct : accts) {
	            if (acct.name.equals(accountName)) {
	            	mgr.getAuthToken(acct, "ah", null, this, new AccountManagerCallback<Bundle>() {
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Bundle authTokenBundle = future.getResult();
                                String authToken = authTokenBundle
                                        .get(AccountManager.KEY_AUTHTOKEN).toString();
                                String authCookie = getAuthCookie(authToken);
                                Log.i(TAG,"AUTH_COOKIE_NAME " + authCookie);
                                prefs.edit().putString(SeekikaConstants.AUTH_COOKIE, authCookie).commit();

                                C2DMessaging.register(mContext, SeekikaConstants.SENDER_ID);
                            } catch (AuthenticatorException e) {
                                Log.w(TAG, "Got AuthenticatorException " + e);
                                Log.w(TAG, Log.getStackTraceString(e));
                            } catch (IOException e) {
                                Log.w(TAG, "Got IOException " + Log.getStackTraceString(e));
                                Log.w(TAG, Log.getStackTraceString(e));
                            } catch (OperationCanceledException e) {
                                Log.w(TAG, "Got OperationCanceledException " + e);
                                Log.w(TAG, Log.getStackTraceString(e));
                            }
                        }
                    }, null);
	            }
	        }
	}
	
	 /**
	     * Retrieves the authorization cookie associated with the given token. This
	     * method should only be used when running against a production appengine
	     * backend (as opposed to a dev mode server).
	     */
	    private String getAuthCookie(String authToken) {
	        try {
	            // Get SACSID cookie
	            DefaultHttpClient client = new DefaultHttpClient();
	            String continueURL = SeekikaConstants.SERVER;
	            URI uri = new URI(SeekikaConstants.SERVER + "/_ah/login?continue="
	                    + URLEncoder.encode(continueURL, "UTF-8") + "&auth=" + authToken);
	            Log.i(TAG,"authToken  " + authToken);
	            HttpGet method = new HttpGet(uri);
	            final HttpParams getParams = new BasicHttpParams();
	            HttpClientParams.setRedirecting(getParams, false);
	            method.setParams(getParams);

	            HttpResponse res = client.execute(method);
	            Header[] headers = res.getHeaders("Set-Cookie");
	            if (res.getStatusLine().getStatusCode() != 302 || headers.length == 0) {
	                return null;
	            }
	            
	            for (Cookie cookie : client.getCookieStore().getCookies()) {
	            	Log.i(TAG,"cookie value " + cookie.getValue());
	                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
	                	 
	                    return AUTH_COOKIE_NAME + "=" + cookie.getValue();
	                   
	                }
	            }
	        } catch (IOException e) {
	            Log.w(TAG, "Got IOException " + e);
	            Log.w(TAG, Log.getStackTraceString(e));
	        } catch (URISyntaxException e) {
	            Log.w(TAG, "Got URISyntaxException " + e);
	            Log.w(TAG, Log.getStackTraceString(e));
	        }

	        return null;
	    }
	 
	 private List<String> getGoogleAccounts() {
	        ArrayList<String> result = new ArrayList<String>();
	        Account[] accounts = AccountManager.get(mContext).getAccounts();
	        for (Account account : accounts) {
	            if (account.type.equals("com.google")) {
	                result.add(account.name);
	            }
	        }

	        return result;
	    }
}
