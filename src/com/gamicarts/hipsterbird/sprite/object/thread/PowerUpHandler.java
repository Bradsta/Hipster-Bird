package com.gamicarts.hipsterbird.sprite.object.thread;

import android.graphics.Point;
import android.location.Location;
import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.R;
import com.gamicarts.hipsterbird.game.DodgeBird;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.sprite.data.PowerUpData;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.sprite.object.PowerUp;
import com.gamicarts.hipsterbird.utility.Timer;
import com.gamicarts.hipsterbird.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 8/9/14
 * Time: 4:18 PM
 */
public class PowerUpHandler extends Thread {

    private final Game game;
    private final Bird hipsterBird;

    public boolean stop;

    private long lastTimeLeft; //Used for invincibility flickering

    public PowerUpHandler(DodgeBird dodgeBird) {
        this.game = dodgeBird.game;
        this.hipsterBird = this.game.hipsterBird;
    }

    @Override
    public void run() {
        try {
            while (game.dodgeBird != null
                    && game.dodgeBird.active
                    && !stop) {
                if (HipsterBird.GAME_HANDLER.isPaused()
                        || game.currentState == GameState.PAUSE) {
                    Thread.sleep(33);
                    continue;
                }

                for (PowerUpData pud : PowerUpData.values()) {
                    if (Utility.random(1, 5000) <= pud.getChance()) {
                        Utility.playGameSound(R.raw.powerup);

                        game.dodgeBird.getActivePowerUps().add(new PowerUp(pud, new Point(Utility.random(50, HipsterBird.SCREEN_WIDTH - 50), Utility.random(50, HipsterBird.SCREEN_HEIGHT - 50))));
                    }
                }

                for (int i=0; i<game.dodgeBird.getActivePowerUps().size(); i++) {
                    PowerUp powerUp = game.dodgeBird.getActivePowerUps().get(i);

                    if (!powerUp.getFloatingTimer().isRunning()) {
                        game.dodgeBird.getActivePowerUps().remove(i);
                        i--; //Avoid index out of bounds exception

                        continue;
                    }

                    if (powerUp.index >= (powerUp.floatingLocations.size() - 1)) {
                        powerUp.index = 0;
                        Collections.reverse(powerUp.floatingLocations);
                    }

                    powerUp.setCurrentLocation(powerUp.floatingLocations.get(powerUp.index));
                    powerUp.index++;

                    if (game.hipsterBird.contains(powerUp.getRectLocation())) {
                        Utility.playGameSound(R.raw.powerup);

                        switch (powerUp.getPowerUpData()) {
                            case EXTRA_LIFE:
                                game.lives++;
                                break;
                            case DOUBLE_SCORE:
                                game.dodgeBird.scoreMultiplier = 2;
                                game.dodgeBird.scoreMultiplierTimer = new Timer(powerUp.getPowerUpData().getDuration());
                                break;
                            case TRIPLE_SCORE:
                                game.dodgeBird.scoreMultiplier = 3;
                                game.dodgeBird.scoreMultiplierTimer = new Timer(powerUp.getPowerUpData().getDuration());
                                break;
                            case INVINCIBILITY:
                                game.hipsterBird.invincible = true;
                                game.dodgeBird.invincibilityTimer = new Timer(powerUp.getPowerUpData().getDuration());
                                break;
                        }

                        game.dodgeBird.getActivePowerUps().remove(i);
                        i--; //Avoid index out of bounds exception
                    }
                }

                if ((game.dodgeBird.scoreMultiplierTimer == null || !game.dodgeBird.scoreMultiplierTimer.isRunning())
                        && game.dodgeBird.scoreMultiplier != 1) {
                    game.dodgeBird.scoreMultiplier = 1;
                } else if ((game.dodgeBird.invincibilityTimer == null || !game.dodgeBird.invincibilityTimer.isRunning())
                        && game.hipsterBird.invincible == true) {
                    game.hipsterBird.invincible = false;
                    game.hipsterBird.flicker = false;

                    lastTimeLeft = 0;
                }

                if (game.dodgeBird.invincibilityTimer != null
                        && game.dodgeBird.invincibilityTimer.isRunning()) {
                    long timeLeft = game.dodgeBird.invincibilityTimer.timeLeft();

                    if (timeLeft < 1000 && (lastTimeLeft - timeLeft) > 200) {
                        game.hipsterBird.flicker = true;

                        lastTimeLeft = timeLeft;
                    } else if (timeLeft < 1000 && (lastTimeLeft - timeLeft) > 100)  {
                        game.hipsterBird.flicker = false;
                    }

                    if (lastTimeLeft == 0) lastTimeLeft = timeLeft;
                }

                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
