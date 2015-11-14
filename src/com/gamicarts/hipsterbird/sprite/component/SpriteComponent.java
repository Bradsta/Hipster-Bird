package com.gamicarts.hipsterbird.sprite.component;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/22/14
 * Time: 5:50 PM
 *
 * Provides information of where to grab the sub-sprites from the entire sprite image.
 */
public class SpriteComponent {

    private final Rect originalBounds; //Original bounds of the sub sprite in the sprite image.
    private Point positionedLocation; //Where to position the sub sprite to assemble the true sprite.

    public SpriteComponent(Rect originalBounds, Point positionedLocation) {
        this.originalBounds = originalBounds;
        this.positionedLocation = positionedLocation;
    }

    @Override
    public SpriteComponent clone() {
        SpriteComponent cloned = new SpriteComponent(this.getOriginalBounds(), this.getPositionedLocation());

        return cloned;
    }

    public Rect getOriginalBounds() {
        return this.originalBounds;
    }

    public Point getPositionedLocation() {
        return this.positionedLocation;
    }

    public void setPositionedLocation(Point newPosition) {
        this.positionedLocation = newPosition;
    }

}
