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
        int x_ = 0, y_ = 0;
        if (Math.abs(xOffset) < 10e-6) x_ = x;
        else if (xOffset < 0) x_ = x - 1;
        else if (xOffset > 0) x_ = x + 1;
        if (Math.abs(yOffset) < 10e-6) y_ = y;
        else if (yOffset < 0) y_ = y - 1;
        else if (yOffset > 0) y_ = y + 1;

        int lx_ = 0, ly_ = 0;
        if (Math.abs(location.xOffset) < 10e-6) lx_ = location.x;
        else if (location.xOffset < 0) lx_ = location.x - 1;
        else if (location.xOffset > 0) lx_ = location.x + 1;
        if (Math.abs(location.yOffset) < 10e-6) ly_ = location.y;
        else if (location.yOffset < 0) ly_ = location.y - 1;
        else if (location.yOffset > 0) ly_ = location.y + 1;

        if (x == location.x && y == location.y) {
            return true;
        } else if (x == location.x && y == ly_) {
            return true;
        } else if (x == lx_ && y == location.y) {
            return true;
        } else if (x == lx_ && y == ly_) {
            return true;
        } else if (x_ == location.x && y_ == location.y) {
            return true;
        } else if (x_ == location.x && y_ == ly_) {
            return true;
        } else if (x_ == lx_ && y_ == location.y) {
            return true;
        } else if (x_ == lx_ && y_ == ly_) {
            return true;
        }
        return false;
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
