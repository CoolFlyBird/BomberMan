package com.unual.bomberman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.bean.Bomb;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBall;
import com.unual.bomberman.bean.MoveModel;
import com.unual.bomberman.bean.Prop;
import com.unual.bomberman.bean.Door;
import com.unual.bomberman.bean.PropModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by unual on 2017/7/20.
 */

public class GameConfig {
    public static int HEIGHT_SIZE = 13;
    public static int WIDTH_SIZE = 21;
    public static int MAP_FPS = 30;
    public static final byte MAP_TYPE_BACKGROUND = 0;
    public static final byte MAP_TYPE_TEMP = 1;
    public static final byte MAP_TYPE_FIRE = 2;
    public static final byte MAP_TYPE_WALL = 3;
    public static final byte MAP_TYPE_BRICK = 4;

    public static final byte PROP_TYPE_LENGTH = 1;
    public static final byte PROP_TYPE_COUNT = 2;
    public static final byte PROP_TYPE_SPEED = 3;
    public static final byte PROP_TYPE_TIMER = 4;

    public static final byte WALL_PERCENT_20 = 5;
    public static final byte WALL_PERCENT_25 = 4;
    public static final byte WALL_PERCENT_33 = 3;
    public static final byte WALL_PERCENT_50 = 2;

    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static int PER_WIDTH;
    public static int PER_HEIGHT;

    private Bitmap background;
    private Bitmap brick;
    private Bitmap wall;
    private MapInfo mapInfo;
    private int propType;
    private int percent;
    private List<MoveModel> emys;
    private List<Bomb> bombs;
    private Bomber bomber;
    private PropModel door;
    private PropModel prop;
    private int mapLevel;
    private int emyCount;
    private int bombCount;
    private int bombLength;
    private int bomberSpeed;


