package com.seekika.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import com.google.android.c2dm.C2DMBaseReceiver;

public class C2DMReceiver extends C2DMBaseReceiver {
	private static final String TAG = "DataMessageReceiver";
	public C2DMReceiver() {
		 super(DeviceRegistrar.SENDER_ID);
		
	}
	
	@Override
    public void onRegistered(Context context, String registrationId)
            throws java.io.IOException {
		Log.e("YOUR_TAG", "register occured!!! " + registrationId);
		DeviceRegistrar.registerWithServer(context, registrationId);
    };
    
    @Override
    public void onUnregistered(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        String deviceRegistrationID = prefs.getString("deviceRegistrationID", null);
        DeviceRegistrar.unregisterWithServer(context, deviceRegistrationID);
    }

	@Override
	public void onError(Context context, String error) {
		Log.e("YOUR_TAG", "Error occured!!!");

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		MessageDisplay.displayMessage(context, intent);

	}

}
