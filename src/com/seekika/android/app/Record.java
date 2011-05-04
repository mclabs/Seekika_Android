/**
 * Recording Activity
 */
package com.seekika.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Record extends Activity {
	
	private int SETTINGS=Menu.FIRST;
	private int LOGOUT=Menu.FIRST + 1;
	private int EXIT=Menu.FIRST + 2;
	
	//components
	private Button mBtnRecord;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordstory);
    }
    
    public void initComponents(){
    	mBtnRecord=(Button)findViewById(R.id.btn_record);
    	mBtnRecord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, SETTINGS, Menu.NONE, R.string.menu_settings);
        menu.add(Menu.NONE, LOGOUT, Menu.NONE, R.string.menu_logout);
        menu.add(EXIT, EXIT, Menu.NONE, R.string.menu_exit);
		return super.onCreateOptionsMenu(menu);
    	
    }
    
    private boolean applyMenuChoice(MenuItem item) {
    	
    	return false;
    }
}
