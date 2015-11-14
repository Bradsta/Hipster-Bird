package com.gamicarts.hipsterbird.game;

import com.gamicarts.hipsterbird.game.task.Game;
import com.gamicarts.hipsterbird.game.task.GameTask;
import com.gamicarts.hipsterbird.game.task.LoadingScreen;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 3/11/14
 * Time: 2:45 PM
 *
 * Handles the flow of the game.
 */
public class GameHandler extends Thread {

    private GameTask gameTask;

    private boolean active;
    private boolean paused;

    @Override
    public void run() {
        gameTask = new LoadingScreen();
        gameTask.run();

        gameTask = new Game();
        gameTask.run();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public GameTask getGameTask() {
        return this.gameTask;
    }

}
