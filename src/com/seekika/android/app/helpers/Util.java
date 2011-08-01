package com.seekika.android.app.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class Util {
	public static boolean DataEnabled(){
		//ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return false;
	}
	
	public static boolean SDCardMounted(){
		boolean mounted=false;
		String state = android.os.Environment.getExternalStorageState();
		if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
			mounted=true;
		}
		return mounted;
	}
}
