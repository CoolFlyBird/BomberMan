package com.unual.bomberman.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.unual.bomberman.AppCache;
import com.unual.bomberman.GameConfig;
import com.unual.bomberman.R;

/**
 * Created by unual on 2017/7/19.
 */

public class Prop extends PropModel {

    public Prop(GameConfig gameConfig) {
        super(gameConfig);
        icon = BitmapFactory.decodeResource(AppCache.getInstance().getContext().getResources(), R.drawable.game_view_prop_bomb);
        icon = Bitmap.createScaledBitmap(icon, perWidth, perHeight, false);
    }
}
