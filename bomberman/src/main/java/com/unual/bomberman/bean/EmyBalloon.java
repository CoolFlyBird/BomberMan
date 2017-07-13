package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.interfaces.IDirection;
import com.unual.bomberman.widget.GameConfig;

import java.util.Random;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyBalloon extends BaseModel {
    private static Random random = new Random();
    private static final int PERCENT = 4;
    private int walkOverCrossRoadCount = 0;

    public EmyBalloon(Context context, int resId, IDirection iDirection, int perWidth, int perHeight) {
        super(context, resId, iDirection, perWidth, perHeight);
        speed_value = (float) (1.0 / LEVEL[0]);
    }

    @Override
    public void initLocation(Location location) {
        int x = random.nextInt(GameConfig.getInstance().width - 2) + 1;
        int y = random.nextInt(GameConfig.getInstance().height - 2) + 1;
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
        nextDirection = random.nextInt(4) + 1;
        walkOverCrossRoadCount = random.nextInt(PERCENT);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
