package chat.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by henrikbossart on 25.02.2016.
 */
public class MessageParser {

    private String request;
    private String content;
    private JSONObject payload;
    private HashMap<String, String> possibleResponses = new HashMap<>();

    public MessageParser(JSONObject payload) {
        this.payload = payload;

        this.request = (String) payload.get("request");
        this.content = (String) payload.get("content");

        possibleResponses.put("help", parseHelp());
        possibleResponses.put("logout", parseLogout());
        possibleResponses.put("msg", parseMsg());
        possibleResponses.put("names", parseNames());

        System.out.println(returnPayload());

    }

    private boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    public String parseHelp() {
        String helpText = "The requests supported by this chat system are: \n" +
                "'login' <username> - signs you in to the chatroom with a valid username. \n" +
                "'logout'           - disconnects you from the server. \n" +
                "'msg' <message>    - sends a message to the chatroom. \n" +
                "'names'            - lists all the users currently in the chatroom. \n" +
                "'help'             - displays this help windows.";

        return helpText;
    }

    public String parseLogout() {
        return "You are now disconnected from the server";
    }

    public String parseMsg(){
        return this.content;
    }

    public String parseNames() {
        return "These people are currently in the chatroom: ";
    }

    public JSONObject returnPayload() {
        JSONObject returnPayload = new JSONObject();

        returnPayload.put("request", this.request);
        returnPayload.put("content", this.content);
        returnPayload.put("answer", this.possibleResponses.get(this.request));

        return returnPayload;
    }
}
