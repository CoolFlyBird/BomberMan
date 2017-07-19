package com.unual.bomberman.interfaces;

/**
 * Created by unual on 2017/7/7.
 */

public interface IControl {
    int DIRECTION_NONE = 0;
    int DIRECTION_UP = 1;
    int DIRECTION_DOWN = 2;
    int DIRECTION_LEFT = 3;
    int DIRECTION_RIGHT = 4;

    int ACTION_A = 6;

    void setAction(int action);

    void setDirection(int direction);
}
