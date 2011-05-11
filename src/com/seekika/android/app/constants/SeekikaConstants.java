package com.seekika.android.app.constants;

import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;

public abstract class SeekikaConstants {
	public static final String SERVER="http://seekikanow.appspot.com";
	public static final String PREFS_NAME = "SeekikaPrefs";
	public static final String LOGIN_URL=SERVER + "/api/auth/";
	public static final String SIGNUP_URL= SERVER + "/api/signup/";
	public static final String PUBLISH_URL=SERVER + "/api/publish/";
	public static final String DEVICE=android.os.Build.DEVICE;
	public static final String PRODUCT=android.os.Build.PRODUCT;
	public static final String ANDROID_ID=Settings.Secure.ANDROID_ID;
	public static final String AUDIO_FILEPATH=Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
	
}
