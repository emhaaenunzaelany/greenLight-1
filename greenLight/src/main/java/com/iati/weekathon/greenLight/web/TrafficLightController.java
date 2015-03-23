package com.iati.weekathon.greenLight.web;

import com.iati.weekathon.greenLight.services.TelnetService;
import com.iati.weekathon.greenLight.services.TrafficLightsMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequestMapping(value = "/trafficLights")
@Controller
public class TrafficLightController {


    private final static Logger log = LoggerFactory.getLogger(TrafficLightController.class);

    @Autowired
    private TelnetService telnetService;

    @Autowired
    private TrafficLightsMgr trafficLightsMgr;


    @RequestMapping(value = "/on", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> setLightOn(@RequestParam(value = "lightId", required = false, defaultValue = "1") int lightId) {

        Map<String, Object> model = new HashMap<String, Object>();
        telnetService.sendOnCommand(lightId);
        model.put("Message", "Sent on command to Traffic-Light #" + lightId);
        return model;

    }

    @RequestMapping(value = "/off", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> setLightOff(@RequestParam(value = "lightId", required = false, defaultValue = "1") int lightId) {

        Map<String, Object> model = new HashMap<String, Object>();
        telnetService.sendOffCommand(lightId);
        model.put("Message", "Sent off command to Traffic-Light #" + lightId);
        return model;

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> setTrafficLightColor(@PathVariable(value = "id") long id) {

        Map<String, Object> model = new HashMap<String, Object>();
        trafficLightsMgr.setLightToRed(id);
        model.put("Message", "Changed Traffic Light '" + id + "' to RED");
        return model;

    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
