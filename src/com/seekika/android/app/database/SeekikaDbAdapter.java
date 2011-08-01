package com.seekika.android.app.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.seekika.android.app.helpers.FileUtils;
import com.seekika.android.app.models.Story;
import com.seekika.android.app.models.User;

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
	    private static final String USER_TABLE="User";
	    private static final int DATABASE_VERSION = 4;
	    
	    //User table
	    public static final String USERID="_id";
	    public static final String USER_KEY="user_key";
	    public static final String  SECRET="secret";
	    public static final String TOKEN="token";
	    
	    //Story table
	   
	    public static final String STORY_ID="_id";
	    public static final String  TITLE="title";
	    public static final String USER_ID="user_id";
	    public static final String FILENAME="filename";
	    public static final String LAT="lat";
	    public static final String LON="lon";
	    public static final String DESCRIPTION="description";
	    public static final String STATUS="status";
	    public static final String CREATED_ON="created_on";
	    public static final String FILE_SIZE="file_size";
	    public static final String UPLOADED="uploaded";
	    public static final String STORYKEY="storykey";
	    
	    public static final String[] STORY_COLUMNS=new String[]{
	    	STORY_ID,TITLE,USER_ID,FILENAME,LAT,LON,STATUS,DESCRIPTION,CREATED_ON,UPLOADED
	    };
	    
	    private static final String CREATE_USER_TABLE="CREATE TABLE IF NOT EXISTS "
	    	+ USER_TABLE + "(" + USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	    	+ USER_KEY + " TEXT NULL, "
	    	+ SECRET + " TEXT  NULL, "
	    	+ TOKEN + " TEXT  NULL)";
	    	
	    
	    
	    private static final String CREATE_STORY_TABLE="CREATE TABLE IF NOT EXISTS "
	    	+ STORY_TABLE + "(" + STORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	    	+ USER_ID + " TEXT NULL, "
	    	+ TITLE + " TEXT  NULL, "
	    	+ FILENAME + " TEXT  NULL, "
	    	+ LAT + " TEXT  NULL, "
	    	+ LON + " TEXT  NULL, "
	    	+ STATUS + " TEXT  NULL, "
	    	+ CREATED_ON + " TEXT  NULL, "
	    	+ FILE_SIZE + " TEXT  NULL, "
	    	+ UPLOADED + " INTEGER  NULL, "
	    	+ STORYKEY + " TEXT  NULL, "
	    	+ DESCRIPTION + " TEXT NULL)";
	    
	    private Context applicationContext;
	    
	private static class DatabaseHelper extends SQLiteOpenHelper {
		

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STORY_TABLE);
			db.execSQL(CREATE_USER_TABLE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG,"Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			List<String> storiesColumns;
			List<String> userColumns;
			//db.execSQL("DROP TABLE IF EXISTS " + STORY_TABLE);
			//db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
			db.execSQL(CREATE_STORY_TABLE);
			storiesColumns=SeekikaDbAdapter.getColumns(db, STORY_TABLE);
			db.execSQL("ALTER TABLE " + STORY_TABLE + " RENAME TO temp_" + STORY_TABLE);
            db.execSQL(CREATE_STORY_TABLE);
            storiesColumns.retainAll(SeekikaDbAdapter.getColumns(db, STORY_TABLE));
            String storyCols = SeekikaDbAdapter.join(storiesColumns, ",");
            db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", STORY_TABLE,
            		storyCols, storyCols, STORY_TABLE));
            db.execSQL("DROP TABLE IF EXISTS temp_" + STORY_TABLE);
            
            db.execSQL(CREATE_USER_TABLE);
            userColumns=SeekikaDbAdapter.getColumns(db, USER_TABLE);
			db.execSQL("ALTER TABLE " + USER_TABLE + " RENAME TO temp_" + USER_TABLE);
            db.execSQL(CREATE_USER_TABLE);
            userColumns.retainAll(SeekikaDbAdapter.getColumns(db, USER_TABLE));
            String userCols = SeekikaDbAdapter.join(userColumns, ",");
            db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", USER_TABLE,
            		userCols, userCols, USER_TABLE));
            db.execSQL("DROP TABLE IF EXISTS temp_" + USER_TABLE);
			
			
            onCreate(db);
			
		}
		
	}
	
	public SeekikaDbAdapter(Context context){
		this.applicationContext=context;
	}
	
	public SeekikaDbAdapter open() throws SQLException{
		mDbHelper=new DatabaseHelper(applicationContext);
		if(mDbHelper instanceof DatabaseHelper){
			mDb=mDbHelper.getWritableDatabase();
		}
		
		return this;
	}
	
	public void close(){
		if(mDbHelper!=null){
			
			mDbHelper.close();
			mDbHelper=null;
		}
		
	}
	//Method to create story/recording
	public long addStory(Story story){
		ContentValues initialValues = new ContentValues();
		//initialValues.put(STORY_ID, story.getId());
		initialValues.put(TITLE, story.getTitle());
		initialValues.put(USER_ID, story.getUserKey());
		initialValues.put(DESCRIPTION, story.getDescription());
		initialValues.put(LAT, story.getLat());
		initialValues.put(LON, story.getLon());
		initialValues.put(STATUS, story.getStatus());
		initialValues.put(FILENAME, story.getFileName());
		initialValues.put(CREATED_ON, FileUtils.now("dd MMMMM yyyy"));
		initialValues.put(UPLOADED, story.getUploaded());
		initialValues.put(STORYKEY, story.getStoryKey());
		return mDb.insert(STORY_TABLE, null, initialValues);
	}
	
	public long adduser(User user){
		ContentValues initialValues = new ContentValues();
		initialValues.put(USER_KEY, user.getUserKey());
		initialValues.put(TOKEN, user.getToken());
		initialValues.put(SECRET, user.getSecret());
		return mDb.insert(USER_TABLE, null, initialValues);
	}
	
	public Cursor fetchStories(){
		String sql="SELECT * FROM " + STORY_TABLE + " WHERE " + USER_ID + "=?";
		return mDb.query(STORY_TABLE, STORY_COLUMNS, null, null, null, null, STORY_ID + " ASC");
		
	}
	
	public Cursor fetchStories(String id){
		String sql="SELECT * FROM " + STORY_TABLE + " WHERE " + USER_ID + "=? ORDER BY _ID DESC";
		return mDb.rawQuery(sql, new String[]{
				id
		});
	}
	
	public void deleteStory(long storyId){
		
		mDb.delete(STORY_TABLE, "_id=" + storyId, null);
	}
	
	public Cursor fetchTokens(String userKey){
		String sql="SELECT * FROM " + USER_TABLE + " WHERE " + USER_KEY + "=?";
		return mDb.rawQuery(sql, new String[]{
				userKey
		});
		
	}
	
	public void updateStoryStatus(long storyId,int uploaded,String storyKey){
		ContentValues args = new ContentValues();
		args.put("uploaded", uploaded);
		args.put("status", "Public");
		args.put("storykey", storyKey);
		mDb.update(STORY_TABLE, args, "_id=" + storyId, null);
	}
	
	public static List<String> getColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        Cursor c = null;

        try {
            c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);

            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }

        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    public static String join(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append((String)list.get(i));
        }
        return buf.toString();
    }

}
