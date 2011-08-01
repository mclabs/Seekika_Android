package com.seekika.android.app;

import com.seekika.android.app.database.SeekikaDbAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class StoryList extends ListActivity {
	private SeekikaDbAdapter seekikaDb;
	private CursorAdapter dataSource;
	private Cursor mStoryCursor;
	private static final String fields[] = { "title", "status", "description","created_on", BaseColumns._ID };
	private static final String TAG="StoryList";
	private SharedPreferences prefs;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story_list);
		//first retrieve stories from online datastore if they exist
		//if not then do local
		fillData();
		
		ListView view = getListView();
		view.setHeaderDividersEnabled(true);
		view.setClickable(true);
		view.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long rowId) {
				
				Cursor c=mStoryCursor;
				c.moveToPosition(position);
				Log.i(TAG,"clicked id " + String.valueOf(rowId) + " position " + String.valueOf(position));
				//pass values as extras
				Intent intent=new Intent(StoryList.this,Story.class);
				//Cursor cursor = dataSource.getItem(position);
				
				intent.putExtra(SeekikaDbAdapter.STORY_ID, String.valueOf(rowId));
				intent.putExtra(SeekikaDbAdapter.TITLE,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.TITLE)));
				intent.putExtra(SeekikaDbAdapter.DESCRIPTION,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.DESCRIPTION)));
				intent.putExtra(SeekikaDbAdapter.STATUS,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.STATUS)));
				intent.putExtra(SeekikaDbAdapter.CREATED_ON,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.CREATED_ON)));
				intent.putExtra(SeekikaDbAdapter.FILENAME,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.FILENAME)));
				intent.putExtra(SeekikaDbAdapter.STORYKEY,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.STORYKEY)));
				intent.putExtra(SeekikaDbAdapter.UPLOADED,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.UPLOADED)));
				intent.putExtra(SeekikaDbAdapter.LAT,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.LAT)));
				intent.putExtra(SeekikaDbAdapter.LON,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.LON)));
				intent.putExtra(SeekikaDbAdapter.USER_ID,c.getString(c.getColumnIndexOrThrow(SeekikaDbAdapter.USER_ID)));
				startActivity(intent);
				
				
			}
			
		});
		
		//seekikaDb.close();

	}
	
	
	private void fillData() {
		seekikaDb=new SeekikaDbAdapter(this);
		seekikaDb.open();
		prefs = Prefs.get(this);
		String id=prefs.getString("userKey", null);
		mStoryCursor= seekikaDb.fetchStories(id);
		startManagingCursor(mStoryCursor);
		String[] from = new String[] { SeekikaDbAdapter.TITLE,SeekikaDbAdapter.DESCRIPTION,SeekikaDbAdapter.STATUS,SeekikaDbAdapter.CREATED_ON,SeekikaDbAdapter.UPLOADED,SeekikaDbAdapter.LAT,SeekikaDbAdapter.LON,SeekikaDbAdapter.USER_ID };
		int[] to = new int[] { R.id.title,R.id.description,R.id.status,R.id.created_on };
		SimpleCursorAdapter stories=new SimpleCursorAdapter(this,
				R.layout.story_list_text,mStoryCursor,from,to);
		setListAdapter(stories);
		
		//seekikaDb.close();
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		fillData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@Override
	protected void onStop(){
		super.onStop();
		
	}
	
	/**This AsyncTask will pull stories from the server if they exist and 
	  re-populate the local data store as well as save them on the
	  phones SD Card. A utility class needs to be written to for common 
	  operations such as internet connectivity check and presence of SD Card
	  
	**/
	private class ServerStories extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			return null;
		}
		
	}
	
	
}
