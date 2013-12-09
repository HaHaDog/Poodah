package com.poodah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends Activity {
	private String lastIp;
	private String lastPort;
	private EditText ipText = null;
	private EditText portText = null;
	private Button connect = null;
	MyReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		this.setContentView(R.layout.connect);
		ipText = (EditText)findViewById(R.id.editText1);
		portText = (EditText)findViewById(R.id.editText2);
		connect = (Button)findViewById(R.id.button1);
		
		// 显示上次连接的设置
		init();
		
		connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String ip = ipText.getText().toString();
				String port = portText.getText().toString();
				sendMessage("address", ip+":"+port);
				
				//将连接设置到历史记录中
				updateHistory();
			}
		});
		
	}
	
	public void sendMessage(String name, String value){
		Intent intent = new Intent();
		intent.putExtra(name, value);
		intent.setClass(ConnectActivity.this, NetService.class);
		startService(intent);
	}
	
	public void regist(){
		//因为该activity要接收广播消息，所以先顶一个一个接收器对象
        //该对象自己实现，是继承BroadcastReceiver的
        receiver=new MyReceiver();
        //定义一个IntentFilter的对象，来过滤掉一些intent
        IntentFilter filter = new IntentFilter();
        //只接收发送到action为"android.intent.action.MAIN"的intent
//        "android.intent.action.MAIN"是在MainFest中定义的
        filter.addAction("android.intent.action.MAIN");
        //启动广播接收器
        ConnectActivity.this.registerReceiver(receiver, filter);
	}
	public void alert(String msg){
		new AlertDialog.Builder(this)
		.setTitle("警告")
		.setMessage(msg)
		.setPositiveButton("确定", null)
		.show();
	}
	
	public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            
            Bundle bundle = intent.getExtras();
            String connect = bundle.getString("connect");
            if(connect.equals("success")){
            	Intent intent1 = new Intent();
            	intent1.setClass(ConnectActivity.this, MenuActivity.class);
            	startActivity(intent1);
            }else{
            	String error = bundle.getString("error");
            	alert(error);
            	
            }
        } 
    }
	public void test(){
		Intent intent = new Intent();
		intent.putExtra("target", "settings");
		intent.setClass(ConnectActivity.this, MenuActivity.class);
		startActivity(intent);
	}
	
	public void init(){
		SharedPreferences settings = getPreferences(0);
		lastIp = settings.getString("lastip", "");
		lastPort = settings.getString("lastport", "");
		ipText.setText(lastIp);
		portText.setText(lastPort);
	}
	
	public void updateHistory(){
		SharedPreferences settings = getPreferences(0);
		Editor editor = settings.edit();
		editor.putString("lastip", ipText.getText().toString());
		editor.putString("lastport", portText.getText().toString());
		editor.commit();
	}
	
}
