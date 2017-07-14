package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.interfaces.IDirection;

import java.util.Random;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyBalloon extends MoveModel {
    private static final int PERCENT = 4;
    private int walkOverCrossRoadCount = 0;

    public EmyBalloon(int resId, IDirection iDirection, int perWidth, int perHeight) {
        super(resId, iDirection, perWidth, perHeight);
        speed_value = (float) (1.0 / LEVEL[0]);
    }

    @Override
    public void initLocation(Location location) {
        int x = AppCache.getInstance().getGameConfig().random.nextInt(AppCache.getInstance().getGameConfig().getWidthSize() - 2) + 1;
        int y = AppCache.getInstance().getGameConfig().random.nextInt(AppCache.getInstance().getGameConfig().getHeightSize() - 2) + 1;
        location.x = x;
        location.y = y;
        setRandomDirection();
    }

    @Override
    public void onCrossRoad() {
        walkOverCrossRoadCount++;
        if (walkOverCrossRoadCount % PERCENT == 0)
            setRandomDirection();
    }

    public void onDirectionError() {
        setRandomDirection();
    }

    private void setRandomDirection() {
        nextDirection = AppCache.getInstance().getGameConfig().random.nextInt(4) + 1;
        walkOverCrossRoadCount = AppCache.getInstance().getGameConfig().random.nextInt(PERCENT);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
