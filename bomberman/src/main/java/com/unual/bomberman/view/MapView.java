package com.unual.bomberman.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;
import com.unual.bomberman.bean.Bomb;

import java.util.Random;

/**
 * Created by unual on 2017/7/6.
 */

public class MapView extends SurfaceView implements SurfaceHolder.Callback, Bomb.BombCallback {
    private SurfaceHolder mHolder;
    private GameConfig gameConfig;
    private MapCallback callback;

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

    public void addMapCallback(MapCallback callback) {
        this.callback = callback;
    }

    public void generateMapInfos() {
        int x, y, a;
        for (y = 0; y < gameConfig.heightSize; y++) {
            for (x = 0; x < gameConfig.widthSize; x++) {
                if (x == 0 || y == 0 || x == gameConfig.widthSize - 1 || y == gameConfig.heightSize - 1) {
                    gameConfig.mapInfo[x][y] = gameConfig.TYPE_BRICK;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    gameConfig.mapInfo[x][y] = gameConfig.TYPE_BRICK;
                } else {
                    a = gameConfig.random.nextInt(100);
                    if (a % gameConfig.percent == 0) {
                        gameConfig.mapInfo[x][y] = gameConfig.TYPE_WALL;
                    } else {
                        gameConfig.mapInfo[x][y] = gameConfig.TYPE_BACKGROUND;
                    }
                }
            }
        }
    }


    private void generateConfig(int width, int height) {
        int perWidth = width / gameConfig.widthSize;
        int perHeight = height / gameConfig.heightSize;
        gameConfig.mapWidth = width;
        gameConfig.mapHeight = height;
        gameConfig.perWidth = perWidth;
        gameConfig.perHeight = perHeight;
        gameConfig.brick = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_brick);
        gameConfig.wall = BitmapFactory.decodeResource(getResources(), R.drawable.game_view_wall);
        gameConfig.background = Bitmap.createBitmap(perWidth, perHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(gameConfig.background);
        canvas.drawColor(getResources().getColor(R.color.game_background));
        gameConfig.brick = Bitmap.createScaledBitmap(gameConfig.brick, perWidth, perHeight, false);
        gameConfig.wall = Bitmap.createScaledBitmap(gameConfig.wall, perWidth, perHeight, false);
        gameConfig.mapInfo = new byte[gameConfig.widthSize][gameConfig.heightSize];
        generateMapInfos();
        callback.onMapConfiged(gameConfig, this);
        renderMap();
    }

    private void renderMap() {
        Canvas canvas = mHolder.lockCanvas();
        drawMap(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void drawMap(Canvas canvas) {
        int x, y;
        for (y = 0; y < gameConfig.heightSize; y++) {
            for (x = 0; x < gameConfig.widthSize; x++) {
                switch (gameConfig.mapInfo[x][y]) {
                    case GameConfig.TYPE_BRICK:
                        canvas.drawBitmap(gameConfig.brick, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
                    case GameConfig.TYPE_WALL:
                    case GameConfig.TYPE_PROPS:
                        canvas.drawBitmap(gameConfig.wall, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
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
            generateConfig(width, height);
        } else {
            renderMap();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onFireOn(int x, int y, int length) {
        gameConfig.mapInfo[x][y] = GameConfig.TYPE_BACKGROUND;
        if (x - length > 0 && gameConfig.mapInfo[x - length][y] == GameConfig.TYPE_WALL) {
            gameConfig.mapInfo[x - length][y] = GameConfig.TYPE_FIRE;
        }

        if (x + length < GameConfig.widthSize - 1 && gameConfig.mapInfo[x + length][y] == GameConfig.TYPE_WALL) {
            gameConfig.mapInfo[x + length][y] = GameConfig.TYPE_FIRE;
        }

        if (y - length > 0 && gameConfig.mapInfo[x][y - length] == GameConfig.TYPE_WALL) {
            gameConfig.mapInfo[x][y - length] = GameConfig.TYPE_FIRE;
        }

        if (y + length < GameConfig.heightSize - 1 && gameConfig.mapInfo[x][y + length] == GameConfig.TYPE_WALL) {
            gameConfig.mapInfo[x][y + length] = GameConfig.TYPE_FIRE;
        }
        renderMap();
    }

    @Override
    public void onFireOff(int x, int y, int length) {
        gameConfig.mapInfo[x][y] = GameConfig.TYPE_BACKGROUND;
        if (x - length > 0 && gameConfig.mapInfo[x - length][y] == GameConfig.TYPE_FIRE) {
            gameConfig.mapInfo[x - length][y] = GameConfig.TYPE_BACKGROUND;
        }


        if (x + length < GameConfig.widthSize - 1 && gameConfig.mapInfo[x + length][y] == GameConfig.TYPE_FIRE) {
            gameConfig.mapInfo[x + length][y] = GameConfig.TYPE_BACKGROUND;
        }

        if (y - length > 0 && gameConfig.mapInfo[x][y - length] == GameConfig.TYPE_FIRE) {
            gameConfig.mapInfo[x][y - length] = GameConfig.TYPE_BACKGROUND;
        }

        if (y + length < GameConfig.heightSize - 1 && gameConfig.mapInfo[x][y + length] == GameConfig.TYPE_FIRE) {
            gameConfig.mapInfo[x][y + length] = GameConfig.TYPE_BACKGROUND;
        }
        renderMap();
    }


    public static class GameConfig {
        public Random random = new Random(47);
        public static final byte TYPE_BACKGROUND = 0;
        public static final byte TYPE_TEMP = 1;
        public static final byte TYPE_PROPS = 2;
        public static final byte TYPE_FIRE = 3;
        public static final byte TYPE_WALL = 4;
        public static final byte TYPE_BRICK = 5;
        public static int widthSize = 21;
        public static int heightSize = 13;
        public static int mapFps = 30;
        int mapWidth, mapHeight;
        int perWidth, perHeight;
        byte[][] mapInfo;
        int percent;
        Bitmap background;
        Bitmap brick;
        Bitmap wall;
        int mapLevel;
        int emyCount;

        public GameConfig() {
            this.percent = 3;
            this.mapLevel = 1;
            this.emyCount = 6;
        }

        public int getEmyCount() {
            return emyCount;
        }


        public int getWidthSize() {
            return widthSize;
        }

        public int getHeightSize() {
            return heightSize;
        }

        public int getMapWidth() {
            return mapWidth;
        }

        public int getMapHeight() {
            return mapHeight;
        }

        public int getPerWidth() {
            return perWidth;
        }

        public int getPerHeight() {
            return perHeight;
        }

        public void clean() {
            mapInfo = null;
        }

    }

    public interface MapCallback {
        void onMapConfiged(GameConfig gameConfig, Bomb.BombCallback callback);
    }
}
