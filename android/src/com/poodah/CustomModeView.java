package com.poodah;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class CustomModeView extends SurfaceView
implements SurfaceHolder.Callback {

	public MainActivity activity;
	public MyThread myThread;
	public CustomModeView(MainActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		getHolder().addCallback(this);
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
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	class MyThread extends Thread{
		public SurfaceHolder holder;
		public MyThread(SurfaceHolder holder) {
			// TODO Auto-generated constructor stub
			this.holder = holder;
		}
		public void run(){
			Canvas canvas = null;
			try{
				canvas = holder.lockCanvas();
				synchronized (holder) {
					CustomModeView.this.draw(canvas);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(canvas != null){
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.custom_bg);
		canvas.drawBitmap(bgBitmap, null, 
				new Rect(0, 0, dm.widthPixels, dm.heightPixels), paint);
	}

}
