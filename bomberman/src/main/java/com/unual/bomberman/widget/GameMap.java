package com.unual.bomberman.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.unual.bomberman.R;
import com.unual.bomberman.bean.Bomber;
import com.unual.bomberman.interfaces.IControl;

import java.util.Random;

/**
 * Created by unual on 2017/7/7.
 */

public class GameMap {
    public static final int TYPE_BACKGROUND = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_BRICK = 2;
    private int mapWidth, mapHeight;
    private int perWidth, perHeight;
    private GameConfig gameConfig;
    private byte[][] mapInfo;
    private Bitmap background;
    private Bitmap brick;
    private Bitmap wall;
    private Bitmap button;
    private Bomber bomber;

    private Rect up, left, down, right, action;

    public GameMap(Context context, int width, int height) {
        this.gameConfig = new GameConfig();
        mapHeight = height;
        mapWidth = width;
        perWidth = width / gameConfig.width;
        perHeight = height / gameConfig.height;
        brick = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_view_brick);
        wall = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_view_wall);
        button = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_round_button);
        background = Bitmap.createBitmap(perWidth, perHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        canvas.drawColor(context.getResources().getColor(R.color.game_background));
        brick = Bitmap.createScaledBitmap(brick, perWidth, perHeight, false);
        wall = Bitmap.createScaledBitmap(wall, perWidth, perHeight, false);
        button = Bitmap.createScaledBitmap(button, perWidth, perHeight, false);
        mapInfo = new byte[gameConfig.width][gameConfig.height];
        bomber = new Bomber(context, perWidth, perHeight);

//        canvas.drawBitmap(button, 2 * perWidth, 5 * perHeight, null);
//        canvas.drawBitmap(button, 1 * perWidth, 6 * perHeight, null);
//        canvas.drawBitmap(button, 3 * perWidth, 6 * perHeight, null);
//        canvas.drawBitmap(button, 2 * perWidth, 7 * perHeight, null);
//        canvas.drawBitmap(button, 14 * perWidth, 6 * perHeight, null);
        up = new Rect(2 * perWidth, 5 * perHeight, 3 * perWidth, 6 * perHeight);
        left = new Rect(1 * perWidth, 6 * perHeight, 2 * perWidth, 7 * perHeight);
        right = new Rect(3 * perWidth, 6 * perHeight, 4 * perWidth, 7 * perHeight);
        down = new Rect(2 * perWidth, 7 * perHeight, 3 * perWidth, 8 * perHeight);

        action = new Rect(14 * perWidth, 6 * perHeight, 15 * perWidth, 7 * perHeight);
    }

    public void generateGameMap(int p) {
        int x, y, a;
        Random random = new Random(47);
        for (y = 0; y < gameConfig.height; y++) {
            for (x = 0; x < gameConfig.width; x++) {
                if (x == 0 || y == 0 || x == gameConfig.width - 1 || y == gameConfig.height - 1) {
                    mapInfo[x][y] = TYPE_BRICK;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    mapInfo[x][y] = TYPE_BRICK;
                } else {
                    a = random.nextInt(100);
                    if (a % p == 0) {
                        mapInfo[x][y] = TYPE_WALL;
                    } else {
                        mapInfo[x][y] = TYPE_BACKGROUND;
                    }
                }
            }
        }
    }

    private void drawMap(Canvas canvas) {
        int x, y;
        for (y = 0; y < gameConfig.height; y++) {
            for (x = 0; x < gameConfig.width; x++) {
                switch (mapInfo[x][y]) {
                    case TYPE_BRICK:
                        canvas.drawBitmap(brick, x * perWidth, y * perHeight, null);
                        break;
                    case TYPE_WALL:
                        canvas.drawBitmap(wall, x * perWidth, y * perHeight, null);
                        break;
                    case TYPE_BACKGROUND:
                        canvas.drawBitmap(background, x * perWidth, y * perHeight, null);
                        break;
                }
            }
        }
    }

    private void drawMan(Canvas canvas) {
        bomber.draw(canvas);
    }

    private void drawEnemy(Canvas canvas) {

    }

    public int getButton(int x, int y) {
        if (up.contains(x, y)) return IControl.DIRECTION_UP;
        else if (left.contains(x, y)) return IControl.DIRECTION_LEFT;
        else if (right.contains(x, y)) return IControl.DIRECTION_RIGHT;
        else if (down.contains(x, y)) return IControl.DIRECTION_DOWN;
        else if (action.contains(x, y)) return IControl.ACTION_A;
        else return IControl.DIRECTION_NONE;
    }

    private void drawButton(Canvas canvas) {
        canvas.drawBitmap(button, 2 * perWidth, 5 * perHeight, null);
        canvas.drawBitmap(button, 1 * perWidth, 6 * perHeight, null);
        canvas.drawBitmap(button, 3 * perWidth, 6 * perHeight, null);
        canvas.drawBitmap(button, 2 * perWidth, 7 * perHeight, null);

        canvas.drawBitmap(button, 14 * perWidth, 6 * perHeight, null);
    }

    public void draw(Canvas canvas) {
        drawMap(canvas);
        drawMan(canvas);
        drawEnemy(canvas);
        drawButton(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int a = 0, x = 0, y = 0, x1 = 0, y1 = 0, pointsize = 1;
        pointsize = event.getPointerCount();
        if (pointsize == 1) {
            x = (int) event.getX();
            y = (int) event.getY();
        } else if (pointsize == 2) {
            x = (int) event.getX(0);
            y = (int) event.getY(0);
            x1 = (int) event.getX(1);
            y1 = (int) event.getY(1);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointsize == 1) {
                    dealButtonDown(getButton(x, y));
                } else if (pointsize == 2) {
                    dealButtonDown(getButton(x, y));
                    dealButtonDown(getButton(x1, y1));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointsize == 1) {
                    dealButtonUp(getButton(x, y));
                } else if (pointsize == 2) {
                    dealButtonUp(getButton(x, y));
                    dealButtonUp(getButton(x1, y1));
                }
                break;
        }
        return true;
    }

    private void dealButtonDown(int button) {
        switch (button) {
            case IControl.DIRECTION_NONE:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_UP:
                bomber.setDirection(IControl.DIRECTION_UP);
                break;
            case IControl.DIRECTION_DOWN:
                bomber.setDirection(IControl.DIRECTION_DOWN);
                break;
            case IControl.DIRECTION_LEFT:
                bomber.setDirection(IControl.DIRECTION_LEFT);
                break;
            case IControl.DIRECTION_RIGHT:
                bomber.setDirection(IControl.DIRECTION_RIGHT);
                break;
        }
    }

    private void dealButtonUp(int button) {
        switch (button) {
            case IControl.DIRECTION_UP:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_DOWN:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_LEFT:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.DIRECTION_RIGHT:
                bomber.setDirection(IControl.DIRECTION_NONE);
                break;
            case IControl.ACTION_A:
                bomber.setAction(IControl.ACTION_A);
                break;
        }
    }
}
