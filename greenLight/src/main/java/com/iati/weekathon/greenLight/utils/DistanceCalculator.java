package com.iati.weekathon.greenLight.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 23/03/15
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */
public class DistanceCalculator {

    public static double getEuclideanDistance(int x1, int y1, int x2, int y2){
        int xDelta = x1-x2;
        int yDelta = y1-y2;
        double euclideanDistance = Math.sqrt(xDelta*xDelta + yDelta*yDelta);
        return euclideanDistance;
    }
}
