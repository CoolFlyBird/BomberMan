package com.unual.bomberman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.bean.Bomb;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.bean.EmyBase;
import com.unual.bomberman.bean.EmySpeed;
import com.unual.bomberman.bean.EmyThrough;
import com.unual.bomberman.bean.EmyTrack;
import com.unual.bomberman.bean.MoveModel;
import com.unual.bomberman.bean.PropCount;
import com.unual.bomberman.bean.Door;
import com.unual.bomberman.bean.PropLength;
import com.unual.bomberman.bean.PropModel;
import com.unual.bomberman.bean.PropSpeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by unual on 2017/7/20.
 */

/**
 * a
 */
public class GameConfig {
    public static int HEIGHT_SIZE = 13;
    public static int WIDTH_SIZE = 29;

    public static int X_MARGIN = 600;
    public static int Y_MARGIN = 500;

    public static int X_OFFSET = X_MARGIN;
    public static int Y_OFFSET = Y_MARGIN;
    public static int X_DIRECTION_OFFSET = 0;
    public static int Y_DIRECTION_OFFSET = 0;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int PER_WIDTH = 100;
    public static int PER_HEIGHT = 95;


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

    private Bitmap infoBg;
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
    private int type1 = 0, type2 = 0, type3 = 0, type4 = 0;
    private int bombCount;
    private int bombLength;
    private int bomberSpeed;


    public GameConfig() {
        /**
         * map size
         */
        mapLevel = 1;
        bombCount = 1;
//        PER_WIDTH = MAP_WIDTH / WIDTH_SIZE;
//        PER_HEIGHT = MAP_HEIGHT / HEIGHT_SIZE;

        /**
         * map icon
         */
        brick = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_brick);
        wall = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_wall);
        infoBg = Bitmap.createBitmap(PER_WIDTH, PER_HEIGHT, Bitmap.Config.ARGB_8888);
        background = Bitmap.createBitmap(PER_WIDTH, PER_HEIGHT, Bitmap.Config.ARGB_8888);
        brick = Bitmap.createScaledBitmap(brick, PER_WIDTH, PER_HEIGHT, false);
        wall = Bitmap.createScaledBitmap(wall, PER_WIDTH, PER_HEIGHT, false);
        Canvas canvas = new Canvas(background);
        canvas.drawColor(AppCache.getInstance().getContext().getResources().getColor(R.color.game_background));
        canvas = new Canvas(infoBg);
        canvas.drawColor(AppCache.getInstance().getContext().getResources().getColor(R.color.game_info_bg));

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
        bomber = new Bomber(this, bombs);
        generatePropAndEmys();
    }

    public Bitmap getInfoBackground() {
        return infoBg;
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

    private void generatePropAndEmys() {
        switch (propType) {
            case PROP_TYPE_LENGTH:
                prop = new PropLength(this);
                break;
            case PROP_TYPE_COUNT:
                prop = new PropCount(this);
                break;
            case PROP_TYPE_SPEED:
                prop = new PropSpeed(this);
                break;
        }
        emys.clear();
        for (int i = 0; i < type1; i++) {
            emys.add(new EmyBase(this));
        }
        for (int i = 0; i < type2; i++) {
            emys.add(new EmySpeed(this));
        }
        for (int i = 0; i < type3; i++) {
            emys.add(new EmyTrack(this));
        }
        for (int i = 0; i < type4; i++) {
            emys.add(new EmyThrough(this));
        }

    }

    public void reStart() {
        X_OFFSET = X_MARGIN;
        Y_OFFSET = Y_MARGIN;
        mapLevel = 1;
        bombCount = 1;
        bomber.reset();
        door.reset();
        prop.reset();
        Bomb.resetLength();
        setMapLevel(mapLevel);
        generateMapInfo();
        generatePropAndEmys();
    }

    public void nextLevel() {
        X_OFFSET = X_MARGIN;
        Y_OFFSET = Y_MARGIN;
        bomber.reset();
        door.reset();
        prop.reset();
        mapLevel++;
        setMapLevel(mapLevel);
        generateMapInfo();
        generatePropAndEmys();
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
                type1 = 6;
                type2 = 0;
                type3 = 0;
                type4 = 0;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_LENGTH;
                break;
            case 2:
                type1 = 4;
                type2 = 2;
                type3 = 0;
                type4 = 0;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_COUNT;
                break;
            case 3:
                type1 = 2;
                type2 = 2;
                type3 = 0;
                type4 = 2;
                percent = WALL_PERCENT_20;
                propType = PROP_TYPE_SPEED;
                break;
            case 4:
                type1 = 2;
                type2 = 2;
                type3 = 0;
                type4 = 4;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_COUNT;
                break;
            case 5:
                type1 = 2;
                type2 = 2;
                type3 = 2;
                type4 = 2;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_LENGTH;
                break;
            case 6:
                type1 = 0;
                type2 = 3;
                type3 = 3;
                type4 = 3;
                percent = WALL_PERCENT_25;
                propType = PROP_TYPE_COUNT;
                break;
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
