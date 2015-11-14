package com.gamicarts.hipsterbird.sprite.component;

import android.graphics.Point;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/3/14
 * Time: 4:36 PM
 */
public enum BirdComponentSet {

    HIPSTER_BIRD_SET(BirdComponent.HIPSTER_BIRD_BODY,
            BirdComponent.HIPSTER_BIRD_EYES_CLOSED,
            BirdComponent.HIPSTER_BIRD_EYES_OPEN,
            BirdComponent.HIPSTER_BIRD_MOUTH_CLOSED,
            BirdComponent.HIPSTER_BIRD_MOUTH_OPEN,
            BirdComponent.HIPSTER_BIRD_WING,
            new Point(173, 24),
            8,
            false),
    TEMP1_BIRD_SET(BirdComponent.TEMP1_HIPSTER_BIRD_BODY,
            BirdComponent.TEMP1_HIPSTER_BIRD_EYES_CLOSED,
            BirdComponent.TEMP1_HIPSTER_BIRD_EYES_OPEN,
            BirdComponent.TEMP1_HIPSTER_BIRD_MOUTH_CLOSED,
            BirdComponent.TEMP1_HIPSTER_BIRD_MOUTH_OPEN,
            BirdComponent.TEMP1_HIPSTER_BIRD_WING,
            new Point(173, 24),
            8,
            false),
    TEMP2_BIRD_SET(BirdComponent.TEMP2_HIPSTER_BIRD_BODY,
            BirdComponent.TEMP2_HIPSTER_BIRD_EYES_CLOSED,
            BirdComponent.TEMP2_HIPSTER_BIRD_EYES_OPEN,
            BirdComponent.TEMP2_HIPSTER_BIRD_MOUTH_CLOSED,
            BirdComponent.TEMP2_HIPSTER_BIRD_MOUTH_OPEN,
            BirdComponent.TEMP2_HIPSTER_BIRD_WING,
            new Point(173, 24),
            8,
            false),
    MUSTACHE_BIRD_SET(BirdComponent.MUSTACHE_BIRD_BODY,
            BirdComponent.MUSTACHE_BIRD_EYES_CLOSED,
            BirdComponent.MUSTACHE_BIRD_EYES_OPEN,
            BirdComponent.MUSTACHE_BIRD_MOUTH_CLOSED,
            BirdComponent.MUSTACHE_BIRD_MOUTH_OPEN,
            BirdComponent.MUSTACHE_BIRD_WING,
            new Point(15, 20),
            6,
            true),
    CRAZY_BIRD_SET(BirdComponent.CRAZY_BIRD_BODY,
            BirdComponent.CRAZY_BIRD_EYES_1,
            BirdComponent.CRAZY_BIRD_EYES_2,
            BirdComponent.CRAZY_BIRD_MOUTH_CLOSED,
            BirdComponent.CRAZY_BIRD_MOUTH_OPEN,
            BirdComponent.CRAZY_BIRD_WING,
            new Point(14, 20),
            6,
            true),
    POLICE_BIRD_SET(BirdComponent.POLICE_BIRD_BODY,
            BirdComponent.POLICE_BIRD_EYES_2,
            BirdComponent.POLICE_BIRD_EYES_1,
            BirdComponent.POLICE_BIRD_MOUTH_CLOSED,
            BirdComponent.POLICE_BIRD_MOUTH_OPEN,
            BirdComponent.POLICE_BIRD_WING,
            new Point(20, 22),
            6,
            true),
    CLOUD_BIRD_SET(BirdComponent.CLOUD_BIRD_BODY,
            BirdComponent.CLOUD_BIRD_EYES_CLOSED,
            BirdComponent.CLOUD_BIRD_EYES_OPEN,
            BirdComponent.CLOUD_BIRD_MOUTH_CLOSED,
            BirdComponent.CLOUD_BIRD_MOUTH_OPEN,
            BirdComponent.CLOUD_BIRD_WING,
            new Point(18, 23),
            6,
            true);

