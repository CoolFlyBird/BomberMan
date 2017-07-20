package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.bean.Bomb;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBall;
import com.unual.bomberman.bean.MoveModel;
import com.unual.bomberman.bean.PropModel;
import com.unual.bomberman.interfaces.ChapterCallback;
import com.unual.bomberman.interfaces.IControl;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by unual on 2017/7/6.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Paint paint;
    private float rx, ry;
    private int r = 250;
    private int padding = 100;
    private float arx, ary;
    private int ar = 150;
    private int apadding = 200;
    private boolean onPause;
    private long timePerFrame;
    List<MoveModel> remove = new ArrayList<>();
    private Bomber bomber;
    private List<MoveModel> emys;
    private List<Bomb> bombs;
    private PropModel door;
    private PropModel prop;
    private GameConfig gameConfig;
    private ChapterCallback chapterCallback;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void addChapterCallback(ChapterCallback chapterCallback) {
        this.chapterCallback = chapterCallback;
    }

    private void init() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        gameConfig = AppCache.getInstance().getGameConfig();
        bomber = gameConfig.getBomber();
        emys = gameConfig.getEmys();
        bombs = gameConfig.getBombs();
        door = gameConfig.getDoor();
        prop = gameConfig.getProp();
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
                bomber.setDirection(IControl.DIRECTION_UP);
                break;
            case IControl.DIRECTION_DOWN:
                bomber.setDirection(IControl.DIRECTION_DOWN);
                break;
            case IControl.DIRECTION_LEFT:
                bomber.setDirection(IControl.DIRECTION_LEFT);
                break;
            case IControl.DIRECTION_RIGHT:
                bomber.setDirection(IControl.DIRECTION_RIGHT);
                break;
            default:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
        }
    }

    private void dealButtonUp(int button) {
        switch (button) {
            case IControl.DIRECTION_UP:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_DOWN:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_LEFT:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_RIGHT:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.ACTION_A:
                bomber.setAction(IControl.ACTION_A);
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

    public void startRender() {
        onPause = false;
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Looper.prepare();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (onPause) break;
                        long startTime = System.currentTimeMillis();
                        if (bombs != null)
                            render();
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

    void render() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                drawBomb(canvas);
                prop.draw(canvas);
                door.draw(canvas);
                drawMan(canvas);
                drawEnemy(canvas);
                drawButton(canvas);
            }
        } catch (Exception e) {
            Log.e("123", "gameview:" + e.getMessage());
        } finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawButton(Canvas canvas) {
        canvas.drawCircle(rx, ry, r, paint);
        canvas.drawPoint(rx, ry, paint);
        canvas.drawCircle(arx, ary, ar, paint);
        canvas.drawPoint(arx, ary, paint);
    }


    private void drawMan(Canvas canvas) {
        if (bomber.isSkip()) return;
        if (bomber.isRemoved()) {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "游戏结束", Toast.LENGTH_SHORT).show();
                    onPause = true;
                }
            });
            bomber.setSkip(true);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterCallback.reStartGame();
                }
            }, 3000);
        } else {
            bomber.draw(canvas);
        }
        if (prop.isShow() && bomber.meetWith(prop)) {
            Bomb.increaseLength();
            prop.setEat(true);
        }
        if (bomber.isSkipPass()) return;
        if (door.isShow() && emys.size() == 0 && bomber.meetWith(door)) {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "恭喜过关", Toast.LENGTH_SHORT).show();
                    onPause = true;
                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterCallback.nextChapter();
                }
            }, 3000);
            bomber.setSkipPass(true);
        }
    }

    private void drawEnemy(Canvas canvas) {
        for (MoveModel emy : emys) {
            if (emy.isRemoved()) {
                remove.add(emy);
            } else {
                emy.draw(canvas);
            }
        }
        if (remove.size() != 0) {
            emys.removeAll(remove);
            remove.clear();
        }
        for (MoveModel emy : emys) {
            if (bomber.meetWith(emy)) {
                bomber.die();
            } else {
            }
        }
    }

    private void drawBomb(Canvas canvas) {
        for (Bomb bomb : bombs) {
            bomb.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        onPause = false;
        startRender();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        timePerFrame = 1000 / gameConfig.MAP_FPS;
        rx = r + padding;
        ry = height - r - padding;
        arx = width - ar - apadding;
        ary = height - ar - apadding;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onPause = true;
    }
}
