package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLightState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 23/03/15
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class TrafficLightsRunner implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightsRunner.class);

    private Map<Long, TrafficLightState> trafficLightsIdToState;
    private volatile Set<Long> trafficLightsToSetAsGreen;

    public TrafficLightsRunner(Map<Long, TrafficLightState> trafficLightsIdToState) {
        this.trafficLightsIdToState = trafficLightsIdToState;
        this.trafficLightsToSetAsGreen = new HashSet<>();
    }

    public void addLightToSetAsGreen(Long trafficLightId){
        trafficLightsToSetAsGreen.add(trafficLightId);
    }

    @Override
    public void run() {

        for (TrafficLightState trafficLightState : trafficLightsIdToState.values()) {
            trafficLightState.startTrafficLightScenario();

        }

        while (true) {

            if (trafficLightsToSetAsGreen.isEmpty()) {
                try {
                    Thread.sleep(5l);
                } catch (InterruptedException e) {
                    log.error("Traffic Lights Runner caught an interruption", e);
                }

            } else {
                for (Long trafficLightId : trafficLightsToSetAsGreen) {

                    TrafficLightState trafficLightState = trafficLightsIdToState.get(trafficLightId);
                    trafficLightState.setStateGreen();
                }
                trafficLightsToSetAsGreen.clear();

            }

        }

    }
}
