package com.iati.weekathon.greenLight.domain;

import com.iati.weekathon.greenLight.services.TrafficLightDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;


public class TrafficLightState {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightState.class);

    public static enum LightState {STATE_RED, STATE_RED_YELLOW, STATE_GREEN, STATE_YELLOW}

    ;

    private TrafficLight trafficLight;
    private TrafficLightDao trafficLightDao;

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

    public TrafficLightState(TrafficLightDao trafficLightDao, TrafficLight trafficLight, int rtime, int redyellowtime, int greentime, int yellowtime, LightState lightstate) {
        this.trafficLightDao = trafficLightDao;
        this.trafficLight = trafficLight;
        mRedTime = rtime;
        mRedYellowTime = redyellowtime;
        mGreenTime = greentime;
        mYellowTime = yellowtime;

        mLightState = lightstate;

    }

    public TrafficLightState(TrafficLightDao trafficLightDao, TrafficLight trafficLight, int rtime, int redyellowtime, int greentime, int yellowtime) {
        this(trafficLightDao, trafficLight, rtime, redyellowtime, greentime, yellowtime, LightState.STATE_RED);
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
        log.info("Changed Traffic Light " + trafficLight.getId() + "' state to " + mLightState);
        return mLightState;
    }

    public void startTrafficLightScenario() {
        mTimer = new Timer();
        mTimer.schedule(new trafficLightTimerExpired(), getStateTimeInSeconds() * 1000);
    }

    public void setState(LightState newState) {
        if (mTimer != null) {
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
                trafficLightDao.sendOffCommand(trafficLight, color);
            }
        }

        if (on != null) {

            for (TrafficLightColorEnum color : on) {
               trafficLightDao.sendOnCommand(trafficLight, color);
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