    public GameConfig(int width, int height) {
        /**
         * map size
         */
        MAP_WIDTH = width;
        MAP_HEIGHT = height;
        mapLevel = 1;
        bombCount = 1;
        PER_WIDTH = MAP_WIDTH / WIDTH_SIZE;
        PER_HEIGHT = MAP_HEIGHT / HEIGHT_SIZE;

        /**
         * map icon
         */
        brick = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_brick);
        wall = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_wall);
        background = Bitmap.createBitmap(PER_WIDTH, PER_HEIGHT, Bitmap.Config.ARGB_8888);
        brick = Bitmap.createScaledBitmap(brick, PER_WIDTH, PER_HEIGHT, false);
        wall = Bitmap.createScaledBitmap(wall, PER_WIDTH, PER_HEIGHT, false);
        Canvas canvas = new Canvas(background);
        canvas.drawColor(AppCache.getInstance().getContext().getResources().getColor(R.color.game_background));

        /**
         * map info
         */
        mapInfo = new MapInfo(WIDTH_SIZE, HEIGHT_SIZE);
        setMapLevel(mapLevel);
        generateMapInfo();

        /**
         * moving object
         */
        emys = new ArrayList<>();
        bombs = new ArrayList<>();
        door = new Door(this);
        prop = new Prop(this);
        bomber = new Bomber(this, bombs);
        setEmys();
    }

    public Bitmap getBackground() {
        return background;
    }

    public Bitmap getBrick() {
        return brick;
    }

    public Bitmap getWall() {
        return wall;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public int getMapLevel() {
        return mapLevel;
    }

    public int getPropType() {
        return propType;
    }

    public int getPercent() {
        return percent;
    }

    public int getBombCount() {
        return bombCount;
    }

    public List<MoveModel> getEmys() {
        return emys;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public Bomber getBomber() {
        return bomber;
    }

    public PropModel getDoor() {
        return door;
    }

    public PropModel getProp() {
        return prop;
    }

    public void generateMapInfo() {
        mapInfo.generateMap();
    }

    public void reStart() {
        mapLevel = 1;
        bombCount = 1;
        bomber.reset();
        door.reset();
        prop.reset();
        Bomb.resetLength();
        setMapLevel(mapLevel);
        generateMapInfo();
        setEmys();
    }

    public void nextLevel() {
        bomber.reset();
        door.reset();
        prop.reset();
        mapLevel++;
        setMapLevel(mapLevel);
        generateMapInfo();
        setEmys();
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public void setBombLength(int bombLength) {
        this.bombLength = bombLength;
        Bomb.setBombLength(bombLength);
    }

    public void setBomberSpeed(int bomberSpeed) {
        this.bomberSpeed = bomberSpeed;
    }

    public void setLevel(int level) {
        this.mapLevel = level;
    }

    private void setMapLevel(int level) {
        mapLevel = level;
        switch (level) {
            case 1:
                emyCount = 6;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_COUNT;
                break;
            case 2:
                emyCount = 6;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_LENGTH;
                break;
            case 3:
                emyCount = 7;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_SPEED;
                break;
            case 4:
                emyCount = 7;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_COUNT;
                break;
            case 5:
                emyCount = 8;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_LENGTH;
                break;
            case 6:
                emyCount = 8;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_COUNT;
                break;
            case 7:
                emyCount = 9;
                percent = WALL_PERCENT_33;
                propType = PROP_TYPE_COUNT;
                break;
            case 8:
                emyCount = 9;
                percent = WALL_PERCENT_33;
                propType = PROP_TYPE_COUNT;
                break;
            case 9:
                emyCount = 10;
                percent = WALL_PERCENT_33;
                propType = PROP_TYPE_TIMER;
                break;
            case 10:
                emyCount = 10;
                percent = WALL_PERCENT_50;
                propType = PROP_TYPE_COUNT;
                break;
        }
    }

    private void setEmys() {
        emys.clear();
        for (int i = 0; i < emyCount; i++) {
            emys.add(new EmyBall(this));
        }
    }

    public class MapInfo {
        private Random random;
        private byte[][] info;
        private int width, height;
        private int props_x1, props_y1, props_x2, props_y2;

        public MapInfo(int width, int height) {
            random = new Random();
            info = new byte[width][height];
            this.width = width;
            this.height = height;
        }

        public byte[][] getInfo() {
            return info;
        }

        public void generateMap() {
            int x, y, a;
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                        info[x][y] = GameConfig.MAP_TYPE_BRICK;
                    } else if (x % 2 == 0 && y % 2 == 0) {
                        info[x][y] = GameConfig.MAP_TYPE_BRICK;
                    } else {
                        a = random.nextInt(100);
                        if (a % percent == 0) {
                            info[x][y] = GameConfig.MAP_TYPE_WALL;
                        } else {
                            info[x][y] = GameConfig.MAP_TYPE_BACKGROUND;
                        }
                    }
                }
            }
            info[1][1] = GameConfig.MAP_TYPE_BACKGROUND;
            info[2][1] = GameConfig.MAP_TYPE_BACKGROUND;
            info[1][2] = GameConfig.MAP_TYPE_BACKGROUND;

            while (true) {
                x = random.nextInt(WIDTH_SIZE - 2) + 1;
                y = random.nextInt(HEIGHT_SIZE - 2) + 1;
                if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2) && !(x % 2 == 0 && y % 2 == 0)) {
                    info[x][y] = GameConfig.MAP_TYPE_WALL;
                    props_x1 = x;
                    props_y1 = y;
                    break;
                }
            }

            while (true) {
                x = random.nextInt(WIDTH_SIZE - 2) + 1;
                y = random.nextInt(HEIGHT_SIZE - 2) + 1;
                if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2)
                        && !(x == props_x1 && y == props_y1) && !(x % 2 == 0 && y % 2 == 0)) {
                    info[x][y] = GameConfig.MAP_TYPE_WALL;
                    props_x2 = x;
                    props_y2 = y;
                    break;
                }
            }
            Log.e("123", props_x1 + "," + props_y1);
            Log.e("123", props_x2 + "," + props_y2);
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
