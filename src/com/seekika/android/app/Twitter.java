package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Twitter extends Activity {
	
	private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    public final static String consumerKey = "FCjELD7tPGalSVW7bF49A";
    public final static String consumerSecret = "fJ3dnsmEG0GaLeIN4fqF0dZxEziOGzSWjFth80mOro";
    private final String CALLBACKURL = "Twitter://twitt";
    private Button mBtnAuth;
    
    private static String ACCESS_KEY = null;
    private static String ACCESS_SECRET = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);
        mBtnAuth=(Button)findViewById(R.id.btn_twitter);
        mBtnAuth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAuth();
				
			}
		});
        
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
                String userKey = httpOauthConsumer.getToken();
                String userSecret = httpOauthConsumer.getTokenSecret();

                // Save user_key and user_secret in user preferences and return

                SharedPreferences settings = getBaseContext().getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_key", userKey);
                editor.putString("user_secret", userSecret);
                editor.commit();

            } catch(Exception e){

            }
        } else {
            /* Do something if the callback comes from elsewhere */
        }

    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Uri uri = this.getIntent().getData();

    	if (uri != null && uri.toString().startsWith(CALLBACKURL)) {
    		Log.d("OAuthTwitter", uri.toString());
    		String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
    		Log.d("OAuthTwitter", verifier);
    		try {

    			httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
    			ACCESS_KEY = httpOauthConsumer.getToken();
    			ACCESS_SECRET = httpOauthConsumer.getTokenSecret();

    			Log.d("OAuthTwitter", consumerKey);
    			Log.d("OAuthTwitter", consumerSecret);

    		} catch (OAuthMessageSignerException e) {
    			e.printStackTrace();
    		} catch (OAuthNotAuthorizedException e) {
    			e.printStackTrace();
    		} catch (OAuthExpectationFailedException e) {
    			e.printStackTrace();
    		} catch (OAuthCommunicationException e) {
    			e.printStackTrace();
    		}
    	}
    }


}
