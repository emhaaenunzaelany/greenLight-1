package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class TrafficLightsMgr {

    @Resource
    private Map<Long, TrafficLight> trafficLights;

    @Autowired
    private TelnetService telnetService;


    public void setTrafficLights(Map<Long, TrafficLight> trafficLights) {
        this.trafficLights = trafficLights;
    }


    public void setLightToRed(long trafficLightId) {

        TrafficLight trafficLight = trafficLights.get(trafficLightId);
        telnetService.sendOffCommand(trafficLight.getGreenId());
        telnetService.sendOffCommand(trafficLight.getYellowId());
        telnetService.sendOnCommand(trafficLight.getRedId());
    }


}
