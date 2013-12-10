package com.poodah;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	MenuView menuView = null;
	private DisplayMetrics displayMetrics = null;
	private GameModeView gameModeView = null;
	private PPTModeView pptModeView = null;
	private PlayerModeView playerModeView = null;
	private CustomModeView customModeView = null;
	public static boolean isSending = false;
	public static boolean isConnecting = false;
	public static String msg = null;
	public static boolean sendingResult = true;
	private MainActivity activity = null;
	private String error = null;
//	private Socket socket = null;
	private boolean inMenu = true;

	public void showError() {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}

	public DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String target = intent.getStringExtra("target");
		activity = this;
		// …Ë÷√»´∆¡
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay()
				.getMetrics(this.displayMetrics);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(target.equals("game")){
			gotoGameModeView();
		}else if(target.equals("ppt")){
			gotoPPTModeView();
		}else if(target.equals("player")){
			gotoPlayerModeView();
		}else if(target.equals("custom")){
			gotoCustomModeView();
		}else if(target.equals("settings")){
			gotoSettings();
		}
	}

	public void gotoGameModeView() {
		inMenu = false;
//		send("23");
		if (gameModeView == null) {
			gameModeView = new GameModeView(this);
		}
		this.setContentView(gameModeView);
	}

	public void gotoPPTModeView() {
		inMenu = false;
		if (pptModeView == null) {
			pptModeView = new PPTModeView(this);
		}
		this.setContentView(pptModeView);
	}

	public void gotoPlayerModeView() {
		inMenu = false;
		if (playerModeView == null) {
			playerModeView = new PlayerModeView(this);
		}
		this.setContentView(playerModeView);
	}

	public void gotoCustomModeView() {
		inMenu = false;
		if (customModeView == null) {
			customModeView = new CustomModeView(this);
		}
		this.setContentView(customModeView);
	}

	public void gotoSettings(){
		gotoSettingsActivity();
	}
	public void gotoSettingsActivity() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	public void initSettings() throws IOException {
//		buttonSettings.hasShock = true;
//		buttonSettings.hasSound = true;
//		theme = "default";
//		writeToSettings();
//	}

	public boolean send(String msg){
			Intent intent = new Intent();
			intent.putExtra("key", msg);
			intent.setClass(MainActivity.this, NetService.class);
			startService(intent);
			return true;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
