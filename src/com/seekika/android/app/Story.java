package com.seekika.android.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.database.SeekikaDbAdapter;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.listeners.PublishStoryListener;
import com.seekika.android.app.models.User;
import com.seekika.android.app.tasks.PublishStoryTask;
import com.seekika.android.app.tasks.SendTweetTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;

public class Story extends Activity implements Runnable,PublishStoryListener {
	
	private static final String TAG="Story";
	
	private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    public final static String consumerKey = "FCjELD7tPGalSVW7bF49A";
    public final static String consumerSecret = "fJ3dnsmEG0GaLeIN4fqF0dZxEziOGzSWjFth80mOro";
    private final String CALLBACKURL = "Story://twitt";
    
    Button mBtnTweet;
    Button mBtnPlay;
    Button mBtnPause;
    Button mBtnEmail;
    Button mBtnDelete;
    Button mBtnUpload;
    Button mBtnDone;
    
    TextView mTitle;
    TextView mDescription;
    TextView mStatus;
    TextView mCreatedOn;
    SeekBar progressBar;
    
    private static final int HOME=Menu.FIRST;
	private static final int SETTINGS=Menu.FIRST + 1;
	
    
    boolean isAuth=false;
    private String dbUserToken;
    private String dbUserSecret;
    
    private SeekikaDbAdapter seekikaDb;
    private Cursor cursor;
    private Bundle extras;
    private MediaPlayer mediaPlayer;
    
    private String audioFile;
    private String title;
    private String storyKey;
    private String uploaded;
    private String description;
    private String status;
    private String createdOn;
    private String lat;
    private String lon;
    private String userKey;
    private String storyId;
    
    private SharedPreferences prefs;
    private SendTweetTask mSendTweetTask;
    private PublishStoryTask mPublishStoryTask;
    private boolean internetOn=true;
    
    private ProgressDialog mProgressDialog;
	private AlertDialog mAlertDialog;
	
