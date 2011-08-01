package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
	public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
    }
}
