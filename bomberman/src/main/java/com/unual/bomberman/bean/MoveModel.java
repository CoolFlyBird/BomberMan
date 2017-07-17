package com.unual.bomberman.bean;

import android.graphics.Canvas;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/14.
 */

public abstract class MoveModel extends BaseModel {
    protected static int[] LEVEL = {15, 14, 13, 12, 11, 10};
    protected int nextDirection;
    protected float speed_value;
    protected boolean death;
    protected Speed speed;

    public abstract void initLocation();

    public abstract void onPoint();

    public abstract void onCrossRoad();

    public abstract void onDirectionError();

    @Override
    public boolean canUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        if (mapInfo[x][y - 1] == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        if (mapInfo[x - 1][y] == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canRight(int x, int y) {
        if (x >= MapView.GameConfig.widthSize - 1) {
            return false;
        }
        if (mapInfo[x + 1][y] == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canDown(int x, int y) {
        if (y >= MapView.GameConfig.heightSize - 1) {
            return false;
        }
        if (mapInfo[x][y + 1] == MapView.GameConfig.TYPE_BACKGROUND) {
            return true;
        }
        return false;
    }

    private void updateLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    public void checkDeath() {
        if (mapInfo[location.x][location.y] == MapView.GameConfig.TYPE_FIRE) {
            death = true;
        } else {
            if (location.yOffset < -0.5 && location.y > 0 && mapInfo[location.x][location.y - 1] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.yOffset > 0.5 && location.y < MapView.GameConfig.heightSize - 2 && mapInfo[location.x][location.y + 1] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset < -0.5 && location.x > 0 && mapInfo[location.x - 1][location.y] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset > 0.5 && location.x < MapView.GameConfig.widthSize - 2 && mapInfo[location.x + 1][location.y] == MapView.GameConfig.TYPE_FIRE) {
                death = true;
            }
        }
    }

    public void changeDirectionCheck() {
        if (onXYLine()) {
            onPoint();
            if (location.x % 2 != 0 && location.y % 2 != 0) {
                onCrossRoad();
            }
            switch (nextDirection) {
                case IControl.DIRECTION_UP:
                    if (canUp(location.x, location.y)) {
                        speed.xSpeed = 0;
                        speed.ySpeed = -speed_value;
                    } else {
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_DOWN:
                    if (canDown(location.x, location.y)) {
                        speed.xSpeed = 0;
                        speed.ySpeed = speed_value;
                    } else {
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_LEFT:
                    if (canLeft(location.x, location.y)) {
                        speed.xSpeed = -speed_value;
                        speed.ySpeed = 0;
                    } else {
                        speed.xSpeed = 0;
                        speed.ySpeed = 0;
                        onDirectionError();
                    }
                    break;
                case IControl.DIRECTION_RIGHT:
                    if (canRight(location.x, location.y)) {
                        speed.xSpeed = speed_value;
                        speed.ySpeed = 0;
                    } else {
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

    boolean onXLine() {
        if ((Math.abs(location.yOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    boolean onYLine() {
        if ((Math.abs(location.xOffset) <= 10e-6)) {
            return true;
        }
        return false;
    }

    boolean onXYLine() {
        if (onXLine() && onYLine()) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        checkDeath();
        changeDirectionCheck();
        updateLocation();
        if (!death)
            canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }

}
