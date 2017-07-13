package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected static int[] LEVEL = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5};
    protected Bitmap icon;
    protected int nextDirection;
    protected Location location;
    protected IDirection iDirection;
    protected float speed_value;
    private Speed speed;
    private int perWidth, perHeight;

    public BaseModel(Context context, int resId, IDirection iDirection, int perWidth, int perHeight) {
        icon = BitmapFactory.decodeResource(context.getResources(), resId);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        this.iDirection = iDirection;
        this.perHeight = perHeight;
        this.perWidth = perWidth;
        location = new Location();
        speed = new Speed();
        initLocation(location);
    }

    public abstract void initLocation(Location location);

    public abstract void onCrossRoad();

    public abstract void onDirectionError();

    private void updataLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    private void changeDirectionCheck() {
        if (onPoint()) {
            if (location.x % 2 != 0 && location.x % 2 != 0) {
                onCrossRoad();
            }
            switch (nextDirection) {
                case IControl.DIRECTION_UP:
                    if (iDirection.canWalkUp(location.x, location.y)) {
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
                    if (iDirection.canWalkDown(location.x, location.y)) {
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
                    if (iDirection.canWalkLeft(location.x, location.y)) {
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
                    if (iDirection.canWalkRight(location.x, location.y)) {
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

    public boolean onPoint() {
        if ((Math.abs(location.xOffset) <= 10e-6) && (Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        changeDirectionCheck();
        updataLocation();
        canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }

}
