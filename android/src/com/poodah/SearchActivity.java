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
		.setTitle("����")
		.setMessage("����wifi�Ƿ��")
		.setPositiveButton("ȷ��", null)
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
		new AlertDialog.Builder(this).setTitle("����").setMessage(msg)
				.setPositiveButton("ȷ��", null).show();
	}
	
	public void waiting(){
		progressDialog = new ProgressDialog(this);
//		dialog.setProgressDrawable(getResources().getDrawable(R.drawable.scan));
		progressDialog.setTitle("ɨ����");
		progressDialog.show();
	}

	public void regist() {
		// ��Ϊ��activityҪ���չ㲥��Ϣ�������ȶ�һ��һ������������
		// �ö����Լ�ʵ�֣��Ǽ̳�BroadcastReceiver��
		receiver = new MyReceiver();
		// ����һ��IntentFilter�Ķ��������˵�һЩintent
		IntentFilter filter = new IntentFilter();
		// ֻ���շ��͵�actionΪ"android.intent.action.MAIN"��intent
		// "android.intent.action.MAIN"����MainFest�ж����
		filter.addAction("android.intent.action.MAIN");
		// �����㲥������
		SearchActivity.this.registerReceiver(receiver, filter);
	}
}