package com.gamicarts.hipsterbird.sprite.data;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/12/14
 * Time: 9:06 PM
 */
public enum BirdState {

    EYES_MOUTH_CLOSED(false, false),
    EYES_MOUTH_OPEN(true, true),
    EYES_CLOSED_MOUTH_OPEN(false, true),
    EYES_OPEN_MOUTH_CLOSED(true, false);

    private final boolean eyesOpen;
    private final boolean mouthOpen;

    public static final BirdState DEFAULT_STATE = EYES_OPEN_MOUTH_CLOSED;

    private BirdState(boolean eyesOpen, boolean mouthOpen) {
        this.eyesOpen = eyesOpen;
        this.mouthOpen = mouthOpen;
    }

    public boolean isEyesOpen() {
        return this.eyesOpen;
    }

    public boolean isMouthOpen() {
        return this.mouthOpen;
    }

}
