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
import com.unual.bomberman.bean.BaseModel;
import com.unual.bomberman.bean.Bomb;
import com.unual.bomberman.bean.MapInfo;
import com.unual.bomberman.bean.PropBomb;
import com.unual.bomberman.bean.PropDoor;
import com.unual.bomberman.bean.PropModel;

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

    public void generateMapInfo() {
        int x, y, a, c, d;
        for (y = 0; y < gameConfig.heightSize; y++) {
            for (x = 0; x < gameConfig.widthSize; x++) {
                if (x == 0 || y == 0 || x == gameConfig.widthSize - 1 || y == gameConfig.heightSize - 1) {
                    gameConfig.mapInfo.getInfo()[x][y] = gameConfig.TYPE_BRICK;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    gameConfig.mapInfo.getInfo()[x][y] = gameConfig.TYPE_BRICK;
                } else {
                    a = gameConfig.random.nextInt(100);
                    if (a % gameConfig.percent == 0) {
                        gameConfig.mapInfo.getInfo()[x][y] = gameConfig.TYPE_WALL;
                    } else {
                        gameConfig.mapInfo.getInfo()[x][y] = gameConfig.TYPE_BACKGROUND;
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
        gameConfig.mapInfo = new MapInfo(gameConfig.widthSize, gameConfig.heightSize);
        gameConfig.mapInfo.generateMap();
        gameConfig.propDoor = new PropDoor();
        gameConfig.propBomb = new PropBomb();
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
        Log.e("123", "map surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("123", "map surfaceChanged");
        if (gameConfig.mapInfo == null) {
            Log.e("123", "map surfaceChanged");
            generateConfig(width, height);
        } else {
            renderMap();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("123", "map surfaceDestroyed");
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


    public static class GameConfig {
        public Random random = new Random();
        public static final byte TYPE_BACKGROUND = 0;
        public static final byte TYPE_TEMP = 1;
        public static final byte TYPE_FIRE = 4;
        public static final byte TYPE_WALL = 5;
        public static final byte TYPE_BRICK = 6;
        public static final int percent = 4;
        public static int widthSize = 21;
        public static int heightSize = 13;
        public static int mapFps = 30;
        public static int perWidth, perHeight;
        public static MapInfo mapInfo;
        private PropModel propDoor;
        private PropModel propBomb;
        //public static byte[][] mapInfo;
        int mapWidth, mapHeight;
        Bitmap background;
        Bitmap brick;
        Bitmap wall;
        int mapLevel;
        int emyCount;
        int bombCount;

        public GameConfig() {
            this.mapLevel = 1;
            this.bombCount = 1;
            this.emyCount = 6;
        }

        public PropModel getPropDoor() {
            return propDoor;
        }

        public PropModel getPropBomb() {
            return propBomb;
        }

        public int getEmyCount() {
            return emyCount;
        }

        public int getBombCount() {
            return bombCount;
        }

        public int getMapWidth() {
            return mapWidth;
        }

        public int getMapHeight() {
            return mapHeight;
        }

        public static int getWidthSize() {
            return widthSize;
        }

        public static int getHeightSize() {
            return heightSize;
        }

        public void clean() {
            mapInfo = null;
        }

    }

    public interface MapCallback {
        void onMapConfiged(GameConfig gameConfig, Bomb.BombCallback callback);
    }
}
