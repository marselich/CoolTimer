package ru.kalievmars.cooltimer;

import android.annotation.SuppressLint;

public class Time {
    private int minutes;
    private int seconds;

    public Time(int minutes, int seconds) {
        int temp = 0;
        temp = seconds / 60;

        this.minutes = minutes + temp;
        this.seconds = seconds % 60;
    }

    public Time(int seconds) {
        this.minutes = seconds / 60;
        this.seconds = seconds % 60;
    }

    public void setTime(long milliseconds) {
        int temp = (int)milliseconds / 1000;
        this.minutes = temp / 60;
        this.seconds = temp % 60;
    }

    public void setTime(int seconds) {
        this.minutes = seconds / 60;
        this.seconds = seconds % 60;
    }

    public int getSeconds() {
        return minutes * 60 + seconds;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%02d:%02d", minutes, seconds);
    }
}
