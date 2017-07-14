package com.unual.bomberman.bean;

import android.content.Context;
import android.util.Log;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends MoveModel implements IControl {
    private boolean setBomb;

    public Bomber(int iconId, IDirection iDirection, int perWidth, int perHeight) {
        super(iconId, iDirection, perWidth, perHeight);
        speed_value = (float) (1.0 / LEVEL[0]);
    }

    @Override
    public void initLocation(Location location) {
        location.x = 1;
        location.y = 1;
    }


    public boolean isSetBomb() {
        return setBomb;
    }

    public void setSetBomb(boolean setBomb) {
        this.setBomb = setBomb;
    }


    @Override
    public void onCrossRoad() {

    }

    @Override
    public void onDirectionError() {

    }

    @Override
    public void setAction(int action) {

    }

    @Override
    public void setDirection(int direction) {
        nextDirection = direction;
    }

    @Override
    public void setSpeed(int speedValue) {
        speed_value = speedValue;
    }
}
