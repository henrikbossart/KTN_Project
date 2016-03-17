package main;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

public class MessageReciever implements Runnable {

    private Socket clientSocket;
    private BufferedReader incoming;
    private boolean running;
    private JSONParser parser = new JSONParser();
    private JSONObject message;
    private MessageParser messageParser;

    public MessageReciever(Socket socket, MessageParser messageParser) {
        this.clientSocket = socket;
        this.messageParser = messageParser;
        try {
            incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        running = false;
    }

    public void run() {
        running = true;
        String text = "";
        while (running) {
                try {
                    text = incoming.readLine();
                    message = (JSONObject) parser.parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (message != null) {
                try {
                    messageParser.setPayload(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
