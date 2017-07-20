package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.bean.Bomb;

/**
 * Created by unual on 2017/7/6.
 */

public class MapView extends SurfaceView implements SurfaceHolder.Callback, Bomb.BombCallback {
    private SurfaceHolder mHolder;
    private GameConfig gameConfig;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
        gameConfig = AppCache.getInstance().getGameConfig();
    }

    private void renderMap() {
        Canvas canvas = mHolder.lockCanvas();
        drawMap(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    public void addCallbak() {

    }

    private void drawMap(Canvas canvas) {
        int x, y;
        for (y = 0; y < gameConfig.heightSize; y++) {
            for (x = 0; x < gameConfig.widthSize; x++) {
                switch (gameConfig.mapInfo.getInfo()[x][y]) {
                    case GameConfig.TYPE_BRICK:
                        canvas.drawBitmap(gameConfig.brick, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
                    case GameConfig.TYPE_WALL:
                        canvas.drawBitmap(gameConfig.wall, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
                    case GameConfig.TYPE_TEMP:
                    case GameConfig.TYPE_FIRE:
                    case GameConfig.TYPE_BACKGROUND:
                        canvas.drawBitmap(gameConfig.background, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (gameConfig.mapInfo == null) {
            renderMap();
        } else {
            renderMap();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onFireOn(int x, int y, int upLength, int leftLength, int rightLength, int downLength) {
        for (int i = 0; i <= upLength; i++) {
            gameConfig.mapInfo.getInfo()[x][y - i] = GameConfig.TYPE_FIRE;
            if (gameConfig.mapInfo.isDoor(x, y - i)) {
                gameConfig.getPropDoor().set(x, y - i);
            } else if (gameConfig.mapInfo.isProp(x, y - i) && !gameConfig.propBomb.isEat()) {
                gameConfig.getPropBomb().set(x, y - i);
            }
        }
        for (int i = 0; i <= leftLength; i++) {
            gameConfig.mapInfo.getInfo()[x - i][y] = GameConfig.TYPE_FIRE;
            if (gameConfig.mapInfo.isDoor(x - i, y)) {
                gameConfig.getPropDoor().set(x - i, y);
            } else if (gameConfig.mapInfo.isProp(x - i, y) && !gameConfig.propBomb.isEat()) {
                gameConfig.getPropBomb().set(x - i, y);
            }
        }
        for (int i = 0; i <= rightLength; i++) {
            gameConfig.mapInfo.getInfo()[x + i][y] = GameConfig.TYPE_FIRE;
            if (gameConfig.mapInfo.isDoor(x + i, y)) {
                gameConfig.getPropDoor().set(x + i, y);
            } else if (gameConfig.mapInfo.isProp(x + i, y) && !gameConfig.propBomb.isEat()) {
                gameConfig.getPropBomb().set(x + i, y);
            }
        }
        for (int i = 0; i <= downLength; i++) {
            gameConfig.mapInfo.getInfo()[x][y + i] = GameConfig.TYPE_FIRE;
            if (gameConfig.mapInfo.isDoor(x, y + i)) {
                gameConfig.getPropDoor().set(x, y + i);
            } else if (gameConfig.mapInfo.isProp(x, y + i) && !gameConfig.propBomb.isEat()) {
                gameConfig.getPropBomb().set(x, y + i);
            }
        }
    }

    @Override
    public void onFireOff(int x, int y, int upLength, int leftLength, int rightLength, int downLength) {
        for (int i = 0; i <= upLength; i++) {
            gameConfig.mapInfo.getInfo()[x][y - i] = GameConfig.TYPE_BACKGROUND;
        }
        for (int i = 0; i <= leftLength; i++) {
            gameConfig.mapInfo.getInfo()[x - i][y] = GameConfig.TYPE_BACKGROUND;
        }
        for (int i = 0; i <= rightLength; i++) {
            gameConfig.mapInfo.getInfo()[x + i][y] = GameConfig.TYPE_BACKGROUND;
        }
        for (int i = 0; i <= downLength; i++) {
            gameConfig.mapInfo.getInfo()[x][y + i] = GameConfig.TYPE_BACKGROUND;
        }
        renderMap();
    }
}
