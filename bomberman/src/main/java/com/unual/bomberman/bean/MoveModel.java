package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;

/**
 * Created by unual on 2017/7/14.
 */

public abstract class MoveModel extends BaseModel {
    protected static int[] LEVEL = {
            (int) (GameConfig.MAP_FPS / 1.2),//level 0
            (int) (GameConfig.MAP_FPS / 1.6),//level 1
            (int) (GameConfig.MAP_FPS / 2.0),  //level 2
            (int) (GameConfig.MAP_FPS / 2.4),//level 3
            (int) (GameConfig.MAP_FPS / 2.8),//level 4
            (int) (GameConfig.MAP_FPS / 3.2),//level 5
            (int) (GameConfig.MAP_FPS / 3.6) //level 6
    };
    protected int nextDirection;
    protected float speed_value;
    protected Speed speed;
    protected int level;
    protected boolean death;
    protected int died_value;
    protected boolean removed;
    private Bitmap icon_x;
    private Bitmap icon_xx;
    private Bitmap icon_xxx;

    public MoveModel(GameConfig gameConfig) {
        super(gameConfig);
    }

    public abstract void initLocation();

    public abstract void onPoint();

    public abstract void onCrossRoad();

    public abstract void onDirectionError();

    public abstract boolean meetWith(BaseModel model);

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void die() {
        death = true;
        speed.xSpeed = 0;
        speed.ySpeed = 0;
        speed_value = 0;
        died_value++;
        if (icon_x == null) {
            icon_x = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_x);
            icon_x = Bitmap.createScaledBitmap(icon_x, perWidth, perHeight, false);

            icon_xx = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_xx);
            icon_xx = Bitmap.createScaledBitmap(icon_xx, perWidth, perHeight, false);

            icon_xxx = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_xxx);
            icon_xxx = Bitmap.createScaledBitmap(icon_xxx, perWidth, perHeight, false);
        }
    }

    public boolean canUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        if (mapInfo[x][y - 1] < GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        if (mapInfo[x - 1][y] < GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canRight(int x, int y) {
        if (x >= GameConfig.WIDTH_SIZE - 1) {
            return false;
        }
        if (mapInfo[x + 1][y] < GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    public boolean canDown(int x, int y) {
        if (y >= GameConfig.HEIGHT_SIZE - 1) {
            return false;
        }
        if (mapInfo[x][y + 1] < GameConfig.MAP_TYPE_WALL) {
            return true;
        }
        return false;
    }

    protected void updateLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    public boolean checkDeath() {
        if (mapInfo[location.x][location.y] == GameConfig.MAP_TYPE_FIRE) {
            death = true;
        } else {
            if (location.yOffset < -0.5 && location.y > 0 && mapInfo[location.x][location.y - 1] == GameConfig.MAP_TYPE_FIRE) {
                death = true;
            }
            if (location.yOffset > 0.5 && location.y < GameConfig.HEIGHT_SIZE - 2 && mapInfo[location.x][location.y + 1] == GameConfig.MAP_TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset < -0.5 && location.x > 0 && mapInfo[location.x - 1][location.y] == GameConfig.MAP_TYPE_FIRE) {
                death = true;
            }
            if (location.xOffset > 0.5 && location.x < GameConfig.WIDTH_SIZE - 2 && mapInfo[location.x + 1][location.y] == GameConfig.MAP_TYPE_FIRE) {
                death = true;
            }
        }
        return death;
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
        if (checkDeath()) die();
        if(icon!=null)
        canvas.drawBitmap(icon, GameConfig.X_OFFSET + GameConfig.X_DIRECTION_OFFSET + (location.x + location.xOffset) * perWidth, GameConfig.Y_OFFSET + GameConfig.Y_DIRECTION_OFFSET + (location.y + location.yOffset) * perHeight, null);
        if (died_value >= GameConfig.MAP_FPS / 3 && died_value <= GameConfig.MAP_FPS * 2 / 3) {
            canvas.drawBitmap(icon_x, GameConfig.X_OFFSET + GameConfig.X_DIRECTION_OFFSET + (location.x + location.xOffset) * perWidth, GameConfig.Y_OFFSET + GameConfig.Y_DIRECTION_OFFSET + (location.y + location.yOffset) * perHeight, null);
        } else if (died_value >= GameConfig.MAP_FPS * 2 / 3 && died_value <= GameConfig.MAP_FPS) {
            canvas.drawBitmap(icon_xx, GameConfig.X_OFFSET + GameConfig.X_DIRECTION_OFFSET + (location.x + location.xOffset) * perWidth, GameConfig.Y_OFFSET + GameConfig.Y_DIRECTION_OFFSET + (location.y + location.yOffset) * perHeight, null);
        } else if (died_value >= GameConfig.MAP_FPS && died_value <= GameConfig.MAP_FPS * 4 / 3) {
            canvas.drawBitmap(icon_xxx, GameConfig.X_OFFSET + GameConfig.X_DIRECTION_OFFSET + (location.x + location.xOffset) * perWidth, GameConfig.Y_OFFSET + GameConfig.Y_DIRECTION_OFFSET + (location.y + location.yOffset) * perHeight, null);
        } else if (died_value >= GameConfig.MAP_FPS * 4 / 3) {
            canvas.drawBitmap(icon_xxx, GameConfig.X_OFFSET + GameConfig.X_DIRECTION_OFFSET + (location.x + location.xOffset) * perWidth, GameConfig.Y_OFFSET + GameConfig.Y_DIRECTION_OFFSET + (location.y + location.yOffset) * perHeight, null);
            removed = true;
        }
    }

}
