package com.poodah;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressLint("ViewConstructor")
public class PlayerModeView extends SurfaceView
implements SurfaceHolder.Callback {

	public int mScreenWidth;
	public int mScreenHeight;
	public MainActivity activity;
	
	public boolean bPlay;
	public boolean bFullScreen;
	public Rect mPlayRect = new Rect();
	public Rect mNextRect = new Rect();
	public Rect mPrevRect = new Rect();
	public Rect mFullScreenRect = new Rect();
	public VolumeBar volumeBar = new VolumeBar();
	
	public Bitmap mPlayBitmap;
	public Bitmap mNextBitmap;
	public Bitmap mPrevBitmap;
	public Bitmap mFullScreenBitmap;
	public Bitmap mBgBitmap;
	
	public MyThread myThread;
	public void InitRects(){
		mPlayRect.set(mScreenWidth*7/24, mScreenHeight/3,
				mScreenWidth*17/24, mScreenHeight*7/12);
		mPrevRect.set(mPlayRect);
		mPrevRect.left = mScreenWidth/25;
		mPrevRect.top = mScreenHeight/2;
		mPrevRect.right = mScreenWidth*9/50;
		mNextRect.set(mPlayRect);
		mNextRect.left = mScreenWidth*41/50;
		mNextRect.top = mScreenHeight/2;
		mNextRect.right = mScreenWidth*24/25;
		volumeBar.setPosition(mScreenWidth/25, mScreenHeight*11/18,
				mScreenWidth*21/25, mScreenHeight*2/3);
		volumeBar.setCurrentVolume(50);
		mFullScreenRect.set(mScreenWidth*22/25, mScreenHeight*11/18,
				mScreenWidth*24/25, mScreenHeight*2/3);
		
		mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_bg);
		mFullScreenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_escape);
		mNextBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_next);
		mPrevBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_prev);
		mPlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_play);
	}
	public PlayerModeView(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		getHolder().addCallback(this);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		bPlay = false;
		bFullScreen = false;
		myThread = new MyThread(getHolder());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if(myThread.isAlive() == false)
			myThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		InitRects();
		MyOnclickListener myOnclickListener = new MyOnclickListener();
		setOnTouchListener(myOnclickListener);
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		//paint.setColor(Color.GRAY);
		//canvas.drawRect(mPlayRect, paint);
		//canvas.drawRect(mPrevRect, paint);
		//canvas.drawRect(mNextRect, paint);
		canvas.drawBitmap(mBgBitmap, null, new Rect(0,0, mScreenWidth,mScreenHeight), paint);
		canvas.drawBitmap(mPlayBitmap, null, mPlayRect, paint);
		canvas.drawBitmap(mPrevBitmap, null, mPrevRect, paint);
		canvas.drawBitmap(mNextBitmap, null, mNextRect, paint);
		//canvas.drawBitmap(mFullScreenBitmap, null, mFullScreenRect, paint);
		paint.setColor(Color.DKGRAY);
		canvas.drawRect(volumeBar.mPosition, paint);
		paint.setColor(Color.GRAY);
		canvas.drawRect(volumeBar.mCurrentVolumeRect, paint);
		canvas.drawRect(mFullScreenRect, paint);
		//canvas.drawRect(mFullScreenRect, paint);
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
					PlayerModeView.this.draw(canvas);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	class MyOnclickListener implements OnTouchListener{
		public int x;
		public int y;
		public int downX;
		//public int downY;
		public int upX;
		//public int upY;
		public boolean returnValue;
		public int funcChosen;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			returnValue = false;
			
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				downX = (int) event.getX();
				//downY = (int) event.getY();
				funcChosen = 0;
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(funcChosen == 2){
					upX = (int) event.getX();
					//upY = (int) event.getY();
					if(upX - downX > 40){
			
						System.out.println("下一个");
					}
					else if(upX -downX < -40){
						
						System.out.println("上一个");
					}
				}
				return false;
			}
			//System.out.println("PlayerMode onTouching");
			x = (int) event.getX();
			y = (int) event.getY();
			
			
			if(mPlayRect.contains(x, y) && funcChosen!=2){
				activity.send("32");
				if(bPlay == false){
					mPlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_pause);
					System.out.println("播放:32");
				}
				else{
					mPlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_play);
					System.out.println("暂停:32");
				}
				bPlay = !bPlay;
				funcChosen = 1;
			}
			else if(mPrevRect.contains(x, y) && funcChosen != 2){
				activity.send("37");
				System.out.println("退后:37");
				funcChosen = 1;
			}
			else if(mNextRect.contains(x, y) && funcChosen != 2){
				activity.send("39");
				System.out.println("前进:39");
				funcChosen = 1;
			}
			else if(mFullScreenRect.contains(x, y) && funcChosen != 2){
				if(bFullScreen == false){
					activity.send("27");
					System.out.println("全屏:27");
				}
				else{
					activity.send("27");
					System.out.println("退出全屏:27");
				}
				bFullScreen = !bFullScreen;
				funcChosen = 1;
			}
			else if(volumeBar.mPosition.contains(x, y) && funcChosen != 2){
				//volumeBar.setVolume(y);
				int iv = x - volumeBar.mPosition.left;
				if(iv>volumeBar.mPosition.width())
					iv = 100;
				if(iv<0)
					iv = 0;
				int dv = iv - volumeBar.mCurrentVolume;
				for(int i=0; i<Math.abs(dv); i+=2){
					if(dv<0)
						activity.send("22");
					else if(dv>0)
						activity.send("21");
				}
				volumeBar.setVolume(x);
				System.out.println("音量："+volumeBar.mCurrentVolume);
				returnValue = true;
				funcChosen = 1;
			}
			else{
				if(funcChosen != 1)
				funcChosen = 2;
				returnValue = true;
			}
			//System.out.println("funcChosen:"+funcChosen);
			Canvas canvas;
			canvas = PlayerModeView.this.getHolder().lockCanvas();
			draw(canvas);
			PlayerModeView.this.getHolder().unlockCanvasAndPost(canvas);
			return returnValue;
		}
	}
	class VolumeBar{
		public Rect mPosition = new Rect();
		public Rect mCurrentVolumeRect = new Rect();
		public int max;
		public int mCurrentVolume;
		public VolumeBar(){
			max = 100;
		}
		public void setCurrentVolume(int volume){
			mCurrentVolume = volume;
			mCurrentVolumeRect.set(mPosition);
			mCurrentVolumeRect.right = mCurrentVolumeRect.left + 
					mPosition.width()*mCurrentVolume/max;
		}
		public void setVolume(int x){
			if(x>mPosition.right)
				x = mPosition.right;
			else if(x<mPosition.left)
				x = mPosition.left;
			mCurrentVolumeRect.right = x;
			x = x - mPosition.left;
			mCurrentVolume = (int)(((float)x)/((float)mPosition.width())*100);
		}
		public void setPosition(int left, int top, int right, int bottom){
			mPosition.set(left, top, right, bottom);
		}
	}
}
