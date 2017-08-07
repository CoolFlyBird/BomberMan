package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;

import java.util.TimerTask;

/**
 * Created by unual on 2017/7/14.
 */

public class Bomb extends BaseModel {
    private static final int BOMB_TIME = 3000;
    private static final int BOOM_TIME = 1000;
    private static int BOMB_LENGTH = 1;
    private boolean calcul;
    private boolean isPlaced;
    private boolean boom;
    private BombCallback callback;
    private Bitmap horizon, vertical, center;
    private int upLength = 0;
    private int leftLength = 0;
    private int rightLength = 0;
    private int downLength = 0;

    public Bomb(GameConfig gameConfig, BombCallback callback) {
        super(gameConfig);
        this.callback = callback;
        location = new Location();
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        horizon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_horizon);
        horizon = Bitmap.createScaledBitmap(horizon, perWidth, perHeight, false);
        vertical = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_vertical);
        vertical = Bitmap.createScaledBitmap(vertical, perWidth, perHeight, false);
        center = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_center);
        center = Bitmap.createScaledBitmap(center, perWidth, perHeight, false);
    }

    public static int getBombLength() {
        return BOMB_LENGTH;
    }

    public static void setBombLength(int length) {
        BOMB_LENGTH = length;
    }

    public static void resetLength() {
        BOMB_LENGTH = 1;
    }

    public static void increaseLength() {
        BOMB_LENGTH++;
    }

    public BombCallback getCallback() {
        return callback;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
        mapInfo[location.x][location.y] = GameConfig.MAP_TYPE_TEMP;
        setPlaced();
    }

    public void setPlaced() {
        isPlaced = true;
        AppCache.getInstance().setDelayTask(new TimerTask() {
            @Override
            public void run() {
                mapInfo[location.x][location.y] = GameConfig.MAP_TYPE_TEMP;
                boom = true;
                calcul = true;
                callback.onFireOn(location.x, location.y, boomUpLength(), boomLeftLength(), boomRightLength(), boomDownLength());
                calcul = false;
                try {
                    Thread.currentThread().sleep(BOOM_TIME);
                    boom = false;
                    isPlaced = false;
                    mapInfo[location.x][location.y] = GameConfig.MAP_TYPE_BACKGROUND;
                    callback.onFireOff(location.x, location.y, boomUpLength(), boomLeftLength(), boomRightLength(), boomDownLength());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, BOMB_TIME);
    }

    public boolean canUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        if (mapInfo[x][y - 1] <= GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canDown(int x, int y) {
        if (y >= GameConfig.HEIGHT_SIZE - 1) {
            return false;
        }
        if (mapInfo[x][y + 1] <= GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        if (mapInfo[x - 1][y] <= GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canRight(int x, int y) {
        if (x >= GameConfig.WIDTH_SIZE - 1) {
            return false;
        }
        if (mapInfo[x + 1][y] <= GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean isBackGround(int x, int y) {
        if (x < 0 || y > GameConfig.WIDTH_SIZE - 1) {
            return false;
        }
        if (mapInfo[x][y] == GameConfig.MAP_TYPE_BACKGROUND || mapInfo[x][y] == GameConfig.MAP_TYPE_TEMP) {
            return true;
        }
        return false;
    }


    private int boomUpLength() {
        if (calcul) {
            int l = 0;
            for (int i = 0; i < BOMB_LENGTH; i++) {
                if (canUp(location.x, location.y - i)) {
                    if (!isBackGround(location.x, location.y - i)) break;
                    l = i + 1;
                } else {
                    break;
                }
            }
            upLength = l;
        }
        return upLength;
    }

    private int boomDownLength() {
        if (calcul) {
            int l = 0;
            for (int i = 0; i < BOMB_LENGTH; i++) {
                if (canDown(location.x, location.y + i)) {
                    if (!isBackGround(location.x, location.y + i)) break;
                    l = i + 1;
                } else {
                    break;
                }
            }
            downLength = l;
        }
        return downLength;

    }

    private int boomLeftLength() {
        if (calcul) {
            int l = 0;
            for (int i = 0; i < BOMB_LENGTH; i++) {
                if (canLeft(location.x - i, location.y)) {
                    if (!isBackGround(location.x - i, location.y)) break;
                    l = i + 1;
                } else {
                    break;
                }
            }
            leftLength = l;
        }
        return leftLength;
    }


    private int boomRightLength() {
        if (calcul) {
            int l = 0;
            for (int i = 0; i < BOMB_LENGTH; i++) {
                if (canRight(location.x + i, location.y)) {
                    if (!isBackGround(location.x + i, location.y)) break;
                    l = i + 1;
                } else {
                    break;
                }
            }
            rightLength = l;
        }
        return rightLength;
    }

    public void checkBoom() {
        if (mapInfo[location.x][location.y] == GameConfig.MAP_TYPE_FIRE) {
            boom = true;
        } else {
            if (location.yOffset < -0.5 && location.y > 0 && mapInfo[location.x][location.y - 1] == GameConfig.MAP_TYPE_FIRE) {
                boom = true;
            }
            if (location.yOffset > 0.5 && location.y < GameConfig.HEIGHT_SIZE - 2 && mapInfo[location.x][location.y + 1] == GameConfig.MAP_TYPE_FIRE) {
                boom = true;
            }
            if (location.xOffset < -0.5 && location.x > 0 && mapInfo[location.x - 1][location.y] == GameConfig.MAP_TYPE_FIRE) {
                boom = true;
            }
            if (location.xOffset > 0.5 && location.x < GameConfig.WIDTH_SIZE - 2 && mapInfo[location.x + 1][location.y] == GameConfig.MAP_TYPE_FIRE) {
                boom = true;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isPlaced) {
            checkBoom();
            if (boom) {
                canvas.drawBitmap(center, GameConfig.X_OFFSET + location.x * perWidth, GameConfig.Y_OFFSET + location.y * perHeight, null);
                if (upLength != 0) {
                    for (int i = upLength; i > 0; i--) {
                        canvas.drawBitmap(vertical, GameConfig.X_OFFSET + location.x * perWidth, GameConfig.Y_OFFSET + (location.y - i) * perHeight, null);
                    }
                }
                if (leftLength != 0) {
                    for (int i = leftLength; i > 0; i--) {
                        canvas.drawBitmap(horizon, GameConfig.X_OFFSET + (location.x - i) * perWidth, GameConfig.Y_OFFSET + location.y * perHeight, null);
                    }
                }
                if (rightLength != 0) {
                    for (int i = rightLength; i > 0; i--) {
                        canvas.drawBitmap(horizon, GameConfig.X_OFFSET + (location.x + i) * perWidth, GameConfig.Y_OFFSET + location.y * perHeight, null);
                    }
                }
                if (downLength != 0) {
                    for (int i = downLength; i > 0; i--) {
                        canvas.drawBitmap(vertical, GameConfig.X_OFFSET + location.x * perWidth, GameConfig.Y_OFFSET + (location.y + i) * perHeight, null);
                    }
                }
            } else {
                canvas.drawBitmap(icon, GameConfig.X_OFFSET + location.x * perWidth, GameConfig.Y_OFFSET + location.y * perHeight, null);
            }
        } else {

        }
    }

    public interface BombCallback {
        void onFireOn(int x, int y, int upLength, int leftLength, int rightLength, int downLength);

        void onFireOff(int x, int y, int upLength, int leftLength, int rightLength, int downLength);
    }
}
