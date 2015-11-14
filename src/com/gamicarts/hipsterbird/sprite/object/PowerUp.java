package com.gamicarts.hipsterbird.sprite.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import com.gamicarts.hipsterbird.sprite.Sprite;
import com.gamicarts.hipsterbird.sprite.data.PowerUpData;
import com.gamicarts.hipsterbird.utility.Timer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 7/21/14
 * Time: 10:17 PM
 */
public class PowerUp {

    private final PowerUpData powerUpData;
    private final Point baseLocation;
    private final Timer floatingTimer;

    public final ArrayList<Point> floatingLocations = new ArrayList<Point>();
    public int index = 0;

    private Point currentLocation;
    private long lastTimeLeft;

    public PowerUp(PowerUpData powerUpData, Point baseLocation) {
        this.powerUpData = powerUpData;
        this.baseLocation = baseLocation;
        this.floatingTimer = new Timer(this.powerUpData.getFloatingTime());
        this.lastTimeLeft = this.powerUpData.getFloatingTime();

        for (int y=-10; y<=10; y+= 2) {
            floatingLocations.add(new Point(baseLocation.x, baseLocation.y + y));
        }

        currentLocation = floatingLocations.get(0);

        this.powerUpData.allocate();
    }

    public void draw(Canvas canvas, Paint paint) {
        if (floatingTimer.timeLeft() < 1000 && (lastTimeLeft - floatingTimer.timeLeft()) > 200) {
            lastTimeLeft = floatingTimer.timeLeft();

            paint.setAlpha(0);
        }

        canvas.drawBitmap(powerUpData.getSprite().getBitmap(), currentLocation.x, currentLocation.y, paint);

        paint.setAlpha(255);
    }

    public PowerUpData getPowerUpData() {
        return this.powerUpData;
    }

    public Point getCurrentLocation() {
        return currentLocation;
    }

    public Timer getFloatingTimer() {
        return this.floatingTimer;
    }

    public RectF getRectLocation() {
        return new RectF(this.currentLocation.x,
                this.currentLocation.y,
                this.currentLocation.x + this.powerUpData.getSprite().getBitmap().getWidth(),
                this.currentLocation.y + this.powerUpData.getSprite().getBitmap().getHeight());
    }

    public void setCurrentLocation(Point newLocation) {
        this.currentLocation = newLocation;
    }

}
