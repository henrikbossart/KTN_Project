package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.sf.json.JSONObject;

public class MessageReciever {
	ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
	Socket clientSocket;
	private ObjectInputStream incoming;
	sdsd
	
	public MessageReciever(Socket socket){
		this.clientSocket = socket;
		try {
			this.incoming = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		try {
			JSONObject message = (JSONObject) incoming.readObject();
			messages.add(message);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
