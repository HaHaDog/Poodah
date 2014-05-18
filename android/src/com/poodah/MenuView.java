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
public class MenuView extends SurfaceView implements SurfaceHolder.Callback {

	private MenuActivity activity = null;
	private Bitmap bgBmp = null;
	private Bitmap settingsBmp = null;
	private Bitmap gameBmp = null;
	private Bitmap pptBmp = null;
	private Bitmap mediaBmp = null;
	private Bitmap customBmp = null;
	private Rect bitmapRect = new Rect();
	// private Bitmap starBmp = null;
	private DisplayMetrics dm = null;
	private boolean isWaiting = true;
	private float angle = 0;
	private int settingsBmpWidth = 0;
	private int settingsBmpHeight = 0;
	private int settingsBmpCenterX = 0;
	private int settingsBmpCenterY = 0;

	private int gameX = 0;
	private int gameY = 0;
	private int pptX = 0;
	private int pptY = 0;
	private int playerX = 0;
	private int playerY = 0;
	private int customX = 0;
	private int customY = 0;

	private int modeWidth = 0;

	public MenuView(final MenuActivity activity) {
		super(activity);
		this.activity = activity;
		this.dm = this.activity.getDisplayMetrics();
		this.getHolder().addCallback(this);
		bgBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.sky_bg);
		settingsBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.sun);
		// starBmp = BitmapFactory.decodeResource(activity.getResources(),
		// R.drawable.star);
		gameBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.game);
		pptBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.ppt);
		mediaBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.media);
		customBmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.custom);
		initBitmap();

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getAction();
				float x = event.getX();
				float y = event.getY();
				if (touchOnSettings(x, y)) {
					isWaiting = false;
					activity.gotoSettingsActivity();
				} else if (touchOnGame(x, y)) {
					isWaiting = false;
					activity.gotoGameModeView();
				} else if (touchOnPpt(x, y)) {
					isWaiting = false;
					activity.gotoPPTModeView();
				} else if (touchOnPlayer(x, y)) {
					isWaiting = false;
					activity.gotoPlayerModeView();
				} else if (touchOnCustom(x, y)) {
					isWaiting = false;
					activity.gotoCustomModeView();
				}
				return false;
			}
		});
	}

	public void initBitmap() {
		float wRatio = (float) dm.widthPixels / bgBmp.getWidth();
		float hRatio = (float) dm.heightPixels / bgBmp.getHeight();
		bgBmp = PicLoadUtil.scaleToFitFullScreen(bgBmp, wRatio, hRatio);
		wRatio = (float) dm.widthPixels / 4 / gameBmp.getWidth();
		hRatio = wRatio;
		gameBmp = PicLoadUtil.scaleToFitFullScreen(gameBmp, wRatio, hRatio);
		wRatio = (float) dm.widthPixels / 4 / pptBmp.getWidth();
		hRatio = wRatio;
		pptBmp = PicLoadUtil.scaleToFitFullScreen(pptBmp, wRatio, hRatio);
		wRatio = (float) dm.widthPixels / 4 / mediaBmp.getWidth();
		hRatio = wRatio;
		mediaBmp = PicLoadUtil.scaleToFitFullScreen(mediaBmp, wRatio, hRatio);
		wRatio = (float) dm.widthPixels / 4 / customBmp.getWidth();
		hRatio = wRatio;
		customBmp = PicLoadUtil.scaleToFitFullScreen(customBmp, wRatio, hRatio);

		wRatio = (float) dm.widthPixels * 3 / 4 / settingsBmp.getWidth();
		hRatio = wRatio;
		settingsBmp = PicLoadUtil.scaleToFitFullScreen(settingsBmp, wRatio,
				hRatio);

		settingsBmpWidth = settingsBmp.getWidth() / 2;
		settingsBmpHeight = settingsBmp.getHeight();
		settingsBmpCenterX = 0;
		settingsBmpCenterY = dm.heightPixels / 2;

		modeWidth = dm.widthPixels * 3 / 4;
		double arc = Math.PI / 5;
		int w = gameBmp.getWidth() / 2;
		int rx = 0;
		int ry = dm.heightPixels / 2;
		int x = 0;
		int y = 0;
		x = (int) (Math.sin(arc) * modeWidth);
		y = (int) (Math.cos(arc) * modeWidth);
		customX = rx + x - w;
		customY = ry + y - w;
		arc += Math.PI / 5;
		x = (int) (Math.sin(arc) * modeWidth);
		y = (int) (Math.cos(arc) * modeWidth);
		pptX = rx + x - w;
		pptY = ry + y - w;
		arc += Math.PI / 5;
		x = (int) (Math.sin(arc) * modeWidth);
		y = (int) (Math.cos(arc) * modeWidth);
		playerX = rx + x - w;
		playerY = ry + y - w;
		arc += Math.PI / 5;
		x = (int) (Math.sin(arc) * modeWidth);
		y = (int) (Math.cos(arc) * modeWidth);
		gameX = rx + x - w;
		gameY = ry + y - w;
	}

	public float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public boolean touchOnSettings(float x, float y) {
		return getDistance(x, y, settingsBmpCenterX, settingsBmpCenterY) < settingsBmpWidth;
	}

	public boolean touchOnGame(float x, float y) {
		bitmapRect.set(gameX, gameY, gameBmp.getWidth() + gameX,
				gameBmp.getHeight() + gameY);
		return bitmapRect.contains((int) x, (int) y);
		// return getDistance(x, y, gameX, gameY) < modeWidth/6;
	}

	public boolean touchOnPpt(float x, float y) {
		bitmapRect.set(pptX, pptY, pptBmp.getWidth() + pptX, pptBmp.getHeight()
				+ pptY);
		return bitmapRect.contains((int) x, (int) y);
		// return getDistance(x, y, pptX, pptY) < modeWidth/6;
	}

	public boolean touchOnPlayer(float x, float y) {
//		Toast.makeText(this.getContext(), "touch on player", Toast.LENGTH_SHORT)
//				.show();
		bitmapRect.set(playerX, playerY, mediaBmp.getWidth() + playerX,
				mediaBmp.getHeight() + playerY);
		return bitmapRect.contains((int) x, (int) y);
		// return getDistance(x, y, playerX, playerY) < modeWidth/6;
	}

	public boolean touchOnCustom(float x, float y) {
		bitmapRect.set(customX, customY, customBmp.getWidth() + customX,
				customBmp.getHeight() + customY);
		return bitmapRect.contains((int) x, (int) y);
		// return getDistance(x, y, customX, customY) < modeWidth/6;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// »­±³¾°Í¼Æ¬
		canvas.drawBitmap(bgBmp, 0, 0, null);
		// »­ÉèÖÃÍ¼Æ¬
		canvas.save();
		canvas.rotate(angle, 0, dm.heightPixels / 2);
		canvas.drawBitmap(settingsBmp, -settingsBmp.getWidth() / 2,
				(dm.heightPixels - settingsBmp.getHeight()) / 2, null);
		canvas.restore();

		canvas.drawBitmap(gameBmp, gameX, gameY, null);
		canvas.drawBitmap(pptBmp, pptX, pptY, null);
		canvas.drawBitmap(mediaBmp, playerX, playerY, null);
		canvas.drawBitmap(customBmp, customX, customY, null);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(24);
		// canvas.drawText("ÉèÖÃ", 0, dm.heightPixels / 2, paint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread() {
			public void run() {
				repaint();
			}
		}.start();

	}

	@SuppressLint("WrongCall")
	public void repaint() {
		SurfaceHolder mHolder = this.getHolder();
		Canvas canvas = mHolder.lockCanvas();
		try {
			synchronized (mHolder) {
				onDraw(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

}
