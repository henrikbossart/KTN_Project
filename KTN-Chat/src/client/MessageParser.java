package client;

import net.sf.json.JSONObject;

public class MessageParser {

    private String timestamp;
    private String response;
    private String content;
    private String sender;
    private JSONObject payload;

    public MessageParser(JSONObject payload) {
        this.payload = payload;
    }

    






    public String toString() {
        String decodedMessage = timestamp + " (" + sender + "): " + content;
        return decodedMessage;
    }




}