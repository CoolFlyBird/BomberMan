
package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;

/**
 * Created by unual on 2017/7/12.
 */

public class EmySpeed extends EmyBase {
    private static final int PERCENT = 3;
    private int walkOverCrossRoadCount = 0;
    private int waitError = 0;

    public EmySpeed(GameConfig gameConfig) {
        super(gameConfig);
        level = 4;
        speed_value = (float) (1.0 / LEVEL[level]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_emy_speed);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
    }

    @Override
    public void onCrossRoad() {
        walkOverCrossRoadCount++;
        if (walkOverCrossRoadCount % PERCENT == 0)
            setRandomDirection();
    }


    @Override
    public void onPoint() {
    }

    public void onDirectionError() {
        waitError++;
        if (waitError == GameConfig.MAP_FPS / 4) {
            waitError = 0;
            setRandomDirection();
        }
    }
}