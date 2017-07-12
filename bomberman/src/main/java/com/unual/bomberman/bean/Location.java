package com.unual.bomberman.bean;

import android.util.Log;

/**
 * Created by unual on 2017/7/7.
 */

public class Location {
    int x;
    int y;
    public float xOffset;
    public float yOffset;

    public void update() {
        int tempx = 0, tempy = 0;
        if (xOffset >= 1 || yOffset >= 1) {
            tempx = (int) Math.floor(xOffset);
            tempy = (int) Math.floor(yOffset);
        } else if (xOffset - 10e-6 <= -1 || yOffset - 10e-6 <= -1) {
            xOffset -= 10e-6;
            yOffset -= 10e-6;
            tempx = (int) Math.ceil(xOffset);
            tempy = (int) Math.ceil(yOffset);
        }
        x += tempx;
        y += tempy;
        xOffset = xOffset - tempx;
        yOffset = yOffset - tempy;
        if (Math.abs(xOffset) < 10e-5) {
            xOffset = 0.0f;
        }
        if (Math.abs(yOffset) < 10e-5) {
            yOffset = 0.0f;
        }
    }
}
