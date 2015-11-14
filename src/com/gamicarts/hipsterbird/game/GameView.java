package com.gamicarts.hipsterbird.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.game.graphics.Shop;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.sprite.data.Accessory;
import com.gamicarts.hipsterbird.utility.Utility;

import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 3/11/14
 * Time: 2:44 PM
 *
 * The view where the game is painted.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameHandler gameHandler;

    private final Context context;
    private final Paint paint;

    private boolean active = true;

    private static final int FILE_VERSION = 0;

    public GameView(Context context, GameHandler gameHandler) {
        super(context);

        this.gameHandler = gameHandler;
        this.context = context;
        this.paint = new Paint();

        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gameHandler.getGameTask() != null
                && canvas != null) {
            gameHandler.getGameTask().onDraw(canvas, this.paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return event != null && gameHandler.getGameTask() != null && gameHandler.getGameTask().onTouchEvent(event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Surface created");

        this.gameHandler.setPaused(false);
        this.active = true;

        new CanvasPainter().start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Surface destroyed");

        this.gameHandler.setPaused(true);

        if (this.gameHandler.getGameTask() instanceof Game
                && ((Game) this.gameHandler.getGameTask()).currentState == GameState.GAME) {
            ((Game) this.gameHandler.getGameTask()).currentState = GameState.PAUSE;
        }

        this.active = false;

        Game g = null;

        if (gameHandler.getGameTask() != null
                && gameHandler.getGameTask() instanceof Game) {
            try {
                g = (Game) gameHandler.getGameTask();

                System.out.println("Writing the file...");

                FileOutputStream fos = context.openFileOutput("hbsave", Context.MODE_PRIVATE);

                fos.write((Utility.e(String.valueOf(FILE_VERSION), '1') + "\r\n").getBytes()); //File writing version.
                fos.write((Utility.e("cbd " + g.hipsterBird.currentBirdData.toString(), '1') + "\r\n").getBytes());
                fos.write((Utility.e("ca " + g.hipsterBird.chestAccessory + "\r\n", '1') + "\r\n").getBytes());
                fos.write((Utility.e("ea " + g.hipsterBird.eyeAccessory + "\r\n", '1') + "\r\n").getBytes());
                fos.write((Utility.e("ha " + g.hipsterBird.headAccessory + "\r\n", '1') + "\r\n").getBytes());

                for (Accessory a : Accessory.values()) {
                    fos.write((Utility.e(a.toString() + " " + a.unlocked, '1') + "\r\n").getBytes());
                }

                fos.write((Utility.e("sp " + Shop.shopPoints, '1') + "\r\n").getBytes()); //shop points
                fos.write((Utility.e("hs " + Game.highscore, '1') + "\r\n").getBytes()); //high score
                fos.write((Utility.e("lives " + g.lives, '1') + "\r\n").getBytes()); //lives

                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CanvasPainter extends Thread {

        @Override
        public void run() {
            System.out.println("Repaint thread started");

            Canvas canvas = null;
            SurfaceHolder holder = getHolder();

            while (active
                    && gameHandler.isActive()) {
                canvas = null;

                try {
                    canvas = holder.lockCanvas();

                    synchronized (holder) {
                        onDraw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

            }

            System.out.println("Repaint stopped");
        }

    }

}

