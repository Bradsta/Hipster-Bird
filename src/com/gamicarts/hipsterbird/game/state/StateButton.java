package com.gamicarts.hipsterbird.game.state;

import android.graphics.*;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/21/14
 * Time: 4:20 PM
 */
public enum StateButton {

    //Not as efficient as it could be. I may add PaintString/Rects in the future.

    //Main menu buttons
    CHEST_BUTTON(new RectF(50, 80, 210, 130), Color.argb(150, 0, 0, 0), "CHEST", 30, Typeface.NORMAL, Game.SHOP_STATES, GameState.SHOP_CHEST),
    EYES_BUTTON(new RectF(230, 80, 390, 130), Color.argb(150, 0, 0, 0), "EYES", 30, Typeface.NORMAL, Game.SHOP_STATES, GameState.SHOP_EYES),
    HEAD_BUTTON(new RectF(410, 80, 570, 130), Color.argb(150, 0, 0, 0), "HEAD", 30, Typeface.NORMAL, Game.SHOP_STATES, GameState.SHOP_HEAD),
    OTHER_BUTTON(new RectF(590, 80, 750, 130), Color.argb(150, 0, 0, 0), "OTHER", 30, Typeface.NORMAL, Game.SHOP_STATES, GameState.SHOP_OTHER),

    BACK_BUTTON(new RectF(565, 390, 755, 445), Color.argb(150, 0, 0, 0), "BACK", 36, Typeface.NORMAL, Game.SHOP_STATES, GameState.MAIN_MENU),
    START_BUTTON(new RectF(320, 230, 510, 285), Color.argb(150, 0, 0, 0), "START", 36, Typeface.NORMAL, new GameState[] { GameState.MAIN_MENU }, GameState.GAME),
    SHOP_BUTTON(new RectF(320, 295, 510, 350), Color.argb(150, 0, 0, 0), "SHOP", 36, Typeface.NORMAL, new GameState[] { GameState.MAIN_MENU }, GameState.SHOP_CHEST),
    PAUSE_BUTTON(new RectF(730, 450, 790, 470), Color.argb(150, 0, 0, 0), "PAUSE", 18, Typeface.NORMAL, new GameState[] { GameState.GAME }, GameState.PAUSE),
    RESUME_BUTTON(new RectF(320, 230, 510, 285), Color.argb(150, 0, 0, 0), "RESUME", 36, Typeface.NORMAL, new GameState[] { GameState.PAUSE }, GameState.GAME),
    QUIT_BUTTON(new RectF(320, 305, 510, 360), Color.argb(150, 0, 0, 0), "QUIT", 36, Typeface.NORMAL, new GameState[] { GameState.PAUSE }, GameState.MAIN_MENU),
    MAIN_MENU(new RectF(310, 325, 500, 380), Color.argb(150, 0, 0, 0), "MAIN MENU", 36, Typeface.NORMAL, new GameState[] { GameState.LOST }, GameState.MAIN_MENU);

    public RectF buttonRect;
    public int buttonBackgroundColor;
    public String buttonText;
    public float fontSize;
    public final int fontStyle;
    public final GameState[] currentState;
    public GameState nextState;

    public boolean showClicked;

    private static Rect bounds = new Rect();
    private static Point point = new Point();
    private static Paint paint = new Paint();

    private StateButton(RectF buttonRect, int buttonBackgroundColor, String buttonText, float fontSize, int fontStyle, GameState[] currentState, GameState nextState) {
        this.buttonRect = buttonRect;
        this.buttonBackgroundColor = buttonBackgroundColor;
        this.buttonText = buttonText;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
        this.currentState = currentState;
        this.nextState = nextState;
    }

    public void configure() {
        Utility.resizeRectangle(buttonRect);

        fontSize = Utility.getXRatio(fontSize);
    }

    public static GameState doButtonClick(GameState currentState, float currentX, float currentY) {
        for (StateButton b : StateButton.values()) {
            if (Utility.containsState(b.currentState, currentState)
                && b.buttonRect.contains(currentX, currentY)) {
                Utility.playGameSound(R.raw.button);

                return b.nextState;
            }
        }

        return currentState;
    }

    public static void drawStateButtons(Canvas canvas, GameState currentState, float currentX, float currentY) {
        paint.setAntiAlias(true);

        for (StateButton b : StateButton.values()) {
            paint.setTextSize(b.fontSize);
            paint.setTypeface(Typeface.create(HipsterBird.HOBO, b.fontStyle));
            paint.getTextBounds(b.buttonText, 0, b.buttonText.length(), bounds);
            point = Utility.getStringCentered(b.buttonRect, bounds);

            paint.setColor(Color.WHITE);
            if (Utility.containsState(b.currentState, currentState)) {
                if (b.buttonRect.contains(currentX, currentY) || b.showClicked) { //Because mission select buttons are picutres
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(b.buttonRect, paint);
                    paint.setColor(b.buttonBackgroundColor);
                    canvas.drawText(b.buttonText, point.x, point.y, paint);
                } else {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(b.buttonBackgroundColor);
                    canvas.drawRect(b.buttonRect, paint);

                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(b.buttonRect, paint);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText(b.buttonText, point.x, point.y, paint);
                }
            }
        }
    }

}
