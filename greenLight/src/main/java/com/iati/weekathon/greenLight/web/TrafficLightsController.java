package com.iati.weekathon.greenLight.web;

import com.iati.weekathon.greenLight.services.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/trafficLights")
@Controller
public class TrafficLightsController {


    private final static Logger log = LoggerFactory.getLogger(TrafficLightsController.class);

    @Autowired
    private TelnetService telnetService;


    @RequestMapping(value = "/on", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> setLightOn(@RequestParam(value = "lightId", required = false, defaultValue = "1") int lightId) {

        Map<String, Object> model = new HashMap<String, Object>();
        telnetService.sendOnCommand(lightId);
        model.put("Message", "Sent on command to Traffic-Light #"+lightId);
        return model;

    }

    @RequestMapping(value = "/off", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> setLightOff(@RequestParam(value = "lightId", required = false, defaultValue = "1") int lightId) {

        Map<String, Object> model = new HashMap<String, Object>();
        telnetService.sendOffCommand(lightId);
        model.put("Message", "Sent off command to Traffic-Light #"+lightId);
        return model;

    }



    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
