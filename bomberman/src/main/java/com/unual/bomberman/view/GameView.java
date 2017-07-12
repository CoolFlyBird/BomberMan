package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.widget.GameConfig;
import com.unual.bomberman.widget.GameMap;

/**
 * Created by unual on 2017/7/6.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private GameMap gameMap;

    public GameView(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameMap.onTouchEvent(event);
    }

    private void init() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
    }


    private void generateGameMap(int width, int height) {
        gameMap = new GameMap(getContext(), width, height);
        gameMap.generateGameMap(4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        renderMap();
                        Thread.currentThread().sleep(1000 / GameConfig.getInstance().fps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void renderMap() {
        Canvas canvas = mHolder.lockCanvas();
        gameMap.draw(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        Canvas canvas = mHolder.lockCanvas();
//        canvas.drawColor(getContext().getResources().getColor(R.color.game_background));
//        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        generateGameMap(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
