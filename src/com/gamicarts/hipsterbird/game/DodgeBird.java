package com.gamicarts.hipsterbird.game;

import android.graphics.Point;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.graphics.Shop;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.data.BirdState;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.sprite.object.PowerUp;
import com.gamicarts.hipsterbird.sprite.object.thread.BirdMovement;
import com.gamicarts.hipsterbird.sprite.object.thread.PowerUpHandler;
import com.gamicarts.hipsterbird.utility.Timer;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 6/8/14
 * Time: 7:41 PM
 */
public class DodgeBird extends Thread {

    public final Game game;

    private ArrayList<Bird> activeBirds = new ArrayList<Bird>();
    private ArrayList<PowerUp> activePowerUps = new ArrayList<PowerUp>();

    public boolean active = true;
    public boolean lost = false;

    //Power up variables
    public int scoreMultiplier = 1;
    public int shopPointsGained;
    public Timer scoreMultiplierTimer;
    public Timer invincibilityTimer;

    public int currentScore = 0;
    public Bird birdHit = null;

    public DodgeBird(Game game) {
        this.game = game;

        currentScore = 0;
    }

    @Override
    public void run() {
        try {
            HipsterBird.GAME_SONG.seekTo(0);
            HipsterBird.GAME_SONG.start();

            BirdMovement bm = new BirdMovement(this); //Initiates bird movement thread
            bm.start();

            PowerUpHandler puh = new PowerUpHandler(this);
            puh.start();

            int extraLifes = 0;

            while (active
                    && !lost) {
                if (HipsterBird.GAME_HANDLER.isPaused()
                        || this.game.currentState == GameState.PAUSE) {
                    if (HipsterBird.GAME_SONG.isPlaying()) HipsterBird.GAME_SONG.pause();

                    while (HipsterBird.GAME_HANDLER.isPaused() || this.game.currentState == GameState.PAUSE) Thread.sleep(50);

                    if (!HipsterBird.GAME_SONG.isPlaying()) HipsterBird.GAME_SONG.start();
                }

                if (Utility.random(1, getBirdRandom(currentScore)) < 6) {
                    Bird nextBird = null;

                    if (Utility.random(1, 4) == 2) { //33% Chance
                        nextBird = new Bird(getBird(currentScore), new Point(800, (int) (game.hipsterBird.getBirdLocation().y * (1/Utility.getHeightRatio())))); //Backwards
                    } else {
                        nextBird = new Bird(getBird(currentScore), new Point(800, Utility.random(-50, HipsterBird.SCREEN_HEIGHT + 50)));
                    }

                    nextBird.allocateBird(HipsterBird.RESOURCES);
                    activeBirds.add(nextBird);
                }

                for (int i=0; i<activeBirds.size(); i++) {
                    if (activeBirds.get(i).scoreTallied
                            && (activeBirds.get(i).scoreTalliedTimer != null && !activeBirds.get(i).scoreTalliedTimer.isRunning())
                            && activeBirds.get(i).isDoneFlying()) {
                        currentScore += (activeBirds.get(i).getPointValue() * scoreMultiplier);

                        Utility.playGameSound(R.raw.point);

                        activeBirds.get(i).deallocateBird();
                        activeBirds.remove(i);
                        i--;
                    } else if (!game.hipsterBird.invincible
                            && game.hipsterBird.contains(activeBirds.get(i))) {
                        if (game.lives > 0) {
                            game.hipsterBird.invincible = true;
                            invincibilityTimer = new Timer(3000);
                        }

                        lost = (game.lives == 0 ? true : false);
                        game.lives--;

                        if (lost) {
                            birdHit = activeBirds.get(i);
                        }

                        break;
                    }
                }

                currentScore += scoreMultiplier;

                if (!HipsterBird.GAME_SONG.isPlaying()) {
                    HipsterBird.GAME_SONG.seekTo(0);
                    HipsterBird.GAME_SONG.start();
                }

                Thread.sleep(50);
            }

            shopPointsGained = currentScore / 500000;
            Shop.shopPoints += shopPointsGained;

            if (currentScore > Game.highscore) Game.highscore = currentScore;

            for (Bird b : activeBirds) b.deallocateBird();

            activeBirds.clear();
            activePowerUps.clear();

            game.hipsterBird.invincible = false;

            bm.stop = true;
            puh.stop = true;

            if (lost) {
                game.lives = 0;

                game.hipsterBird.birdState = BirdState.EYES_CLOSED_MOUTH_OPEN;
                Utility.playGameSound(R.raw.hit);

                if (birdHit != null) {
                    Bird lostBird = null;

                    //All points already get configured...
                    switch (birdHit.getBirdData()) {
                        case CLOUD_BIRD:
                            lostBird = new Bird(BirdData.CLOUD_BIRD, new Point(0, 175));
                            Game.adviceText.setContent("Hint: It's not actually a cloud...");
                            break;
                        case CRAZY_BIRD:
                            lostBird = new Bird(BirdData.CRAZY_BIRD, new Point(0, 155));
                            Game.adviceText.setContent("Hint: Crazy birds go up and down.");
                            break;
                        case MUSTACHE_BIRD:
                            lostBird = new Bird(BirdData.MUSTACHE_BIRD, new Point(0, 170));
                            Game.adviceText.setContent("Hint: Stache birds only go straight.");
                            break;
                        case POLICE_BIRD:
                            lostBird = new Bird(BirdData.POLICE_BIRD, new Point(0, 160));
                            Game.adviceText.setContent("Hint: Pass police birds to stop the chase.");
                            break;

                    }

                    if (lostBird != null) {
                        lostBird.allocateBird(HipsterBird.RESOURCES);

                        lostBird.setBirdLocation((int) Game.lostMenu.getRect().left + (int) ((Utility.getXRatio(300) - lostBird.getBirdData().getBirdComponentSet().getBody().getComponentBitmap().getWidth()) / 2),
                                lostBird.getBirdLocation().y); //Based on lost menu

                        activeBirds.add(lostBird);
                    }
                }

                game.currentState = GameState.LOST;
            } else {
                game.currentState = GameState.MAIN_MENU;
            }

            HipsterBird.GAME_SONG.stop();
            HipsterBird.GAME_SONG.prepareAsync();

            active = false;

        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public ArrayList<Bird> getActiveBirds() {
        return this.activeBirds;
    }

    public ArrayList<PowerUp> getActivePowerUps() {
        return this.activePowerUps;
    }

    private int getBirdRandom(int score) {
        if (score > 1000000) {
            return 45;
        } else {
            return 85 - (int) (score/25000.0D);
        }
    }

    private BirdData getBird(int score) {
        int random = Utility.random(0, 20);

        if (score > 120000
                && random <= 5) {
            return BirdData.CRAZY_BIRD;
        } else if (score > 300000
                && random <= 7) {
            return BirdData.CLOUD_BIRD;
        } else if (score > 500000
                && random == 8) {
            return BirdData.POLICE_BIRD;
        } else {
            return BirdData.MUSTACHE_BIRD;
        }
    }

}
