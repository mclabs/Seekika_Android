package com.seekika.android.app;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;




import com.seekika.android.app.constants.SeekikaConstants;

public class DeviceRegistrar {
	
	private static final String TAG = "DeviceRegistrar";
    public static final String SENDER_ID = "johnwesonga@gmail.com";
    private static final String BASE_URL = "https://seekikanow.appspot.com";


    private static final String REGISTER_URL = SeekikaConstants.SERVER + "/api/register/";
    private static final String UNREGISTER_URL = SeekikaConstants.SERVER + "/api/unregister/";
    
    final Intent updateUIIntent = new Intent("com.seekika.android.app.UPDATE_UI");
    public static final String STATUS_EXTRA = "Status";

    public static final int REGISTERED_STATUS = 1;

    public static final int UNREGISTERED_STATUS = 2;

    public static final int ERROR_STATUS = 3;
    
    private static final String AUTH_URL = BASE_URL + "/_ah/login";
    
	public static void registerWithServer(final Context context,
	          final String deviceRegistrationID){
		 Log.i(TAG,"deviceRegistrationID " + deviceRegistrationID);
		 SharedPreferences settings = Prefs.get(context);
		 String authToken = settings.getString(SeekikaConstants.AUTH_COOKIE, null);
		 String account=settings.getString(SeekikaConstants.ACCOUNT_NAME, null);
		 try{
			 HttpResponse res =sendToServer(context,deviceRegistrationID);
			 if (res.getStatusLine().getStatusCode() == 200) {
				 
		         SharedPreferences.Editor editor = settings.edit();
		         editor.putString(SeekikaConstants.DEVICE_REGISTRATION_ID, deviceRegistrationID);
		         editor.commit();
			 }else{
				 Log.w(TAG, "Registration error " +
	                        String.valueOf(res.getStatusLine().getStatusCode()));
			 }
		 	} catch (Exception e) {
	            Log.w(TAG, "Registration error " + e.getMessage());
	        }
		
		 
         context.sendBroadcast(new Intent(SeekikaConstants.UPDATE_UI_INTENT));
		 }
	
	public static void unregisterWithServer(final Context context,
            final String deviceRegistrationID){
		Log.i(TAG,"deviceRegistrationID " + deviceRegistrationID);
		
		try{
			HttpResponse res =unregisterDevice(context,deviceRegistrationID);
			if(res.getStatusLine().getStatusCode() == 200){
				SharedPreferences settings = Prefs.get(context); 
				SharedPreferences.Editor editor = settings.edit();
				editor.remove(SeekikaConstants.DEVICE_REGISTRATION_ID);
				editor.commit();
			}else{
				
			}
		}catch (Exception e) {
            Log.w(TAG, "Unregistration error " + e.getMessage());
        }
	    context.sendBroadcast(new Intent(SeekikaConstants.UPDATE_UI_INTENT));
	}
	
	private static HttpResponse unregisterDevice(Context context, String deviceRegistrationID
    ) throws Exception {
		
		DefaultHttpClient client = new DefaultHttpClient();
		String continueURL = UNREGISTER_URL + "?devregid=" +deviceRegistrationID;
		URI uri = new URI(continueURL);
		HttpGet method = new HttpGet(uri);
		HttpResponse res = client.execute(method);
		return res;
		
	}
	
	private static HttpResponse sendToServer(Context context, String deviceRegistrationID
          ) throws Exception {
		
		SharedPreferences settings = Prefs.get(context);
		String authToken = settings.getString(SeekikaConstants.AUTH_COOKIE, null);
		String account=settings.getString(SeekikaConstants.ACCOUNT_NAME, null);
		String userKey=settings.getString("userKey",null);
				
		DefaultHttpClient client = new DefaultHttpClient();
		String continueURL = REGISTER_URL + "?devregid=" +deviceRegistrationID;
		
		
		//URI uri = new URI(AUTH_URL + "?continue=" +
          //      URLEncoder.encode(continueURL, "UTF-8") +
            //    "&auth=" + authToken);
		
		  URI uri = new URI(continueURL + "&auth=" + authToken + "&account=" + account + "&userkey=" + userKey);
		  Log.i(TAG,"authToken " + authToken);
		  HttpGet method = new HttpGet(uri);
		  HttpResponse res = client.execute(method);
		  return res;
	}
	
	
}
