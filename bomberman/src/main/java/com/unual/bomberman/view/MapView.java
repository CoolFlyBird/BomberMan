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
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBalloon;
import com.unual.bomberman.bean.Model;
import com.unual.bomberman.bean.MoveModel;
import com.unual.bomberman.interfaces.IDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by unual on 2017/7/6.
 */

public class MapView extends SurfaceView implements SurfaceHolder.Callback {
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
                    gameConfig.mapInfos[x][y] = gameConfig.TYPE_BRICK;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    gameConfig.mapInfos[x][y] = gameConfig.TYPE_BRICK;
                } else {
                    a = gameConfig.random.nextInt(100);
                    if (a % gameConfig.percent == 0) {
                        gameConfig.mapInfos[x][y] = gameConfig.TYPE_WALL;
                    } else {
                        gameConfig.mapInfos[x][y] = gameConfig.TYPE_BACKGROUND;
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
        gameConfig.mapInfos = new byte[gameConfig.widthSize][gameConfig.heightSize];
        generateMapInfos();
        gameConfig.setModel();
        callback.onMapConfiged(gameConfig);
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
                switch (gameConfig.mapInfos[x][y]) {
                    case GameConfig.TYPE_BRICK:
                        canvas.drawBitmap(gameConfig.brick, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
                    case GameConfig.TYPE_WALL:
                    case GameConfig.TYPE_PROPS:
                        canvas.drawBitmap(gameConfig.wall, x * gameConfig.perWidth, y * gameConfig.perHeight, null);
                        break;
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
        if (gameConfig.mapInfos == null) {
            generateConfig(width, height);
        } else {
            renderMap();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    public static class GameConfig implements IDirection {
        public Random random = new Random(47);
        public static final byte TYPE_BACKGROUND = 0;
        public static final byte TYPE_PROPS = 1;
        public static final byte TYPE_WALL = 2;
        public static final byte TYPE_BRICK = 3;
        int mapWidth, mapHeight;
        int perWidth, perHeight;
        int widthSize, heightSize;
        byte[][] mapInfos;
        int mapFps;
        int percent;
        Bitmap background;
        Bitmap brick;
        Bitmap wall;
        int mapLevel;
        int emyCount;
        Bomber bomber;
        List<MoveModel> emys;

        public GameConfig() {
            this.widthSize = 21;
            this.heightSize = 13;
            this.mapFps = 30;
            this.percent = 3;
            this.mapLevel = 1;
            this.emyCount = 6;
        }

        private void setModel() {
            bomber = new Bomber(R.drawable.game_view_man, this, perWidth, perHeight);
            emys = new ArrayList<>();
            for (int i = 0; i < emyCount; i++) {
                emys.add(new EmyBalloon(R.drawable.game_view_ball, this, perWidth, perHeight));
            }
        }

        public Bomber getBomber() {
            return bomber;
        }

        public List<MoveModel> getEmyList() {
            return emys;
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
            mapInfos = null;
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
            if (x >= widthSize - 1) {
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
            if (y >= heightSize - 1) {
                return false;
            }
            int mapInfo = mapInfos[x][y + 1];
            if (mapInfo == TYPE_BACKGROUND) {
                return true;
            }
            return false;
        }
    }

    public interface MapCallback {
        void onMapConfiged(GameConfig gameConfig);
    }
}
