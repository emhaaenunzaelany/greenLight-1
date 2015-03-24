package com.iati.weekathon.greenLight.domain;

public class TrafficLight {

    private long id;
    private String npsIp;
    private int x;
    private int y;
    private int redId;
    private int yellowId;
    private int greenId;
    private int redTimeInSeconds;
    private int redYellowTimeInSeconds;
    private int greenTimeInSeconds;
    private int yellowTimeInSeconds;

    public TrafficLight() {
    }

    public TrafficLight(long id, String npsIp, int x, int y, int redId, int yellowId, int greenId,
                        int redTimeInSeconds, int redYellowTimeInSeconds, int greenTimeInSeconds, int yellowTimeInSeconds) {
        this.id = id;
        this.npsIp = npsIp;
        this.x = x;
        this.y = y;
        this.redId = redId;
        this.yellowId = yellowId;
        this.greenId = greenId;
        this.redTimeInSeconds = redTimeInSeconds;
        this.redYellowTimeInSeconds = redYellowTimeInSeconds;
        this.greenTimeInSeconds = greenTimeInSeconds;
        this.yellowTimeInSeconds = yellowTimeInSeconds;
    }

    public String getNpsIp() {
        return npsIp;
    }

    public void setNpsIp(String npsIp) {
        this.npsIp = npsIp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRedId() {
        return redId;
    }

    public void setRedId(int redId) {
        this.redId = redId;
    }

    public int getYellowId() {
        return yellowId;
    }

    public void setYellowId(int yellowId) {
        this.yellowId = yellowId;
    }

    public int getGreenId() {
        return greenId;
    }

    public void setGreenId(int greenId) {
        this.greenId = greenId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRedTimeInSeconds() {
        return redTimeInSeconds;
    }

    public void setRedTimeInSeconds(int redTimeInSeconds) {
        this.redTimeInSeconds = redTimeInSeconds;
    }

    public int getRedYellowTimeInSeconds() {
        return redYellowTimeInSeconds;
    }

    public void setRedYellowTimeInSeconds(int redYellowTimeInSeconds) {
        this.redYellowTimeInSeconds = redYellowTimeInSeconds;
    }

    public int getGreenTimeInSeconds() {
        return greenTimeInSeconds;
    }

    public void setGreenTimeInSeconds(int greenTimeInSeconds) {
        this.greenTimeInSeconds = greenTimeInSeconds;
    }

    public int getYellowTimeInSeconds() {
        return yellowTimeInSeconds;
    }

    public void setYellowTimeInSeconds(int yellowTimeInSeconds) {
        this.yellowTimeInSeconds = yellowTimeInSeconds;
    }
}
