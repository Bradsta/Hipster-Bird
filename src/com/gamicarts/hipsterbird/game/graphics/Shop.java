package com.gamicarts.hipsterbird.game.graphics;

import android.graphics.*;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.state.FunctionButton;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.sprite.component.BirdComponentSet;
import com.gamicarts.hipsterbird.sprite.data.Accessory;
import com.gamicarts.hipsterbird.sprite.data.AccessoryType;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 9/19/14
 * Time: 1:10 AM
 */
public class Shop {

    public static RectF shopBackground = new RectF(30, 20, 770, 460); //Background
    public static RectF shopFrame = new RectF(100, 140, 700, 380); //Frame around items

    public static PaintString shopTitle;
    public static PaintString shopPointDisplay;
    public static PaintString shopAdvice;

    private static int chestPage = 1;
    private static int eyesPage = 1;
    private static int headPage = 1;
    private static int colorPage = 1;

    public static int shopPoints = 5;

    private static Accessory selectedAccessory; //To buy

    private static Accessory[] accessoriesDisplayed = new Accessory[8];
    private static Point selectedSquare = new Point();

    public static void drawShop(Canvas canvas, Paint paint, GameState currentState, float currentX, float currentY) {
        paint.setARGB(200, 0, 0, 0);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(shopBackground, 10, 10, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(shopBackground, 10, 10, paint);
        canvas.drawRect(shopFrame, paint);

        for (int y=0; y<shopFrame.height(); y += (shopFrame.height() / 2)) { //2 rows
            for (int x=0; x<shopFrame.width(); x += (shopFrame.width() / 4)) { //4 columns
                float xBase = shopFrame.left + x;
                float yBase = shopFrame.top + y;

                canvas.drawRect(xBase, yBase, xBase + (shopFrame.width() / 4), yBase + (shopFrame.height() / 2), paint);
            }
        }

        if (selectedMatchesPage(selectedAccessory, currentState)) {
            paint.setAlpha(100);
            paint.setStyle(Paint.Style.FILL);

            float xBase = shopFrame.left + selectedSquare.x * (shopFrame.width() / 4);
            float yBase = shopFrame.top + selectedSquare.y * (shopFrame.height() / 2);

            canvas.drawRect(new RectF(xBase, yBase, xBase + (shopFrame.width() / 4), yBase + (shopFrame.height() / 2)), paint);

            paint.setAlpha(255);
        }

        switch (currentState) {
            case SHOP_OTHER:
                drawPage(Accessory.OTHER, colorPage, canvas, paint);
                break;
            case SHOP_CHEST:
                drawPage(Accessory.CHEST_ACCESSORIES, chestPage, canvas, paint);
                break;
            case SHOP_EYES:
                drawPage(Accessory.EYE_ACCESSORIES, eyesPage, canvas, paint);
                break;
            case SHOP_HEAD:
                drawPage(Accessory.HEAD_ACCESSORIES, headPage, canvas, paint);
                break;
        }

        FunctionButton.drawFunctionButtons(canvas, currentState, currentX, currentY);

        shopTitle.paint(canvas);
        shopPointDisplay.setContent("Shop points: " + shopPoints);
        shopPointDisplay.paint(canvas);
        shopAdvice.paint(canvas);
     }

    public static void handleShopButtons(GameState currentState, Game game, Bird hipsterBird, float currentX, float currentY) {
        boolean boughtColor = false;

        for (FunctionButton functionButton : FunctionButton.values()) {
            if (functionButton.selected(currentState, currentX, currentY)) {
                if (functionButton == FunctionButton.PAGE_LEFT) {
                    pageDown(currentState);
                    return;
                } else if (functionButton == FunctionButton.PAGE_RIGHT) {
                    pageUp(currentState);
                    return;
                } else if (functionButton == FunctionButton.BUY) {
                    if (selectedAccessory != null
                            && !selectedAccessory.unlocked
                            && shopPoints >= selectedAccessory.getCost()) {
                        Utility.playGameSound(R.raw.button);

                        shopPoints -= selectedAccessory.getCost();

                        if (selectedAccessory != Accessory.EXTRA_LIFE) { //ONLY FOR EXTRA LIFE
                            selectedAccessory.unlocked = true;
                        }

                        if (selectedAccessory.getType() == AccessoryType.CHEST) hipsterBird.chestAccessory = selectedAccessory;
                        else if (selectedAccessory.getType() == AccessoryType.EYES) hipsterBird.eyeAccessory = selectedAccessory;
                        else if (selectedAccessory.getType() == AccessoryType.HEAD) hipsterBird.headAccessory = selectedAccessory;
                        else if (selectedAccessory == Accessory.EXTRA_LIFE) game.lives++;

                        if (selectedAccessory.getType() != AccessoryType.COLOR) {
                            return;
                        } else {
                            boughtColor = true;
                        }
                    }
                }
            }
        }

        if (shopFrame.contains(currentX, currentY)
                || boughtColor) {
            if (!boughtColor) {
                selectedSquare.x = (int) ((currentX - shopFrame.left) / (shopFrame.width() / 4));
                selectedSquare.y = (int) ((currentY - shopFrame.top) / (shopFrame.height() / 2));

                selectedAccessory = accessoriesDisplayed[selectedSquare.x + (selectedSquare.y * 4)];
            }

            if (selectedAccessory != null) {
                if (selectedAccessory.getType() == AccessoryType.CHEST) {
                    if (selectedAccessory.getCost() == 0
                            || selectedAccessory.unlocked) hipsterBird.chestAccessory = selectedAccessory;

                    hipsterBird.previewChest = selectedAccessory;
                    hipsterBird.previewEye = null;
                    hipsterBird.previewHead = null;
                    hipsterBird.getBirdData().setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                } else if (selectedAccessory.getType() == AccessoryType.EYES) {
                    if (selectedAccessory.getCost() == 0
                            || selectedAccessory.unlocked) hipsterBird.eyeAccessory = selectedAccessory;

                    hipsterBird.previewEye = selectedAccessory;
                    hipsterBird.previewHead = null;
                    hipsterBird.previewChest = null;
                    hipsterBird.getBirdData().setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                } else if (selectedAccessory.getType() == AccessoryType.HEAD) {
                    if (selectedAccessory.getCost() == 0
                            || selectedAccessory.unlocked) hipsterBird.headAccessory = selectedAccessory;

                    hipsterBird.previewHead = selectedAccessory;
                    hipsterBird.previewEye = null;
                    hipsterBird.previewChest = null;
                    hipsterBird.getBirdData().setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                } else if (selectedAccessory.getType() == AccessoryType.COLOR) {
                    selectedColor(selectedAccessory, hipsterBird);

                    hipsterBird.previewEye = null;
                    hipsterBird.previewHead = null;
                    hipsterBird.previewChest = null;
                } else if (selectedAccessory.getType() == AccessoryType.OTHER) {
                    hipsterBird.previewEye = null;
                    hipsterBird.previewHead = null;
                    hipsterBird.previewChest = null;
                }
            }
        }
    }

    private static void drawPage(Accessory[] accessories, int page, Canvas canvas, Paint paint) {
        paint.setTextSize(Utility.getXRatio(20));
        paint.setTypeface(Typeface.create(HipsterBird.HOBO, Typeface.NORMAL));
        Rect stringBounds = new Rect();
        Point stringPoint;

        accessoriesDisplayed = new Accessory[8];

        FunctionButton.PAGE_LEFT.draw = (page != 1);
        FunctionButton.PAGE_RIGHT.draw = (accessories.length > (page * 8));

        for (int i=((page-1) * 8); i < (page * 8); i++) {
            int loc = i % 8;

            if (i < accessories.length) {
                accessoriesDisplayed[loc] = accessories[i];

                Bitmap temp = null;

                if (accessories != Accessory.OTHER) {
                    temp = Utility.getRatioedBitmap(accessories[i].getSprite().getBitmap(), 1.5F);//Can use get string centered rect to place the accessories within the middle of the buttons.
                }

                float xBase = shopFrame.left + ((loc % 4) * (shopFrame.width() / 4));
                float yBase = shopFrame.top + ((loc > 3 ? 1 : 0) * (shopFrame.height() / 2));

                RectF surrounding = new RectF(xBase, yBase, xBase + (shopFrame.width() / 4), yBase + (shopFrame.height() / 2));

                if (temp != null) {
                    Point centered = Utility.getCenteredPoint(surrounding, new RectF(0, 0, temp.getWidth(), temp.getHeight()));
                    canvas.drawBitmap(temp, centered.x, centered.y, paint);
                }

                //Draw item name above item.
                paint.setStyle(Paint.Style.FILL);
                paint.getTextBounds(accessories[i].getName(), 0, accessories[i].getName().length(), stringBounds);
                stringPoint = Utility.getStringTopCenter(surrounding, stringBounds);
                canvas.drawText(accessories[i].getName(), stringPoint.x, stringPoint.y, paint);

                //Draw item cost below item
                if (!accessories[i].unlocked
                        && accessories[i].getCost() != 0) {
                    paint.getTextBounds("Cost: " + accessories[i].getCost(), 0, ("Cost: " + accessories[i].getCost()).length(), stringBounds);
                    stringPoint = Utility.getStringBottomCenter(surrounding, stringBounds);
                    canvas.drawText("Cost: " + accessories[i].getCost(), stringPoint.x, stringPoint.y, paint);
                } else {
                    paint.getTextBounds("Owned", 0, 5, stringBounds);
                    stringPoint = Utility.getStringBottomCenter(surrounding, stringBounds);
                    canvas.drawText("Owned", stringPoint.x, stringPoint.y, paint);
                }
            }
        }
    }

    private static void selectedColor(Accessory selectedAccessory, Bird hipsterBird) {
        for (BirdData bd : BirdData.values()) {
            if (bd.getID() == selectedAccessory.getID()) {
                if (bd != hipsterBird.getBirdData()) {
                    if (hipsterBird.getBirdData().getBirdComponentSet() == BirdComponentSet.TEMP1_BIRD_SET) {
                        bd.setBirdComponentSet(BirdComponentSet.TEMP2_BIRD_SET); //Temporary bird set

                        BirdComponentSet.TEMP2_BIRD_SET.deallocate();
                    } else {
                        bd.setBirdComponentSet(BirdComponentSet.TEMP1_BIRD_SET); //Temporary bird set

                        BirdComponentSet.TEMP1_BIRD_SET.deallocate();
                    }

                    bd.allocateComponentImages(HipsterBird.RESOURCES, true);

                    hipsterBird.getBirdData().setBirdComponentSet(bd.getBirdComponentSet());

                    if (selectedAccessory.getCost() == 0 || selectedAccessory.unlocked) {
                        bd.setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                        BirdComponentSet.HIPSTER_BIRD_SET.deallocate();
                        bd.allocateComponentImages(HipsterBird.RESOURCES, true);

                        hipsterBird.currentBirdData = bd;
                    }
                } else {
                    //TODO Re-write all my code so I don't have to do this...
                    BirdData.TEMP.setID(hipsterBird.getBirdData().getID());
                    BirdData.TEMP.setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);

                    if (BirdData.TEMP.getBirdComponentSet() != hipsterBird.getBirdData().getBirdComponentSet()) { //Just in case they spam click the default.
                        BirdData.TEMP.allocateComponentImages(HipsterBird.RESOURCES, true);
                    }

                    hipsterBird.getBirdData().setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                    hipsterBird.currentBirdData = hipsterBird.getBirdData();
                }

                break;
            }
        }
    }

