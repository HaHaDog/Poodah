package com.poodah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MenuActivity extends Activity{

	private DisplayMetrics displayMetrics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay()
				.getMetrics(this.displayMetrics);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		this.setContentView(new MenuView(this));
	}
	
	public void gotoMainActivity(Intent intent){
    	intent.setClass(MenuActivity.this, MainActivity.class);
    	startActivity(intent);
	}
	
	public void gotoGameModeView(){
		Intent intent = new Intent();
		intent.putExtra("target", "game");
		gotoMainActivity(intent);
	}
	
	public void gotoPPTModeView(){
		Intent intent = new Intent();
		intent.putExtra("target", "ppt");
		gotoMainActivity(intent);
	}
	
	public void gotoPlayerModeView(){
		Intent intent = new Intent();
		intent.putExtra("target", "player");
		gotoMainActivity(intent);
	}
	
	public DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}
	
	public void gotoCustomModeView(){
		Intent intent = new Intent();
		intent.putExtra("target", "custom");
		gotoMainActivity(intent);
	}

	public void gotoSettingsActivity(){
		Intent intent = new Intent();
		intent.setClass(MenuActivity.this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void onResume(){
		super.onResume();
		setContentView(new MenuView(this));
	}
}
