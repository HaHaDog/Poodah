package com.poodah;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class NetService extends Service {

	private Intent intent = new Intent();
	private String address = null;
	private Exception error = null;
	private boolean isConnecting = false;
	private NetWorkThread netWorkThread = null;
	private String ip;
	private int port;
	private boolean isSending = false;
	private String sendMsg = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public boolean initSocket(String ip, int port) throws IOException{
		isConnecting = true;
		netWorkThread = new NetWorkThread();
		netWorkThread.start();
		return true;
	}
	

	public void send(String msg) {
		sendMsg = msg;
		isSending = true;
	}
	
	class NetWorkThread extends Thread{
		private Socket socket = null;
		private DataOutputStream outStream;
		public void connect(String ip, int port) throws UnknownHostException, IOException{
			socket = new Socket(ip,port);
			outStream = new DataOutputStream(socket.getOutputStream());
		}
		public void run(){
			try {
				connect(ip,port);
				sendSuccess();
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
		intent = new Intent();
		intent.putExtra("connect", "success");
        intent.setAction("android.intent.action.MAIN");
        //service通过广播发送intent
        sendBroadcast(intent);
	}
	
	public void sendFail(){
		intent = new Intent();
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
			String key = bundle.getString("key");
			if(key != null){
				isSending = true;
				sendMsg = key;
				return  super.onStartCommand(intent, flags, startId);
			}
			address = bundle.getString("address");
			boolean flag = false;
			
			try {
				error = null;
				if(address.contains(":")){
					String[] list = address.split(":");
					ip = list[0];
					port = Integer.parseInt(list[1]);
					flag = this.initSocket(ip,port);
					flag = true;
				}else{
					error = new Exception("错误的IP地址：没有指定端口号");
				}
			} 
			catch (UnknownHostException e){
				e.printStackTrace();
				error = new Exception("UnknownHost");
			}catch (IOException e){
				e.printStackTrace();
				if(error==null)error = new Exception("IOException");
			}
			catch (Exception e) {
				e.printStackTrace();
				if(error == null)error = new Exception("Exception");
			}   
	        return super.onStartCommand(intent, flags, startId);
	    }

}
