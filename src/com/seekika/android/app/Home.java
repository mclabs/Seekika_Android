package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends Activity {
	private String _username;
	private static final int SETTINGS=Menu.FIRST;
	private static final int LOGOUT=Menu.FIRST + 1;
	private static final int EXIT=Menu.FIRST + 2;
	
	private Button mBtnRecordStory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve username and display
        SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
	    _username=settings.getString("auth_username", "");
	    Bundle extras=getIntent().getExtras();
	    if(_username.length()==0){
	    	_username=extras.getString("_username");
	    }
	    
        setContentView(R.layout.home);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("This app has been started " + _username + " times.");
        setTitle(getString(R.string.welcome) + " " + _username);
        initComponents();  
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
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, SETTINGS, Menu.NONE, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(Menu.NONE, LOGOUT, Menu.NONE, getString(R.string.menu_logout)).setIcon(android.R.drawable.ic_menu_delete);
        menu.add(EXIT, EXIT, Menu.NONE, getString(R.string.menu_exit));
		return super.onCreateOptionsMenu(menu);
    	
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle item selection
		switch(item.getItemId()){
			case SETTINGS:
			//call settings activity
			return true;
			
			case LOGOUT:
				//call logout activity
				SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("auth_username", "");
				Intent intent=new Intent(Home.this,Seekika.class);
				startActivity(intent);
				
			return true;
			
			case EXIT:
				moveTaskToBack(true);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
		
	}
	
	

}
