package com.seekika.android.app;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class Gallery extends TabActivity implements OnTabChangeListener {
	
	private static final String STORY_TAB = "story_tab";
    private static final String FEED_TAB = "feeds_tab";
    private static final int FONT_SIZE = 21;
    
    private static final int HOME=Menu.FIRST;
    private static final int RECORD=Menu.FIRST + 1;
    
    private TabHost tabHost;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        setTitle(getString(R.string.app_name) + "  Gallery");
        tabHost=getTabHost();
        tabHost.setCurrentTab(0);
        
        tabHost.setOnTabChangedListener(this);
        
        
        //tabHost.setBackgroundColor(Color.parseColor("#0099ff"));
        //tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
        //tabHost.getTabWidget().setBackgroundColor(Color.parseColor("#0099ff"));
        
        Intent stories=new Intent(this, StoryList.class);
        tabHost.addTab(tabHost
        		.newTabSpec(STORY_TAB)
        		.setIndicator("My stories",
        		getResources().getDrawable(android.R.drawable.ic_menu_gallery))
        		.setContent(stories));
        
        //2nd tab
        Intent feed=new Intent(this, ActivityFeed.class);
        tabHost.addTab(tabHost.newTabSpec(FEED_TAB)
        		.setIndicator("Feed",
        		getResources().getDrawable(R.drawable.rss_sq_icon))
        		.setContent(feed));
        
        tabStyling();
    }
    
    private void tabStyling() {
    	for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
    		tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#000000")); //unselected
        }
    	tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#0099ff")); // selected
    
    }
    
   

	@Override
	public void onTabChanged(String tabId) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	        tabHost.getTabWidget().getChildAt(i).setBackgroundColor(
	                Color.parseColor("#000000"));
	    }
	    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
	            .setBackgroundColor(Color.parseColor("#0099ff"));
	 
		
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
    	menu.add(Menu.NONE, HOME, Menu.NONE, "Home").setIcon(R.drawable.home_icon);
        menu.add(Menu.NONE, RECORD, Menu.NONE,getString(R.string.record_story)).setIcon(R.drawable.mic_icon);
        //menu.add(EXIT, EXIT, Menu.NONE, getString(R.string.menu_exit));
		return super.onCreateOptionsMenu(menu);
    	
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle item selection
		switch(item.getItemId()){
			case HOME:
				Intent home=new Intent(this,Home.class);
				startActivity(home);
				return true;
			case RECORD:
				Intent record=new Intent(this,Record.class);
				startActivity(record);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
    

}
