package com.gamicarts.hipsterbird.sprite.data;

import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.sprite.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 7/21/14
 * Time: 10:24 PM
 */
public enum PowerUpData {

    EXTRA_LIFE(R.drawable.extra_life, 2, 0, 2000),
    DOUBLE_SCORE(R.drawable.double_score, 5, 20000, 10000),
    TRIPLE_SCORE(R.drawable.triple_score, 4, 20000, 5000),
    INVINCIBILITY(R.drawable.invincibility, 3, 10000, 3000);

    private final int chance;
    private final int duration;
    private final int floatingTime; //How long it'll be available to grab.

    private final Sprite sprite;

    private PowerUpData(int id, int chance, int duration, int floatingTime) {
        this.chance = chance;
        this.duration = duration;
        this.floatingTime = floatingTime;

        this.sprite = new Sprite(id);
    }

    public void allocate() {
        this.sprite.allocateSprite(HipsterBird.RESOURCES, true);
    }

    public int getChance() {
        return this.chance;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getFloatingTime() {
        return this.floatingTime;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

}
