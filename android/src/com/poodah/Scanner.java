package com.poodah;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Scanner {
	private String ip = null;
	private ArrayList<String> ipList;
	private ArrayList<String> avaliableIpList;
	private final int MAXTHREADNUM = 30;

	public Scanner(String ip) {
		this.ip = ip;
		ipList = new ArrayList<String>();
		avaliableIpList = new ArrayList<String>();
		initIpList();
	}

	public void initIpList() {
		int lastPointIndex = ip.lastIndexOf('.');     
        String head = ip.substring(0, ++lastPointIndex);
		for (int i = 2; i <= 255; i++) {
			String tmp = head + i;
			ipList.add(tmp);
		}
	}

	public void scan() throws InterruptedException {
		Thread thread = new Thread(new WorkerGroup());
		thread.start();
		thread.join();
	}
	
	public ArrayList<String> getAvaliableIpList(){
		return avaliableIpList;
	}

	//检查ip列表中能连接的
	class WorkerGroup implements Runnable {
		private ArrayList<Thread> threadList = null;

		public WorkerGroup() {
			threadList = new ArrayList<Thread>();
		}

		@Override
		public void run() {
			int threadNumNow = 0;
			try {
				while (ipList.size() > 0) {
					while (threadNumNow >= MAXTHREADNUM) {
						for (Thread thread : threadList) {
							if (!thread.getState().equals(
									Thread.State.TERMINATED)) {
								thread.join(5);//等待线程结束
							}
							--threadNumNow;
						}
						threadList = new ArrayList<Thread>();
					}
					Thread thread = new Thread(new Worker(ipList.remove(0)));
					thread.start();
					threadNumNow++;
					threadList.add(thread);
				}
			} catch (Exception e) {
			}
		}

		//检查ip是否有连接的最小线程
		class Worker implements Runnable {
			private String ip;

			public Worker(String ip) {
				this.ip = ip;
			}

			public boolean isAvaliable() {
				synchronized (this) {
					Socket socket = null;
					try {
						//使用socket访问5757端口
						socket = new Socket(ip,5757);
						socket.close();
					} catch (IOException e) {
						return false;
					}
					return true;
				}
			}

			@Override
			public void run() {
				synchronized (this) {
					if (isAvaliable()) {
						avaliableIpList.add(ip);
					}
				}
			}

		}

	}
}
