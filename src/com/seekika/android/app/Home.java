package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class Home extends Activity {
	private String _username;
	private int SETTINGS=Menu.FIRST;
	private int LOGOUT=Menu.FIRST + 1;
	private int EXIT=Menu.FIRST + 2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve username and display
        SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
	    _username=settings.getString("auth_username", "");
	    
	    
        setContentView(R.layout.home);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("This app has been started " + _username + " times.");
        setTitle(getString(R.string.app_name));
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, SETTINGS, Menu.NONE, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(Menu.NONE, LOGOUT, Menu.NONE, getString(R.string.menu_logout)).setIcon(android.R.drawable.ic_menu_delete);
        menu.add(EXIT, EXIT, Menu.NONE, getString(R.string.menu_exit));
		return super.onCreateOptionsMenu(menu);
    	
    }

}
