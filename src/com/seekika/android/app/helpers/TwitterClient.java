package com.seekika.android.app.helpers;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class TwitterClient {
	private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    public final static String consumerKey = "FCjELD7tPGalSVW7bF49A";
    public final static String consumerSecret = "fJ3dnsmEG0GaLeIN4fqF0dZxEziOGzSWjFth80mOro";
    private final String CALLBACKURL = "Twitter://twitt";
    
    public TwitterClient(){
    	
    }
    
    public void updateStatus(){
    	
    }
}
