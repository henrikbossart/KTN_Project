package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.sf.json.JSONObject;

public class MessageReciever implements Runnable {
	ArrayList<JSONObject> messages = new ArrayList<JSONObject>();
	Socket clientSocket;
	private ObjectInputStream incoming;
	private boolean running = false;

	public MessageReciever(Socket socket) {
		this.clientSocket = socket;
		try {
			this.incoming = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		running = true;
		while (running) {
			try {
				boolean complete = false;
				JSONObject message = null;
				while (!complete) {
					try {
						message = (JSONObject) incoming.readObject();
					} catch (EOFException e) {
						complete = true;
					}

				}
				if (message != null) {
					messages.add(message);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean hasMessage() {
		if (messages.size() < 1) {
			return false;
		} else {
			return true;

		}

	}

	public JSONObject getMessage() {
		if (!(messages.size() == 0)) {
			JSONObject message = messages.get(0);
			messages.remove(0);
			return message;
		}
		return null;
	}

	public void disconnect() {
		running = false;
	}
}