    private static boolean selectedMatchesPage(Accessory selectedAccessory, GameState currentState) {
        if (selectedAccessory != null) {
            switch (selectedAccessory.getType()) {
                case CHEST:
                    return currentState == GameState.SHOP_CHEST;
                case OTHER:
                case COLOR:
                    return currentState == GameState.SHOP_OTHER;
                case EYES:
                    return currentState == GameState.SHOP_EYES;
                case HEAD:
                    return currentState == GameState.SHOP_HEAD;
            }
        }

        return false;
    }

    private static void pageUp(GameState currentState) {
        switch (currentState) {
            case SHOP_CHEST:
                if (((Accessory.CHEST_ACCESSORIES.length/(8+1)) + 1) > chestPage) chestPage++;

                break;
            case SHOP_EYES:
                if (((Accessory.EYE_ACCESSORIES.length/(8+1)) + 1) > eyesPage) eyesPage++;

                break;
            case SHOP_HEAD:
                if (((Accessory.HEAD_ACCESSORIES.length/(8+1)) + 1) > headPage) headPage++;

                break;
            case SHOP_OTHER:
                if (((Accessory.OTHER.length/(8+1)) + 1) > colorPage) colorPage++;

                break;
        }
    }

    private static void pageDown(GameState currentState) {
        switch (currentState) {
            case SHOP_CHEST:
                if (chestPage > 1) chestPage--;

                break;
            case SHOP_EYES:
                if (eyesPage > 1) eyesPage--;

                break;
            case SHOP_HEAD:
                if (headPage > 1) headPage--;

                break;
            case SHOP_OTHER:
                if (colorPage > 1) colorPage--;

                break;
        }
    }

}
