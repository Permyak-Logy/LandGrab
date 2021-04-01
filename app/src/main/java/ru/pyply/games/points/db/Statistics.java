package ru.pyply.games.points.db;

import java.io.Serializable;

public class Statistics implements Serializable {
    private final long id;
    private final int result;
    private final int countPlayers;
    private final int mPoints;
    private final double mSquare;
    private final int travels;
    private final double averageTravelTime;
    private final double totalGameTime;
    private final int capturedPoints;
    private final int lostPoints;


    public Statistics(long id, int result, int countPlayers, int mPoints, double mSquare, int travels, double averageTravelTime, double totalGameTime, int capturedPoints, int lostPoints) {
        this.id = id;
        this.result = result;
        this.countPlayers = countPlayers;
        this.mPoints = mPoints;
        this.mSquare = mSquare;
        this.travels = travels;
        this.averageTravelTime = averageTravelTime;
        this.totalGameTime = totalGameTime;
        this.capturedPoints = capturedPoints;
        this.lostPoints = lostPoints;
    }

    public long getId() {
        return id;
    }

    public int getResult() {
        return result;
    }

    public int getCountPlayers() {
        return countPlayers;
    }

    public int getMPoints() {
        return mPoints;
    }

    public double getMSquare() {
        return mSquare;
    }

    public int getTravels() {
        return travels;
    }

    public double getAverageTravelTime() {
        return averageTravelTime;
    }

    public double getTotalGameTime() {
        return totalGameTime;
    }

    public int getCapturedPoints() {
        return capturedPoints;
    }

    public int getLostPoints() {
        return lostPoints;
    }
}
