package com.gamicarts.hipsterbird.sprite.object;

import android.content.res.Resources;
import android.graphics.*;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.sprite.component.BirdComponent;
import com.gamicarts.hipsterbird.sprite.data.Accessory;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.data.BirdPart;
import com.gamicarts.hipsterbird.sprite.data.BirdState;
import com.gamicarts.hipsterbird.utility.Timer;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/3/14
 * Time: 4:46 PM
 */
public class Bird {

    private BirdData birdData;
    public BirdData currentBirdData; //Used as a reference when saving.

    private final Matrix wingMatrix = new Matrix();

    public BirdState birdState = BirdState.DEFAULT_STATE;
    private Point birdLocation = new Point();

    private int wingRotationSpeed = 30; //Default is 30, should correspond with how fast the bird actually moves
    private boolean allocated = false;

    private RectF wingRect = new RectF();
    private RectF bodyRect = new RectF();

    public Accessory chestAccessory = null;
    public Accessory eyeAccessory = null;
    public Accessory headAccessory = null;

    public Accessory previewChest = null;
    public Accessory previewEye = null;
    public Accessory previewHead = null;

    /* For movement */
    public boolean isCloud = false; //Only for the cloud bird
    public ArrayList<Point> birdPath = new ArrayList<Point>(); //Used for police man in movement thread
    public int lastLocation = 0; //Also used for police man. Works with bird path.
    public int xps = 0;
    public double xpf = 0;

    public boolean invincible = false;
    public boolean flicker = false; //Used for invincibility flickering
    public boolean scoreTallied = false;
    public Timer scoreTalliedTimer = null;

    public Bird(BirdData birdData, Point birdLocation) {
        this.birdData = birdData;
        this.birdLocation = birdLocation;

        this.currentBirdData = birdData;
    }

    public void allocateBird(Resources resources) {
        this.birdData.allocateComponentImages(resources, false);

        if (this.birdLocation != null) this.birdLocation = Utility.getConfiguredPoint(this.birdLocation);

        this.allocated = true;

        this.wingThread.start();
        this.stateThread.start();
    }

    public void deallocateBird() {
        allocated = false;
    }

    /**
     * Returns true if this Bird contains the inputted Bird.
     *
     * Used to see if hipster bird is intersecting other birds
     *
     * @param bird Bird to check.
     * @return     True if this Bird intersects the inputted Bird.
     */
    public boolean contains(Bird bird) {
        RectF thisBodyRect = this.getBodyRect();
        RectF inputtedBodyRect = bird.getBodyRect();

        if (Utility.distance(thisBodyRect.centerX(), thisBodyRect.centerY(), inputtedBodyRect.centerX(), inputtedBodyRect.centerY())
                > ((thisBodyRect.width() > thisBodyRect.height()) ? thisBodyRect.width() : thisBodyRect.height())) {
            return false;
        }

        //Our body intersects their body
        if (thisBodyRect.intersect(inputtedBodyRect)) {
            Bitmap thisBody = this.birdData.getBirdComponentSet().getBody().getComponentBitmap();
            Bitmap inputtedBody = bird.birdData.getBirdComponentSet().getBody().getComponentBitmap();

            if (Utility.collision(thisBodyRect, thisBody, inputtedBody, this.birdLocation)) return true;
        }

        RectF inputtedWingRect = bird.getWingRect();

        //Our body intersects their wing (Very common)
        if (!bird.isCloud && thisBodyRect.intersect(inputtedWingRect)) {
            Bitmap thisBody = this.birdData.getBirdComponentSet().getBody().getComponentBitmap();
            Bitmap inputtedWing = Bitmap.createBitmap(bird.birdData.getBirdComponentSet().getWing().getComponentBitmap(), 0, 0,
                                    bird.birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth(),
                                    bird.birdData.getBirdComponentSet().getWing().getComponentBitmap().getHeight(),
                                    bird.wingMatrix,
                                    false);

            if (Utility.collision(thisBodyRect, thisBody, inputtedWing, this.birdLocation)) return true;
        }

        RectF thisWingRect = this.getWingRect();

        //Our wing intersects their body
        if (thisWingRect.intersect(inputtedBodyRect)) {
            Bitmap inputtedBody = bird.birdData.getBirdComponentSet().getBody().getComponentBitmap();
            Bitmap thisWing = Bitmap.createBitmap(this.birdData.getBirdComponentSet().getWing().getComponentBitmap(),
                    0,
                    0,
                    this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth(),
                    this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getHeight(),
                    this.wingMatrix,
                    false);

            if (Utility.collision(thisWingRect, thisWing, inputtedBody, this.birdLocation)) return true;
        } /*else if (!bird.isCloud && thisWingRect.intersect(inputtedWingRect)) { //Our wing intersects their wing - Pretty much impossible
            Bitmap thisWing = Bitmap.createBitmap(this.birdData.getBirdComponentSet().getWing().getComponentBitmap(), 0, 0,
                    this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth(),
                    this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getHeight(),
                    this.wingMatrix,
                    false);
            Bitmap inputtedWing = Bitmap.createBitmap(bird.birdData.getBirdComponentSet().getWing().getComponentBitmap(), 0, 0,
                    bird.birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth(),
                    bird.birdData.getBirdComponentSet().getWing().getComponentBitmap().getHeight(),
                    bird.wingMatrix,
                    false);

            if (Utility.collision(thisWingRect, thisWing, inputtedWing, this.birdLocation)) return true;
        }*/

        return false;
    }

