
package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;
import com.unual.bomberman.view.MapView;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyBall extends MoveModel {
    private static final int PERCENT = 4;
    private int walkOverCrossRoadCount = 0;
    private int waitError = 0;

    public EmyBall() {
        location = new Location();
        speed = new Speed();
        speed_value = (float) (1.0 / LEVEL[0]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_ball);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        initLocation();
    }

    @Override
    public void initLocation() {
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


    @Override
    public void onPoint() {
    }

    public void onDirectionError() {
        waitError++;
        if (waitError == MapView.GameConfig.mapFps / 3) {
            waitError = 0;
            setRandomDirection();
        }
    }

    private void setRandomDirection() {
        nextDirection = AppCache.getInstance().getGameConfig().random.nextInt(4) + 1;
    }

    @Override
    public boolean meetWith(BaseModel model) {
        if (model instanceof EmyBall) {

        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

}