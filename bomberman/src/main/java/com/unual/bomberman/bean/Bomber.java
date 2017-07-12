package com.unual.bomberman.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.unual.bomberman.R;
import com.unual.bomberman.interfaces.IControl;

/**
 * Created by unual on 2017/7/7.
 */

public class Bomber implements IControl {
    private static float SPEED_VALUE = 0.1f;
    private SoundPool soundPool;
    private int boomId;
    private Bitmap bomber;
    private Location location;
    private Speed speed;
    private int perWidth, perHeight;
    private int nextDirection;

    public Bomber(Context context, int perWidth, int perHeight) {
        bomber = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_view_man);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
        boomId = soundPool.load(context, R.raw.boom, 1);
        bomber = Bitmap.createScaledBitmap(bomber, perWidth, perHeight, false);
        this.perHeight = perHeight;
        this.perWidth = perWidth;
        location = new Location();
        location.x = 1;
        location.y = 1;
        speed = new Speed();
    }

    private void calculLocation() {
        switch (nextDirection) {
            case IControl.DIRECTION_UP:
            case IControl.DIRECTION_DOWN:
                if (!(Math.abs(location.xOffset) <= 0.01)) break;
                if (nextDirection == IControl.DIRECTION_UP) {
                    speed.xSpeed = 0;
                    speed.ySpeed = -SPEED_VALUE;
                } else {
                    speed.xSpeed = 0;
                    speed.ySpeed = SPEED_VALUE;
                }
                break;
            case IControl.DIRECTION_LEFT:
            case IControl.DIRECTION_RIGHT:
                if (!(Math.abs(location.yOffset) <= 0.01)) break;
                if (nextDirection == IControl.DIRECTION_LEFT) {
                    speed.xSpeed = -SPEED_VALUE;
                    speed.ySpeed = 0;
                } else {
                    speed.xSpeed = SPEED_VALUE;
                    speed.ySpeed = 0;
                }
                break;
            case IControl.DIRECTION_NONE:
                if ((Math.abs(location.xOffset) <= 0.01) && (Math.abs(location.yOffset) <= 0.01)) {
                    speed.xSpeed = 0;
                    speed.ySpeed = 0;
                }
                break;
        }
    }

    private void updataLocation() {
        location.xOffset += speed.xSpeed;
        location.yOffset += speed.ySpeed;
        location.update();
    }

    public void draw(Canvas canvas) {
        calculLocation();
        updataLocation();
        canvas.drawBitmap(bomber, (location.x + location.xOffset) * perWidth, (location.y + location.yOffset) * perHeight, null);
    }

    @Override
    public void setAction(int action) {
        soundPool.play(boomId, 1, 1, 0, 0, 1);
    }

    @Override
    public void setDirection(int direction) {
        nextDirection = direction;
    }

    @Override
    public void setSpeed(int speedValue) {
        SPEED_VALUE = speedValue;
    }
}
