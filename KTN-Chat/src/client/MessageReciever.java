package client;

import net.sf.json.JSONObject;

public class MessageReciever {
	JSONObject message;

	public void recieve(JSONObject message) {
		this.message = message;
	}
	// STACK av JSON objekter
}
