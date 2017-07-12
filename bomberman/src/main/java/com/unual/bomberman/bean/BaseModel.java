package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;

/**
 * Created by unual on 2017/7/7.
 */

public abstract class BaseModel {
    protected float speed_value = 0.1f;
    protected Bitmap icon;
    protected int nextDirection;
    protected Location location;
    protected IDirection iDirection;
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

    public abstract void onDirectionError();

    private void updataLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    private void changeDirection() {
        switch (nextDirection) {
            case IControl.DIRECTION_UP:
                if (!onYLine()) break;
                speed.xSpeed = 0;
                speed.ySpeed = -speed_value;
                if (onPoint() && !iDirection.canWalkUp(location.x, location.y)) {
                    nextDirection = IControl.DIRECTION_NONE;
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                    onDirectionError();
                }
                break;
            case IControl.DIRECTION_DOWN:
                if (!onYLine()) break;
                speed.xSpeed = 0;
                speed.ySpeed = speed_value;
                if (onPoint() && !iDirection.canWalkDown(location.x, location.y)) {
                    nextDirection = IControl.DIRECTION_NONE;
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                    onDirectionError();
                }
                break;
            case IControl.DIRECTION_LEFT:
                if (!onXLine()) break;
                speed.xSpeed = -speed_value;
                speed.ySpeed = 0;
                if (onPoint() && !iDirection.canWalkLeft(location.x, location.y)) {
                    nextDirection = IControl.DIRECTION_NONE;
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                    onDirectionError();
                }
                break;
            case IControl.DIRECTION_RIGHT:
                if (!onXLine()) break;
                speed.xSpeed = speed_value;
                speed.ySpeed = 0;
                if (onPoint() && !iDirection.canWalkRight(location.x, location.y)) {
                    nextDirection = IControl.DIRECTION_NONE;
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                    onDirectionError();
                }
                break;
            case IControl.DIRECTION_NONE:
                if (onPoint()) {
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                }
                break;
        }
    }

    private boolean onXLine() {
        if ((Math.abs(location.yOffset) <= 10e-5)) {
            return true;
        }
        return false;
    }

    private boolean onYLine() {
        if ((Math.abs(location.xOffset) <= 10e-5)) {
            return true;
        }
        return false;
    }

    private boolean onPoint() {
        if ((Math.abs(location.xOffset) <= 10e-5) && (Math.abs(location.yOffset) <= 10e-5)) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        changeDirection();
        updataLocation();
        canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }

}
