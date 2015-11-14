package com.gamicarts.hipsterbird;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.gamicarts.hipsterbird.game.GameHandler;
import com.gamicarts.hipsterbird.game.GameView;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.HashMap;

/**
 * Project Name:   Hipster Bird
 * Author:         Brad Guerrero
 * First released: N/A
 *
 * This is the main activity class which is called in order for the game to start.
 *
 * TODO things I need to rewrite: Bird, BirdMovement, The way BirdData/BirdComponents work.
 */
public class HipsterBird extends Activity {

    public static AssetManager ASSET_MANAGER = null;
    public static Context CONTEXT = null;
    public static Resources RESOURCES = null;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int STANDARD_SCREEN_WIDTH = 800;
    public static int STANDARD_SCREEN_HEIGHT = 480;
    public static Typeface HOBO;

    public static GameHandler GAME_HANDLER;

    public static AudioManager AUDIO_MANAGER;
    public static MediaPlayer GAMIC_THEME;
    public static MediaPlayer GAME_SONG;
    public static SoundPool soundPool; //Want to eventually re-name static variables to lower case
    public static HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();

    private GameView gameView;
    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Basic set up of application
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Need resources to allocate images
        RESOURCES = getResources();

        //Need asset manager to open files
        ASSET_MANAGER = getAssets();

        //Needed to write files
        CONTEXT = this;

        //Grabbing screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        SCREEN_WIDTH = display.getWidth(); //Deprecated
        SCREEN_HEIGHT = display.getHeight();

        HOBO = Typeface.createFromAsset(ASSET_MANAGER, "fonts/Hobo.ttf");

        AUDIO_MANAGER = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        GAMIC_THEME = MediaPlayer.create(this, R.raw.gamictheme);
        GAME_SONG = MediaPlayer.create(this, R.raw.gamesong);

        soundPool = new SoundPool(13, AudioManager.STREAM_MUSIC, 0);

        soundPoolMap.put(R.raw.powerup, soundPool.load(this, R.raw.powerup, 1));
        soundPoolMap.put(R.raw.hit, soundPool.load(this, R.raw.hit, 2));
        soundPoolMap.put(R.raw.button, soundPool.load(this, R.raw.button, 3));
        soundPoolMap.put(R.raw.point, soundPool.load(this, R.raw.point, 4));

        if (GAME_HANDLER == null) {
            GAME_HANDLER = new GameHandler();
            GAME_HANDLER.setActive(true);
            GAME_HANDLER.start();
        }

        //Setting up layout for game
        gameView = new GameView(this, GAME_HANDLER);
        gameView.getHolder().addCallback(gameView);
        relativeLayout = new RelativeLayout(this);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayout.addView(gameView); //Need to add ad view to this if we implement ads.

        setContentView(relativeLayout);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.gameView = new GameView(this, GAME_HANDLER);
    }

}
