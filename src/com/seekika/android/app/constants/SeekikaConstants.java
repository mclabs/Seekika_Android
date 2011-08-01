package com.seekika.android.app.constants;

import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;

public abstract class SeekikaConstants {
	public static final String SERVER="https://seekikanow.appspot.com";
	public static final String PREFS_NAME = "SeekikaPrefs";
	//Server URLS
	public static final String LOGIN_URL=SERVER + "/api/auth/";
	public static final String SIGNUP_URL= SERVER + "/api/signup/";
	public static final String FEED_URL= SERVER + "/api/feed";
	public static final String PUBLISH_URL=SERVER + "/api/publish/";
	public static final String PROFILE_URL=SERVER + "/api/profile/";
	public static final String STORY_URL=SERVER + "/story/?id=";
	public static final String DELETE_STORY_URL=SERVER + "/api/delete/story/";
	public static final String STORYSEARCH_URL= SERVER + "/api/storysearch";
	public static final String PEOPLESEARCH_URL= SERVER + "/api/peoplesearch";
	
	
	public static final String DEVICE=android.os.Build.DEVICE;
	public static final String PRODUCT=android.os.Build.PRODUCT;
	//public static final String ANDROID_ID=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	public static final String AUDIO_FILEPATH=Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
	public static final String S3BUCKET="s3.seekika.com";
	public final static String consumerKey = "FCjELD7tPGalSVW7bF49A";
    public final static String consumerSecret = "fJ3dnsmEG0GaLeIN4fqF0dZxEziOGzSWjFth80mOro";
    /**
     * Key for auth cookie name in shared preferences.
     */
    public static final String AUTH_COOKIE = "authCookie";

    /**
     * Key for device registration id in shared preferences.
     */
    public static final String DEVICE_REGISTRATION_ID = "deviceRegistrationID";
    public static final String SENDER_ID="johnwesonga@gmail.com";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String UPDATE_UI_INTENT = "com.seekika.android.app.UPDATE_UI";


    
}
