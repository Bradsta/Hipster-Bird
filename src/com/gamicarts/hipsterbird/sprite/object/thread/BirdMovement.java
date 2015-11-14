package com.gamicarts.hipsterbird.sprite.object.thread;

import com.gamicarts.hipsterbird.HipsterBird;
import com.gamicarts.hipsterbird.game.DodgeBird;
import com.gamicarts.hipsterbird.game.state.GameState;
import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.sprite.data.BirdData;
import com.gamicarts.hipsterbird.sprite.object.Bird;
import com.gamicarts.hipsterbird.utility.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 6/18/14
 * Time: 8:52 PM
 */
public class BirdMovement extends Thread {

    private final Game game;
    private final Bird hipsterBird;

    public boolean stop = false;

    public BirdMovement(DodgeBird dodgeBird) {
        this.game = dodgeBird.game;
        this.hipsterBird = game.hipsterBird;
    }

    @Override
    public void run() {
        try {
            int minSpeed = (int) Utility.getXRatio(150);
            int maxSpeed = (int) Utility.getXRatio(300);
            int superSpeed = (int) Utility.getXRatio(450);
            int crazyHeight = (int) Utility.getYRatio(20);
            float crazyWidth = Utility.getXRatio(0.015F);

            while (game.dodgeBird != null
                    && game.dodgeBird.active
                    && !stop) {
                if (HipsterBird.GAME_HANDLER.isPaused()
                        || game.currentState == GameState.PAUSE) {
                    Thread.sleep(33);
                    continue;
                }

                for (int i=0; game.dodgeBird.active && i<game.dodgeBird.getActiveBirds().size(); i++) {
                    Bird nextBird = this.game.dodgeBird.getActiveBirds().get(i);

                    if (nextBird.xps == 0) {
                        nextBird.xps = Utility.random(minSpeed, (nextBird.getBirdData() != BirdData.POLICE_BIRD && game.dodgeBird.currentScore > 200000) ? superSpeed : maxSpeed);
                        //X per second, birds can get a lot faster after 200k score. Police birds do not get this boost.
                        nextBird.xpf = nextBird.xps / 30.0D; //X per frame
                    }

                    if (i < game.dodgeBird.getActiveBirds().size()) { //Avoid concurrent modification
                        if (nextBird.getBirdData() == BirdData.MUSTACHE_BIRD
                                || (nextBird.getBirdData() == BirdData.CLOUD_BIRD && !nextBird.isCloud)) {
                            nextBird.setBirdLocation((int) (nextBird.getBirdLocation().x - nextBird.xpf),
                                    nextBird.getBirdLocation().y);
                        } else if (nextBird.getBirdData() == BirdData.CRAZY_BIRD) {
                            nextBird.setBirdLocation((int) (nextBird.getBirdLocation().x - nextBird.xpf),
                                    (int) (crazyHeight * Math.sin(crazyWidth * (nextBird.getBirdLocation().x - nextBird.xpf)) + nextBird.getBirdLocation().y));
                        } else if (nextBird.getBirdData() == BirdData.POLICE_BIRD) {
                            if (nextBird.getBirdLocation().x > hipsterBird.getBirdLocation().x) {
                                nextBird.birdPath = Utility.getStraightPath(nextBird.getBirdLocation(), hipsterBird.getBirdLocation());
                                nextBird.lastLocation = 0;
                            } else {
                                nextBird.birdPath.clear();
                            }

                            if (!nextBird.birdPath.isEmpty()) {
                                nextBird.lastLocation += nextBird.xpf;

                                if (nextBird.lastLocation >= nextBird.birdPath.size()) {
                                    nextBird.setBirdLocation(nextBird.birdPath.get(nextBird.birdPath.size()-1).x, nextBird.birdPath.get(nextBird.birdPath.size()-1).y);

                                    nextBird.birdPath.clear();
                                } else {
                                    nextBird.setBirdLocation(nextBird.birdPath.get(nextBird.lastLocation).x, nextBird.birdPath.get(nextBird.lastLocation).y);
                                }
                            } else {
                                //Now police bird will just bail
                                nextBird.setBirdLocation((int) (nextBird.getBirdLocation().x - nextBird.xpf), nextBird.getBirdLocation().y);
                            }
                        }
                    }
                }

                Thread.sleep(33); //30FPS
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

}
