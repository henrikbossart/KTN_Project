package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import net.sf.json.JSONObject;

public class Client extends Thread {

	private Socket client;
	private MessageReciever messageReciever;
	private MessageParser messageParser;
	private ObjectOutputStream outgoing;
	private String username;
	private boolean connected;

	public Client(String address, int port, String username) {
		try {
			client = new Socket(InetAddress.getByName(address), port);
			this.messageReciever = new MessageReciever(client);
			messageParser = new MessageParser();
			this.outgoing = new ObjectOutputStream(client.getOutputStream());
			this.username = username;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		sendMessage(createPayload("login", username));
		new Thread(this.messageReciever).start();
		connected = true;
		while (connected) {
			if (messageReciever.hasMessage()) {
				messageParser.setPayload(messageReciever.getMessage());
			}
			
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(JSONObject message) {
		try {
			outgoing.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JSONObject createPayload(String request, String content) {
		JSONObject payload = new JSONObject();
		payload.put("request", request);
		payload.put("content", content);
		return payload;

	}

	public static void errorLogger(String error) {
		System.out.println(error);
	}

	public void disconnect () {
		sendMessage(createPayload("logout", username));
		connected = false;
	}
}