package com.gamicarts.hipsterbird.game.task;

import android.graphics.*;
import android.view.MotionEvent;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.DodgeBird;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.state.StateButton;
import com.gamicarts.hipsterbird.game.graphics.Shop;
import com.gamicarts.hipsterbird.sprite.Sprite;
import com.gamicarts.hipsterbird.sprite.component.BirdComponentSet;
import com.gamicarts.hipsterbird.sprite.data.Accessory;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.data.PowerUpData;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.sprite.object.PowerUp;
import com.gamicarts.hipsterbird.game.graphics.PaintRect;
import com.gamicarts.hipsterbird.game.graphics.PaintString;
import com.gamicarts.hipsterbird.utility.Timer;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/21/14
 * Time: 5:09 PM
 */
public class Game implements GameTask {

    public static Sprite background = new Sprite(R.drawable.background);
    public static Sprite title = new Sprite(R.drawable.title, new Point(265, 30));

    public static GameState[] SHOP_STATES = new GameState[] {
            GameState.SHOP,
            GameState.SHOP_EYES,
            GameState.SHOP_HEAD,
            GameState.SHOP_CHEST,
            GameState.SHOP_OTHER
    };

    public static Sprite[] clouds = { new Sprite(R.drawable.cloud1),
                                       new Sprite(R.drawable.cloud2),
                                       new Sprite(R.drawable.cloud3),
                                       new Sprite(R.drawable.cloud1),
                                       new Sprite(R.drawable.cloud2),
                                       new Sprite(R.drawable.cloud3) };

    public static PaintString scoreText;
    public static PaintString livesText;
    public static PaintString highscoreText;
    public static PaintString finalScore;
    public static PaintString adviceText;
    public static PaintString shopPointsGained;
    public static PaintString howTo1;
    public static PaintString howTo2;

    public static PaintRect highscoreBox;
    public static PaintRect scoreBox;
    public static PaintRect lostMenu;

    private Point event = new Point();
    private Point pressed = new Point();
    private Point point = new Point();
    private boolean isMovingBird;

    public GameState currentState = GameState.MAIN_MENU;
    public DodgeBird dodgeBird;
    public Bird hipsterBird;

    public static int lives = 1;
    public static int highscore = 0;

    public static boolean firstTime = false; //Whether or not user is playing this game for the first time.

    //Default values
    public static BirdData defaultData = BirdData.HIPSTER_BIRD_GREEN;
    public static Accessory defaultChest = null;
    public static Accessory defaultEye = Accessory.GLASSES;
    public static Accessory defaultHead = null;