    public boolean contains(RectF rectF) {
        RectF thisBodyRect = this.getBodyRect();

        if (Utility.distance(thisBodyRect.centerX(), thisBodyRect.centerY(), rectF.centerX(), rectF.centerY())
                > ((thisBodyRect.width() > thisBodyRect.height()) ? thisBodyRect.width() : thisBodyRect.height())) { //Not even close baby
            return false;
        }

        if (thisBodyRect.intersect(rectF)) {
            return true;
        }

        RectF thisWingRect = this.getWingRect();

        if (thisWingRect.intersect(rectF)) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the inputted point intersects this Bird.
     *
     * @param x        X coordinate
     * @param y        Y coordinate
     * @param movement If this is true, then it will use the more optimized and less accurate version for movement detection rather than collision with other birds.
     * @return         True if the point is contained within this Bird.
     */
    public boolean contains(int x, int y, boolean movement) {
        //Returns true if this point is within the bird.
        boolean contained = false;

        RectF tempBodyRect = this.getBodyRect();

        if (!movement &&
                tempBodyRect.contains(x, y)) {
            Bitmap body = this.birdData.getBirdComponentSet().getBody().getComponentBitmap();

            contained = Color.alpha(body.getPixel(StrictMath.abs(x - this.birdLocation.x), StrictMath.abs(y - this.birdLocation.y))) > 0;
        } else if (movement) {
            contained = tempBodyRect.contains(x, y);
        }

        if (!contained) { //If it wasn't in the body
            contained = this.getWingRect().contains(x, y);
        }

        return contained;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);

        final Point staticLoc = new Point(this.birdLocation.x, this.birdLocation.y);

        if (this.invincible && !this.flicker) paint.setAlpha(150);

        if (this.isAllocated()) {
            for (BirdComponent bc : this.birdData.getBirdComponentSet().getComponents()) {
                if (bc.getBirdPart() == BirdPart.WING && !this.isCloud) {
                    canvas.drawBitmap(bc.getComponentBitmap(), this.wingMatrix, paint);
                } else if (bc.getBirdPart() == BirdPart.BODY
                        || (bc.getBirdPart() == BirdPart.EYES_OPENED && birdState.isEyesOpen())
                        || (bc.getBirdPart() == BirdPart.EYES_CLOSED && !birdState.isEyesOpen())
                        || (bc.getBirdPart() == BirdPart.MOUTH_OPENED && birdState.isMouthOpen() && !this.isCloud)
                        || (bc.getBirdPart() == BirdPart.MOUTH_CLOSED && !birdState.isMouthOpen() && !this.isCloud)) {
                    if (bc.getComponentBitmap() != null) {
                        canvas.drawBitmap(bc.getComponentBitmap(),
                                bc.getSpriteComponent().getPositionedLocation().x + staticLoc.x,
                                bc.getSpriteComponent().getPositionedLocation().y + staticLoc.y,
                                paint);
                    }
                }
            }

            Accessory draw;

            for (int i=0; i<3; i++) {
                if ((i == 0 && ((this.previewChest == null && (draw = this.chestAccessory) != null) || (draw = this.previewChest) != null))
                        || (i == 1 && ((this.previewEye == null && (draw = this.eyeAccessory) != null) || (draw = this.previewEye) != null))
                        || (i == 2 && ((this.previewHead == null && (draw = this.headAccessory) != null) || (draw = this.previewHead) != null))) {
                    if (draw.toString().contains("NONE")) continue;

                    canvas.drawBitmap(draw.sprite.getBitmap(),
                            draw.getOffset().x + staticLoc.x,
                            draw.getOffset().y + staticLoc.y,
                            paint);
                }
            }
        }

        paint.setAlpha(255); //Reset back to normal
    }

