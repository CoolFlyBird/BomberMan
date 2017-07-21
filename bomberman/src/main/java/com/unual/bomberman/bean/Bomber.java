package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;

import java.util.List;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends MoveModel implements IControl {
    private boolean nextBomb;
    private List<Bomb> bombs;


    public Bomber(GameConfig gameConfig, List<Bomb> bombs) {
        super(gameConfig);
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

    }

    @Override
    public void onPoint() {
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
        if (model instanceof Door) {
            return (location.x == model.location.x) && (location.y == model.location.y);
        }
        return location.meetWith(model.location);
    }
}
