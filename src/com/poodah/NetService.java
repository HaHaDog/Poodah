package com.poodah;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class NetService extends Service {

	private Scanner scanner = null;
	private Exception error = null;
	private boolean isConnecting = false;
	private Thread netWorkThread = null;
	private String ip;
	private int port;
	private boolean isSending = false;
	private String sendMsg = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		Bundle bundle = intent.getExtras();
		String action = bundle.getString("action");
		if(action.equals("search")){
			
		}else if(action.equals("connect")){
			
		}
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public boolean initSocket(String ip, int port){
		isConnecting = true;
		this.ip = ip;
		this.port = port;
		netWorkThread = new Thread(new NetWork());
		netWorkThread.start();
		return true;
	}
	

	public void send(String msg) {
		sendMsg = msg;
		isSending = true;
	}
	
	class NetWork implements Runnable{
		private Socket socket = null;
		private DataOutputStream outStream;
		public void run(){
			try {
				long start = System.currentTimeMillis();
				socket = new Socket(ip, port);
				long end = System.currentTimeMillis();
				Log.v("Time", ""+(end-start));
				sendSuccess();
				long end2 = System.currentTimeMillis();
				Log.v("Time",""+(end2-end));
				outStream = new DataOutputStream(socket.getOutputStream());
//				out.write("st".getBytes());
//				out.flush();
				isConnecting = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				error = e;
				sendFail();
				isConnecting = false;
				return;
			} catch (IOException e) {
				e.printStackTrace();
				error = e;
				sendFail();
				return;
			}
			while(isConnecting){
				if(isSending){
					try {
						send(sendMsg);
					} catch (IOException e) {
						e.printStackTrace();
						isConnecting = false;
					}
					isSending = false;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void send(String sendMsg) throws IOException{
			if(socket == null){
				isConnecting = false;
			}
			outStream.write(sendMsg.getBytes());
			outStream.flush();
		}
	}
	
	public void sendSuccess(){
		Intent intent = new Intent();
		intent.putExtra("connect", "success");
        intent.setAction("android.intent.action.MAIN");
        //service通过广播发送intent
        sendBroadcast(intent);
	}
	
	public void sendFail(){
		Intent intent = new Intent();
		intent.putExtra("connect", "fail");
		if(error != null)
		intent.putExtra("error", error.getMessage());
		else intent.putExtra("error", "error");
        intent.setAction("android.intent.action.MAIN");
        //service通过广播发送intent
        sendBroadcast(intent);
	}
	
	 public int onStartCommand(Intent intent, int flags, int startId) {
//		 sendSuccess();
			Bundle bundle = intent.getExtras();
			String action = bundle.getString("action");
			if(action.equals("search")){
				WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
			     if(!wm.isWifiEnabled())
			         wm.setWifiEnabled(true);
			     WifiInfo wi=wm.getConnectionInfo();
			     int ipAdd=wi.getIpAddress();
			     String ip=intToIp(ipAdd);
				scanner = new Scanner(ip);
				try {
					scanner.scan();
					ArrayList<String> avaliablIpList = scanner.getAvaliableIpList();
					sendList(avaliablIpList);
				} catch (InterruptedException e) {
					e.printStackTrace();
					error = new Exception("扫描被终止");
					sendFail();
				}				
			}else if(action.equals("connect")){
				String ip = bundle.getString("ip");
				int port = bundle.getInt("port");
				initSocket(ip, port);
			}else if(action.equals("command")){
				isSending = true;
				sendMsg = bundle.getString("command");
			}else if(action.equals("password")){
				String password = bundle.getString("password");
			}
	        return super.onStartCommand(intent, flags, startId);
	    }
	 
	 private String intToIp(int i) {
		 return (i & 0xFF ) + "." +
			     ((i >> 8 ) & 0xFF) + "." +
			     ((i >> 16 ) & 0xFF) + "." +
			     ( i >> 24 & 0xFF) ;
	}

	public void sendList(ArrayList<String> list) {
		Intent intent = new Intent();
		intent.putExtra("list", list.toString());
        intent.setAction("android.intent.action.MAIN");
        sendBroadcast(intent);
	}

}
