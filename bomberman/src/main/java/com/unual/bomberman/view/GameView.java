package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unual.bomberman.widget.GameConfig;
import com.unual.bomberman.widget.GameMap;

/**
 * Created by unual on 2017/7/6.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private GameMap gameMap;
    private boolean onPause;
    private long timePerFrame;
    private GameMapCallback callback;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
        timePerFrame = 1000 / GameConfig.getInstance().fps;
    }

    public void addGameMapCallback(GameMapCallback callback) {
        this.callback = callback;
    }

    private void generateGameMap(int width, int height) {
        gameMap = new GameMap(getContext(), width, height);
        gameMap.generateGameMap(4);
        renderMap();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        if (onPause) break;
//                        long startTime = System.currentTimeMillis();
//                        renderMap();
//                        long renderTime = System.currentTimeMillis() - startTime;
//                        if (renderTime < timePerFrame)
//                            Thread.currentThread().sleep(timePerFrame - renderTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
        callback.onGameMapCreate(gameMap);
    }

    private void renderMap() {
        Canvas canvas = mHolder.lockCanvas();
        gameMap.draw(canvas);
//        drawMap(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        Canvas canvas = mHolder.lockCanvas();
//        canvas.drawColor(getContext().getResources().getColor(R.color.game_background));
//        mHolder.unlockCanvasAndPost(canvas);
        onPause = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        generateGameMap(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        onPause = true;
    }

    public interface GameMapCallback {
        void onGameMapCreate(GameMap gameMap);
    }
}
