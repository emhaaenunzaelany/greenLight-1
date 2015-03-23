package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLight;
import com.iati.weekathon.greenLight.domain.TrafficLightState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service
public class TrafficLightsMgr {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightsMgr.class);

    @Resource
    private Map<Long, TrafficLight> trafficLights;

    private Map<Long, TrafficLightState> trafficLightIdToState;

    @Autowired
    private TelnetService telnetService;

    TrafficLightsRunner trafficLightsRunner;



    @PostConstruct
    private void initTrafficLights(){

        if(trafficLights == null){
           log.error("No Traffic lights are configured");
            return;
        }

        trafficLightIdToState = new HashMap<>();

        for (Map.Entry<Long, TrafficLight> entry: trafficLights.entrySet()) {
            long trafficLightId =entry.getKey();
            if(trafficLightId ==1){
                TrafficLight trafficLight = entry.getValue();
                TrafficLightState state = new TrafficLightState(telnetService, trafficLight,10,1,7,2);
                trafficLightIdToState.put(trafficLightId, state);
            } else if (trafficLightId ==2){
                TrafficLight trafficLight = entry.getValue();
                TrafficLightState state = new TrafficLightState(telnetService, trafficLight,6,1,4,2);
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


    public void setLightToGreen(long trafficLightId) {

       trafficLightsRunner.addLightToSetAsGreen(trafficLightId);
    }







}
