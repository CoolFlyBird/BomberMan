
package com.unual.bomberman.bean;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;

/**
 * Created by unual on 2017/7/12.
 */

public class EmyThrough extends EmyBase {
    private static final int PERCENT = 4;
    private int walkOverCrossRoadCount = 0;
    private int waitError = 0;

    public EmyThrough(GameConfig gameConfig) {
        super(gameConfig);
        level = 0;
        speed_value = (float) (1.0 / LEVEL[level]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_emy_through);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
    }

    @Override
    public boolean canUp(int x, int y) {
        if (y < 1) {
            return false;
        }
        if (mapInfo[x][y - 1] < GameConfig.MAP_TYPE_TEMP) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canLeft(int x, int y) {
        if (x < 1) {
            return false;
        }
        if (mapInfo[x - 1][y] < GameConfig.MAP_TYPE_TEMP) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canRight(int x, int y) {
        if (x >= GameConfig.WIDTH_SIZE - 1) {
            return false;
        }
        if (mapInfo[x + 1][y] < GameConfig.MAP_TYPE_TEMP) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canDown(int x, int y) {
        if (y >= GameConfig.HEIGHT_SIZE - 1) {
            return false;
        }
        if (mapInfo[x][y + 1] < GameConfig.MAP_TYPE_TEMP) {
            return true;
        }
        return false;
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
        if (waitError == GameConfig.MAP_FPS / 3) {
            waitError = 0;
            setRandomDirection();
        }
    }
}