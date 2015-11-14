package com.gamicarts.hipsterbird.sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/22/14
 * Time: 5:47 PM
 */
public class Sprite {

    private final int drawableID;
    private Point location;
    private Bitmap bitmap;

    private int speed = 1; //Used to move sprites with a certain formula

    public Sprite(int drawableID) {
        this.drawableID = drawableID;
    }

    public Sprite(int drawableID, Point location) {
        this.drawableID = drawableID;
        this.location = location;
    }

    public void allocateSprite(Resources res, boolean configure) {
        System.gc();

        if (this.bitmap == null) {
            if (configure) {
                this.location = this.location == null ? null : Utility.getConfiguredPoint(this.location);
                this.bitmap = Utility.getConfiguredBitmap(BitmapFactory.decodeResource(res, drawableID));
            } else {
                this.bitmap = BitmapFactory.decodeResource(res, drawableID);
            }
        }
    }

    public void deallocateSprite() {
        this.bitmap = null;

        System.gc();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Point getLocation() {
        return this.location;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Bitmap getSubBitmap(Rect area) {
        return Bitmap.createBitmap(this.bitmap, area.left, area.top, area.width(), area.height());
    }

    public void resizeBitmap(double ratio) {
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, (int) (this.bitmap.getWidth() * ratio), (int) (this.bitmap.getHeight() * ratio), true);
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
