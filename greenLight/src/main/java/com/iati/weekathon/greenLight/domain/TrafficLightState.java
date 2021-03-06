package com.iati.weekathon.greenLight.domain;

import com.iati.weekathon.greenLight.services.TrafficLightDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;


public class TrafficLightState {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightState.class);
    public static final String AUDIO_FILE = "/tada.wav";
    private InputStream audioFileInputStream = null;
    private AudioStream audioStream;

    public static enum LightState {STATE_RED, STATE_RED_YELLOW, STATE_GREEN, STATE_YELLOW} ;

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

    public TrafficLightState(TrafficLightDao trafficLightDao, TrafficLight trafficLight, LightState lightstate) {
        this.trafficLightDao = trafficLightDao;
        this.trafficLight = trafficLight;
        mRedTime = trafficLight.getRedTimeInSeconds();
        mRedYellowTime = trafficLight.getRedYellowTimeInSeconds();
        mGreenTime = trafficLight.getGreenTimeInSeconds();
        mYellowTime = trafficLight.getYellowTimeInSeconds();

        mLightState = lightstate;

    }

    public TrafficLightState(TrafficLightDao trafficLightDao, TrafficLight trafficLight) {
        this(trafficLightDao, trafficLight, LightState.STATE_RED);
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
        //log.info("Changed Traffic Light " + trafficLight.getId() + "' state to " + mLightState);
        return mLightState;
    }


    public void stopTrafficLightScenario(boolean printLog) {

        if (mTimer != null) {

            if (printLog) {
                log.info("Stopping " + mTimer.toString());
            }

            mTimer.cancel();
            mTimer.purge();
        }
    }

    private void stopTrafficLightScenario() {
        stopTrafficLightScenario(false);
    }


    public void startTrafficLightScenario() {

        stopTrafficLightScenario();
        mTimer = new Timer();
        mTimer.schedule(new trafficLightTimerExpired(), getStateTimeInSeconds() * 1000);
    }

    public boolean setState(LightState newState) {

        boolean isUpdated = false;

        stopTrafficLightScenario();

        if (!newState.equals(mLightState)) {

            mLightState = newState;
            updateTrafficLight(mOn[mLightState.ordinal()], mOff[mLightState.ordinal()]);
            isUpdated = true;
        }

        startTrafficLightScenario();
        return isUpdated;
    }

    public void setStateGreen() {
        boolean isUpdated = setState(LightState.STATE_GREEN);
        if (isUpdated) {
            try {

                log.info("playing audio for traffic light id "+this.trafficLight.getId());
                audioFileInputStream = TrafficLightState.class.getResourceAsStream(AUDIO_FILE);
                audioStream = new AudioStream(audioFileInputStream);
                AudioPlayer.player.start(audioStream);
            } catch (Exception e) {
                log.error("Failed to play audio");
            }
        }
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
