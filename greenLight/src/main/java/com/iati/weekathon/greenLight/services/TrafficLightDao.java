package com.iati.weekathon.greenLight.services;

import com.iati.weekathon.greenLight.domain.TrafficLight;
import com.iati.weekathon.greenLight.domain.TrafficLightColorEnum;
import com.iati.weekathon.greenLight.utils.TrafficLightConnector;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TrafficLightDao {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightDao.class);
    private static final String ON = "/on ";
    private static final String OFF = "/off ";

    @Value("${nps1.ip}")
    private String trafficLightIp1;

    @Value("${nps1.port}")
    private int trafficLightPort1;

    @Value("${nps2.ip}")
    private String trafficLightIp2;

    @Value("${nps2.port}")
    private int trafficLightPort2;


    private Map<String, TrafficLightConnector> trafficLightIpToConnector;


    @PostConstruct
    private void init() throws IOException, InvalidTelnetOptionException {

        trafficLightIpToConnector = new HashMap<>();

        TrafficLightConnector trafficLightConnector1 = new TrafficLightConnector(trafficLightIp1, trafficLightPort1);
        TrafficLightConnector trafficLightConnector2 = new TrafficLightConnector(trafficLightIp2, trafficLightPort2);

        trafficLightIpToConnector.put(trafficLightIp1, trafficLightConnector1);
        trafficLightIpToConnector.put(trafficLightIp2, trafficLightConnector2);

        for (TrafficLightConnector trafficLightConnector : trafficLightIpToConnector.values()) {
            trafficLightConnector.connect();
        }
    }


    @PreDestroy
    private void disconnect() {

        for (TrafficLightConnector trafficLightConnector : trafficLightIpToConnector.values()) {
            trafficLightConnector.disconnect();
        }
    }

    private int getLightIdByGivenColor(TrafficLight trafficLight, TrafficLightColorEnum color){

        if (TrafficLightColorEnum.RED.equals(color)) {

            return trafficLight.getRedId();

        } else if (TrafficLightColorEnum.YELLOW.equals(color)) {

            return trafficLight.getYellowId();

        } else if (TrafficLightColorEnum.GREEN.equals(color)) {

            return trafficLight.getGreenId();

        }
        throw new IllegalArgumentException("Unknown Traffic-Light color");
    }


    public void sendOnCommand(TrafficLight trafficLight, TrafficLightColorEnum color) {

        int lightId = getLightIdByGivenColor(trafficLight, color);
        String command = ON + lightId;
        sendCommand(trafficLight, command);

    }

    public void sendOffCommand(TrafficLight trafficLight, TrafficLightColorEnum color) {

        int lightId = getLightIdByGivenColor(trafficLight, color);
        String command = OFF + lightId;
        sendCommand(trafficLight, command);

    }

    private synchronized void sendCommand(TrafficLight trafficLight, String command) {

        log.info("Sending command " + command+" to traffic light id "+trafficLight.getId());
        TrafficLightConnector connector = trafficLightIpToConnector.get(trafficLight.getNpsIp());
        connector.getOutputStream().println(command);
        connector.getOutputStream().flush();
        String commandResult = connector.readUntil(TrafficLightConnector.START_OF_TELNET_COMMAND);
        log.info("Command Result: \n" + commandResult);
    }


}
