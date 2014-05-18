package com.poodah;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class ThemeActivity extends Activity {

	public ThemeActivity() {
		// TODO Auto-generated constructor stub
	}
	
	private RadioButton defaultRadio = null;
	private RadioButton xiaoqingxinRadio = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.theme);
		
		defaultRadio = (RadioButton)findViewById(R.id.defaultRadio);
		xiaoqingxinRadio = (RadioButton)findViewById(R.id.xiaoqingxinRadio);
		
		defaultRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean flag = defaultRadio.isChecked();
				xiaoqingxinRadio.setChecked(!flag);
			}
		});
		
		xiaoqingxinRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean flag = xiaoqingxinRadio.isChecked();
				defaultRadio.setChecked(!flag);
			}
		});
	}
	

}