	private static final int PROGRESS_DIALOG = 1;
	private static final int ALERT_DIALOG = 2;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story);
		prefs = Prefs.get(this);
		extras = getIntent().getExtras();
		if(extras!=null){
			storyKey=extras.getString(SeekikaDbAdapter.STORYKEY);
			uploaded=extras.getString(SeekikaDbAdapter.UPLOADED);
			title = extras.getString(SeekikaDbAdapter.TITLE);
			description=extras.getString(SeekikaDbAdapter.DESCRIPTION);
			status=extras.getString(SeekikaDbAdapter.STATUS);
			createdOn=extras.getString(SeekikaDbAdapter.CREATED_ON);
			audioFile=extras.getString(SeekikaDbAdapter.FILENAME);
			lat=extras.getString(SeekikaDbAdapter.LAT);
			lon=extras.getString(SeekikaDbAdapter.LON);
			userKey=extras.getString(SeekikaDbAdapter.USER_ID);
			storyId=extras.getString(SeekikaDbAdapter.STORY_ID);
		}
		//load intent extra
		initComponents();
		populateView();
		
		
	}
	
	public boolean isInternetOn() {

		ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	public void initComponents(){
		//audioFile=extras.getString(SeekikaDbAdapter.FILENAME);
		mTitle=(TextView)findViewById(R.id.title);
		mDescription=(TextView)findViewById(R.id.description);
		mStatus=(TextView)findViewById(R.id.status);
		mCreatedOn=(TextView)findViewById(R.id.created_on);
		
		mBtnDone=(Button)findViewById(R.id.btn_done);
		mBtnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(mediaPlayer!=null){
					mediaPlayer.stop();
					mediaPlayer=null;
					
				}
				Intent intent=new Intent(Story.this,Home.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		mBtnPause=(Button)findViewById(R.id.btn_pause);
		mBtnPause.setEnabled(false);
		mBtnPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mediaPlayer!=null){
					mediaPlayer.pause();
					mBtnPlay.setEnabled(true);
					mBtnPause.setEnabled(false);
					
				}
				
			}
		});
		
		mBtnPlay=(Button)findViewById(R.id.btn_play);
		mBtnPlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBtnPause.setEnabled(true);
				mBtnPlay.setEnabled(false);
				mediaPlayer=new MediaPlayer();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

					@Override
					public void onCompletion(MediaPlayer mp) {
						mBtnPlay.setEnabled(true);
						mBtnPause.setEnabled(false);
						
					}
					
				});
				
				Log.i(TAG,"audiofile name" + audioFile);
				try{
					mediaPlayer.setDataSource(SeekikaConstants.AUDIO_FILEPATH + audioFile + ".mp4");
				}catch(IllegalArgumentException e){
					e.printStackTrace();
				}catch(IllegalStateException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
				
				try{
					mediaPlayer.prepare();
				}catch (IllegalStateException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
			    mediaPlayer.start();
			    Thread thread = new Thread(Story.this);
		        thread.start();
		        
				
			}
		});
		
		mBtnTweet=(Button)findViewById(R.id.btn_tweet);
		mBtnTweet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(uploaded.equalsIgnoreCase("0")){
					Toast.makeText(Story.this, "Story not published online. Please upload first",
                            Toast.LENGTH_LONG).show();
				}else{
					
					if(isTweetAuth()){
						//check if internet connection exists
						if(isInternetOn()==true){
							//start sending tweet task
							Log.i(TAG,"connection available " + isInternetOn());
							mSendTweetTask=new SendTweetTask(Story.this);
							mSendTweetTask.execute(dbUserToken, dbUserSecret,title,storyKey);
						}else{
							//no connection
							Log.i(TAG,"connection not available " + isInternetOn());
							Toast.makeText(Story.this, "No internet connection available", Toast.LENGTH_LONG).show();
						}
					}else{
						//no twitter tokens
						startAuth();
					}
					
				}
				
				
			}
		});
		
		mBtnEmail=(Button)findViewById(R.id.btn_email);
		mBtnEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(uploaded.equalsIgnoreCase("0")){
					Toast.makeText(Story.this, "Story not published online. Please upload first",
                            Toast.LENGTH_LONG).show();
				}else{
					String url=SeekikaConstants.STORY_URL + storyKey;
					String emailBody="<p>Hey, Listen to my story on Seekika: <a href='" + url + "'>"+ title + "</a>";
					Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Listen to my story on Seekika");

					emailIntent.setType("text/html");
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(emailBody));
					try{
						startActivity(emailIntent);
					}catch(Exception e){
						Log.e(TAG,e.getMessage());
					}
					
				}
				
				
			}
		});
		
		mBtnDelete=(Button)findViewById(R.id.btn_delete);
		mBtnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ask user whether they are usre they want to delete
				AlertDialog alertDialog = new AlertDialog.Builder(Story.this).create();
				alertDialog.setTitle("");
				alertDialog.setMessage("Are you sure you want to delete?");
				alertDialog.setButton("Delete", new DialogInterface.OnClickListener(){
					 public void onClick(DialogInterface dialog, int which) {
						 seekikaDb=new SeekikaDbAdapter(Story.this);
						 seekikaDb.open();
						 Log.i(TAG,"record id " + extras.getString(SeekikaDbAdapter.STORY_ID));
						 long storyId=Long.valueOf(extras.getString(SeekikaDbAdapter.STORY_ID));
						 
						 
						 if(storyKey!=null){
							 RestClient client = new RestClient(SeekikaConstants.DELETE_STORY_URL);
							 client.AddParam("id", storyKey);
							 try{
									client.Execute(RequestMethod.GET);
								}catch(Exception e){
									e.printStackTrace();
								}
						 }
						 seekikaDb.deleteStory(storyId);
						 Toast.makeText(Story.this, "Story deleted", Toast.LENGTH_LONG).show();
						 Intent i=new Intent(Story.this,Gallery.class);
						 startActivity(i);
						 //delete local record
						 //if local record has story key then delete online also
					 }
				});
				alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener(){
					 public void onClick(DialogInterface dialog, int which) {
						 return;
					 }
				});
				
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
				
				
				
			}
		});
		
		mBtnUpload=(Button)findViewById(R.id.btn_uploadnow);
		if(uploaded.equalsIgnoreCase("0")){
			mBtnUpload.setVisibility(Button.VISIBLE);
		}
		
		mBtnUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
				if(isInternetOn()==true){
					showDialog(PROGRESS_DIALOG);
					mPublishStoryTask=new PublishStoryTask(Story.this);
					mPublishStoryTask.ctx=Story.this;
					mPublishStoryTask.setmPublishStoryListener(Story.this);
					mPublishStoryTask.execute(title,audioFile,lat,lon,androidId,description,userKey,status);
									
				}else{
					Toast.makeText(Story.this, "Sorry no internet connection available. Upload later", Toast.LENGTH_LONG).show();
					finish();
				}
				
			}
		});
		
		progressBar=(SeekBar)findViewById(R.id.seekbar);
		progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					mediaPlayer.seekTo(progress);
				}
				
			}
		});
	}
	
	
	
	public void populateView(){
			Log.i(TAG,"uploaded " + uploaded);
			if(title!=null){
				mTitle.setText(title);
			}
			if(description!=null){
				mDescription.setText(description);
			}
			if(status!=null){
				mStatus.setText(status);
			}
			if(createdOn!=null){
				mCreatedOn.setText(createdOn);
			}
			if(uploaded!=null){
				if(uploaded.equalsIgnoreCase("0")){
					
				}else{
					
				}
			}
		
		String id=extras.getString("incident_id");
	}
	
		
	public boolean isTweetAuth(){
		
		retrieveTokens();
		Log.i("Story","userToken " + dbUserToken);
		
		if(dbUserSecret!=null && dbUserToken!=null){
			
			isAuth=true;
		}else{
			isAuth=false;
		}
		
		
		//if not send to twitter to authenticate
		return isAuth;
	}
	
	
	public void startAuth(){
    	try {
    	    httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    	    httpOauthprovider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
    	                                            "http://twitter.com/oauth/access_token",
    	                                            "http://twitter.com/oauth/authorize");
    	    String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, CALLBACKURL);
    	    /* Open the browser */
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    	    
    	} catch (Exception e) {
    	    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    	}
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();

        //Check if you got NewIntent event due to Twitter Call back only

        if (uri != null && uri.toString().startsWith(CALLBACKURL)) {

            String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

            try {
                // this will populate token and token_secret in consumer

                httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
                String userToken = httpOauthConsumer.getToken();
                String userSecret = httpOauthConsumer.getTokenSecret();
                
                // Save user_key and user_secret in user preferences and return

                SharedPreferences settings = getBaseContext().getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userToken", userToken);
                editor.putString("userTokenSecret", userSecret);
                editor.commit();
                
                if(userToken!=null && userSecret!=null){
                	//save values in database
                    saveTokens(userToken,userSecret);
                    //send tweet here
                    mSendTweetTask=new SendTweetTask(Story.this);
                    //mSendTweetTask.execute(userToken,userSecret,title);
                    mSendTweetTask.execute(userToken, userSecret,title,storyKey);
                }else{
                	//no tokens received so tell user they are unable to tweet
                	Toast.makeText(this, "Sorry!! Unable to authorise on twitter. Please try later", Toast.LENGTH_LONG).show();
                }
                

            } catch(Exception e){
            	e.printStackTrace();
            }
        } else {
            /* Do something if the callback comes from elsewhere */
        }

    }
    
    public void saveTokens(String token,String secret){
		seekikaDb=new SeekikaDbAdapter(Story.this);
		seekikaDb.open();
		User user=new User();
		user.setSecret(secret);
		user.setToken(token);
		user.setUserKey(prefs.getString("userKey",null));
		seekikaDb.adduser(user);
		seekikaDb.close();
	}
    
    public void retrieveTokens(){
    	seekikaDb=new SeekikaDbAdapter(Story.this);
		seekikaDb.open();
		Cursor cur=seekikaDb.fetchTokens(prefs.getString("userKey",null));
		startManagingCursor(cur);
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			Log.i(TAG,"Retrieved values from table  " + cur.getString(1) + cur.getString(2) + cur.getString(3));
		
			dbUserSecret=cur.getString(2);
			dbUserToken=cur.getString(3);
			cur.moveToNext();
		}
		cur.close();
		seekikaDb.close();
    }
    
   
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, HOME, Menu.NONE, "Home").setIcon(R.drawable.home_icon);
        menu.add(Menu.NONE, SETTINGS, Menu.NONE, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
        //menu.add(Menu.NONE, ABOUT, Menu.NONE,getString(R.string.seekika_about)).setIcon(android.R.drawable.ic_menu_info_details);
        //menu.add(EXIT, EXIT, Menu.NONE, getString(R.string.menu_exit));
		return super.onCreateOptionsMenu(menu);
    	
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle item selection
		switch(item.getItemId()){
			case HOME:
				Intent i=new Intent(this,Home.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
    
    


	@Override
	public void run() {
		int currentPosition = 0;
        int total = mediaPlayer.getDuration();
        progressBar.setMax(total);
        while(mediaPlayer !=null && currentPosition < total){
        	try{
        		Thread.sleep(1000);
        		currentPosition =mediaPlayer.getCurrentPosition();
        	}catch (InterruptedException e) {
        		return;
        	}catch (Exception e){
        		return;
        	}
        	progressBar.setProgress(currentPosition);
        }
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		extras=null;
		if(mediaPlayer!=null){
			mediaPlayer=null;
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	switch(id){
		case PROGRESS_DIALOG:
			mProgressDialog=new ProgressDialog(this);
			DialogInterface.OnClickListener loadingButtonListener=new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
					mPublishStoryTask.setmPublishStoryListener(null);
				}
			};
			mProgressDialog.setTitle("");
			mProgressDialog.setMessage("Publishing Story");
			mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
			return mProgressDialog;
			
    	}
    	return null;
    }

	@Override
	public void uploadComplete(String result) {
		String dialogMessage = null;
        String dialogTitle = null;
		dismissDialog(PROGRESS_DIALOG);
		Log.i(TAG,result);
		if(result==null){
			new AlertDialog.Builder(Story.this).setTitle("Unable to complete").setMessage(getString(R.string.connection_error)).setNeutralButton("Close", null).show();
			
		}else{
			try{
				JSONObject jsonObj = new JSONObject();
				JSONArray aryJSONStrings = new JSONArray(result);
				int i=0;
				String result_message=aryJSONStrings.getJSONObject(i).getString("message");
				String storyKey=aryJSONStrings.getJSONObject(i).getString("key");
				if(result_message.equalsIgnoreCase("success")){
					seekikaDb=new SeekikaDbAdapter(Story.this);
					seekikaDb.open();
					seekikaDb.updateStoryStatus(Long.valueOf(storyId), 1,storyKey);
					seekikaDb.close();
					Intent intent=new Intent(Story.this,Home.class);
					startActivity(intent);
					finish();
				}else{
					
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
    
   
}
