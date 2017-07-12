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

    public EmyBalloon(Context context, int resId, IDirection iDirection, int perWidth, int perHeight) {
        super(context, resId, iDirection, perWidth, perHeight);
    }

    @Override
    public void initLocation(Location location) {
        int x = random.nextInt(GameConfig.getInstance().width - 2) + 1;
        int y = random.nextInt(GameConfig.getInstance().height - 2) + 1;
        location.x = x;
        location.y = y;
        speed_value = GameConfig.getInstance().pf * 4;
//        speed_value = 0.1f;
        Log.e("123", "ball:" + speed_value);
        setRandomDirection();
    }

    private void setRandomDirection() {
        nextDirection = random.nextInt(4) + 1;
    }

    public void onDirectionError() {
        setRandomDirection();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
