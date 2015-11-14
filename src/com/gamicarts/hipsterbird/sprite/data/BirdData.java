package com.gamicarts.hipsterbird.sprite.data;

import android.content.res.Resources;
import android.graphics.Color;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.sprite.Sprite;
import com.gamicarts.hipsterbird.sprite.component.BirdComponentSet;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/22/14
 * Time: 5:48 PM
 */
public enum BirdData {

    HIPSTER_BIRD_BLACK(R.drawable.hipster_bird_black, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_BLUE(R.drawable.hipster_bird_blue, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_GREEN(R.drawable.hipster_bird_green, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_PINK(R.drawable.hipster_bird_pink, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_RED(R.drawable.hipster_bird_red, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_WHITE(R.drawable.hipster_bird_white, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    HIPSTER_BIRD_YELLOW(R.drawable.hipster_bird_yellow, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0),
    TEMP(R.drawable.hipster_bird_green, BirdComponentSet.HIPSTER_BIRD_SET, 0.30D, 0, 0), //TODO - Re-write all my code so I don't have to do this....
    MUSTACHE_BIRD(R.drawable.mustache_bird, BirdComponentSet.MUSTACHE_BIRD_SET, 0.35D, 5000, Color.RED),
    CRAZY_BIRD(R.drawable.crazy_bird, BirdComponentSet.CRAZY_BIRD_SET, 0.35D, 10000, Color.YELLOW),
    POLICE_BIRD(R.drawable.police_bird, BirdComponentSet.POLICE_BIRD_SET, 0.3D, 20000, Color.BLUE),
    CLOUD_BIRD(R.drawable.cloud_bird, BirdComponentSet.CLOUD_BIRD_SET, 0.5D, 15000, Color.BLACK);

    private int id;
    private BirdComponentSet birdComponentSet; //Not gonna be final so we can preview birds.
    private final double resizeRatio;
    private final int pointValue;
    private final int color;

    private Sprite sprite;

    private BirdData(int id, BirdComponentSet birdComponentSet, double resizeRatio, int pointValue, int color) {
        this.id = id;
        this.birdComponentSet = birdComponentSet;
        this.resizeRatio = resizeRatio;
        this.pointValue = pointValue;
        this.color = color;
    }

    public static BirdData getBirdDataByName(String name) {
        for (BirdData bd : BirdData.values()) {
            if (name.contains(bd.toString())) {
                return bd;
            }
        }

        return null;
    }

    public void allocateComponentImages(Resources resources, boolean reAllocate) {
        if (reAllocate || this.birdComponentSet.getBody().getComponentBitmap() == null) { //No component sprite has been set.
            System.out.println("ALLOCATING SOME SPRITES");
            this.sprite = new Sprite(id);

            this.sprite.allocateSprite(resources, false);

            this.birdComponentSet.getBody().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getBody().getSpriteComponent().getOriginalBounds()));
            this.birdComponentSet.getEyesClosed().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getEyesClosed().getSpriteComponent().getOriginalBounds()));
            this.birdComponentSet.getEyesOpen().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getEyesOpen().getSpriteComponent().getOriginalBounds()));
            this.birdComponentSet.getMouthClosed().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getMouthClosed().getSpriteComponent().getOriginalBounds()));
            this.birdComponentSet.getMouthOpen().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getMouthOpen().getSpriteComponent().getOriginalBounds()));
            this.birdComponentSet.getWing().setComponentBitmap(this.sprite.getSubBitmap(this.getBirdComponentSet().getWing().getSpriteComponent().getOriginalBounds()));

            this.birdComponentSet.resize(Utility.getXRatio((float) this.resizeRatio)); //Initial resize. Future resizes can be done if needed.

            this.sprite.deallocateSprite(); //No need for the original sprite to be allocated anymore because we've extracted all the data that we need.
        }
    }

    public void deallocateComponentImages() {
        this.birdComponentSet.getBody().setComponentBitmap(null);
        this.birdComponentSet.getEyesClosed().setComponentBitmap(null);
        this.birdComponentSet.getEyesOpen().setComponentBitmap(null);
        this.birdComponentSet.getMouthClosed().setComponentBitmap(null);
        this.birdComponentSet.getMouthOpen().setComponentBitmap(null);
        this.birdComponentSet.getWing().setComponentBitmap(null);
    }

    public BirdComponentSet getBirdComponentSet() {
        return this.birdComponentSet;
    }

    public int getID() {
        return this.id;
    }

    public int getPointValue() {
        return this.pointValue;
    }

    public int getRelatedColor() {
        return this.color;
    }

    public void setBirdComponentSet(BirdComponentSet birdComponentSet) {
        this.birdComponentSet = birdComponentSet;
    }

    public void setID(int id) {
        this.id = id;
    }

}
