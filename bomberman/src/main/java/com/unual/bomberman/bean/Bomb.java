package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;
import com.unual.bomberman.view.MapView;

import java.util.TimerTask;

/**
 * Created by unual on 2017/7/14.
 */

public class Bomb extends BaseModel {
    private static final int BOMB_TIME = 3000;
    private static final int BOOM_TIME = 1000;
    private int bombLength = 2;
    private boolean calcul;
    private boolean isPlaced;
    private boolean boom;
    private BombCallback callback;
    private Bitmap horizon, vertical, center;
    private int upLength = 0;
    private int leftLength = 0;
    private int rightLength = 0;
    private int downLength = 0;

    public Bomb(BombCallback callback) {
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


    public boolean isPlaced() {
        return isPlaced;
    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
        mapInfo[location.x][location.y] = MapView.GameConfig.TYPE_TEMP;
        setPlaced();
    }

    public void setPlaced() {
        isPlaced = true;
        AppCache.getInstance().setDelayTask(new TimerTask() {
            @Override
            public void run() {
                mapInfo[location.x][location.y] = MapView.GameConfig.TYPE_TEMP;
                boom = true;
                calcul = true;
                callback.onFireOn(location.x, location.y, boomUpLength(), boomLeftLength(), boomRightLength(), boomDownLength());
                calcul = false;
                try {
                    Thread.currentThread().sleep(BOOM_TIME);
                    boom = false;
                    isPlaced = false;
                    mapInfo[location.x][location.y] = MapView.GameConfig.TYPE_BACKGROUND;
                    callback.onFireOff(location.x, location.y, boomUpLength(), boomLeftLength(), boomRightLength(), boomDownLength());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, BOMB_TIME);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isPlaced) {
            if (boom) {
                canvas.drawBitmap(center, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
                if (upLength != 0) {
                    for (int i = upLength; i > 0; i--) {
                        canvas.drawBitmap(vertical, location.x * perWidth, (location.y - i) * perHeight, null);
                    }
                }
                if (leftLength != 0) {
                    for (int i = leftLength; i > 0; i--) {
                        canvas.drawBitmap(horizon, (location.x - i) * perWidth, location.y * perHeight, null);
                    }
                }
                if (rightLength != 0) {
                    for (int i = rightLength; i > 0; i--) {
                        canvas.drawBitmap(horizon, (location.x + i) * perWidth, location.y * perHeight, null);
                    }
                }
                if (downLength != 0) {
                    for (int i = downLength; i > 0; i--) {
                        canvas.drawBitmap(vertical, location.x * perWidth, (location.y + i) * perHeight, null);
                    }
                }
            } else {
                canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
            }
        } else {

        }
    }

    @Override
    public boolean canUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        if (mapInfo[x][y - 1] <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canDown(int x, int y) {
        if (y >= MapView.GameConfig.heightSize - 1) {
            return false;
        }
        if (mapInfo[x][y + 1] <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        if (mapInfo[x - 1][y] <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canRight(int x, int y) {
        if (x >= MapView.GameConfig.widthSize - 1) {
            return false;
        }
        if (mapInfo[x + 1][y] <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean isBackGround(int x, int y) {
        if (x < 0 || y > MapView.GameConfig.widthSize - 1) {
            return false;
        }
        if (mapInfo[x][y] == MapView.GameConfig.TYPE_BACKGROUND || mapInfo[x][y] == MapView.GameConfig.TYPE_TEMP) {
            return true;
        }
        return false;
    }


    private int boomUpLength() {
        if (calcul) {
            int l = 0;
            for (int i = 0; i < bombLength; i++) {
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
            for (int i = 0; i < bombLength; i++) {
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
            for (int i = 0; i < bombLength; i++) {
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
            for (int i = 0; i < bombLength; i++) {
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

    public interface BombCallback {
        void onFireOn(int x, int y, int upLength, int leftLength, int rightLength, int downLength);

        void onFireOff(int x, int y, int upLength, int leftLength, int rightLength, int downLength);
    }
}
