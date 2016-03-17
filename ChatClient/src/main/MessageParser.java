package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class MessageParser {

    private String timestamp;
    private String response;
    private String content;
    private String sender;
    private JSONObject payload;
    private JTextArea chatLog;
    private JTextArea userLog;
    private Client client;

    //Initializes the parser
    public MessageParser(JTextArea chatLog, JTextArea userLog, Client client) {
        this.chatLog = chatLog;
        this.userLog = userLog;
        this.client = client;
    }

    public void setPayload(JSONObject payload) throws IOException {
        this.payload = payload;
        if (payloadChecker()) {
            payloadSplitter();
        }
    }

    private void payloadSplitter() throws IOException {
        if (payload.get("response").toString().equals("history")) {
            JSONArray tempLog = (JSONArray) payload.get("content");
            for (int i = 0; i < tempLog.size(); i++) {
                JSONObject obj = (JSONObject) tempLog.get(i);
                this.timestamp = (String) obj.get("timestamp");
                this.response = (String) obj.get("response");
                this.content = (String) obj.get("content");
                this.sender = (String) obj.get("sender");

                logger(timestamp, sender, content);

            }
        } else if (payload.get("response").toString().equals("names")) {
            JSONArray tempUsers = (JSONArray) payload.get("content");
            userLog.setText("");
            for (int i = 0; i < tempUsers.size(); i++) {
                userLog.append(tempUsers.get(i) + "\n");
            }
        } else if (payload.get("response").toString().equals("logout")) {
            this.timestamp = (String) payload.get("timestamp");
            this.response = (String) payload.get("response");
            this.sender = (String) payload.get("sender");
            this.content = "Server has shut down...";

        }
        else {
            this.timestamp = (String) payload.get("timestamp");
            this.response = (String) payload.get("response");
            this.content = (String) payload.get("content");
            this.sender = (String) payload.get("sender");
            logger(timestamp, sender, content);
        }

    }

    public boolean payloadChecker() {
        if (!payload.containsKey("timestamp") ||
                !payload.containsKey("response") ||
                !payload.containsKey("content") ||
                !payload.containsKey("sender")) {
            Client.errorLogger("payload does not contain all required elements!");
            return false;
        }
        return true;
    }

    public void logger(String time, String user, String message) {

        LocalDateTime tempTime = LocalDateTime.parse((CharSequence) time);

        chatLog.append(tempTime.getHour() + ":" + tempTime.getMinute() + ":" + tempTime.getSecond() + " (" + user + ") :" + message + "\n");
    }




}