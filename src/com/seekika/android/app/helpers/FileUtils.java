package com.seekika.android.app.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.RestClient.RequestMethod;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class FileUtils {
	
	public static String read( InputStream stream ) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
			byte[] buffer = new byte[1024];
			int length = 0;
			while ( ( length = stream.read( buffer ) ) > 0 ) {
				baos.write( buffer, 0, length );
			}
			
			return baos.toString();
		}
		catch ( Exception exception ) {
			return exception.getMessage();
		}
	}
	
	public static String slurp (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
	
	public static String now(String dateFormat) {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    return sdf.format(cal.getTime());

	  }
	
	public static String shortenUrl(String url){
		String shortUrl=null;
		//create new rest client with the 
		RestClient client = new RestClient("http://api.bitly.com/v3/shorten");
		client.AddParam("login", "jwesonga");
		client.AddParam("apiKey", "R_e99e59e45579d79ac91a6f1f34cc2908");
		client.AddParam("longUrl", url);
		client.AddParam("format", "json");
		try{
			client.Execute(RequestMethod.GET);
		}catch(Exception e){
			e.printStackTrace();
		}
		String response=client.getResponse();
		Log.i("FileUtils",response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONObject dataObject = jsonObj.getJSONObject("data");
            shortUrl = dataObject.getString("url");
            Log.i("FileUtils",shortUrl);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return shortUrl;
	}
	
		

}
