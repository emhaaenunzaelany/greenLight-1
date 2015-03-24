package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLight;
import com.iati.weekathon.greenLight.domain.TrafficLightState;
import com.iati.weekathon.greenLight.utils.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
public class TrafficLightsMgr {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightsMgr.class);


    @Resource
    private Map<Long, TrafficLight> trafficLights;

    private Map<Long, TrafficLightState> trafficLightIdToState;

    @Autowired
    private TrafficLightDao trafficLightDao;

    @Value("${max.distance}")
    private double maxDistanceToTurnTrafficLightToGreen;

    TrafficLightsRunner trafficLightsRunner;


    public Map<Long, TrafficLight> getTrafficLights() {
        return trafficLights;
    }


    @PostConstruct
    private void initTrafficLights() {

        if (trafficLights == null) {
            log.error("No Traffic lights are configured");
            return;
        }

        trafficLightIdToState = new HashMap<>();

        for (Map.Entry<Long, TrafficLight> entry : trafficLights.entrySet()) {
            long trafficLightId = entry.getKey();
            if (trafficLightId == 1) {
                TrafficLight trafficLight = entry.getValue();
                TrafficLightState state = new TrafficLightState(trafficLightDao, trafficLight, 10, 1, 7, 2);
                trafficLightIdToState.put(trafficLightId, state);
            } else if (trafficLightId == 2) {
                TrafficLight trafficLight = entry.getValue();
                TrafficLightState state = new TrafficLightState(trafficLightDao, trafficLight, 6, 1, 4, 2);
                trafficLightIdToState.put(trafficLightId, state);
            }

        }

        trafficLightsRunner = new TrafficLightsRunner(trafficLightIdToState);
        Thread trafficLightsRunnerThread = new Thread(trafficLightsRunner);
        trafficLightsRunnerThread.start();

    }

    public void setTrafficLights(Map<Long, TrafficLight> trafficLights) {
        this.trafficLights = trafficLights;
    }


    public void setTrafficLightToGreenAccordingToVehicleLocation(int x, int y) {

        Set<Long> trafficLightsInRange = new HashSet<>();

        for (TrafficLight trafficLight : trafficLights.values()) {

            double distanceFromVehicle = DistanceCalculator.getEuclideanDistance(x, y, trafficLight.getX(), trafficLight.getY());
            if (distanceFromVehicle <= maxDistanceToTurnTrafficLightToGreen) {
                trafficLightsInRange.add(trafficLight.getId());
            }
        }

        if (!trafficLightsInRange.isEmpty()) {

            log.info("Adding the following Traffic-Lights to be set as Green: "+trafficLightsInRange.toString());
            trafficLightsRunner.addLightsToSetAsGreen(trafficLightsInRange);
        }

    }


    public void setTrafficLightToGreen(Long trafficLightId) {
        Set<Long> ids = new HashSet<>();
        ids.add(trafficLightId);
        trafficLightsRunner.addLightsToSetAsGreen(ids);
    }


}
