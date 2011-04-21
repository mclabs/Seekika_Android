package com.seekika.android.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SeekikaDbAdapter {
	 	private static final String TAG = "SeekikaDbAdapter";
	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    
	    private static final String DATABASE_NAME = "Seekika";
	    private static final String DATABASE_TABLE = "stories";
	    private static final int DATABASE_VERSION = 2;
	    
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG,"Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS stories");
            onCreate(db);
			
		}
		
	}
}
