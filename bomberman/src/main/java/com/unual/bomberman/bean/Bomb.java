package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IDirection;

/**
 * Created by unual on 2017/7/14.
 */

public class Bomb extends Model {
    private int bombLength = 1;
    private boolean boom;
    Bitmap horizon, vertical, center;

    public Bomb(int resId, int perWidth, int perHeight) {
        super(resId, perWidth, perHeight);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
        horizon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_horizon);
        horizon = Bitmap.createScaledBitmap(horizon, perWidth, perHeight, false);
        vertical = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_vertical);
        vertical = Bitmap.createScaledBitmap(vertical, perWidth, perHeight, false);
        center = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_bomb_center);
        center = Bitmap.createScaledBitmap(center, perWidth, perHeight, false);
    }

    @Override
    public void initLocation(Location location) {

    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }



    @Override
    public void draw(Canvas canvas) {
        if (boom) {
            canvas.drawBitmap(center, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
            canvas.drawBitmap(horizon, (location.x - 1 + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
            canvas.drawBitmap(horizon, (location.x + 1 + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
            canvas.drawBitmap(vertical, (location.x + location.xOffset) * perWidth, (location.y - 1 + location.yOffset) * perHeight, null);
            canvas.drawBitmap(vertical, (location.x + location.xOffset) * perWidth, (location.y + 1 + location.yOffset) * perHeight, null);
        } else {
            canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
        }
    }
}
