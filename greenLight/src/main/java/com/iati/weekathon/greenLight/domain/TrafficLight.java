package com.iati.weekathon.greenLight.domain;

public class TrafficLight {

    private String npsIp;
    private int x;
    private int y;
    private int redId;
    private int yellowId;
    private int greenId;
    private long id;

    public TrafficLight() {
    }

    public TrafficLight(long id, String npsIp, int x, int y, int redId, int yellowId, int greenId) {
        this.npsIp = npsIp;
        this.x = x;
        this.y = y;
        this.redId = redId;
        this.yellowId = yellowId;
        this.greenId = greenId;
        this.id = id;
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
}
