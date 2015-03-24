package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLightState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private volatile Set<Long> trafficLightsToSetAsGreen; //todo: make it synchronized

    public TrafficLightsRunner(Map<Long, TrafficLightState> trafficLightsIdToState) {
        this.trafficLightsIdToState = trafficLightsIdToState;
        this.trafficLightsToSetAsGreen = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());
    }


    public void addLightsToSetAsGreen(Set<Long> trafficLightIds){
        trafficLightsToSetAsGreen.addAll(trafficLightIds);
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

                log.info("Received the following Traffic Light ids to set as Green: "+trafficLightsToSetAsGreen);
                for (Long trafficLightId : trafficLightsToSetAsGreen) {

                    TrafficLightState trafficLightState = trafficLightsIdToState.get(trafficLightId);
                    trafficLightState.setStateGreen();
                }

                trafficLightsToSetAsGreen.clear();

            }

        }

    }
}
