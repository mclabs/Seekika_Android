package com.seekika.android.app.database;

import com.seekika.android.app.models.Story;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SeekikaDbAdapter {
	
	 	private static final String TAG = "SeekikaDbAdapter";
	 	
	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    
	    private static final String DATABASE_NAME = "Seekika";
	    private static final String STORY_TABLE = "Story";
	    private static final int DATABASE_VERSION = 2;
	    
	    //Story table
	   
	    private static final String STORY_ID="_id";
	    private static final String  TITLE="title";
	    private static final String USER_ID="user_id";
	    private static final String FILENAME="filename";
	    private static final String LAT="lat";
	    private static final String LON="lon";
	    
	    public static final String[] STORY_COLUMNS=new String[]{
	    	STORY_ID,TITLE,USER_ID,FILENAME,LAT,LON
	    };
	    private static final String CREATE_STORY_TABLE="CREATE TABLE IF NOT EXISTS "
	    	+ STORY_TABLE + " (" + STORY_ID + " INTEGER PRIMARY KEY, "
	    	+ USER_ID + " TEXT NOT NULL, "
	    	+ TITLE + " TEXT NOT NULL, "
	    	+ FILENAME + " TEXT NOT NULL, "
	    	+ LAT + " TEXT NOT NULL, "
	    	+ LON + " TEXT NOT NULL, ";
	    
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
		if(mDbHelper instanceof DatabaseHelper){
			
		}
		mDb=mDbHelper.getWritableDatabase();
		return this;
	}
	//Method to create story/recording
	public long addStory(Story story){
		ContentValues initialValues = new ContentValues();
		initialValues.put(STORY_ID, story.getId());
		return mDb.insert(STORY_TABLE, null, initialValues);
	}
	
	public Cursor fetchStories(){
		return null;
	}
}
