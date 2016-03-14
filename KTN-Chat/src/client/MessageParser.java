package client;

import net.sf.json.JSONObject;

public class MessageParser {

    private String timestamp;
    private String response;
    private String content;
    private String sender;
    private JSONObject payload;


    public void setPayload(JSONObject payload) {
        this.payload = payload;
        payloadChecker();
        payloadSplitter();
    }

    private void payloadSplitter() {
        this.timestamp = payload.getString("timestamp");
        this.response = payload.getString("response");
        this.content = payload.getString("content");
        this.sender = payload.getString("sender");
    }

    public String payloadChecker() {
        if (!payload.containsKey("timestamp") ||
                !payload.containsKey("response") ||
                !payload.containsKey("content") ||
                !payload.containsKey("sender")) {
            Client.errorLogger("payload does not contain all required elements!");
        }
        return toString();
    }

    public String toString() {
        String decodedMessage = timestamp + " (" + sender + "): " + content;
        return decodedMessage;
    }




}