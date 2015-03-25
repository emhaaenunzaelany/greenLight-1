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
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;


@Service
public class TrafficLightsMgr {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightsMgr.class);
    public boolean lightMatrix[][][] = {
            //light 1(0,0)
            {
                    {true, true, false, false},
                    {true, false, false, false},
                    {true, false, false, false},
                    {true, false, false, false},
                    {true, false, false, false}
            },

            //light 2 (3,3)
            {
                    {false, false, false, true},
                    {false, false, false, true},
                    {false, false, false, true},
                    {false, false, false, true},
                    {false, false, true, true}
            },

            //light 3 (0,4)
            {
                    {true, false, false, false},
                    {true, false, false, false},
                    {true, false, false, false},
                    {true, false, false, false},
                    {true, true, false, false}
            },



            //light 4 (3,0)
            {
                    {false, false, true, true},
                    {false, false, false, true},
                    {false, false, false, true},
                    {false, false, false, true},
                    {false, false, false, true}
            }


    };


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

            TrafficLight trafficLight = entry.getValue();

            TrafficLightState state = new TrafficLightState(trafficLightDao, trafficLight);

            trafficLightIdToState.put(trafficLightId, state);
        }

        trafficLightsRunner = new TrafficLightsRunner(trafficLightIdToState);
        Thread trafficLightsRunnerThread = new Thread(trafficLightsRunner);
        trafficLightsRunnerThread.start();

    }

    @PreDestroy
    private void stopThreads(){
        for(TrafficLightState trafficLightState: trafficLightIdToState.values()){
            trafficLightState.stopTrafficLightScenario(true);
        }
    }

    public void setTrafficLights(Map<Long, TrafficLight> trafficLights) {
        this.trafficLights = trafficLights;
    }


    public void setTrafficLightToGreenAccordingToVehicleLocationByEuclideanDistance(int x, int y) {

        List<Long> trafficLightsInRange = new LinkedList<>();

        for (TrafficLight trafficLight : trafficLights.values()) {

            double distanceFromVehicle = DistanceCalculator.getEuclideanDistance(x, y, trafficLight.getX(), trafficLight.getY());
            if (distanceFromVehicle <= maxDistanceToTurnTrafficLightToGreen) {
                trafficLightsInRange.add(trafficLight.getId());
            }
        }

        if (!trafficLightsInRange.isEmpty()) {

            log.info("Adding the following Traffic-Lights to be set as Green: " + trafficLightsInRange.toString());
            trafficLightsRunner.addLightsToSetAsGreen(trafficLightsInRange);
        }

    }

    public void setTrafficLightToGreenAccordingToVehicleLocation(int x, int y) {

        //setTrafficLightToGreenAccordingToVehicleLocationByEuclideanDistance(x, y);
        setTrafficLightAsGreenAccordingToMatrix(x, y);

    }


    private void setTrafficLightAsGreenAccordingToMatrix(int x, int y) {

        List<Long> trafficLightsInRange = new LinkedList<>();

        for (TrafficLight trafficLight : trafficLights.values()) {
            if(lightMatrix[(int)trafficLight.getId()-1][y][x])  //traffic lights ids start from zero, so subtract 1
                trafficLightsInRange.add(trafficLight.getId());
        }

        if (!trafficLightsInRange.isEmpty()) {

            log.info("Adding the following Traffic-Lights to be set as Green: " + trafficLightsInRange.toString());
            trafficLightsRunner.addLightsToSetAsGreen(trafficLightsInRange);
        }
    }


    public void setTrafficLightToGreen(Long trafficLightId) {
        List<Long> ids = new LinkedList<>();
        ids.add(trafficLightId);
        trafficLightsRunner.addLightsToSetAsGreen(ids);
    }


}
