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

	public Client(String address, int port) {
		try {
			client = new Socket(InetAddress.getByName(address), port);
			messageReciever = new MessageReciever(client);
			messageParser = new MessageParser();
			this.outgoing = new ObjectOutputStream(client.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		messageReciever.run();
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
}