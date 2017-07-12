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
import com.unual.bomberman.interfaces.IDirection;
import com.unual.bomberman.widget.GameConfig;

/**
 * Created by unual on 2017/7/12.
 */

public class Bomber extends BaseModel implements IControl {

    public Bomber(Context context, int iconId, IDirection iDirection, int perWidth, int perHeight) {
        super(context, iconId, iDirection, perWidth, perHeight);
        speed_value = GameConfig.getInstance().pf * 2;
//        speed_value = 0.1f;
        Log.e("123", "ball:" + speed_value);
    }

    @Override
    public void initLocation(Location location) {
        location.x = 1;
        location.y = 1;
    }

    @Override
    public void onDirectionError() {

    }

    @Override
    public void setAction(int action) {
        Log.e("123", "set bomber");
    }

    @Override
    public void setDirection(int direction) {
        nextDirection = direction;
    }

    @Override
    public void setSpeed(int speedValue) {
        speed_value = speedValue;
    }
}
