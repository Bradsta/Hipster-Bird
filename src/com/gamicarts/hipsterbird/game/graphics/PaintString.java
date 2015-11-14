package com.gamicarts.hipsterbird.game.graphics;

import android.graphics.*;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 6/7/14
 * Time: 6:43 PM
 *
 * A basic class that draws a string that is adjusted based on screen resolution.
 */
public class PaintString {

    private String content;

    private final float fontSize;
    private final Typeface fontType;
    private final Point location;

    private int fontColor;

    private final Paint paint = new Paint();

    public PaintString(String content, Typeface fontType, int fontColor, float fontSize, Point location) {
        this.content = content;
        this.fontType = fontType;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.location = location;

        paint.setAntiAlias(true);
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        paint.setTypeface(Typeface.create(fontType, Typeface.NORMAL));
    }

    public String getContent() {
        return this.content;
    }

    public int getFontColor() {
        return this.fontColor;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public Point getLocation() {
        return this.location;
    }

    public Rect getTextBounds() {
        Rect bounds = new Rect();

        this.paint.getTextBounds(this.content, 0, this.content.length(), bounds);

        return bounds;
    }

    public void paint(Canvas canvas) {
        paint.setColor(this.fontColor);

        canvas.drawText(this.content, location.x, location.y, paint);
    }

    public void setColor(int color) {
        this.fontColor = color;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(int x, int y) {
        this.location.x = x;
        this.location.y = y;
    }

}