    private RectF getBodyRect() {
        this.bodyRect.left = birdData.getBirdComponentSet().getBody().getSpriteComponent().getPositionedLocation().x + this.birdLocation.x;
        this.bodyRect.top = this.birdData.getBirdComponentSet().getBody().getSpriteComponent().getPositionedLocation().y + this.birdLocation.y;
        this.bodyRect.right = this.birdData.getBirdComponentSet().getBody().getSpriteComponent().getPositionedLocation().x + this.birdLocation.x + this.birdData.getBirdComponentSet().getBody().getComponentBitmap().getWidth();
        this.bodyRect.bottom = this.birdData.getBirdComponentSet().getBody().getSpriteComponent().getPositionedLocation().y + this.birdLocation.y + this.birdData.getBirdComponentSet().getBody().getComponentBitmap().getHeight();

        return this.bodyRect;
    }

    public BirdData getBirdData() {
        return this.birdData;
    }

    public Point getBirdLocation() {
        return this.birdLocation;
    }

    public int getPointValue() {
        return this.birdData.getPointValue();
    }

    private RectF getWingRect() {
        this.wingRect.left = 0;
        this.wingRect.top = 0;
        this.wingRect.right = this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth();
        this.wingRect.bottom = this.birdData.getBirdComponentSet().getWing().getComponentBitmap().getHeight();

        this.wingMatrix.mapRect(this.wingRect);

        return this.wingRect;
    }

    public boolean isAllocated() {
        return this.allocated;
    }

    public boolean isDoneFlying() {
        //If bird if off left screen, its done flying.
        int bodyEnd = birdData.getBirdComponentSet().getBody().getSpriteComponent().getPositionedLocation().x
                + birdData.getBirdComponentSet().getBody().getComponentBitmap().getWidth()
                + birdLocation.x;
        int wingEnd = birdData.getBirdComponentSet().getWing().getSpriteComponent().getPositionedLocation().x
                + birdData.getBirdComponentSet().getWing().getComponentBitmap().getWidth()
                + birdLocation.x;

        return (bodyEnd <= 0 && wingEnd <= 0);
    }

    public void setBirdLocation(int x, int y) {
        this.wingMatrix.postTranslate(x - this.birdLocation.x, y - this.birdLocation.y);
        this.birdLocation.x = x;
        this.birdLocation.y = y;
    }

    private Thread wingThread = new Thread() {
        //Handles wing rotations

        @Override
        public void run() {
            wingMatrix.setTranslate(birdData.getBirdComponentSet().getWing().getSpriteComponent().getPositionedLocation().x + birdLocation.x,
                    birdData.getBirdComponentSet().getWing().getSpriteComponent().getPositionedLocation().y + birdLocation.y);

            int sign = birdData.getBirdComponentSet().doesFaceLeft() ? -1 : 1;
            int xWing, yWing;

            try {
                while (isAllocated()) { //While bird is allocated
                    if (HipsterBird.GAME_HANDLER.isPaused()) {
                        Thread.sleep(100);
                        continue;
                    }

                    for (int i=0; i<20 && birdData.getBirdComponentSet().getBody().getComponentBitmap() != null; i++) {
                        xWing = birdData.getBirdComponentSet().getWingAnchor().x + birdData.getBirdComponentSet().getWing().getSpriteComponent().getPositionedLocation().x + birdLocation.x;
                        yWing = birdData.getBirdComponentSet().getWingAnchor().y + birdData.getBirdComponentSet().getWing().getSpriteComponent().getPositionedLocation().y + birdLocation.y;

                        if (i < 10 || birdData == BirdData.CRAZY_BIRD) { //Only rotates one way and not back.
                            wingMatrix.postRotate(sign * birdData.getBirdComponentSet().getWingRotationDegree(), xWing, yWing);
                        } else {
                            wingMatrix.postRotate(sign * -birdData.getBirdComponentSet().getWingRotationDegree(), xWing, yWing);
                        }

                        Thread.sleep(wingRotationSpeed);
                    }
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    };

    private Thread stateThread = new Thread() {
        //Handles opening/closing of mouth and eyes of bird

        @Override
        public void run() {
            Timer cloudTimer = null;

            try {
                while (isAllocated()) { //While bird is allocated
                    if (HipsterBird.GAME_HANDLER.isPaused()) {
                        Thread.sleep(200);
                        continue;
                    }

                    /** CLOUD BIRD **/
                    if (birdData == BirdData.CLOUD_BIRD
                            && !isCloud
                            && Utility.random(0, 10) < 2) {
                        isCloud = true;
                        cloudTimer = new Timer(Utility.random(1000, 5000));
                    } else if (birdData == BirdData.CLOUD_BIRD
                            && isCloud
                            && cloudTimer != null
                            && !cloudTimer.isRunning()) {
                        isCloud = false;
                        cloudTimer = null;
                    }
                    /** END CLOUD BIRD **/

                    if (birdState == BirdState.DEFAULT_STATE
                            && Utility.random(0, 40) < 5) {
                        birdState = BirdState.values()[Utility.random(0, BirdState.values().length)];
                    } else {
                        birdState = BirdState.DEFAULT_STATE;
                    }

                    Thread.sleep(200);
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

    };

}
