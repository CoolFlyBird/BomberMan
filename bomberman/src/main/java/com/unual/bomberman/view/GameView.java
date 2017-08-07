package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
import com.unual.bomberman.bean.Bomb;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBase;
import com.unual.bomberman.bean.MoveModel;
import com.unual.bomberman.bean.PropCount;
import com.unual.bomberman.bean.PropLength;
import com.unual.bomberman.bean.PropModel;
import com.unual.bomberman.bean.PropSpeed;
import com.unual.bomberman.interfaces.ChapterCallback;
import com.unual.bomberman.interfaces.IControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unual on 2017/7/6.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Paint paint;
    private Paint textPaint;
    private Paint.FontMetricsInt fontMetrics;
    private float rx, ry;
    private int r = 250;
    private int padding = 80;
    private float arx, ary;
    private int ar = 150;
    private int apadding = 150;
    private boolean onPause;
    private long timePerFrame;
    List<MoveModel> removes = new ArrayList<>();
    private Bomber bomber;
    private List<EmyBase> emys;
    private List<Bomb> bombs;
    private PropModel door;
    private PropModel prop;
    private GameConfig gameConfig;
    private ChapterCallback chapterCallback;
    private int checkMeetCount = 0;
    private long time;

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


        textPaint = new Paint();
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(60);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        fontMetrics = textPaint.getFontMetricsInt();

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

    public void stopRender() {
        onPause = true;
    }

    public void startRender() {
        onPause = false;
        prop = gameConfig.getProp();
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Looper.prepare();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (onPause) break;
                        time = System.currentTimeMillis();
                        render();
                        time = System.currentTimeMillis() - time;
                        if (time < timePerFrame)
                            Thread.currentThread().sleep(timePerFrame - time);
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
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                checkMeetCount++;
                drawBomb(canvas);
                prop.draw(canvas);
                door.draw(canvas);
                drawMan(canvas);
                drawEnemy(canvas);
                drawButton(canvas);
                drawInfo(canvas);
                if (bomber.isMapMoved()) chapterCallback.renderMap();
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

    private void drawInfo(Canvas canvas) {
        Rect left = new Rect(0, 0, GameConfig.SCREEN_WIDTH / 3, 150);
        Rect mid = new Rect(GameConfig.SCREEN_WIDTH / 3, 0, GameConfig.SCREEN_WIDTH * 2 / 3, 150);
        Rect right = new Rect(GameConfig.SCREEN_WIDTH * 2 / 3, 0, GameConfig.SCREEN_WIDTH, 150);
        drawText(canvas, left, "TIME:199");
        drawText(canvas, mid, "LEVEL:" + gameConfig.getMapLevel());
        drawText(canvas, right, "COUNT:" + gameConfig.getEmys().size());
    }

    public void drawText(Canvas canvas, Rect targetRect, String text) {
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, textPaint);
    }


    private void drawMan(Canvas canvas) {
        if (bomber.isRemoved()) {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "游戏结束", Toast.LENGTH_SHORT).show();
                    Log.e("123", "游戏结束");
                    onPause = true;
                    bomber.reset();
                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterCallback.reStartGame();
                }
            }, 2000);
        } else {
            bomber.draw(canvas);
        }
        if ((checkMeetCount % (GameConfig.MAP_FPS / 2) == 0) && prop.isShow() && bomber.meetWith(prop)) {
            if (prop instanceof PropCount) {
                gameConfig.increaseCount();
            } else if (prop instanceof PropLength) {
                gameConfig.increaseLength();
            } else if (prop instanceof PropSpeed) {
                gameConfig.increaseSpeed();
            } else {
                Log.e("123", "Prop:" + prop);
            }
            prop.setEat(true);
        }

        if ((checkMeetCount % (GameConfig.MAP_FPS / 2) == 0) && door.isShow() && emys.size() == 0 && bomber.meetWith(door)) {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "恭喜过关", Toast.LENGTH_SHORT).show();
                    Log.e("123", "恭喜过关");
                    onPause = true;
                    bomber.reset();
                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterCallback.nextChapter();
                }
            }, 2000);
        }
    }

    private void drawEnemy(Canvas canvas) {
        for (EmyBase emy : emys) {
            if (emy.isRemoved()) {
                removes.add(emy);
            } else {
                emy.draw(canvas);
            }
        }
        if (removes.size() != 0) {
            emys.removeAll(removes);
            removes.clear();
        }
        if ((checkMeetCount % (GameConfig.MAP_FPS / 2) == 0)) {
            for (MoveModel emy : emys) {
                if (bomber.meetWith(emy)) {
                    bomber.die();
                } else {
                }
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
        GameConfig.SCREEN_WIDTH = width;
        GameConfig.SCREEN_HEIGHT = height;
        timePerFrame = 1000 / gameConfig.MAP_FPS;
        rx = r + padding;
        ry = height - r - padding;
        arx = width - ar - apadding;
        ary = height - ar - apadding;
        if (chapterCallback != null)
            chapterCallback.renderMap();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onPause = true;
    }
}
