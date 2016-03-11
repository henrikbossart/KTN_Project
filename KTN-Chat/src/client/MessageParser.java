package client;

import net.sf.json.JSONObject;

public class MessageParser {

    private String timestamp;
    private String response;
    private String content;
    private String sender;

    public MessageParser(JSONObject payload) {
        this.sender = payload.getString("sender");
        this.content = payload.getString("content");
        this.response = payload.getString("response");
        this.timestamp = payload.getString("timestamp");
}