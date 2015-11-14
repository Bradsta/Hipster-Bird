package com.gamicarts.hipsterbird.game.task;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 3/11/14
 * Time: 2:42 PM
 *
 * This interface is a way to keep track of classes
 * that handle sections within the game. Such as
 * the main menu and in game tasks.
 */
public interface GameTask {

    public void onDraw(Canvas canvas, Paint paint);

    public boolean onTouchEvent(MotionEvent event);

    public void run();

}
