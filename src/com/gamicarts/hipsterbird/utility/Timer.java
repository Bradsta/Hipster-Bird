package com.gamicarts.hipsterbird.utility;

public class Timer {

	private final long duration;

    private long startTime;
	
	public Timer(long duration) {
		this.duration = duration;
        this.startTime = System.currentTimeMillis();
	}
	
	public boolean isRunning() {
        return (System.currentTimeMillis() - startTime) < duration;
    }

    public long timeLeft() {
        return (duration - (System.currentTimeMillis() - startTime));
    }

    @Override
    public String toString() {
        String time = "";

        long timeLeft = System.currentTimeMillis() - startTime;
        int mins = (int) (timeLeft / 60000);

        time += mins + ":";

        int secs = ((int) (timeLeft - mins)) / 1000;

        time += secs;

        return time;
    }

}
