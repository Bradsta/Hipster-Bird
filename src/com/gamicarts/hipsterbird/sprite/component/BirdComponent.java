package com.gamicarts.hipsterbird.sprite.component;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import com.gamicarts.hipsterbird.sprite.data.BirdPart;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/3/14
 * Time: 3:42 PM
 */
public enum BirdComponent {

    //HIPSTER BIRD COMPONENTS
    HIPSTER_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 334, 293), new Point(0, 0))),
    HIPSTER_BIRD_EYES_CLOSED(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(0, 334, 83, 367), new Point(219, 48))),
    HIPSTER_BIRD_EYES_OPEN(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(0, 297, 78, 330), new Point(223, 48))),
    HIPSTER_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(90, 300, 136, 331), new Point(245, 78))),
    HIPSTER_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(90, 335, 135, 371), new Point(246, 77))),
    HIPSTER_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(140, 299, 331, 408), new Point(25, 130))),

    //HIPSTER BIRD TEMP COMPONENTS
    TEMP1_HIPSTER_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 334, 293), new Point(0, 0))),
    TEMP1_HIPSTER_BIRD_EYES_CLOSED(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(0, 334, 83, 367), new Point(219, 48))),
    TEMP1_HIPSTER_BIRD_EYES_OPEN(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(0, 297, 78, 330), new Point(223, 48))),
    TEMP1_HIPSTER_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(90, 300, 136, 331), new Point(245, 78))),
    TEMP1_HIPSTER_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(90, 335, 135, 371), new Point(246, 77))),
    TEMP1_HIPSTER_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(140, 299, 331, 408), new Point(25, 130))),

    //HIPSTER BIRD TEMP COMPONENTS
    TEMP2_HIPSTER_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 334, 293), new Point(0, 0))),
    TEMP2_HIPSTER_BIRD_EYES_CLOSED(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(0, 334, 83, 367), new Point(219, 48))),
    TEMP2_HIPSTER_BIRD_EYES_OPEN(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(0, 297, 78, 330), new Point(223, 48))),
    TEMP2_HIPSTER_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(90, 300, 136, 331), new Point(245, 78))),
    TEMP2_HIPSTER_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(90, 335, 135, 371), new Point(246, 77))),
    TEMP2_HIPSTER_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(140, 299, 331, 408), new Point(25, 130))),

    //MUSTACHE BIRD COMPONENTS
    MUSTACHE_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 297, 178), new Point(0, 0))),
    MUSTACHE_BIRD_EYES_CLOSED(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(201, 230, 285, 260), new Point(24, 45))),
    MUSTACHE_BIRD_EYES_OPEN(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(201, 263, 285, 293), new Point(24, 45))),
    MUSTACHE_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(200, 180, 274, 225), new Point(25, 80))), //Same as mouth open, just different location
    MUSTACHE_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(200, 180, 274, 225), new Point(25, 75))),
    MUSTACHE_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(0, 182, 198, 294), new Point(125, 30))),

    //CRAZY BIRD COMPONENTS
    CRAZY_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 126, 262), new Point(0, 0))),
    CRAZY_BIRD_EYES_1(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(140, 172, 215, 207), new Point(22, 62))),
    CRAZY_BIRD_EYES_2(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(138, 217, 214, 252), new Point(22, 62))),
    CRAZY_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(144, 53, 189, 104), new Point(10, 110))),
    CRAZY_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(148, 110, 199, 161), new Point(10 , 110))),
    CRAZY_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(126, 8, 220, 47), new Point(90, 150))),

    //POLICE BIRD COMPONENTS
    POLICE_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 179, 291), new Point(0, 0))),
    POLICE_BIRD_EYES_2(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(191, 237, 298, 280), new Point(40, 110))),
    POLICE_BIRD_EYES_1(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(192, 176, 299, 219), new Point(30, 110))),
    POLICE_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(253, 102, 299, 143), new Point(60, 150))),
    POLICE_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(189, 65, 235, 145), new Point(60, 125))),
    POLICE_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(181, 0, 311, 45), new Point(120, 210))),

    //CLOUD BIRD COMPONENTS
    CLOUD_BIRD_BODY(BirdPart.BODY, new SpriteComponent(new Rect(0, 0, 241, 112), new Point(0, 0))),
    CLOUD_BIRD_EYES_CLOSED(BirdPart.EYES_CLOSED, new SpriteComponent(new Rect(160, 114, 225, 139), new Point(5, 35))),
    CLOUD_BIRD_EYES_OPEN(BirdPart.EYES_OPENED, new SpriteComponent(new Rect(160, 145, 226, 171), new Point(5, 35))),
    CLOUD_BIRD_MOUTH_CLOSED(BirdPart.MOUTH_CLOSED, new SpriteComponent(new Rect(243, 94, 334, 165), new Point(-5, 20))),
    CLOUD_BIRD_MOUTH_OPEN(BirdPart.MOUTH_OPENED, new SpriteComponent(new Rect(246, 8, 337, 79), new Point(-5, 20))),
    CLOUD_BIRD_WING(BirdPart.WING, new SpriteComponent(new Rect(9, 122, 147, 167), new Point(120, 25)));

    private final BirdPart birdPart;
    private final SpriteComponent spriteComponent;

    private Bitmap componentBitmap;

    private BirdComponent(BirdPart birdPart, SpriteComponent spriteComponent) {
        this.spriteComponent = spriteComponent;
        this.birdPart = birdPart;
    }

    public BirdPart getBirdPart() {
        return this.birdPart;
    }

    public Bitmap getComponentBitmap() {
        return this.componentBitmap;
    }

    public SpriteComponent getSpriteComponent() {
        return this.spriteComponent;
    }

    public void setComponentBitmap(Bitmap bitmap) {
        this.componentBitmap = bitmap;
    }

}
