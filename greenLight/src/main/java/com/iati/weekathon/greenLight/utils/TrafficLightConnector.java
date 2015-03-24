package com.iati.weekathon.greenLight.utils;

import org.apache.commons.net.telnet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 24/03/15
 * Time: 08:18
 * To change this template use File | Settings | File Templates.
 */
public class TrafficLightConnector {

    private final static Logger log = LoggerFactory.getLogger(TrafficLightConnector.class);

    private String ip;

    private int port;

    private PrintStream outputStream;

    private InputStream inputStream;

    private TelnetClient telnetClient;

    public static final String START_OF_TELNET_COMMAND = "NPS>";

    public TrafficLightConnector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() throws IOException, InvalidTelnetOptionException {

        this.telnetClient = new TelnetClient();

        this.telnetClient.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
        this.telnetClient.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
        this.telnetClient.addOptionHandler(new EchoOptionHandler(true, false, true, false));

        log.info("Connecting to telnetClient. IP: " + ip + " , trafficLightPort:" + port);
        this.telnetClient.connect(ip, port);
        log.info("Connected to telnetClient1. IP: " + ip + " , trafficLightPort1:" + port);
        //telnetClient.setSoTimeout(20000);  //0 seconds

        this.outputStream = new PrintStream(telnetClient.getOutputStream());
        this.inputStream = telnetClient.getInputStream();
        String sessionStartText = readUntil(START_OF_TELNET_COMMAND);
        log.info("Started telnetClient1:" + sessionStartText);
    }


    public void disconnect() {

        if (telnetClient == null) {
            return;
        }
        try {
            log.info("Disconnecting telnetClient with IP: " + ip);
            telnetClient.disconnect();
            log.info("Disconnected  telnetClient with IP: " + ip);
        } catch (Exception e) {
            log.error("Failed to disconnect telnetClient with IP: " + ip, e);
        }
    }


    public String readUntil(String pattern) {
        log.info("Reading from telnet until pattern '" + pattern + "'");
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuilder sb = new StringBuilder();
            char ch = (char) inputStream.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) inputStream.read();
            }
        } catch (Exception e) {
            log.error("Failed to read telnet inputStream", e);
        }
        log.error("Failed to find pattern '" + pattern + "' from telnet inputStream");
        return null;
    }


    public void sendCommand(String command) {
        synchronized (this) {
            log.info("Sending command " + command + " to connector " + this.getIp());
            getOutputStream().println(command);
            getOutputStream().flush();
            log.info("Sent command " + command + " to connector " + this.getIp());
            //String commandResult = readUntil(TrafficLightConnector.START_OF_TELNET_COMMAND);
            //log.info("Command Result: \n" + commandResult);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PrintStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public TelnetClient getTelnetClient() {
        return telnetClient;
    }

    public void setTelnetClient(TelnetClient telnetClient) {
        this.telnetClient = telnetClient;
    }
}
