
package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyTrack extends EmyBase {
    private static final int PERCENT = 4;
    private Bomber bomber;
    private int walkOverCrossRoadCount = 0;
    private int waitError = 0;

    public EmyTrack(GameConfig gameConfig, Bomber bomber) {
        super(gameConfig);
        this.bomber = bomber;
        level = 0;
        speed_value = (float) (1.0 / LEVEL[level]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_emy_track);
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

    protected void setRandomDirection() {
        int x = bomber.location.x - location.x;
        int y = bomber.location.y - location.y;
        if (x >= 0 & y >= 0) {//右下
            nextDirection = AppCache.getInstance().getRandom().nextInt(2) + 3;
//            nextDirection = IControl.DIRECTION_DOWN;3
//            nextDirection = IControl.DIRECTION_RIGHT;4
        } else if (x >= 0 & y <= 0) {//右上
            nextDirection = AppCache.getInstance().getRandom().nextInt(2);
            if (nextDirection == 0) {
                nextDirection = IControl.DIRECTION_UP;
            } else {
                nextDirection = IControl.DIRECTION_RIGHT;
            }
//            nextDirection = IControl.DIRECTION_UP; 1
//            nextDirection = IControl.DIRECTION_RIGHT; 4
        } else if (x <= 0 & y >= 0) {//左下
            nextDirection = AppCache.getInstance().getRandom().nextInt(2) + 2;
//            nextDirection = IControl.DIRECTION_LEFT; 2
//            nextDirection = IControl.DIRECTION_DOWN; 3
        } else if (x <= 0 & y <= 0) {//左上
            nextDirection = AppCache.getInstance().getRandom().nextInt(2) + 1;
//            nextDirection = IControl.DIRECTION_UP; 1
//            nextDirection = IControl.DIRECTION_LEFT; 2
        }
    }

}