package com.seekika.android.app.database;

import com.seekika.android.app.models.Story;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SeekikaDbAdapter {
	
	 	private static final String TAG = "SeekikaDbAdapter";
	 	private static final String CREATE_STORY_TABLE="";
	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    
	    private static final String DATABASE_NAME = "Seekika";
	    private static final String STORY_TABLE = "Story";
	    private static final int DATABASE_VERSION = 2;
	    
	    //Story table
	    private static final String STORY_ID="_id";
	    
	    
	    private Context applicationContext;
	    
	private static class DatabaseHelper extends SQLiteOpenHelper {
		

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STORY_TABLE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG,"Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS story");
            onCreate(db);
			
		}
		
	}
	
	public SeekikaDbAdapter(Context context){
		this.applicationContext=context;
	}
	
	public SeekikaDbAdapter open() throws SQLException{
		mDbHelper=new DatabaseHelper(applicationContext);
		mDb=mDbHelper.getWritableDatabase();
		return this;
	}
	//Method to create story/recording
	public long addStory(Story story){
		ContentValues initialValues = new ContentValues();
		
		return mDb.insert(STORY_TABLE, null, initialValues);
	}
}
