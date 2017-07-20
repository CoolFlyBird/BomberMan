package com.unual.bomberman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.bean.PropModel;

import java.util.Random;

/**
 * Created by unual on 2017/7/20.
 */

public class GameConfig {
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
    public MapInfo mapInfo;
    public PropModel propDoor;
    public PropModel propBomb;
    public Bitmap background;
    public Bitmap brick;
    public Bitmap wall;
    int mapWidth, mapHeight;
    int mapLevel;
    int emyCount;
    int bombCount;

    public GameConfig(int width, int height) {
        this.mapLevel = 1;
        this.bombCount = 1;
        this.emyCount = 6;
        mapWidth = width;
        mapHeight = height;
        perWidth = mapWidth / widthSize;
        perHeight = mapHeight / heightSize;
        brick = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_brick);
        wall = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_wall);
        background = Bitmap.createBitmap(perWidth, perHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        canvas.drawColor(AppCache.getInstance().getContext().getResources().getColor(R.color.game_background));
        brick = Bitmap.createScaledBitmap(brick, perWidth, perHeight, false);
        wall = Bitmap.createScaledBitmap(wall, perWidth, perHeight, false);
        mapInfo = new MapInfo(widthSize, heightSize);
        mapInfo.generateMap();
    }

    public void setPropDoor(PropModel propDoor) {
        this.propDoor = propDoor;
    }

    public void setPropBomb(PropModel propBomb) {
        this.propBomb = propBomb;
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

    public void clean() {
        mapInfo = null;
    }

    public class MapInfo {
        private byte[][] info;
        private Random random = new Random();
        private int width, height;
        private int props_x1, props_y1, props_x2, props_y2;

        public MapInfo(int width, int height) {
            info = new byte[width][height];
            this.width = width;
            this.height = height;
        }

        public void clearInfo() {
            info = null;
        }

        public byte[][] getInfo() {
            return info;
        }

        public void generateMap() {
            int x, y, a, c, d;
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                        info[x][y] = GameConfig.TYPE_BRICK;
                    } else if (x % 2 == 0 && y % 2 == 0) {
                        info[x][y] = GameConfig.TYPE_BRICK;
                    } else {
                        a = random.nextInt(100);
                        if (a % GameConfig.percent == 0) {
                            info[x][y] = GameConfig.TYPE_WALL;
                        } else {
                            info[x][y] = GameConfig.TYPE_BACKGROUND;
                        }
                    }
                }
            }
            info[1][1] = GameConfig.TYPE_BACKGROUND;
            info[2][1] = GameConfig.TYPE_BACKGROUND;
            info[1][2] = GameConfig.TYPE_BACKGROUND;

            while (true) {
                x = AppCache.getInstance().getRandom().nextInt(widthSize - 2) + 1;
                y = AppCache.getInstance().getRandom().nextInt(heightSize - 2) + 1;
                if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2) && !(x % 2 == 0 && y % 2 == 0)) {
                    info[x][y] = GameConfig.TYPE_WALL;
                    props_x1 = x;
                    props_y1 = y;
                    break;
                }
            }

            while (true) {
                x = AppCache.getInstance().getRandom().nextInt(widthSize - 2) + 1;
                y = AppCache.getInstance().getRandom().nextInt(heightSize - 2) + 1;
                if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2)
                        && !(x == props_x1 && y == props_y1) && !(x % 2 == 0 && y % 2 == 0)) {
                    info[x][y] = GameConfig.TYPE_WALL;
                    props_x2 = x;
                    props_y2 = y;
                    break;
                }
            }
        }

        public boolean isDoor(int x, int y) {
            if ((x == props_x1) && (y == props_y1)) {
                return true;
            }
            return false;
        }

        public boolean isProp(int x, int y) {
            if ((x == props_x2) && (y == props_y2)) {
                return true;
            }
            return false;
        }
    }
}
