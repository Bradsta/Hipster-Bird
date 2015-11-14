package com.gamicarts.hipsterbird.sprite.data;

import android.graphics.Point;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.sprite.Sprite;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 8/9/14
 * Time: 4:27 PM
 */
public enum Accessory {

    BOWTIE_BLACK(R.drawable.bowtie_black, "Black Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    BOWTIE_BLUE(R.drawable.bowtie_blue, "Blue Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    BOWTIE_PINK(R.drawable.bowtie_pink, "Pink Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    BOWTIE_RED(R.drawable.bowtie_red, "Red Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    BOWTIE_WHITE(R.drawable.bowtie_white, "White Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    BOWTIE_YELLOW(R.drawable.bowtie_yellow, "Yellow Bowtie", AccessoryType.CHEST, 1, new Point(65, 50)),
    CAMERA(R.drawable.camera, "Camera", AccessoryType.CHEST, 4, new Point(62, 37)),
    COLOR_BLACK(R.drawable.hipster_bird_black, "Black Bird", AccessoryType.COLOR, 5, null),
    COLOR_BLUE(R.drawable.hipster_bird_blue, "Blue Bird", AccessoryType.COLOR, 5, null),
    COLOR_GREEN(R.drawable.hipster_bird_green, "Green Bird", AccessoryType.COLOR, 0, null),
    COLOR_PINK(R.drawable.hipster_bird_pink, "Pink Bird", AccessoryType.COLOR, 5, null),
    COLOR_RED(R.drawable.hipster_bird_red, "Red Bird", AccessoryType.COLOR, 5, null),
    COLOR_WHITE(R.drawable.hipster_bird_white, "White Bird", AccessoryType.COLOR, 5, null),
    COLOR_YELLOW(R.drawable.hipster_bird_yellow, "Yellow Bird", AccessoryType.COLOR, 5, null),
    EXTRA_LIFE(R.drawable.extra_life, "Extra Life", AccessoryType.OTHER, 1, null),
    FEDORA(R.drawable.fedora, "Fedora", AccessoryType.HEAD, 2, new Point(62, -11)),
    GLASSES(R.drawable.glasses, "Glasses", AccessoryType.EYES, 0, new Point(64, 12)),
    HAIR_BLACK(R.drawable.hair_black, "Black Hair", AccessoryType.HEAD, 1, new Point(62, -12)),
    HAIR_BLONDE(R.drawable.hair_blonde, "Blonde Hair", AccessoryType.HEAD, 1, new Point(62, -12)),
    HAIR_BROWN(R.drawable.hair_brown, "Brown Hair", AccessoryType.HEAD, 1, new Point(62, -12)),
    HAIR_RED(R.drawable.hair_red, "Red Hair", AccessoryType.HEAD, 1, new Point(62, -12)),
    MONOCLE(R.drawable.monocle, "Monocle", AccessoryType.EYES, 4, new Point(63, 12)),
    NONE_CHEST(R.drawable.nothing, "None", AccessoryType.CHEST, 0, null),
    NONE_EYES(R.drawable.nothing, "None", AccessoryType.EYES, 0, null),
    NONE_HEAD(R.drawable.nothing, "None", AccessoryType.HEAD, 0, null),
    SCARF(R.drawable.scarf, "Scarf", AccessoryType.CHEST, 3, new Point(60, 35)),
    SMART_GLASSES(R.drawable.smart_glasses, "Smart Glasses", AccessoryType.EYES, 2, new Point(60, 15)),
    SOMBRERO(R.drawable.sombrero, "Sombrero", AccessoryType.HEAD, 3, new Point(55, -23)),
    SUN_GLASSES(R.drawable.sun_glasses, "Sun Glasses", AccessoryType.EYES, 3, new Point(64, 12)),
    TIE_BLACK(R.drawable.tie_black, "Black Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TIE_BLUE(R.drawable.tie_blue, "Blue Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TIE_PINK(R.drawable.tie_pink, "Pink Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TIE_RED(R.drawable.tie_red, "Red Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TIE_WHITE(R.drawable.tie_white, "White Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TIE_YELLOW(R.drawable.tie_yellow, "Yellow Tie", AccessoryType.CHEST, 2, new Point(72, 52)),
    TOP_HAT(R.drawable.top_hat, "Top Hat", AccessoryType.HEAD, 4, new Point(58, -13)),
    UKULELE(R.drawable.ukulele, "Ukulele", AccessoryType.CHEST, 5, new Point(57, 37));

    private final int id;
    private final AccessoryType accessoryType;
    private final Point offset;
    private final int cost;
    private final String name;

    private static final double SIZE_RATIO = 0.30D; //Ratio of hipster bird is same.

    public final Sprite sprite;

    public boolean unlocked = false;

    public static Accessory[] CHEST_ACCESSORIES = { NONE_CHEST, BOWTIE_RED, BOWTIE_BLACK, BOWTIE_BLUE, BOWTIE_PINK, BOWTIE_WHITE, BOWTIE_YELLOW,
                                                    TIE_BLACK, TIE_BLUE, TIE_PINK, TIE_RED, TIE_WHITE, TIE_YELLOW, SCARF, CAMERA, UKULELE };
    public static Accessory[] EYE_ACCESSORIES = { NONE_EYES, GLASSES, SMART_GLASSES, SUN_GLASSES, MONOCLE };
    public static Accessory[] HEAD_ACCESSORIES = { NONE_HEAD, HAIR_BLACK, HAIR_BLONDE, HAIR_BROWN, HAIR_RED, FEDORA, SOMBRERO, TOP_HAT };
    public static Accessory[] OTHER = { COLOR_BLACK, COLOR_BLUE, COLOR_GREEN, COLOR_PINK, COLOR_RED, COLOR_WHITE, COLOR_YELLOW, EXTRA_LIFE };

    private Accessory(int id, String name, AccessoryType accessoryType, int cost, Point offset) {
        this.id = id;
        this.name = name;
        this.offset = offset;
        this.cost = cost;
        this.accessoryType = accessoryType;

        this.sprite = new Sprite(id);
    }

    public static void allocateSprites() {
        for (Accessory accessory : Accessory.values()) {
            if (accessory.getType() != AccessoryType.COLOR) accessory.allocate();
        }
    }

    public static Accessory getAccessoryByName(String name) {
        for (Accessory a : Accessory.values()) {
            if (name.contains(a.toString())) {
                return a;
            }
        }

        return null;
    }

    public void allocate() {
        if (this.sprite.getBitmap() == null) {
            this.sprite.allocateSprite(HipsterBird.RESOURCES, true);
            this.sprite.resizeBitmap(SIZE_RATIO);

            if (this.offset != null) {
                this.offset.x = (int) Utility.getXRatio(this.offset.x);
                this.offset.y = (int) Utility.getYRatio(this.offset.y);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public int getID() {
        return this.id;
    }

    public Point getOffset() {
        return this.offset;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public AccessoryType getType() {
        return this.accessoryType;
    }

}
