package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
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
        gameConfig.getBombs().clear();
        for (int i = 0; i < gameConfig.getBombCount(); i++) {
            gameConfig.getBombs().add(new Bomb(gameConfig, this));
        }
    }

    public void renderMap() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(getResources().getColor(R.color.game_info_bg));
                drawMap(canvas);
            }
        } catch (Exception e) {
            Log.e("123", "mapview:" + e.getMessage());
        } finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawMap(Canvas canvas) {
        int x, y, i = 0;
//        for (y = 0; y < gameConfig.HEIGHT_SIZE; y++) {
//            for (x = 0; x < gameConfig.WIDTH_SIZE; x++) {
//                switch (gameConfig.getMapInfo().getInfo()[x][y]) {
//                    case GameConfig.MAP_TYPE_BRICK:
//                        canvas.drawBitmap(gameConfig.getBrick(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
//                        break;
//                    case GameConfig.MAP_TYPE_WALL:
//                        canvas.drawBitmap(gameConfig.getWall(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
//                        break;
//                    case GameConfig.MAP_TYPE_TEMP:
//                    case GameConfig.MAP_TYPE_FIRE:
//                    case GameConfig.MAP_TYPE_BACKGROUND:
//                        canvas.drawBitmap(gameConfig.getBackground(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
//                        break;
//                }
//            }
//        }
        for (y = 0; y < gameConfig.HEIGHT_SIZE; y++) {
            for (x = 0; x < gameConfig.WIDTH_SIZE; x++) {
                if (gameConfig.X_OFFSET + (x + 1) * gameConfig.PER_WIDTH < 0)
                    continue;
                else if (gameConfig.Y_OFFSET + (y + 1) * gameConfig.PER_HEIGHT < 0)
                    continue;
                else if (gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH > gameConfig.SCREEN_WIDTH)
                    continue;
                else if (gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT > gameConfig.SCREEN_HEIGHT)
                    continue;
                switch (gameConfig.getMapInfo().getInfo()[x][y]) {
                    case GameConfig.MAP_TYPE_BRICK:
                        i++;
                        canvas.drawBitmap(gameConfig.getBrick(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
                        break;
                    case GameConfig.MAP_TYPE_WALL:
                        i++;
                        canvas.drawBitmap(gameConfig.getWall(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
                        break;
                    case GameConfig.MAP_TYPE_TEMP:
                    case GameConfig.MAP_TYPE_FIRE:
                    case GameConfig.MAP_TYPE_BACKGROUND:
                        i++;
                        canvas.drawBitmap(gameConfig.getBackground(), gameConfig.X_OFFSET + x * gameConfig.PER_WIDTH, gameConfig.Y_OFFSET + y * gameConfig.PER_HEIGHT, null);
                        break;
                }
            }
        }
        Log.e("123", "i:" + i);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        renderMap();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onFireOn(int x, int y, int upLength, int leftLength, int rightLength,
                         int downLength) {
        for (int i = 0; i <= upLength; i++) {
            gameConfig.getMapInfo().getInfo()[x][y - i] = GameConfig.MAP_TYPE_FIRE;
            if (gameConfig.getMapInfo().isDoor(x, y - i)) {
                gameConfig.getDoor().set(x, y - i);
            } else if (gameConfig.getMapInfo().isProp(x, y - i) && !gameConfig.getProp().isEat()) {
                gameConfig.getProp().set(x, y - i);
            }
        }
        for (int i = 0; i <= leftLength; i++) {
            gameConfig.getMapInfo().getInfo()[x - i][y] = GameConfig.MAP_TYPE_FIRE;
            if (gameConfig.getMapInfo().isDoor(x - i, y)) {
                gameConfig.getDoor().set(x - i, y);
            } else if (gameConfig.getMapInfo().isProp(x - i, y) && !gameConfig.getProp().isEat()) {
                gameConfig.getProp().set(x - i, y);
            }
        }
        for (int i = 0; i <= rightLength; i++) {
            gameConfig.getMapInfo().getInfo()[x + i][y] = GameConfig.MAP_TYPE_FIRE;
            if (gameConfig.getMapInfo().isDoor(x + i, y)) {
                gameConfig.getDoor().set(x + i, y);
            } else if (gameConfig.getMapInfo().isProp(x + i, y) && !gameConfig.getProp().isEat()) {
                gameConfig.getProp().set(x + i, y);
            }
        }
        for (int i = 0; i <= downLength; i++) {
            gameConfig.getMapInfo().getInfo()[x][y + i] = GameConfig.MAP_TYPE_FIRE;
            if (gameConfig.getMapInfo().isDoor(x, y + i)) {
                gameConfig.getDoor().set(x, y + i);
            } else if (gameConfig.getMapInfo().isProp(x, y + i) && !gameConfig.getProp().isEat()) {
                gameConfig.getProp().set(x, y + i);
            }
        }
    }

    @Override
    public void onFireOff(int x, int y, int upLength, int leftLength, int rightLength,
                          int downLength) {
        for (int i = 0; i <= upLength; i++) {
            gameConfig.getMapInfo().getInfo()[x][y - i] = GameConfig.MAP_TYPE_BACKGROUND;
        }
        for (int i = 0; i <= leftLength; i++) {
            gameConfig.getMapInfo().getInfo()[x - i][y] = GameConfig.MAP_TYPE_BACKGROUND;
        }
        for (int i = 0; i <= rightLength; i++) {
            gameConfig.getMapInfo().getInfo()[x + i][y] = GameConfig.MAP_TYPE_BACKGROUND;
        }
        for (int i = 0; i <= downLength; i++) {
            gameConfig.getMapInfo().getInfo()[x][y + i] = GameConfig.MAP_TYPE_BACKGROUND;
        }
        renderMap();
    }
}
