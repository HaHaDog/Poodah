package com.poodah;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressLint("ViewConstructor")
public class PPTModeView extends SurfaceView 
						implements SurfaceHolder.Callback {

	public MainActivity activity;
	public int mScreenWidth;
	public int mScreenHeight;
	public int mButtonWidth;
	public int mButtonHeight;
	private Rect mNextRect = new Rect();
	private Rect mPrevRect = new Rect();
	private Rect mEndRect = new Rect();
	private Rect mF5Rect = new Rect();
	public Paint paint = new Paint();
	public Bitmap mNextBitmap;
	public Bitmap mPrevBitmap;
	public Bitmap mEndBitmap;
	public Bitmap mF5Bitmap;
	public Bitmap mBackgroundBitmap;
	public MyThread myThread;
	public PPTModeView(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		InitRectAndBitmap();
		getHolder().addCallback(this);
		myThread = new MyThread(this.getHolder());
	}

	public void InitRectAndBitmap(){
		//int top, left, bottom, right;
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDensity = dm.densityDpi;
		opts.inTargetDensity = dm.densityDpi;
		mNextBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.ppt_next, opts);
		mPrevBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.ppt_prev, opts);
		mEndBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.ppt_stop, opts);
		mF5Bitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.ppt_start, opts);
		mBackgroundBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.ppt_bg, opts);
		
		mF5Rect.set(0,0,mScreenHeight/10, mScreenHeight/10);
		mEndRect.set(mScreenWidth-mScreenHeight/10, 0, mScreenWidth, mScreenHeight/10);
		mNextRect.set(0, mScreenHeight/15, mScreenWidth, mScreenHeight/2);
		mPrevRect.set(0, mScreenHeight/2, mScreenWidth, mScreenHeight);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		setOnTouchListener(myOnClickListener);
		myThread.start();
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
		/*canvas.drawBitmap(mNextBitmap, null, mNextRect, paint);
		canvas.drawBitmap(mPrevBitmap, null, mPrevRect, paint);
		canvas.drawBitmap(mEndBitmap, null, mEndRect, paint);
		*/
		paint.setAntiAlias(true);
		paint.setColor(Color.CYAN);
		canvas.drawRect(mF5Rect, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawRect(mEndRect, paint);
		paint.setColor(Color.GRAY);
		canvas.drawRect(mNextRect, paint);
		paint.setColor(Color.DKGRAY);
		canvas.drawRect(mPrevRect, paint);
		canvas.drawBitmap(mBackgroundBitmap, null, 
				new Rect(0, 0, mScreenWidth, mScreenHeight), paint);
		canvas.drawBitmap(mF5Bitmap, null, mF5Rect, paint);
		canvas.drawBitmap(mEndBitmap, null, mEndRect, paint);
		canvas.drawBitmap(mNextBitmap, null, mNextRect, paint);
		canvas.drawBitmap(mPrevBitmap, null, mPrevRect, paint);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
	
	class MyThread extends Thread{
		public SurfaceHolder holder;
		public MyThread(SurfaceHolder holder){
			this.holder = holder;
		}
		public void run(){
			Canvas c = null;
			try{
				c = holder.lockCanvas();
				synchronized (holder) {
					PPTModeView.this.draw(c);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(c != null){
					holder.unlockCanvasAndPost(c);
				}
			}
		}
	}

	class MyOnClickListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			boolean b=false;
			// TODO Auto-generated method stub
			int x, y;
			x = (int)event.getX();
			y = (int)event.getY();
			if(mNextRect.contains(x, y)){
				b = activity.send("40");
				System.out.println("b"+b+"下一张:40");
			}
			else if(mPrevRect.contains(x, y)){
				activity.send("38");
				System.out.println("上一张:38");
			}
			else if(mEndRect.contains(x, y)){
				activity.send("27");
				System.out.println("ESC:27");
			}
			else if(mF5Rect.contains(x, y)){
				activity.send("45");
				System.out.println("F5:45");
			}
			return false;
		}
		
	}
}
