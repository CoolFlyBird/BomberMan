package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;

import java.util.List;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends MoveModel implements IControl {
    private List<Bomb> bombs;
    protected boolean nextBomb;
    protected boolean mapMoved;
    protected Location previousLocation;


    public Bomber(GameConfig gameConfig, List<Bomb> bombs) {
        super(gameConfig);
        previousLocation = new Location();
        this.bombs = bombs;
        location = new Location();
        speed = new Speed();
        level = 2;
        speed_value = (float) (1.0 / LEVEL[level]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_man);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        initLocation();
    }

    public void reset() {
        speed_value = (float) (1.0 / LEVEL[level]);
        died_value = 0;
        removed = false;
        death = false;
        nextDirection = DIRECTION_NONE;
        nextBomb = false;
        initLocation();
    }

    @Override
    public void initLocation() {
        location.xOffset = 0.0f;
        location.yOffset = 0.0f;
        location.x = 1;
        location.y = 1;
    }

    public boolean isMapMoved() {
        return mapMoved;
    }

    @Override
    protected void updateLocation() {
        Log.e("123", "updateLocation()");
        previousLocation.cloneFrom(location);
        super.updateLocation();
        if (nextDirection == IControl.DIRECTION_LEFT || nextDirection == IControl.DIRECTION_RIGHT)
            GameConfig.X_OFFSET = GameConfig.X_OFFSET - (int) (speed.xSpeed * perWidth);
        if (nextDirection == IControl.DIRECTION_UP || nextDirection == IControl.DIRECTION_DOWN)
            GameConfig.Y_OFFSET = GameConfig.Y_OFFSET - (int) (speed.ySpeed * perHeight);
        mapMoved = true;
        switch (nextDirection) {
            case DIRECTION_LEFT:
            case DIRECTION_RIGHT:
                GameConfig.X_DIRECTION_OFFSET = (int) (speed.xSpeed * perWidth);
                GameConfig.Y_DIRECTION_OFFSET = 0;
                break;
            case DIRECTION_UP:
            case DIRECTION_DOWN:
                GameConfig.X_DIRECTION_OFFSET = 0;
                GameConfig.Y_DIRECTION_OFFSET = (int) (speed.ySpeed * perHeight);
                break;
            default:
                GameConfig.X_DIRECTION_OFFSET = 0;
                GameConfig.Y_DIRECTION_OFFSET = 0;
                break;
        }
    }

    public void increaseSpeed() {
        level++;
        if (level < LEVEL.length)
            speed_value = (float) (1.0 / LEVEL[level]);
    }

    public void setSpeedLevel(int level) {
        this.level = level;
        speed_value = (float) (1.0 / LEVEL[level]);
    }

    public int getSpeedLevel() {
        return level;
    }

    @Override
    public void onCrossRoad() {
        Log.e("123", "onCrossRoad()");
    }

    @Override
    public void onPoint() {
        Log.e("123", "onPoint()");
        if (nextBomb && (mapInfo[location.x][location.y] != GameConfig.MAP_TYPE_TEMP)) {
            for (Bomb bomb : bombs) {
                if (!bomb.isPlaced()) {
                    bomb.setLocation(location.x, location.y);
                    nextBomb = false;
                    break;
                }
            }
        }
    }

    @Override
    public void onDirectionError() {

    }

    @Override
    public void setAction(int action) {
        int j = 0;
        for (Bomb bomb : bombs)
            if (bomb.isPlaced()) j++;
        if (j < bombs.size())
            nextBomb = true;
        else {

        }
    }

    @Override
    public void setDirection(int direction) {
        nextDirection = direction;
    }

    @Override
    public boolean meetWith(BaseModel model) {
        return location.meetWith(model.location);
    }

    @Override
    public void draw(Canvas canvas) {
        if (previousLocation.equalWith(location) && nextDirection == IControl.DIRECTION_NONE && !nextBomb) {
            mapMoved = false;
        } else {
            mapMoved = true;
            changeDirectionCheck();
            updateLocation();
        }
        super.draw(canvas);
    }

}
