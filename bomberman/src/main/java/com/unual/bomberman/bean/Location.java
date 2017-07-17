package com.unual.bomberman.bean;

import android.util.Log;

/**
 * Created by unual on 2017/7/7.
 */

public class Location {
    int x;
    int y;
    float xOffset;
    float yOffset;

    public void update() {
        if (xOffset > 0) {
            if (Math.abs(xOffset - 1) < 10e-6) {
                xOffset = 0.0f;
                x += 1;
            }
        } else {
            if (Math.abs(xOffset + 1) < 10e-6) {
                xOffset = 0.0f;
                x -= 1;
            }
        }

        if (yOffset > 0) {
            if (Math.abs(yOffset - 1) < 10e-6) {
                yOffset = 0.0f;
                y += 1;
            }
        } else {
            if (Math.abs(yOffset + 1) < 10e-6) {
                yOffset = 0.0f;
                y -= 1;
            }
        }
    }
}
