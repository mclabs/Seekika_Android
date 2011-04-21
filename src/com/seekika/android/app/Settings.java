/**
 * Preferences Activity
 */
package com.seekika.android.app;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
	
	 @Override
	    protected void onPause() {
	        super.onPause();
	        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
	            this);
	    }
	 @Override
	    protected void onResume() {
	        super.onResume();
	        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub

	}

}
