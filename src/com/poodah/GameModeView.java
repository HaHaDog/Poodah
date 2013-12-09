package com.poodah;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
@SuppressLint("ViewConstructor")
public class GameModeView extends SurfaceView
implements SurfaceHolder.Callback {
	public MainActivity activity;
	
	public int mScreenWidth;
	public int mScreenHeight;
	public int mButtonWidth;
	public int mButtonHeight;
	
	public Point centerOfDirection = new Point();
	public Rect leftButtonRect = new Rect();
	public Rect rightButtonRect = new Rect();
	public Point centerOfABXY = new Point();
	public int mRadiusOfDirection;
	public int mRadiusOfABXY;
	public Point centerOfA = new Point();
	public Point centerOfB = new Point();
	public Point centerOfX = new Point();
	public Point centerOfY = new Point();
	
	public Bitmap mDirectionsBitmap;
	public Bitmap mABitmap;
	public Bitmap mBBitmap;
	public Bitmap mXBitmap;
	public Bitmap mYBitmap;
	public Bitmap mLBBitmap;
	public Bitmap mRBBitmap;
	public Bitmap mBgBitmap;
	
	public MyThread myThread;
	public Paint paint = new Paint();
	public GameModeView(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		isSend = false;
		this.activity = activity;
		getHolder().addCallback(this);
		
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		System.out.print("width:"+mScreenWidth+"   height:"+mScreenHeight);
		myThread = new MyThread(getHolder());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		InitButtonsPosition();
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		setOnTouchListener(myOnClickListener);
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	public void InitButtonsPosition(){
		int x,y;
		y = mScreenHeight;
		x = mScreenWidth;
		centerOfDirection.set(x/2, y*2/7);
		centerOfABXY.set(x/2, y*3/4);
		mRadiusOfDirection = x/4;
		mRadiusOfABXY = x/12;
		leftButtonRect.set(x*4/5, 0, x, y/3);
		rightButtonRect.set(x*4/5, y*2/3, x, y);
		centerOfA.x = centerOfABXY.x - mRadiusOfABXY*2;
		centerOfA.y = centerOfABXY.y;
		centerOfY.x = centerOfABXY.x + mRadiusOfABXY*2;
		centerOfY.y = centerOfABXY.y;
		centerOfB.x = centerOfABXY.x;
		centerOfB.y = centerOfABXY.y + mRadiusOfABXY*2;
		centerOfX.x = centerOfABXY.x;
		centerOfX.y = centerOfABXY.y - mRadiusOfABXY*2;
		
		mDirectionsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_directions);
		mABitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_a);
		mBBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_b);
		mXBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_x);
		mYBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_y);
		mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg);
		mLBBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_lb);
		mRBBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game_rb);
	}
	
	public Rect getRectFromCircle(Point centerOfCircle, int radius){
		return new Rect(centerOfCircle.x-radius, centerOfCircle.y-radius,
				centerOfCircle.x+radius, centerOfCircle.y+radius);
	}
	public boolean isInCircle(Point centerOfCircle, int radius, int x, int y){
		int dx = x - centerOfCircle.x;
		int dy = y - centerOfCircle.y;
		int r = dx*dx + dy*dy;
		if(Math.sqrt(r) < radius)
			return true;
		else
			return false;
	}
	@Override
	public void draw(Canvas canvas){
		super.draw(canvas);
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
		paint.setColor(Color.GRAY);
		paint.setAntiAlias(true);
		/*canvas.drawCircle(centerOfDirection.x, centerOfDirection.y, 
				mRadiusOfDirection, paint);
		canvas.drawRect(leftButtonRect, paint);
		canvas.drawRect(rightButtonRect, paint);
		canvas.drawCircle(centerOfA.x, centerOfA.y, mRadiusOfABXY, paint);
		canvas.drawCircle(centerOfB.x, centerOfB.y, mRadiusOfABXY, paint);
		canvas.drawCircle(centerOfX.x, centerOfX.y, mRadiusOfABXY, paint);
		canvas.drawCircle(centerOfY.x, centerOfY.y, mRadiusOfABXY, paint);*/
		canvas.drawBitmap(mBgBitmap, null,
				new Rect(0, 0, mScreenWidth, mScreenHeight), paint);
		canvas.drawBitmap(mDirectionsBitmap, null,
				getRectFromCircle(centerOfDirection, mRadiusOfDirection), paint);
		canvas.drawBitmap(mABitmap, null, getRectFromCircle(centerOfA, mRadiusOfABXY), paint);
		canvas.drawBitmap(mBBitmap, null, getRectFromCircle(centerOfB, mRadiusOfABXY), paint);
		canvas.drawBitmap(mXBitmap, null, getRectFromCircle(centerOfX, mRadiusOfABXY), paint);
		canvas.drawBitmap(mYBitmap, null, getRectFromCircle(centerOfY, mRadiusOfABXY), paint);
		canvas.drawBitmap(mLBBitmap, null, leftButtonRect, paint);
		canvas.drawBitmap(mRBBitmap, null, rightButtonRect, paint);
	}
	class MyThread extends Thread{
		public SurfaceHolder holder;
		public MyThread(SurfaceHolder holder){
			this.holder = holder;
		}
		public void run(){
			Canvas canvas = null;
			try{
				canvas = holder.lockCanvas();
				synchronized (canvas) {
					GameModeView.this.draw(canvas);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(canvas!=null){
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	public boolean isSend;
	public String sendMsg = new String();
	class MyOnClickListener implements OnTouchListener{

		
		public SendThread mySendThread = new SendThread();
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(mySendThread.isAlive() == false){
				mySendThread.start();
				/*System.out.println("mySendThread.start()");
				
				try {
					System.out.println("ready to mySendThread.wait()");
					mySendThread.wait();
					System.out.println("end of mySendThread.wait()");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				System.out.println("end");
				isSend = false;
				return false;
			}
			//System.out.println("GameMode onTouching!");
			int x = (int)event.getX();
			int y = (int)event.getY();
			if(isInCircle(centerOfDirection, mRadiusOfDirection, x, y)){
				float sinX, r;
				int dx = x-centerOfDirection.x;
				if(x==centerOfDirection.x && y==centerOfDirection.y)
					return false;
				r = (x-centerOfDirection.x)*(x-centerOfDirection.x) + 
						(y-centerOfDirection.y)*(y-centerOfDirection.y);
				r = (float) Math.sqrt(r);
				sinX = (y-centerOfDirection.y)/r;
				if (sinX>Math.sqrt(2)/2) {
					/*while(event.getAction() != MotionEvent.ACTION_UP){
						try {
							activity.send("68");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("ср:68");
					}*/
					//if(event.getActionMasked() != MotionEvent.ACTION_UP){
						System.out.println(sendMsg);
						sendMsg = "68";
						isSend = true;
						synchronized (mySendThread) {
							mySendThread.notify();
						}
					//}
					//else{
					//	isSend = false;
					//}
				}
				else if(dx<0&&sinX<Math.sqrt(2)/2&&sinX>-Math.sqrt(2)/2){
					/*while(event.getAction() != MotionEvent.ACTION_UP){
						try {
							activity.send("83");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("об:83");
					}*/
					//if(event.getActionMasked() != MotionEvent.ACTION_UP){
						sendMsg = "83";
						isSend = true;
						synchronized (mySendThread) {
							mySendThread.notify();
						}
					//}
					//else{
					//	isSend = false;
					//}
				}
				else if(sinX<-Math.sqrt(2)/2){
					/*while(event.getAction() != MotionEvent.ACTION_UP){
						try {
							activity.send("65");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("вС:65");
					}*/
					//if(event.getAction() != MotionEvent.ACTION_UP){
						sendMsg = "65";
						isSend = true;
						synchronized (mySendThread) {
							mySendThread.notify();
						}
					//}
					//else{
					//	isSend = false;
					//}
				}
				else if(dx>0&&sinX<Math.sqrt(2)/2&&sinX>-Math.sqrt(2)/2){
					/*while(event.getAction() != MotionEvent.ACTION_UP){
						try {
							activity.send("87");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("ио:87");
					}*/
					//if(event.getAction() != MotionEvent.ACTION_UP){
						sendMsg = "87";
						isSend = true;
						synchronized (mySendThread) {
							mySendThread.notify();
						}
					//}
					//else{
					//	isSend = false;
					//}
				}
			}
			else if(isInCircle(centerOfA, mRadiusOfABXY, x, y)){
				activity.send("73");
				System.out.println("A:73");
			}
			else if(isInCircle(centerOfB, mRadiusOfABXY, x, y)){
				activity.send("74");
				System.out.println("B:74");
			}
			else if(isInCircle(centerOfX, mRadiusOfABXY, x, y)){
				activity.send("75");
				System.out.println("X:75");
			}
			else if(isInCircle(centerOfY, mRadiusOfABXY, x, y)){
				activity.send("85");
				System.out.println("Y:85");
			}
			else if(leftButtonRect.contains(x, y)){
				activity.send("77");
				System.out.println("LB:77");
			}
			else if(rightButtonRect.contains(x, y)){
				activity.send("78");
				System.out.println("RB:78");
			}
			else{
				isSend = false;
			}
			return true;
		}
		
	}
	
	class SendThread extends Thread{
		public void run(){
			while(true){
				if(isSend == false){
					synchronized (Thread.currentThread()) {
						try {
							Thread.currentThread().wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else{
					activity.send(sendMsg);
					System.out.println(sendMsg);
				}
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	}
}