    @Override
    public void run() {
        cloudThread.start();

        hipsterBird = new Bird(defaultData, new Point(360, 120));
        hipsterBird.chestAccessory = defaultChest;
        hipsterBird.eyeAccessory = defaultEye;
        hipsterBird.headAccessory = defaultHead;

        hipsterBird.allocateBird(HipsterBird.RESOURCES);

        try {
            while (HipsterBird.GAME_HANDLER.isActive()) {
                if (currentState == GameState.GAME
                        && (dodgeBird == null || !dodgeBird.active)) {
                    (dodgeBird = new DodgeBird(this)).start();
                    firstTime = false;
                } else if (currentState == GameState.MAIN_MENU
                        && dodgeBird != null) {
                    dodgeBird.active = false;
                    dodgeBird.getActiveBirds().clear(); //Lost bird will show next time otherwise.
                }

                if (!currentState.toString().contains("SHOP")) {
                    //Disabling previews from the shop
                    hipsterBird.previewChest = null;
                    hipsterBird.previewHead = null;
                    hipsterBird.previewEye = null;

                    hipsterBird.getBirdData().setBirdComponentSet(BirdComponentSet.HIPSTER_BIRD_SET);
                }

                StateButton.CHEST_BUTTON.showClicked = (this.currentState == GameState.SHOP_CHEST);
                StateButton.EYES_BUTTON.showClicked = (this.currentState == GameState.SHOP_EYES);
                StateButton.HEAD_BUTTON.showClicked = (this.currentState == GameState.SHOP_HEAD);
                StateButton.OTHER_BUTTON.showClicked = (this.currentState == GameState.SHOP_OTHER);

                scoreText.setContent("Score: " + (dodgeBird != null ? Utility.getFormatted(dodgeBird.currentScore) : 0));
                livesText.setContent("" + lives);
                highscoreText.setContent("Highscore: " + (dodgeBird != null && dodgeBird.currentScore > highscore ? Utility.getFormatted(dodgeBird.currentScore) : Utility.getFormatted(highscore)));

                Thread.sleep(100);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);

        if (hipsterBird == null
                || !hipsterBird.isAllocated()) { //Just show a black screen while bird is being allocated.
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, 0, HipsterBird.SCREEN_WIDTH, HipsterBird.SCREEN_HEIGHT, paint);
        } else {

            /* Static backgrounds */
            //Static background are the blue background and the clouds.

            if (background.getBitmap() != null) {
                canvas.drawBitmap(background.getBitmap(), 0, 0, paint);
            }

            for (Sprite s : clouds) {
                if (s.getBitmap() != null) canvas.drawBitmap(s.getBitmap(), s.getLocation().x, s.getLocation().y, paint);
            }

            /* End of static backgrounds */

            //The Hipster Bird title text.
            if (currentState == GameState.MAIN_MENU
                    && title.getBitmap() != null) {
                canvas.drawBitmap(title.getBitmap(), title.getLocation().x, title.getLocation().y, paint);

                if (firstTime) {
                    howTo1.paint(canvas);
                    howTo2.paint(canvas);
                }
            }

            if (currentState == GameState.LOST) {
                lostMenu.paint(canvas);

                point = Utility.getStringCentered(lostMenu.getRect(), adviceText.getTextBounds());
                adviceText.setLocation(point.x, adviceText.getLocation().y);
                adviceText.paint(canvas);

                finalScore.setContent(Game.highscore == dodgeBird.currentScore ? "Highscore: " + Utility.getFormatted(Game.highscore) : "Score: " + Utility.getFormatted(dodgeBird.currentScore));
                point = Utility.getStringCentered(lostMenu.getRect(), finalScore.getTextBounds());
                finalScore.setLocation(point.x, finalScore.getLocation().y);
                finalScore.paint(canvas);

                shopPointsGained.setContent("Shop points gained: " + dodgeBird.shopPointsGained);
                point = Utility.getStringCentered(lostMenu.getRect(), shopPointsGained.getTextBounds());
                shopPointsGained.setLocation(point.x, shopPointsGained.getLocation().y);
                shopPointsGained.paint(canvas);
            }

            drawGame(canvas, paint); //Everything that is associated with drawing in the actual game.
            //Will only draw if we're actually in a game.

            livesText.paint(canvas);
            canvas.drawBitmap(PowerUpData.EXTRA_LIFE.getSprite().getBitmap(), Utility.getXRatio(15), Utility.getYRatio(425), paint);

            highscoreBox.getRect().right = highscoreText.getLocation().x + highscoreText.getTextBounds().width() + Utility.getXRatio(10);
            highscoreBox.paint(canvas);
            highscoreText.paint(canvas);

            if (currentState.toString().contains("SHOP")) Shop.drawShop(canvas, paint, currentState, pressed.x, pressed.y);
            //Draws everything in the shop

            //What is drawn when the game is paused. A simple box that is filled with buttons.
            if (currentState == GameState.PAUSE) {
                paint.setColor(Color.argb(150, 0, 0, 0));
                paint.setStyle(Paint.Style.FILL);

                canvas.drawRect(0, 0, HipsterBird.SCREEN_WIDTH, HipsterBird.SCREEN_HEIGHT, paint);

                paint.setAlpha(255);
            }

            //Draws all buttons that allow the program to change states.
            StateButton.drawStateButtons(canvas, currentState, pressed.x, pressed.y);

            //Drawing Hipster Bird
            if (hipsterBird != null
                    && hipsterBird.isAllocated()) {
                hipsterBird.draw(canvas, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        event.x = (int) motionEvent.getX();
        event.y = (int) motionEvent.getY();

        //System.out.println(motionEvent.getAction());

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            pressed.x = event.x;
            pressed.y = event.y;

            if (hipsterBird.contains(pressed.x, pressed.y, true)
                    && currentState != GameState.PAUSE) isMovingBird = true;
        } else if (motionEvent.getAction() == 261) { //Multiple fingers touch, used to avoid teleporting of bird :P
            if (isMovingBird) isMovingBird = false;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (currentState != GameState.PAUSE
                    && isMovingBird) {
                hipsterBird.setBirdLocation(hipsterBird.getBirdLocation().x + (event.x - pressed.x), hipsterBird.getBirdLocation().y + (event.y - pressed.y));
            }

            pressed.x = event.x;
            pressed.y = event.y;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            isMovingBird = false;

            if (currentState.toString().contains("SHOP")) {
                Shop.handleShopButtons(currentState, this, hipsterBird, pressed.x, pressed.y);
            }

            currentState = StateButton.doButtonClick(currentState, pressed.x, pressed.y);

            pressed.x = -1;
            pressed.y = -1;
        }

        return true;
    }

    private Thread cloudThread = new Thread() {

        @Override
        public void run() {
            int minSpeed = (int) Utility.getXRatio(3);
            int maxSpeed = (int) Utility.getXRatio(6);

            for (Sprite cloud : clouds) {
                cloud.setSpeed(Utility.random(minSpeed, maxSpeed));
                cloud.setLocation(new Point(Utility.random(0, HipsterBird.SCREEN_WIDTH), Utility.random(0, HipsterBird.SCREEN_HEIGHT)));
            }

            try {
                while (HipsterBird.GAME_HANDLER.isActive()) {
                    if (HipsterBird.GAME_HANDLER.isPaused()) {
                        Thread.sleep(100);
                        continue;
                    }

                    for (Sprite cloud : clouds) {
                        cloud.setLocation(new Point(cloud.getLocation().x - cloud.getSpeed(), cloud.getLocation().y));

                        if (cloud.getBitmap() != null
                            && cloud.getLocation().x < -cloud.getBitmap().getWidth()) {
                            cloud.setLocation(new Point(HipsterBird.SCREEN_WIDTH, Utility.random(0, HipsterBird.SCREEN_HEIGHT)));
                            cloud.setSpeed(Utility.random(minSpeed, maxSpeed));
                        }
                    }

                    Thread.sleep(25);
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

    };

    private void drawGame(Canvas canvas, Paint paint) {
        if ((currentState == GameState.GAME || currentState == GameState.PAUSE || currentState == GameState.LOST)
                && dodgeBird != null
                && dodgeBird.getActiveBirds().size() > 0) {
            for (PowerUp powerUp : dodgeBird.getActivePowerUps()) {
                powerUp.draw(canvas, paint);
            }

            for (int i=0; i<dodgeBird.getActiveBirds().size(); i++) {
                Bird next = i < dodgeBird.getActiveBirds().size() ? dodgeBird.getActiveBirds().get(i) : null;

                if (next != null
                        && next.isAllocated()) {
                    next.draw(canvas, paint);
                }

                if (next.isDoneFlying()
                        && (!next.scoreTallied || (next.scoreTalliedTimer != null && next.scoreTalliedTimer.isRunning()))) {
                    paint.setColor(next.getBirdData().getRelatedColor());
                    paint.setTextSize(Utility.getXRatio(30));
                    paint.setTypeface(Typeface.create(HipsterBird.HOBO, Typeface.NORMAL));
                    canvas.drawText("+" + (next.getPointValue() * dodgeBird.scoreMultiplier), 0, next.getBirdLocation().y, paint);

                    if (next.scoreTalliedTimer == null) next.scoreTalliedTimer = new Timer(500);

                    next.scoreTallied = true;
                }
            }
        }

        if (currentState == GameState.GAME || currentState == GameState.PAUSE) {
            scoreBox.getRect().right = scoreText.getLocation().x + scoreText.getTextBounds().width() + Utility.getXRatio(10);
            scoreBox.paint(canvas);
            scoreText.paint(canvas);
        }
    }

}
