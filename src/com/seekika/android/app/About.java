package com.seekika.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class About extends Activity {
	
	private static final int HOME=Menu.FIRST;
	private static final int SETTINGS=Menu.FIRST + 1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	menu.add(Menu.NONE, HOME, Menu.NONE, getString(R.string.menu_logout)).setIcon(R.drawable.home_icon);
    	menu.add(Menu.NONE, SETTINGS, Menu.NONE, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
       
        
		return super.onCreateOptionsMenu(menu);
    	
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	return false;
    }
}
