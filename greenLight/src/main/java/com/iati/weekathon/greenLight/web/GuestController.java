package com.iati.weekathon.greenLight.web;

import com.iati.weekathon.greenLight.domain.Guest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liron on 27/04/2014.
 */
@RequestMapping(value = "/guests")
@Controller
public class GuestController {


    private final static Logger log = LoggerFactory.getLogger(GuestController.class);

    private static final Object lock = new Object();


    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> showGuests(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "100") int size) {

        Map<String, Object> model = new HashMap<String, Object>();
        List<Guest> guests = new ArrayList<>();
        model.put("guests", guests);

        long guestsCount = 0;
        float numOfPages = (float) guestsCount / size;
        model.put("maxPages", (int) ((numOfPages > (int) numOfPages || numOfPages == 0.0) ? numOfPages + 1 : numOfPages));

        return model;

    }



    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showTestJson(Model uiModel) {

        return "test";
    }
}
