package client;

import java.net.Socket;
import java.util.ArrayList;

import net.sf.json.JSONObject;

public class MessageReciever {
	ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
	Socket clientSocket;
	
	public MessageReciever(Socket socket){
		this.clientSocket = socket;
	}

	public void recieve(JSONObject message) {
		messages.add(message);
	}
	
	public JSONObject getMessage(){
		if (! (messages.size() == 0)){
			JSONObject message = messages.get(0);
			messages.remove(0);
			return message;
		}
		return null;
	}
}
