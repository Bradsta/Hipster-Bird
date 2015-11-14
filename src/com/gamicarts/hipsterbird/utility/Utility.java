package com.gamicarts.hipsterbird.utility;

import android.graphics.*;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.state.GameState;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/21/14
 * Time: 4:24 PM
 */
public class Utility {

    public static String e(String t, char k) {
        StringBuilder enc = new StringBuilder();

        for (int j = 0; j < t.toCharArray().length; j++) {
            char current = t.charAt(j);
            enc.append(current ^= k);
        }

        return enc.toString();
    }

    //Used for collisions between birds
    public static boolean collision(RectF collisionRect, Bitmap first, Bitmap second, Point offsetLocation) {
        for (float x=collisionRect.left; x<=collisionRect.right; x++) {
            for (float y=collisionRect.top; y<=collisionRect.bottom; y++) {
                int xTrans = (int) x - offsetLocation.x;
                int yTrans = (int) y - offsetLocation.y;

                if ((xTrans > 0 && (xTrans < first.getWidth() && xTrans < second.getWidth()))
                        && (yTrans > 0 && (yTrans < first.getHeight() && yTrans < second.getHeight()))
                        && Color.alpha(first.getPixel(xTrans, yTrans)) > 0
                        && Color.alpha(second.getPixel(xTrans, yTrans)) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsState(GameState[] states, GameState state) {
        for (GameState gs : states) {
            if (gs == state) return true;
        }

        return false;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public static int distance(Point first, Point second) {
        return (int) Math.sqrt(Math.pow(first.x-second.x, 2) + Math.pow(first.y-second.y, 2));
    }

    /**
     * Returns a re-configured point based on the original location of the point in the android emulator
     */
    public static Point getConfiguredPoint(Point point) {
        return new Point((int) getXRatio(point.x), (int) getYRatio(point.y));
    }

    /**
     * Gets the width ratio of the standard emulator compared to the new screen width.
     *
     * @return The width ratio.
     */
    public static float getWidthRatio() {
        return ((float) HipsterBird.SCREEN_WIDTH / (float) HipsterBird.STANDARD_SCREEN_WIDTH);
    }

    /**
     * Gets the height ratio of the standard emulator compared to the new screen height.
     *
     * @return The height ratio.
     */
    public static float getHeightRatio() {
        return ((float) HipsterBird.SCREEN_HEIGHT / (float) HipsterBird.STANDARD_SCREEN_HEIGHT);
    }

    /**
     * This algorithm is also known as Bresenham's algorithm. The algorithm was originally used to
     * draw lines in between two points but I use this algorithm to make paths walkable when there
     * are spaces in between Locations within the path.
     * <p>
     * This java implementation of the algorithm I found off the webpage:
     * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
     * <p>
     * All credit goes to the author, all I did was edit it for use with Locations rather than
     * x and y integer values.
     * <p>
     *
     * @param start       Start Location
     * @param destination End Location
     * @return            All Locations in between these two inputted Locations in a linear formation
     */
    public static ArrayList<Point> getStraightPath(Point start, Point destination) {
        ArrayList<Point> locationsBetween = new ArrayList<Point>();

        int x = start.x;
        int y = start.y;

        int w = destination.x - x;
        int h = destination.y - y;

        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w<0) dx1 = -1; else if (w>0) dx1 = 1;
        if (h<0) dy1 = -1; else if (h>0) dy1 = 1;
        if (w<0) dx2 = -1; else if (w>0) dx2 = 1;

        int longest = Math.abs(w);
        int shortest = Math.abs(h);

        if (!(longest>shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h<0) dy2 = -1; else if (h>0) dy2 = 1;
            dx2 = 0;
        }

        int numerator = longest >> 1;

        for (int i=1; i < longest; i++) {
            numerator += shortest;

            if (!(numerator<longest)) {
                numerator -= longest;

                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }

            locationsBetween.add(new Point(x, y));
        }

        return locationsBetween;
    }

    public static Point getStringBottomCenter(RectF surrounding, Rect stringRect) {
        int x = (int) (surrounding.centerX() - (stringRect.width() / 2));
        int y = (int) (surrounding.bottom - Utility.getXRatio(10));

        return new Point(x, y);
    }

    /**
     * Returns a point where a string should be drawn.
     *
     * @param surrounding Entire rect to paint string within.
     * @param stringRect  Bounds of the string to paint.
     * @return            The point where the string should be drawn.
     */
    public static Point getStringCentered(RectF surrounding, Rect stringRect) {
        int x = (int) (surrounding.centerX() - (stringRect.width() / 2));
        int y = (int) (surrounding.centerY() + (stringRect.height() / 2));

        return new Point(x, y);
    }

    public static Point getStringTopCenter(RectF surrounding, Rect stringRect) {
        int x = (int) (surrounding.centerX() - (stringRect.width() / 2));
        int y = (int) (surrounding.top + stringRect.height() + Utility.getXRatio(10));

        return new Point(x, y);
    }

    public static Point getCenteredPoint(RectF surrounding, RectF inside) {
        int x = (int) (surrounding.centerX() - (inside.width() / 2));
        int y = (int) (surrounding.centerY() - (inside.height() / 2));

        return new Point(x, y);
    }

    public static Bitmap getRatioedBitmap(Bitmap bitmap, float ratio) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * ratio), (int) (bitmap.getHeight() * ratio), true);
    }

    /**
     * Returns a re-sized version of the passed bitmap
     *
     * @param bitmap Bitmap to resize
     * @param width  New x dimension
     * @param height New y dimension
     * @return       New scaled bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * Resized so that it's ratioed to the original emulator values.
     *
     * @param bitmap Bitmap to ratio
     * @return       Configured bitmap
     */
    public static Bitmap getConfiguredBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * getWidthRatio()), (int) (bitmap.getHeight() * getHeightRatio()), true);
    }

    /**
     * Returns the configured Rect based on screen dimensions.
     *
     * @param rectangle Rect to configure.
     * @return          Configured Rect.
     */
    public static RectF getConfiguredRect(RectF rectangle) {
        return new RectF(rectangle.left * getWidthRatio(),
                rectangle.top * getHeightRatio(),
                rectangle.right * getWidthRatio(),
                rectangle.bottom * getHeightRatio());
    }

    /**
     * Formats with commas.
     * <p>
     *
     * @param n
     * @return
     */
    public static String getFormatted(int n) {
        return NumberFormat.getNumberInstance(Locale.US).format(n);
    }

    /**
     * Returns a re-sized x-axis font/ratio number based on the original size of the font/ratio in the android emulator
     *
     * @param originalX Original size of font or ratio
     */
    public static float getXRatio(float originalX) {
        return originalX * getWidthRatio();
    }

    /**
     * Returns a re-sized y-axis font/ratio number based on the original size of the font/ratio in the android emulator
     *
     * @param originalY Original size of font or ratio
     */
    public static float getYRatio(float originalY) {
        return originalY * getHeightRatio();
    }

    public static void playGameSound(int ID) {
        HipsterBird.soundPool.play(HipsterBird.soundPoolMap.get(ID), 1f, 1f, 1, 0, 1f);
    }

    /**
     * Generates a random number between inputted min and max. The max is excluded.
     *
     * @param min Min number
     * @param max Highest possible number generated will be max-1
     * @return    Random number between min and max (exclusive)
     */
    public static int random(int min, int max) {
        return new Random().nextInt((max-min)) + min;
    }

    /**
     * Re-configures a RectF based on the original size/location of the RectF in the android emulator
     *
     * @param rectangle Original RectF
     */
    public static void resizeRectangle(RectF rectangle) {
        rectangle.left = rectangle.left * getWidthRatio();
        rectangle.right = rectangle.right * getWidthRatio();
        rectangle.top = rectangle.top * getHeightRatio();
        rectangle.bottom = rectangle.bottom * getHeightRatio();
    }

    /**
     * Returns the string between the inputted String s and String e in String mixed.
     *
     * @param s     Start of index to parse
     * @param e     End limit to parse
     * @param mixed Entire string
     * @return      Parsed string
     */
    public static String parse(String s, String e, String mixed) {
        try {
            return mixed.substring(mixed.indexOf(s) + s.length(), mixed.indexOf(e, mixed.indexOf(s) + s.length()));
        } catch(Exception ee) {
            ee.printStackTrace();
            return null;
        }
    }

}
