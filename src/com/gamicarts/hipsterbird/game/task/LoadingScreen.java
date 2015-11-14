package com.gamicarts.hipsterbird.game.task;

import android.graphics.*;
import android.view.MotionEvent;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.state.FunctionButton;
import com.gamicarts.hipsterbird.game.state.StateButton;
import com.gamicarts.hipsterbird.game.graphics.Shop;
import com.gamicarts.hipsterbird.sprite.Sprite;
import com.gamicarts.hipsterbird.sprite.data.Accessory;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.data.PowerUpData;
import com.gamicarts.hipsterbird.game.graphics.PaintRect;
import com.gamicarts.hipsterbird.game.graphics.PaintString;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.utility.Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/28/14
 * Time: 5:45 PM
 */
public class LoadingScreen implements GameTask {

    private double alphaMultiplier = 0;
    private Sprite gamicArtsLogo = new Sprite(R.drawable.gamic_arts, new Point(220, 30));

    private static boolean setup = false;

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        //White background
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, HipsterBird.SCREEN_WIDTH, HipsterBird.SCREEN_HEIGHT, paint);

        //Gamic Arts logo
        if (gamicArtsLogo.getBitmap() != null) {
            paint.setAlpha((int) (255 * alphaMultiplier));
            canvas.drawBitmap(gamicArtsLogo.getBitmap(), gamicArtsLogo.getLocation().x, gamicArtsLogo.getLocation().y, paint);
        }
    }

    @Override
    public void run() {
        HipsterBird.GAMIC_THEME.seekTo(0);
        HipsterBird.GAMIC_THEME.start();

        logoThread.start();

        gamicArtsLogo.allocateSprite(HipsterBird.RESOURCES, true);

        try {
            FileInputStream fis = HipsterBird.CONTEXT.openFileInput("hbsave");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;

            System.out.println("Reading the file...");

            while ((line = br.readLine()) != null) {
                String d = Utility.e(line, '1');

                System.out.println(d);

                if (d.contains("cbd ")) Game.defaultData = BirdData.getBirdDataByName(d.split(" ")[1]);
                else if (d.contains("ca ")) Game.defaultChest = Accessory.getAccessoryByName(d.split(" ")[1]);
                else if (d.contains("ea ")) Game.defaultEye = Accessory.getAccessoryByName(d.split(" ")[1]);
                else if (d.contains("ha ")) Game.defaultHead = Accessory.getAccessoryByName(d.split(" ")[1]);
                else if (d.contains("sp ")) Shop.shopPoints = Integer.parseInt(d.split(" ")[1]);
                else if (d.contains("hs ")) Game.highscore = Integer.parseInt(d.split(" ")[1]);
                else if (d.contains("lives ")) Game.lives = Integer.parseInt(d.split(" ")[1]);

                if (d.contains(" ")
                        && Accessory.getAccessoryByName(d.split(" ")[0]) != null) {
                    Accessory.getAccessoryByName(d.split(" ")[0]).unlocked = Boolean.parseBoolean(d.split(" ")[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Game.firstTime = true;
        }

        if (!setup) { //We haven't set up the configured rectangles
            for (StateButton stateButton : StateButton.values()) stateButton.configure();
            for (FunctionButton functionButton : FunctionButton.values()) functionButton.configure();

            Shop.shopBackground = Utility.getConfiguredRect(Shop.shopBackground);
            Shop.shopFrame = Utility.getConfiguredRect(Shop.shopFrame);
            Shop.shopTitle = new PaintString("The Thrift Store", HipsterBird.HOBO, Color.WHITE, Utility.getXRatio(50), Utility.getConfiguredPoint(new Point(235, 70)));
            Shop.shopPointDisplay = new PaintString("Shop points: 0", HipsterBird.HOBO, Color.WHITE, Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(40, 450)));
            Shop.shopAdvice = new PaintString("1 Shop point = 500K score", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(45, 415)));

            Game.scoreText = new PaintString("Score: ", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(40), Utility.getConfiguredPoint(new Point(20, 45)));
            Game.scoreBox = new PaintRect(Utility.getConfiguredRect(new RectF(10, 10, 150, 50)), Color.argb(150, 0, 0, 0), Color.WHITE);
            Game.livesText = new PaintString("", HipsterBird.HOBO, Color.WHITE, Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(75, 460)));
            Game.lostMenu = new PaintRect(Utility.getConfiguredRect(new RectF(250, 100, 550, 400)), Color.argb(150, 0, 0, 0), Color.WHITE);
            Game.highscoreBox = new PaintRect(Utility.getConfiguredRect(new RectF(120, 440, 320, 470)), Color.argb(150, 0, 0, 0), Color.WHITE);
            Game.highscoreText = new PaintString("Highscore: ", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(130, 465)));
            Game.finalScore = new PaintString("", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(0, 140)));
            Game.adviceText = new PaintString("", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(18), Utility.getConfiguredPoint(new Point(0, 270)));
            Game.shopPointsGained = new PaintString("", HipsterBird.HOBO, Color.rgb(255, 255, 255), Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(0, 310)));
            Game.howTo1 = new PaintString("Move with finger ->", HipsterBird.HOBO, Color.BLACK, Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(80, 180)));
            Game.howTo2 = new PaintString("to dodge other birds", HipsterBird.HOBO, Color.BLACK, Utility.getXRatio(30), Utility.getConfiguredPoint(new Point(75, 205)));

            for (Sprite s : Game.clouds) s.allocateSprite(HipsterBird.RESOURCES, true); //Not the best thing to do but meh

            Game.title.allocateSprite(HipsterBird.RESOURCES, true);
            Game.background.allocateSprite(HipsterBird.RESOURCES, true);

            PowerUpData.EXTRA_LIFE.allocate();

            setup = true;
        }

        Accessory.allocateSprites();

        try {
            logoThread.join();

            while (HipsterBird.GAMIC_THEME.isPlaying()) Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HipsterBird.GAMIC_THEME.stop();
        HipsterBird.GAMIC_THEME.prepareAsync();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Not used for loading screen. You must watch the Gamic Arts logo!
        return false;
    }

    Thread logoThread = new Thread() {

        @Override
        public void run() {
            try {
                //Was giving me some weird error when I tried to put alphaMultiplier in the for loop...
                for (int i=0; i<35; i++) {
                    alphaMultiplier += 0.025D;

                    Thread.sleep(100);
                }

                alphaMultiplier = 1;

            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

    };

}
