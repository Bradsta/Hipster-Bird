package com.gamicarts.hipsterbird.game.graphics;

import android.graphics.*;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 6/7/14
 * Time: 7:29 PM
 *
 * A basic Rect drawing class that adjusts based on screen resolution.
 */
public class PaintRect {

    private final RectF rect;

    private int backgroundColor;
    private int borderColor;

    private final Paint paint = new Paint();

    public PaintRect(RectF rect, int backgroundColor, int borderColor) {
        this.rect = rect;

        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    public RectF getRect() {
        return this.rect;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void paint(Canvas canvas) {
        paint.setColor(this.backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(this.rect, paint);

        paint.setColor(this.borderColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(this.rect, paint);
    }

}
