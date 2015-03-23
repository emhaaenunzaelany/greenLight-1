package com.iati.weekathon.greenLight.domain;

import com.iati.weekathon.greenLight.services.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;


public class TrafficLightState  {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightState.class);

    public static enum LightState {STATE_RED, STATE_RED_YELLOW, STATE_GREEN, STATE_YELLOW};

    private TrafficLight trafficLight;
    private TelnetService telnetService;

    private TrafficLightColorEnum[][] mOn = {{TrafficLightColorEnum.RED},
            {TrafficLightColorEnum.YELLOW},
            {TrafficLightColorEnum.GREEN},
            {TrafficLightColorEnum.YELLOW}};

    private TrafficLightColorEnum[][] mOff = {{TrafficLightColorEnum.YELLOW},
            {},
            {TrafficLightColorEnum.RED, TrafficLightColorEnum.YELLOW},
            {TrafficLightColorEnum.GREEN}};

    private int mRedTime;
    private int mRedYellowTime;
    private int mGreenTime;
    private int mYellowTime;

    public LightState mLightState;
    private Timer mTimer;

    public TrafficLightState(TelnetService telnetService, TrafficLight trafficLight, int rtime, int redyellowtime, int greentime, int yellowtime, LightState lightstate) {
        this.telnetService = telnetService;
        this.trafficLight = trafficLight;
        mRedTime = rtime;
        mRedYellowTime = redyellowtime;
        mGreenTime = greentime;
        mYellowTime = yellowtime;

        mLightState = lightstate;

    }

    public TrafficLightState(TelnetService telnetService, TrafficLight trafficLight, int rtime, int redyellowtime, int greentime, int yellowtime) {
        this(telnetService, trafficLight, rtime, redyellowtime, greentime, yellowtime, LightState.STATE_RED);
    }

    private long getStateTimeInSeconds() {
        switch (mLightState) {
            case STATE_RED:
                return mRedTime;
            case STATE_RED_YELLOW:
                return mRedYellowTime;
            case STATE_GREEN:
                return mGreenTime;
            case STATE_YELLOW:
                return mYellowTime;
        }
        return 0;
    }

    private LightState nextState() {
        if (mLightState == LightState.STATE_YELLOW)
            mLightState = LightState.STATE_RED;
        else {
            int num = mLightState.ordinal();
            num++;
            mLightState = LightState.values()[num];
        }

        updateTrafficLight(mOn[mLightState.ordinal()], mOff[mLightState.ordinal()]);
        log.info("Changed Traffic Light "+trafficLight.getId()+"' state to " + mLightState);
        return mLightState;
    }

    public void startTrafficLightScenario() {
        mTimer = new Timer();
        mTimer.schedule(new trafficLightTimerExpired(), getStateTimeInSeconds() * 1000);
    }

    public void setState(LightState newState) {
        if (mTimer != null)         {
            mTimer.cancel();
            mTimer.purge();
        }
        mLightState = newState;

        updateTrafficLight(mOn[mLightState.ordinal()], mOff[mLightState.ordinal()]);

        startTrafficLightScenario();
    }

    public void setStateGreen() {
        setState(LightState.STATE_GREEN);
    }

    public void setStateRed() {
        setState(LightState.STATE_RED);
    }

    public void updateTrafficLight(TrafficLightColorEnum[] on, TrafficLightColorEnum[] off) {



        if (off != null) {

            for (TrafficLightColorEnum color : off) {
                if (TrafficLightColorEnum.RED.equals(color)) {
                    telnetService.sendOffCommand(trafficLight.getRedId());
                } else if (TrafficLightColorEnum.YELLOW.equals(color)) {
                    telnetService.sendOffCommand(trafficLight.getYellowId());
                } else if (TrafficLightColorEnum.GREEN.equals(color)) {
                    telnetService.sendOffCommand(trafficLight.getGreenId());
                }
            }
        }

        if (on != null) {

            for (TrafficLightColorEnum color : on) {
                if (TrafficLightColorEnum.RED.equals(color)) {
                    telnetService.sendOnCommand(trafficLight.getRedId());
                } else if (TrafficLightColorEnum.YELLOW.equals(color)) {
                    telnetService.sendOnCommand(trafficLight.getYellowId());
                } else if (TrafficLightColorEnum.GREEN.equals(color)) {
                    telnetService.sendOnCommand(trafficLight.getGreenId());
                }
            }
        }

    }


    class trafficLightTimerExpired extends TimerTask {
        public void run() {
            nextState();
            startTrafficLightScenario();

        }
    }

}
