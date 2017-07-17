package com.unual.bomberman.bean;

import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyBall extends MoveModel {
    private static final int PERCENT = 4;
    private int walkOverCrossRoadCount = 0;
    private int waitError = 0;

    public EmyBall(int resId, byte[][] mapInfo, int perWidth, int perHeight) {
        super(resId, mapInfo, perWidth, perHeight);
        speed_value = (float) (1.0 / LEVEL[0]);
    }

    @Override
    public void initLocation(Location location) {
        int x = AppCache.getInstance().getGameConfig().random.nextInt(AppCache.getInstance().getGameConfig().getWidthSize() - 2) + 1;
        int y = AppCache.getInstance().getGameConfig().random.nextInt(AppCache.getInstance().getGameConfig().getHeightSize() - 2) + 1;
        location.x = x;
        location.y = y;
        setRandomDirection();
    }

    @Override
    public void onCrossRoad() {
        walkOverCrossRoadCount++;
        if (walkOverCrossRoadCount % PERCENT == 0)
            setRandomDirection();
    }

    @Override
    public void onPoint() {
    }

    public void onDirectionError() {
        waitError++;
        if (waitError == MapView.GameConfig.mapFps / 3) {
            waitError = 0;
            setRandomDirection();
        }
    }

    private void setRandomDirection() {
        nextDirection = AppCache.getInstance().getGameConfig().random.nextInt(4) + 1;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public boolean canWalkUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        int mapInfo = mapInfos[x][y - 1];
        if (mapInfo == MapView.GameConfig.TYPE_BACKGROUND) {
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
        if (mapInfo == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canWalkRight(int x, int y) {
        if (x >= MapView.GameConfig.widthSize - 1) {
            return false;
        }
        int mapInfo = mapInfos[x + 1][y];
        if (mapInfo == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canWalkDown(int x, int y) {
        if (y >= MapView.GameConfig.heightSize - 1) {
            return false;
        }
        int mapInfo = mapInfos[x][y + 1];
        if (mapInfo == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }
}
