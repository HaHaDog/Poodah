package com.poodah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class ConnectActivity extends Activity {
	private String lastIp;
	private String lastPort;
	private EditText ipText = null;
	private EditText portText = null;
	private ProgressDialog progressDialog = null;
	private String warn;
	private String ok;
	private String gotoMain;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent();
		intent.setClass(ConnectActivity.this,NetService.class);
		stopService(intent);
	}

	private ImageButton connect = null;
	MyReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		this.setContentView(R.layout.connect);
		initString();
		ipText = (EditText)findViewById(R.id.editText1);
		portText = (EditText)findViewById(R.id.editText2);
		connect = (ImageButton)findViewById(R.id.themeBtn);
		
		// ��ʾ�ϴ����ӵ�����
		init();
		connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ipText.getText() == null || ipText.getText().length() == 0){
					alert(getResources().getString(R.string.pleaseInputIp));
					return;
				}
				if(portText.getText() == null || portText.getText().length() == 0){
					alert(getResources().getString(R.string.pleaseInputPort));
					return;
				}
				String ip = ipText.getText().toString();
				String port = portText.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("action", "connect");
				intent.putExtra("ip", ip);
				intent.putExtra("port", Integer.parseInt(port));
				intent.setClass(ConnectActivity.this, NetService.class);
				startService(intent);
				waitForConnect();
				//���������õ���ʷ��¼��
				updateHistory();
			}
		});
		
	}
	
	public void initString()
	{
		warn = getResources().getString(R.string.warn);
		ok = getResources().getString(R.string.OK);
		gotoMain = getResources().getString(R.string.gotoMain);
	}

	public void waitForConnect(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getResources().getString(R.string.waitTitle));
		progressDialog.setMessage(getResources().getString(R.string.connecting));
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Intent i = new Intent();
				i.setClass(ConnectActivity.this, NetService.class);
				i.putExtra("action", "cancel");
				startService(i);
			}
		});
		progressDialog.show();
	}
	
	public void gotoMainActivity(){
		Intent i = new Intent();
		i.setClass(ConnectActivity.this, MainActivity.class);
		startActivity(i);
	}
	
	public void sendMessage(String name, String value){
		Intent intent = new Intent();
		intent.putExtra("action", "connect");
		intent.putExtra(name, value);
		intent.setClass(ConnectActivity.this, NetService.class);
		startService(intent);
	}
	
	public void regist(){
        //�ö����Լ�ʵ�֣��Ǽ̳�BroadcastReceiver��
        receiver=new MyReceiver();
        //����һ��IntentFilter�Ķ��������˵�һЩintent
        IntentFilter filter = new IntentFilter();
//        "android.intent.action.MAIN"����MainFest�ж����
        filter.addAction("android.intent.action.MAIN");
        //�����㲥������
        ConnectActivity.this.registerReceiver(receiver, filter);
	}
	public void alert(String msg){
		new AlertDialog.Builder(this)
		.setTitle(warn)
		.setMessage(msg)
		.setPositiveButton(getResources().getString(R.string.OK), null)
		.show();
	}
	
	public void ErrorMsg(String msg){
		new AlertDialog.Builder(this)
		.setTitle(warn)
		.setMessage(msg)
		.setPositiveButton(ok, null)
		.setNegativeButton(gotoMain, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				gotoMenu();
			}
		})
		.show();
	}
	
	public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog != null){
            	progressDialog.dismiss();
            }
            Bundle bundle = intent.getExtras();
            String connect = bundle.getString("connect");
            if(connect != null && connect.equals("success")){
            	Intent intent1 = new Intent();
            	intent1.setClass(ConnectActivity.this, MenuActivity.class);
            	startActivity(intent1);
            }else{
            	String error = bundle.getString("error");
//            	gotoMenu();
            	ErrorMsg(error);
//            	Intent intent2 = new Intent();
//            	intent.setClass(ConnectActivity.this, SettingsActivity.class);
//            	startActivity(intent2);
            }
        } 
    }
	
	public void gotoMenu(){
		Intent intent = new Intent();
		intent.setClass(ConnectActivity.this, MenuActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
