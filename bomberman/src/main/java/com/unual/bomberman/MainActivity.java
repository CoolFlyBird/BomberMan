package com.unual.bomberman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.unual.bomberman.view.ControlView;
import com.unual.bomberman.view.GameView;

public class MainActivity extends Activity {
    private static final int FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        GameView gameview = (GameView) findViewById(R.id.gameview);
        ControlView control = (ControlView) findViewById(R.id.control);
        gameview.addGameMapCallback(control);
        control.setZOrderOnTop(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(FLAG);
    }
}
