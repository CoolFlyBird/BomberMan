package com.unual.bomberman.bean;

/**
 * Created by unual on 2017/7/7.
 */

public class Location {
    float xOffset;
    float yOffset;
    int x;
    int y;

    public boolean meetWith(Location location) {
        float x_ = x + xOffset;
        float y_ = y + yOffset;
        float lx_ = location.x + location.xOffset;
        float ly_ = location.y + location.yOffset;
        if (((x_ - lx_) * (x_ - lx_) + (y_ - ly_) * (y_ - ly_)) < 0.7 * 0.7) {
            return true;
        }
        return false;
    }

    public void cloneFrom(Location location) {
        x = location.x;
        y = location.y;
        xOffset = location.xOffset;
        yOffset = location.yOffset;
    }

    public boolean equalWith(Location location) {
        return (x == location.x) && (y == location.y) && (Math.abs(xOffset - location.xOffset) < 10e-6) && (Math.abs(yOffset - location.yOffset) < 10e-6);
    }

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
