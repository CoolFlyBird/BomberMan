package com.unual.bomberman.bean;

import android.graphics.Canvas;
import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyBase extends MoveModel {

    public EmyBase(GameConfig gameConfig) {
        super(gameConfig);
        location = new Location();
        speed = new Speed();
        initLocation();
    }

    @Override
    public void initLocation() {
        int x = AppCache.getInstance().getRandom().nextInt(GameConfig.WIDTH_SIZE - 2) + 1;
        int y = AppCache.getInstance().getRandom().nextInt(GameConfig.HEIGHT_SIZE - 2) + 1;
        if ((x <= 3 && y <= 3) || mapInfo[x][y] != GameConfig.MAP_TYPE_BACKGROUND) {
            initLocation();
        } else {
            location.x = x;
            location.y = y;
            setRandomDirection();
        }
    }

    @Override
    public void onCrossRoad() {
    }


    @Override
    public void onPoint() {
    }

    public void onDirectionError() {
    }

    @Override
    public boolean meetWith(BaseModel model) {
        return false;
    }

    protected void setRandomDirection() {
        nextDirection = AppCache.getInstance().getRandom().nextInt(4) + 1;
    }

    @Override
    public void draw(Canvas canvas) {
        changeDirectionCheck();
        updateLocation();
        super.draw(canvas);
    }

}