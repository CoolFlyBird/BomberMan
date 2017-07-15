package com.unual.bomberman.bean;

import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/14.
 */

public abstract class MoveModel extends Model implements IDirection {
    protected static int[] LEVEL = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5};
    protected int nextDirection;
    protected float speed_value;
    protected byte[][] mapInfos;
    protected boolean death;
    private Speed speed;

    public MoveModel(int resId, byte[][] mapInfo, int perWidth, int perHeight) {
        super(resId, perWidth, perHeight);
        this.mapInfos = mapInfo;
        speed = new Speed();
    }

    public abstract void initLocation(Location location);

    public abstract void onCrossRoad();

    public abstract void onPoint();

    public abstract void onDirectionError();

    private void updataLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    private void checkDeath() {
        if (mapInfos[location.x][location.y] == MapView.GameConfig.TYPE_FIRE) {
            death = true;
        } else {
            if (location.yOffset > -0.2 && location.y > 0 && mapInfos[location.x][location.y - 1] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.yOffset > 0.2 && location.y < MapView.GameConfig.heightSize - 2 && mapInfos[location.x][location.y + 1] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset > -0.2 && location.x > 0 && mapInfos[location.x - 1][location.y] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset > 0.2 && location.x < MapView.GameConfig.widthSize - 2 && mapInfos[location.x + 1][location.y] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
        }

    }

    private void changeDirectionCheck() {
        if (onXYLine()) {
            if (location.x % 2 != 0 && location.x % 2 != 0) {
                onCrossRoad();
            }
            onPoint();
            switch (nextDirection) {
                case IControl.DIRECTION_UP:
                    if (canWalkUp(location.x, location.y)) {
                        speed.xSpeed = 0;
                        speed.ySpeed = -speed_value;
                    } else {
                        nextDirection = IControl.DIRECTION_NONE;
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_DOWN:
                    if (canWalkDown(location.x, location.y)) {
                        speed.xSpeed = 0;
                        speed.ySpeed = speed_value;
                    } else {
                        nextDirection = IControl.DIRECTION_NONE;
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_LEFT:
                    if (canWalkLeft(location.x, location.y)) {
                        speed.xSpeed = -speed_value;
                        speed.ySpeed = 0;
                    } else {
                        nextDirection = IControl.DIRECTION_NONE;
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_RIGHT:
                    if (canWalkRight(location.x, location.y)) {
                        speed.xSpeed = speed_value;
                        speed.ySpeed = 0;
                    } else {
                        nextDirection = IControl.DIRECTION_NONE;
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_NONE:
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                    break;
            }
        }
    }

    private boolean onXLine() {
        if ((Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    private boolean onYLine() {
        if ((Math.abs(location.xOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    private boolean onXYLine() {
        if ((Math.abs(location.xOffset) <= 10e-6) && (Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        checkDeath();
        changeDirectionCheck();
        updataLocation();
        if (!death)
            canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }

}
