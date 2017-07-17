package com.unual.bomberman.bean;

import android.util.Log;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends MoveModel implements IControl {
    private boolean nextBomb;
    private Bomb bomb;

    public Bomber(Bomb bomb, byte[][] mapInfo, int iconId, int perWidth, int perHeight) {
        super(iconId, mapInfo, perWidth, perHeight);
        this.bomb = bomb;
        speed_value = (float) (1.0 / LEVEL[0]);
    }

    @Override
    public void initLocation(Location location) {
        location.x = 1;
        location.y = 1;
    }


    @Override
    public void onCrossRoad() {

    }

    @Override
    public void onPoint() {
        if (nextBomb) {
            bomb.setLocation(location.x, location.y);
            nextBomb = false;
        }
    }

    @Override
    public void onDirectionError() {

    }

    @Override
    public void setAction(int action) {
        if (bomb.isPlaced()) return;
        nextBomb = true;
    }

    @Override
    public void setDirection(int direction) {
        nextDirection = direction;
    }

    @Override
    public void setSpeed(int speedValue) {
        speed_value = speedValue;
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
