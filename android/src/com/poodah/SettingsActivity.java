package com.poodah;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;

public class SettingsActivity extends Activity {

	private ButtonSettings buttonSettings = null;
	private String theme = null;
	private CheckBox shock;
	private CheckBox sound;
	private ImageButton themeBtn = null;
	private ImageButton aboutBtn = null;
	public SettingsActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);
		getButtonSettings();
		shock = (CheckBox)findViewById(R.id.checkBox1);
		sound = (CheckBox)findViewById(R.id.CheckBox2);
		themeBtn = (ImageButton)findViewById(R.id.themeBtn);
		aboutBtn = (ImageButton)findViewById(R.id.aboutBtn);
		initSettings();
		
//		themeBtn.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch(event.getAction()){
//				case MotionEvent.ACTION_DOWN:
//					themeBtn.setBackground(getResources().getDrawable(R.drawable.theme_btn_press));
//					break;
//				case MotionEvent.ACTION_UP:
//					themeBtn.setBackground(getResources().getDrawable(R.drawable.theme_btn));
//					break;
//				}
//				return false;
//			}
//		});
//		
//		aboutBtn.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch(event.getAction()){
//				case MotionEvent.ACTION_DOWN:
//					aboutBtn.setBackground(getResources().getDrawable(R.drawable.about_btn_press));
//					break;
//				case MotionEvent.ACTION_UP:
//					aboutBtn.setBackground(getResources().getDrawable(R.drawable.about_btn));
//					break;
//				}
//				return false;
//			}
//		});
		shock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean flag = shock.isChecked();
				buttonSettings.hasShock = flag;
				updateSettings();
			}
		});
		
		sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean flag = sound.isChecked();
				buttonSettings.hasSound = flag;
				updateSettings();
				
			}
		});
		
		aboutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SettingsActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
		
		themeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SettingsActivity.this,ThemeActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void initSettings(){
		getButtonSettings();
		shock.setChecked(buttonSettings.hasShock);
		sound.setChecked(buttonSettings.hasSound);
	}

	public ButtonSettings getButtonSettings() {
		buttonSettings = new ButtonSettings();
		SharedPreferences settings = getPreferences(0);
		buttonSettings.hasShock = settings.getBoolean("shock", true);
		buttonSettings.hasSound = settings.getBoolean("sound", true);
		theme = settings.getString("theme", "default");
		return buttonSettings;
	}
	

	public void updateSettings() {
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

}
