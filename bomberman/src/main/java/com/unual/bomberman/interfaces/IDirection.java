package com.unual.bomberman.interfaces;

import com.unual.bomberman.bean.Location;

/**
 * Created by unual on 2017/7/12.
 */

public interface IDirection {
    boolean canWalkUp(int x, int y);

    boolean canWalkLeft(int x, int y);

    boolean canWalkRight(int x, int y);

    boolean canWalkDown(int x, int y);
}
