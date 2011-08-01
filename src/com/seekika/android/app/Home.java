package com.seekika.android.app;




import com.google.android.c2dm.C2DMessaging;
import com.seekika.android.app.constants.SeekikaConstants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends Activity {
	private static final String TAG="Home";
	private String _username;
	private static final int SETTINGS=Menu.FIRST;
	private static final int LOGOUT=Menu.FIRST + 1;
	private static final int EXIT=Menu.FIRST + 2;
	private static final int ABOUT=Menu.FIRST + 3;
	private static final int ACCOUNTS=Menu.FIRST + 4;
	
	private Button mBtnRecordStory;
	private Button mBtnGallery;
	private Button mBtnProfile;
	private Button mBtnc2dm;
	private SharedPreferences prefs;
	private Context mContext = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve username and display
        prefs = Prefs.get(this);
        //SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
	    _username=prefs.getString("auth_username", null);
	    setContentView(R.layout.home);
        setTitle(getString(R.string.welcome) + " " + _username);
        initComponents();  
        mContext = getApplicationContext();
        
        
    }
	
	
	
	public void initComponents(){
		mBtnRecordStory=(Button)findViewById(R.id.btn_record);
		mBtnRecordStory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Home.this,Record.class);
				startActivity(i);
				
			}
		});
		mBtnGallery=(Button)findViewById(R.id.btn_gallery);
		mBtnGallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Home.this,Gallery.class);
				startActivity(i);
				
			}
		});
		
		mBtnProfile=(Button)findViewById(R.id.btn_profile);
		mBtnProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Home.this,Profile.class);
				startActivity(i);
				
			}
		});
		
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	//menu.add(Menu.NONE, SETTINGS, Menu.NONE, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(Menu.NONE, LOGOUT, Menu.NONE, getString(R.string.menu_logout)).setIcon(android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, ABOUT, Menu.NONE,getString(R.string.seekika_about)).setIcon(android.R.drawable.ic_menu_info_details);
        //menu.add(EXIT, EXIT, Menu.NONE, getString(R.string.menu_exit));
        menu.add(ACCOUNTS, ACCOUNTS, Menu.NONE,getString(R.string.notifications)).setIcon(R.drawable.c2dm_icon);
		return super.onCreateOptionsMenu(menu);
    	
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle item selection
		switch(item.getItemId()){
			case SETTINGS:
			Intent i=new Intent(Home.this,Settings.class);
			startActivity(i);
			return true;
			
			case ABOUT:
				CustomDialog dialog=new CustomDialog(this);
				dialog.setTitle(getString(R.string.seekika_about) + " Seekika");
				dialog.show();
				
			return true;
			
			case ACCOUNTS:
				
				Intent accounts=new Intent(Home.this,Accounts.class);
				startActivity(accounts);
				return true;
			
			case LOGOUT:
				//call logout activity
				SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("auth_username", "");
				editor.putString("userKey", "");
				editor.commit();
				Intent intent=new Intent(Home.this,SeekikaStart.class);
				startActivity(intent);
				finish();
				
			return true;
			
			case EXIT:
				moveTaskToBack(true);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {

	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

	     return true;

	     }

	     return super.onKeyDown(keyCode, event);    
	}
	
	public class CustomDialog extends Dialog{
		Button okButton;
		public CustomDialog(Context context) {
			super(context);
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.about);
			okButton = (Button) findViewById(R.id.OkButton);
			okButton.setOnClickListener(new OKListener());			
			}
		private class OKListener implements android.view.View.OnClickListener {

			@Override
			public void onClick(View v) {
				CustomDialog.this.dismiss();
				
			}
			
		}
	}
		
}
	
	


