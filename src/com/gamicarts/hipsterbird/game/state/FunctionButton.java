package com.gamicarts.hipsterbird.game.state;

import android.graphics.*;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.game.graphics.PaintRect;
import com.gamicarts.hipsterbird.game.graphics.PaintString;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 9/21/14
 * Time: 2:45 PM
 */
public enum FunctionButton {

    //Much more efficient button enum than StateButton

    PAGE_LEFT(new RectF(40, 240, 90, 280), "<-", 45, Game.SHOP_STATES),
    PAGE_RIGHT(new RectF(710, 240, 760, 280), "->", 45, Game.SHOP_STATES),
    BUY(new RectF(400, 390, 550, 445), "BUY", 36, Game.SHOP_STATES);

    public RectF buttonRect;
    public String buttonText;
    public float fontSize;
    public final GameState[] currentStates;

    public PaintString paintString;
    public PaintRect paintRect;

    public boolean showClicked;
    public boolean draw = true;

    private static Rect bounds = new Rect();
    private static Paint paint = new Paint();
    private static Point point = new Point();

    public static int ALPHA_BLACK = Color.argb(150, 0, 0, 0);

    private FunctionButton(RectF buttonRect, String buttonText, float fontSize, GameState[] currentStates) {
        this.buttonRect = buttonRect;
        this.buttonText = buttonText;
        this.fontSize = fontSize;
        this.currentStates = currentStates;
    }

    public void configure() {
        paint.setAntiAlias(true);

        buttonRect = Utility.getConfiguredRect(buttonRect);

        paint.setTextSize(Utility.getXRatio(this.fontSize));
        paint.setTypeface(Typeface.create(HipsterBird.HOBO, Typeface.NORMAL));
        paint.getTextBounds(buttonText, 0, buttonText.length(), bounds);

        point = Utility.getStringCentered(buttonRect, bounds);

        this.paintString = new PaintString(buttonText, HipsterBird.HOBO, Color.WHITE, Utility.getXRatio(this.fontSize), point);
        this.paintRect = new PaintRect(buttonRect, FunctionButton.ALPHA_BLACK, Color.WHITE);
    }

    public boolean selected(GameState currentState, float currentX, float currentY) {
       return Utility.containsState(currentStates, currentState) && buttonRect.contains(currentX, currentY);
    }

    public static void drawFunctionButtons(Canvas canvas, GameState currentState, float currentX, float currentY) {
        paint.setAntiAlias(true);

        for (FunctionButton functionButton : FunctionButton.values()) {
            if (Utility.containsState(functionButton.currentStates, currentState)
                    && functionButton.draw) {
                if (functionButton.buttonRect.contains(currentX,  currentY) || functionButton.showClicked) {
                    functionButton.paintString.setColor(FunctionButton.ALPHA_BLACK);
                    functionButton.paintRect.setBackgroundColor(Color.WHITE);
                } else {
                    functionButton.paintString.setColor(Color.WHITE);
                    functionButton.paintRect.setBackgroundColor(FunctionButton.ALPHA_BLACK);
                }

                functionButton.paintRect.paint(canvas);
                functionButton.paintString.paint(canvas);
            }
        }
    }

}
