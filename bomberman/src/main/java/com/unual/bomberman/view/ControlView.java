package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unual.bomberman.R;
import com.unual.bomberman.bean.BaseModel;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBalloon;
import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;
import com.unual.bomberman.widget.GameConfig;
import com.unual.bomberman.widget.GameMap;

/**
 * Created by unual on 2017/7/6.
 */

public class ControlView extends SurfaceView implements SurfaceHolder.Callback, GameView.GameMapCallback, IDirection {
    public static final int TYPE_BACKGROUND = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_BRICK = 2;
    private SurfaceHolder mHolder;
    private Paint paint;
    private float rx, ry;
    private int r = 250;
    private int padding = 100;
    private float arx, ary;
    private int ar = 150;
    private int apadding = 200;
    private IControl iControl;
    private boolean onPause;
    private long timePerFrame;
    private Context context;
    private byte[][] mapInfos;

    private BaseModel ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8, ball9, ball0;
    private Bomber bomber;

    public ControlView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        timePerFrame = 1000 / GameConfig.getInstance().fps;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float px = 0, py = 0, px1 = 0, py1 = 0, pointsize = 1;
        pointsize = event.getPointerCount();
        if (pointsize == 1) {
            px = event.getX();
            py = event.getY();
        } else if (pointsize == 2) {
            px = event.getX(0);
            py = event.getY(0);
            px1 = event.getX(1);
            py1 = event.getY(1);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointsize == 1) {
                    dealButtonDown(getButton(px, py));
                } else if (pointsize == 2) {
                    dealButtonDown(getButton(px, py));
                    dealButtonDown(getButton(px1, py1));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointsize == 1) {
                    dealButtonUp(getButton(px, py));
                } else if (pointsize == 2) {
                    dealButtonUp(getButton(px, py));
                    dealButtonUp(getButton(px1, py1));
                }
                break;
        }
        return true;
    }

    private void dealButtonDown(int button) {
        switch (button) {
            case IControl.DIRECTION_UP:
                iControl.setDirection(IControl.DIRECTION_UP);
                break;
            case IControl.DIRECTION_DOWN:
                iControl.setDirection(IControl.DIRECTION_DOWN);
                break;
            case IControl.DIRECTION_LEFT:
                iControl.setDirection(IControl.DIRECTION_LEFT);
                break;
            case IControl.DIRECTION_RIGHT:
                iControl.setDirection(IControl.DIRECTION_RIGHT);
                break;
            default:
                iControl.setDirection(IControl.DIRECTION_NONE);
                break;
        }
    }

    private void dealButtonUp(int button) {
        switch (button) {
            case IControl.DIRECTION_UP:
                iControl.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_DOWN:
                iControl.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_LEFT:
                iControl.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_RIGHT:
                iControl.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.ACTION_A:
                iControl.setAction(IControl.ACTION_A);
                break;
        }
    }

    public int getButton(float px, float py) {
        float x = px - rx;
        float y = py - ry;
        if ((x * x + y * y) > (r * r)) {
            return getActionButton(px, py);
        } else if (x > y && x < -y) {
            return IControl.DIRECTION_UP;
        } else if (x < y && x < -y) {
            return IControl.DIRECTION_LEFT;
        } else if (x > y && x > -y) {
            return IControl.DIRECTION_RIGHT;
        } else if (x < y && x > -y) {
            return IControl.DIRECTION_DOWN;
        }
        return IControl.DIRECTION_NONE;
    }

    public int getActionButton(float px, float py) {
        float x = px - arx;
        float y = py - ary;
        if ((x * x + y * y) > (ar * ar)) {
            return IControl.DIRECTION_NONE;
        } else {
            return IControl.ACTION_A;
        }
    }


    private void generate(int width, int height) {
        int perWidth = width / GameConfig.getInstance().width;
        int perHeight = height / GameConfig.getInstance().height;

        bomber = new Bomber(context, R.drawable.game_view_man, this, perWidth, perHeight);
        iControl = bomber;
        ball1 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball2 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball3 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball4 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball5 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball6 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball7 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball8 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball9 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);
        ball0 = new EmyBalloon(context, R.drawable.game_view_ball, this, perWidth, perHeight);

        rx = r + padding;
        ry = height - r - padding;
        arx = width - ar - apadding;
        ary = height - ar - apadding;
//        Canvas canvas = mHolder.lockCanvas();
//        canvas.drawCircle(rx, ry, r, paint);
//        canvas.drawPoint(rx, ry, paint);
//        canvas.drawCircle(arx, ary, ar, paint);
//        canvas.drawPoint(arx, ary, paint);
//        mHolder.unlockCanvasAndPost(canvas);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (onPause) break;
                        long startTime = System.currentTimeMillis();
                        renderMap();
                        long renderTime = System.currentTimeMillis() - startTime;
                        if (renderTime < timePerFrame)
                            Thread.currentThread().sleep(timePerFrame - renderTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void renderMap() {
        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        drawMan(canvas);
        drawEnemy(canvas);
        drawButton(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void drawButton(Canvas canvas) {
        canvas.drawCircle(rx, ry, r, paint);
        canvas.drawPoint(rx, ry, paint);
        canvas.drawCircle(arx, ary, ar, paint);
        canvas.drawPoint(arx, ary, paint);
    }


    private void drawMan(Canvas canvas) {
        bomber.draw(canvas);
    }

    private void drawEnemy(Canvas canvas) {
        ball1.draw(canvas);
        ball2.draw(canvas);
        ball3.draw(canvas);
        ball4.draw(canvas);
        ball5.draw(canvas);
        ball6.draw(canvas);
        ball7.draw(canvas);
        ball8.draw(canvas);
        ball9.draw(canvas);
        ball0.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        generate(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean canWalkUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        int mapInfo = mapInfos[x][y - 1];
        if (mapInfo == TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canWalkLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        int mapInfo = mapInfos[x - 1][y];
        if (mapInfo == TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canWalkRight(int x, int y) {
        if (x >= GameConfig.getInstance().width - 1) {
            return false;
        }
        int mapInfo = mapInfos[x + 1][y];
        if (mapInfo == TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canWalkDown(int x, int y) {
        if (y >= GameConfig.getInstance().height - 1) {
            return false;
        }
        int mapInfo = mapInfos[x][y + 1];
        if (mapInfo == TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public void onGameMapCreate(GameMap gameMap) {
        this.mapInfos = gameMap.getMapInfo();
    }
}
