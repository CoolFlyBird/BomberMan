package com.unual.bomberman.bean;

import android.graphics.Canvas;

import com.unual.bomberman.GameConfig;

import java.security.PublicKey;

/**
 * Created by unual on 2017/7/19.
 */

public abstract class PropModel extends BaseModel {
    private boolean isShow;
    private boolean eat;

    public PropModel(GameConfig gameConfig) {
        super(gameConfig);
        location = new Location();
    }

    public void set(int x, int y) {
        location.x = x;
        location.y = y;
        isShow = true;
    }

    public boolean isEat() {
        return eat;
    }

    public void setEat(boolean eat) {
        this.eat = eat;
        isShow = false;
        location.x = 0;
        location.y = 0;
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isShow())
            canvas.drawBitmap(icon, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }
}
