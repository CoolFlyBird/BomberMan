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
    private int bombLength = 1;
    private boolean isPlaced;
    private boolean boom;
    Bitmap horizon, vertical, center;
    private byte[][] mapInfo;
    private BombCallback callback;

    public Bomb(BombCallback callback, byte[][] mapInfo, int resId, int perWidth, int perHeight) {
        super(resId, perWidth, perHeight);
        this.callback = callback;
        this.mapInfo = mapInfo;
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        horizon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_horizon);
        horizon = Bitmap.createScaledBitmap(horizon, perWidth, perHeight, false);
        vertical = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_vertical);
        vertical = Bitmap.createScaledBitmap(vertical, perWidth, perHeight, false);
        center = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_center);
        center = Bitmap.createScaledBitmap(center, perWidth, perHeight, false);
    }

    @Override
    public void initLocation(Location location) {

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
                boom = true;
                callback.onFireOn(location.x, location.y, bombLength);
                Log.e("123", location.x + "," + location.y);
                try {
                    Thread.currentThread().sleep(1000);
                    boom = false;
                    isPlaced = false;
                    mapInfo[location.x][location.y] = MapView.GameConfig.TYPE_BACKGROUND;
                    callback.onFireOff(location.x, location.y, bombLength);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isPlaced) {
            if (boom) {
                canvas.drawBitmap(center, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
                if (canLeft())
                    canvas.drawBitmap(horizon, (location.x - 1 + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
                if (canRight())
                    canvas.drawBitmap(horizon, (location.x + 1 + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
                if (canUp())
                    canvas.drawBitmap(vertical, (location.x + location.xOffset) * perWidth, (location.y - 1 + location.yOffset) * perHeight, null);
                if (canDown())
                    canvas.drawBitmap(vertical, (location.x + location.xOffset) * perWidth, (location.y + 1 + location.yOffset) * perHeight, null);
            } else {
                canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
            }
        } else {

        }

    }

    public boolean canUp() {
        if (location.y < 1) {
            return false;
        }
        int mapInfo = this.mapInfo[location.x][location.y - 1];
        if (mapInfo <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canLeft() {
        if (location.x < 1) {
            return false;
        }
        int mapInfo = this.mapInfo[location.x - 1][location.y];
        if (mapInfo <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canRight() {
        if (location.x >= MapView.GameConfig.widthSize - 1) {
            return false;
        }
        int mapInfo = this.mapInfo[location.x + 1][location.y];
        if (mapInfo <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canDown() {
        if (location.y >= MapView.GameConfig.heightSize - 1) {
            return false;
        }
        int mapInfo = this.mapInfo[location.x][location.y + 1];
        if (mapInfo <= MapView.GameConfig.TYPE_WALL) {
            return true;
        }
        return false;
    }

    public interface BombCallback {
        void onFireOn(int x, int y, int length);

        void onFireOff(int x, int y, int length);
    }
}
