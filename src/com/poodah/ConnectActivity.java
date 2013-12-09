package com.poodah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends Activity {
	private EditText address = null;
	private Button connect = null;
	MyReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regist();
		this.setContentView(R.layout.connect);
		address = (EditText)findViewById(R.id.editText1);
		connect = (Button)findViewById(R.id.button1);
		connect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				String addr = address.getText().toString();
				sendMessage("address", addr);
//				test();
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
		//��Ϊ��activityҪ���չ㲥��Ϣ�������ȶ�һ��һ������������
        //�ö����Լ�ʵ�֣��Ǽ̳�BroadcastReceiver��
        receiver=new MyReceiver();
        //����һ��IntentFilter�Ķ��������˵�һЩintent
        IntentFilter filter = new IntentFilter();
        //ֻ���շ��͵�actionΪ"android.intent.action.MAIN"��intent
//        "android.intent.action.MAIN"����MainFest�ж����
        filter.addAction("android.intent.action.MAIN");
        //�����㲥������
        ConnectActivity.this.registerReceiver(receiver, filter);
	}
	public void alert(String msg){
		new AlertDialog.Builder(this)
		.setTitle("����")
		.setMessage(msg)
		.setPositiveButton("ȷ��", null)
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
}
