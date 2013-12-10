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

public class SettingsActivity extends Activity {

	private ButtonSettings buttonSettings = null;
	private String theme = null;
	private CheckBox shock;
	private CheckBox sound;
	private Button themeBtn = null;
	private Button aboutBtn = null;
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
		themeBtn = (Button)findViewById(R.id.themeBtn);
		aboutBtn = (Button)findViewById(R.id.aboutBtn);
		initSettings();
		
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
