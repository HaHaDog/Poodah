package com.poodah;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	MenuView menuView = null;
	private DisplayMetrics displayMetrics = null;
	private GameModeView gameModeView = null;
	private PPTModeView pptModeView = null;
	private PlayerModeView playerModeView = null;
	private CustomModeView customModeView = null;
	private static ButtonSettings buttonSettings = null;
	private static String theme = null;
	private WebView webView = null;
	private static String fileName = "/mnt/sdcard/poodah.ini";
	private String addr = null;
	private String ip = null;
	private int port = 0;
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

	@SuppressLint("SetJavaScriptEnabled")
	public void gotoSettings() {
		inMenu = false;
		buttonSettings = getButtonSettings();
		webView = new WebView(this);
		this.setContentView(webView);
		webView.getSettings().setJavaScriptEnabled(true);

		class JSObject {
			@JavascriptInterface
			public void enable(String name) throws IOException {
				if (name.equals("shock")) {
					enableShock();
				} else if (name.equals("sound")) {
					enableSound();
				}
			}

			@JavascriptInterface
			public void disable(String name) throws IOException {
				if (name.equals("shock")) {
					disableShock();
				} else if (name.equals("sound")) {
					disableSound();
				}
			}

			@JavascriptInterface
			public void enableShock() throws IOException {
				buttonSettings.hasShock = true;
				writeToSettings();
			}

			@JavascriptInterface
			public void disableShock() throws IOException {
				buttonSettings.hasShock = false;
				writeToSettings();
			}

			@JavascriptInterface
			public void enableSound() throws IOException {
				buttonSettings.hasSound = true;
				writeToSettings();
			}

			@JavascriptInterface
			public void disableSound() throws IOException {
				buttonSettings.hasSound = false;
				writeToSettings();
			}

			@JavascriptInterface
			public void send(String msg) throws IOException {
				activity.send(msg);
			}

			@JavascriptInterface
			public boolean hasSound() {
				return buttonSettings.hasSound;
			}

			@JavascriptInterface
			public boolean hasShock() {
				return buttonSettings.hasShock;
			}

			@JavascriptInterface
			public String getTheme() {
				return theme;
			}

			@JavascriptInterface
			public void test(String msg) throws IOException {
				send(msg);
			}

		}

		webView.addJavascriptInterface(new JSObject(), "android");
		webView.loadUrl("file:///android_asset/settings.html");
	}

	public void gotoSettingsActivity() {
		gotoSettings();
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

	public void writeToSettings() {
		if(buttonSettings == null){
			buttonSettings = getButtonSettings();
		}
		SharedPreferences settings = getPreferences(0);
		Editor editor = settings.edit();
		editor.putBoolean("shock", buttonSettings.hasShock);
		editor.putBoolean("sound", buttonSettings.hasSound);
		editor.putString("theme", theme);
		editor.commit();
	}

	public ButtonSettings getButtonSettings() {
		buttonSettings = new ButtonSettings();
		SharedPreferences settings = getPreferences(0);
		buttonSettings.hasShock = settings.getBoolean("shock", true);
		buttonSettings.hasSound = settings.getBoolean("sound", true);
		theme = settings.getString("theme", "default");
		return buttonSettings;
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
