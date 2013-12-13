package com.poodah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class SearchActivity extends Activity {
	private MyReceiver receiver;
	private LinearLayout scan = null;
	ProgressDialog progressDialog = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		setContentView(R.layout.search);
		scan = (LinearLayout) findViewById(R.id.scan);

		scan.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					scan.setBackgroundColor(Color.YELLOW);
					break;
				case MotionEvent.ACTION_UP:
					scan.setBackgroundColor(Color.GRAY);
					break;
				}
				return false;
			}
		});
		scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
				if(wm.isWifiEnabled()){
					waiting();
					Intent intent = new Intent();
					intent.setClass(SearchActivity.this, NetService.class);
					intent.putExtra("action", "search");
					startService(intent);
				}else{
					pleaseCheckWifi();
				}
//				Intent intent = new Intent();
//				intent.setClass(SearchActivity.this, ConnectActivity.class);
//				startActivity(intent);
//				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.accelerate_interpolator);
			}
		});

//		Intent intent = new Intent();
//		intent.setClass(SearchActivity.this, NetService.class);
//		intent.putExtra("action", "search");
//		startService(intent);
	}

	public void pleaseCheckWifi(){
		new AlertDialog.Builder(this)
		.setTitle("提醒")
		.setMessage("请检查wifi是否打开")
		.setPositiveButton("确定", null)
		.show();
	}
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle bundle = intent.getExtras();
			String list = bundle.getString("list");
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			 alert(list);
		}
	}

	public void alert(String msg) {
		new AlertDialog.Builder(this).setTitle("警告").setMessage(msg)
				.setPositiveButton("确定", null).show();
	}
	
	public void waiting(){
		progressDialog = new ProgressDialog(this);
//		dialog.setProgressDrawable(getResources().getDrawable(R.drawable.scan));
		progressDialog.setTitle("扫描中");
		progressDialog.show();
	}

	public void regist() {
		// 因为该activity要接收广播消息，所以先顶一个一个接收器对象
		// 该对象自己实现，是继承BroadcastReceiver的
		receiver = new MyReceiver();
		// 定义一个IntentFilter的对象，来过滤掉一些intent
		IntentFilter filter = new IntentFilter();
		// 只接收发送到action为"android.intent.action.MAIN"的intent
		// "android.intent.action.MAIN"是在MainFest中定义的
		filter.addAction("android.intent.action.MAIN");
		// 启动广播接收器
		SearchActivity.this.registerReceiver(receiver, filter);
	}
}