package com.unual.bomberman.bean;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;

import java.util.Random;

/**
 * Created by unual on 2017/7/19.
 */

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
            x = AppCache.getInstance().getRandom().nextInt(AppCache.getInstance().getGameConfig().getWidthSize() - 2) + 1;
            y = AppCache.getInstance().getRandom().nextInt(AppCache.getInstance().getGameConfig().getHeightSize() - 2) + 1;
            if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2) && !(x % 2 == 0 && y % 2 == 0)) {
                info[x][y] = GameConfig.TYPE_WALL;
                props_x1 = x;
                props_y1 = y;
                break;
            }
        }
        while (true) {
            x = AppCache.getInstance().getRandom().nextInt(AppCache.getInstance().getGameConfig().getWidthSize() - 2) + 1;
            y = AppCache.getInstance().getRandom().nextInt(AppCache.getInstance().getGameConfig().getHeightSize() - 2) + 1;
            if (!(x == 1 && y == 1) && !(x == 2 && y == 1) && !(x == 1 && y == 2) && !(x == props_x1 && y == props_y1) && !(x % 2 == 0 && y % 2 == 0)) {
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