    private BirdComponent body;
    private BirdComponent eyesClosed;
    private BirdComponent eyesOpen;
    private BirdComponent mouthOpen;
    private BirdComponent mouthClosed;
    private BirdComponent wing;

    private final Point wingAnchor; //Used for wing rotations
    private final int wingRotationDegree;

    private final boolean facesLeft; //Birds either face left or face right. This is needed in order to determine how the wing rotates

    private final ArrayList<BirdComponent> components = new ArrayList<BirdComponent>();

    private boolean resized = false;

    BirdComponentSet(BirdComponent body,
                     BirdComponent eyesClosed,
                     BirdComponent eyesOpen,
                     BirdComponent mouthClosed,
                     BirdComponent mouthOpen,
                     BirdComponent wing,
                     Point wingAnchor,
                     int wingRotationDegree,
                     boolean facesLeft) {
        this.body = body;
        this.eyesClosed = eyesClosed;
        this.eyesOpen = eyesOpen;
        this.mouthClosed = mouthClosed;
        this.mouthOpen = mouthOpen;
        this.wing = wing;
        this.wingAnchor = wingAnchor;
        this.wingRotationDegree = wingRotationDegree;
        this.facesLeft = facesLeft;

        this.components.add(this.body);
        this.components.add(this.mouthClosed);
        this.components.add(this.mouthOpen);
        this.components.add(this.eyesClosed);
        this.components.add(this.eyesOpen);
        this.components.add(this.wing);
    }

    public void deallocate() {
        this.body.setComponentBitmap(null);
        this.eyesClosed.setComponentBitmap(null);
        this.eyesOpen.setComponentBitmap(null);
        this.mouthClosed.setComponentBitmap(null);
        this.mouthOpen.setComponentBitmap(null);
        this.wing.setComponentBitmap(null);
    }

    public boolean doesFaceLeft() {
        return this.facesLeft;
    }

    public ArrayList<BirdComponent> getComponents() {
        return this.components;
    }

    public BirdComponent getBody() {
        return this.body;
    }

    public BirdComponent getEyesClosed() {
        return this.eyesClosed;
    }

    public BirdComponent getEyesOpen() {
        return this.eyesOpen;
    }

    public BirdComponent getMouthClosed() {
        return this.mouthClosed;
    }

    public BirdComponent getMouthOpen() {
        return this.mouthOpen;
    }

    public BirdComponent getWing() {
        return this.wing;
    }

    public Point getWingAnchor() {
        return this.wingAnchor;
    }

    public int getWingRotationDegree() {
        return this.wingRotationDegree;
    }

    public void resize(double ratio) {
        //Would be a problem if ratio somehow changed from the beginning o.O
        if (!resized) { //No need to change the wing anchor position if we've already done it.
            this.wingAnchor.x = (int) (this.wingAnchor.x * ratio);
            this.wingAnchor.y = (int) (this.wingAnchor.y * ratio);
        }

        for (BirdComponent bc : this.getComponents()) {
            int newWidth = (int) (bc.getComponentBitmap().getWidth() * ratio);
            int newHeight = (int) (bc.getComponentBitmap().getHeight() * ratio);

            bc.setComponentBitmap(Utility.getResizedBitmap(bc.getComponentBitmap(), newWidth, newHeight));

            if (!resized) { //We don't need to change the position again once we've already done it.
                Point positionedLoc = bc.getSpriteComponent().getPositionedLocation();
                bc.getSpriteComponent().setPositionedLocation(new Point((int) (positionedLoc.x * ratio), (int) (positionedLoc.y * ratio)));
            }
        }

        resized = true;
    }

    public void transfer(BirdComponentSet to) {
        to.body = this.body;
        to.eyesClosed = this.eyesClosed;
        to.eyesOpen = this.eyesOpen;
        to.mouthOpen = this.mouthOpen;
        to.mouthClosed = this.mouthClosed;
        to.wing = this.wing;
    }

}
