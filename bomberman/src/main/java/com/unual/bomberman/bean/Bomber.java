package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;
import com.unual.bomberman.view.MapView;

import java.util.List;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends MoveModel implements IControl {
    private boolean nextBomb;
    private List<Bomb> bombs;

    public Bomber(List<Bomb> bombs) {
        this.bombs = bombs;
        location = new Location();
        speed = new Speed();
        speed_value = (float) (1.0 / LEVEL[0]);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_man);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        initLocation();
    }

    @Override
    public void initLocation() {
        location.x = 1;
        location.y = 1;
    }

    @Override
    public void onCrossRoad() {

    }

    @Override
    public void onPoint() {
        if (nextBomb && (mapInfo[location.x][location.y] != MapView.GameConfig.TYPE_TEMP)) {
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
    public void setSpeed(int level) {
        speed_value = (float) (1.0 / LEVEL[level]);
    }

    @Override
    public boolean meetWith(BaseModel model) {
        if (location.x == model.location.x && location.y == model.location.y) {
            return true;
        } else if (location.x == model.location.x) {

        } else if (location.y == model.location.y) {

        }
        return false;
    }
}
