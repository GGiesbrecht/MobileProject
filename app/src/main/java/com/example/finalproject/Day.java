package com.example.finalproject;

import androidx.annotation.NonNull;

public class Day {
    private int hours;
    private int minutes;

    public Day() {
    }

    public Day(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "Hours: " + this.hours + ", Minutes: " + this.minutes;
    }
}
