/**
 * Recording Activity
 */
package com.seekika.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Record extends Activity {
	
	private int SETTINGS=Menu.FIRST;
	private int LOGOUT=Menu.FIRST + 1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, SETTINGS, Menu.NONE, R.string.menu_settings);
        menu.add(Menu.NONE, LOGOUT, Menu.NONE, R.string.menu_logout);
        
		return super.onCreateOptionsMenu(menu);
    	
    }
    
    private boolean applyMenuChoice(MenuItem item) {
    	
    	return false;
    }
}
